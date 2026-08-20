package io.github.s3s3l.yggdrasil.game.core.object;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.s3s3l.yggdrasil.game.core.event.EventCenter;
import io.github.s3s3l.yggdrasil.game.core.event.EventDispatcher;
import io.github.s3s3l.yggdrasil.game.core.object.props.Visible;
import io.github.s3s3l.yggdrasil.game.core.runtime.GameTaskScheduler;
import io.github.s3s3l.yggdrasil.game.core.telemetry.NoOpTelemetryRegistry;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryGauge;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryMetric;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryRegistry;
import lombok.NonNull;

public class GameObjectManager implements AutoCloseable {

    private final Map<Long, GameObject> activeObjs = new HashMap<>();
    private final Map<Long, ReleasedObjectReference> releasedObjs = new HashMap<>();
    private final Set<GameObject> dirtyRoots = new HashSet<>();
    private final EventDispatcher eventDispatcher = EventCenter.get();
    private final GameTaskScheduler taskScheduler;
    private final TelemetryRegistry telemetry;
    private final boolean telemetryEnabled;
    private final boolean typeMetricsEnabled;
    private final ReferenceQueue<GameObject> releasedObjectQueue;
    private final ReleasedObjectReferenceFactory releasedReferenceFactory;
    private boolean closed;

    /**
     * Creates a manager with deterministic, caller-thread scheduling.
     */
    public GameObjectManager() {
        this(GameTaskScheduler.serial(), NoOpTelemetryRegistry.INSTANCE);
    }

    /**
     * Creates a manager that reports to the supplied runtime telemetry registry.
     */
    public GameObjectManager(TelemetryRegistry telemetry) {
        this(GameTaskScheduler.serial(), telemetry);
    }

    /**
     * Creates a manager with an explicitly selected non-GL work scheduler.
     */
    public GameObjectManager(GameTaskScheduler taskScheduler) {
        this(taskScheduler, NoOpTelemetryRegistry.INSTANCE);
    }

    /**
     * Creates a manager with explicit scheduling and telemetry ownership.
     */
    public GameObjectManager(GameTaskScheduler taskScheduler, TelemetryRegistry telemetry) {
        this(taskScheduler, telemetry, new ReferenceQueue<>(), ReleasedObjectReference::new);
    }

    GameObjectManager(GameTaskScheduler taskScheduler, TelemetryRegistry telemetry,
            ReferenceQueue<GameObject> releasedObjectQueue,
            ReleasedObjectReferenceFactory releasedReferenceFactory) {
        this.taskScheduler = java.util.Objects.requireNonNull(taskScheduler, "taskScheduler");
        this.telemetry = java.util.Objects.requireNonNull(telemetry, "telemetry");
        this.telemetryEnabled = telemetry.isEnabled();
        this.typeMetricsEnabled = telemetry.isTypeMetricsEnabled();
        this.releasedObjectQueue = java.util.Objects.requireNonNull(releasedObjectQueue, "releasedObjectQueue");
        this.releasedReferenceFactory = java.util.Objects.requireNonNull(releasedReferenceFactory,
                "releasedReferenceFactory");
    }

    public synchronized <T extends GameObject> T create(@NonNull GameObjectConfig config, @NonNull Class<T> type) {
        if (closed) {
            throw new IllegalStateException("Cannot create game objects after the manager is closed");
        }
        drainReleasedObjects();
        if (config.getType() != null && !config.getType().isAssignableFrom(type)) {
            throw new IllegalArgumentException("Config type " + config.getType().getName()
                    + " does not match the requested type " + type.getName());
        }
        try {
            T obj = type.getDeclaredConstructor(GameObjectConfig.class).newInstance(config);
            obj.afterPropertiesSet();
            activeObjs.put(obj.getId(), obj);
            recordObject(TelemetryMetric.OBJECTS_CREATED, obj);
            updateObjectGauges();
            return obj;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create game object of type: " + type.getName(), e);
        }
    }

    public synchronized GameObject getActive(long id) {
        return activeObjs.get(id);
    }

    /**
     * Release a game object. The object will be removed from active objects and
     * added to released objects.
     * Released objects are kept in memory with soft references, allowing them to be
     * garbage collected if
     * memory is needed.
     * 
     * @param obj the game object to release
     */
    public synchronized void release(GameObject obj) {
        if (obj == null) {
            return;
        }
        drainReleasedObjects();
        obj.setEnable(false);
        boolean wasActive = activeObjs.remove(obj.getId()) != null;
        releasedObjs.put(obj.getId(), releasedReferenceFactory.create(obj.getId(), obj, releasedObjectQueue,
                typeMetricsEnabled ? obj.getClass() : null));
        if (wasActive) {
            recordObject(TelemetryMetric.OBJECTS_RELEASED, obj);
        }
        updateObjectGauges();
    }

    /**
     * Release all active game objects. All active objects will be moved to released
     * objects.
     */
    public synchronized void releaseAll() {
        drainReleasedObjects();
        int released = activeObjs.size();
        for (GameObject obj : activeObjs.values()) {
            obj.setEnable(false);
            releasedObjs.put(obj.getId(), releasedReferenceFactory.create(obj.getId(), obj, releasedObjectQueue,
                    typeMetricsEnabled ? obj.getClass() : null));
            recordType(TelemetryMetric.OBJECTS_RELEASED, obj);
        }
        activeObjs.clear();
        record(TelemetryMetric.OBJECTS_RELEASED, released);
        updateObjectGauges();
    }

    /**
     * Activate a released game object. The object will be moved from released
     * objects
     * to active objects.
     * If the object is not in the released state, an exception will be thrown.
     * 
     * @param obj the game object to activate
     * @throws IllegalArgumentException if the object is not in the released state
     */
    public synchronized void active(GameObject obj) {
        if (obj == null) {
            return;
        }
        ReleasedObjectReference released = releasedObjs.get(obj.getId());
        if (released == null) {
            return;
        }
        GameObject retained = released.get();
        if (retained == null) {
            throw new IllegalArgumentException("GameObject has been garbage collected: " + obj);
        }
        if (obj != retained) {
            throw new IllegalArgumentException("GameObject does not match the released object: " + obj);
        }
        obj.setEnable(true);
        activeObjs.put(obj.getId(), obj);
        releasedObjs.remove(obj.getId(), released);
        recordObject(TelemetryMetric.OBJECTS_ACTIVATED, obj);
        drainReleasedObjects();
        updateObjectGauges();
    }

    /**
     * Calculates enabled objects using the configured non-GL scheduler.
     */
    public synchronized void doCalculate(float delta) {
        if (closed) {
            return;
        }
        drainReleasedObjects();
        var candidates = activeObjs.values().stream()
                .filter(GameObject::isEnable)
                .toList();
        Set<GameObject> alreadyDirty = telemetryEnabled ? new HashSet<>() : null;
        if (telemetryEnabled) {
            for (GameObject candidate : candidates) {
                if (candidate.isDirty()) {
                    alreadyDirty.add(candidate);
                }
            }
        }
        taskScheduler.execute(candidates.stream()
                .<Runnable>map(obj -> () -> obj.calculate(delta))
                .toList());
        recordObjects(TelemetryMetric.OBJECTS_CALCULATED, candidates);
        var dirtyObjects = candidates.stream()
                .filter(GameObject::isDirty)
                .collect(java.util.stream.Collectors.toSet());
        int dirtyMarked = 0;
        if (telemetryEnabled) {
            for (GameObject dirtyObject : dirtyObjects) {
                if (!alreadyDirty.contains(dirtyObject)) {
                    dirtyMarked++;
                }
            }
        }

        int rootsAdded = 0;
        for (GameObject dirtyObject : dirtyObjects) {
            var parent = dirtyObject.parent();
            boolean isDirtyRoot = true;
            while (parent != null) {
                if (dirtyRoots.contains(parent)) {
                    isDirtyRoot = false;
                    break;
                }

                parent = parent.parent();
            }

            if (isDirtyRoot) {
                if (dirtyRoots.add(dirtyObject)) {
                    rootsAdded++;
                }
            }
        }
        record(TelemetryMetric.DIRTY_OBJECTS, dirtyObjects.size());
        record(TelemetryMetric.OBJECTS_DIRTY_MARKED, dirtyMarked);
        record(TelemetryMetric.OBJECT_DIRTY_ROOTS, rootsAdded);
        updateObjectGauges(dirtyObjects.size());
    }

    /**
     * Pre-renders dirty visible objects using the configured non-GL scheduler.
     */
    public synchronized void doPreRender() {
        if (closed) {
            return;
        }
        drainReleasedObjects();
        if (!telemetryEnabled) {
            List<Runnable> tasks = dirtyRoots.stream()
                    .filter(obj -> obj.isEnable() && obj.getVisible() == Visible.SHOW)
                    .<Runnable>map(obj -> obj::preRender)
                    .toList();
            taskScheduler.execute(tasks);
            dirtyRoots.clear();
            return;
        }
        List<GameObject> preRendered = dirtyRoots.stream()
                .filter(obj -> obj.isEnable() && obj.getVisible() == Visible.SHOW)
                .toList();
        List<Runnable> tasks = preRendered.stream()
                .<Runnable>map(obj -> obj::preRender)
                .toList();
        taskScheduler.execute(tasks);
        recordObjects(TelemetryMetric.OBJECTS_PRE_RENDERED, preRendered);
        recordObjects(TelemetryMetric.OBJECTS_DIRTY_PRE_RENDERED, preRendered);
        dirtyRoots.clear();
        updateObjectGauges();
    }

    /**
     * Render all active game objects that are marked as visible and enabled.
     * The rendering process is optimized to only render the root objects,
     * avoiding redundant rendering of child objects.
     * This process is performed in a single thread to ensure rendering order.
     */
    public synchronized void doRender() {
        if (closed) {
            return;
        }
        drainReleasedObjects();

        int rendered = 0;
        for (GameObject obj : activeObjs.values()) {
            if (obj.isEnable() && obj.getVisible() == Visible.SHOW && obj.parent() == null) {
                obj.render();
                rendered++;
                recordType(TelemetryMetric.OBJECTS_RENDERED, obj);
            }
        }
        record(TelemetryMetric.OBJECTS_RENDERED, rendered);
        updateObjectGauges();
    }

    /**
     * Render all active game objects that are marked as dirty.
     * The rendering process is optimized to only render the root dirty objects,
     * avoiding redundant rendering of child objects.
     * 
     * @param delta the time elapsed since the last render call
     */
    public synchronized void doRender(float delta) {
        doCalculate(delta);
        doPreRender();
        doRender();
    }

    public synchronized Collection<GameObject> activeObjects() {
        return java.util.List.copyOf(activeObjs.values());
    }

    public EventDispatcher events() {
        return eventDispatcher;
    }

    /**
     * Polls soft references reclaimed asynchronously by the JVM. This never
     * requests garbage collection and only removes the exact retained entry.
     *
     * @return the number of released objects reclaimed since the previous poll
     */
    public synchronized int reclaimReleasedObjects() {
        return drainReleasedObjects();
    }

    @Override
    public synchronized void close() {
        if (closed) {
            return;
        }
        closed = true;
        releaseAll();
        dirtyRoots.clear();
        taskScheduler.close();
    }

    private int drainReleasedObjects() {
        int reclaimed = 0;
        for (ReleasedObjectReference reference;
                (reference = (ReleasedObjectReference) releasedObjectQueue.poll()) != null;) {
            if (releasedObjs.remove(reference.objectId(), reference)) {
                reclaimed++;
                record(TelemetryMetric.OBJECTS_RECLAIMED, 1L);
                if (typeMetricsEnabled && reference.type() != null) {
                    telemetry.incrementType(TelemetryMetric.OBJECTS_RECLAIMED, reference.type());
                }
            }
        }
        if (reclaimed > 0) {
            updateObjectGauges();
        }
        return reclaimed;
    }

    private void recordObject(TelemetryMetric metric, GameObject object) {
        record(metric, 1L);
        recordType(metric, object);
    }

    private void recordObjects(TelemetryMetric metric, Collection<? extends GameObject> objects) {
        record(metric, objects.size());
        if (typeMetricsEnabled) {
            for (GameObject object : objects) {
                telemetry.incrementType(metric, object.getClass());
            }
        }
    }

    private void recordType(TelemetryMetric metric, GameObject object) {
        if (typeMetricsEnabled) {
            telemetry.incrementType(metric, object.getClass());
        }
    }

    private void record(TelemetryMetric metric, long amount) {
        if (telemetryEnabled && amount > 0L) {
            telemetry.add(metric, amount);
        }
    }

    private void updateObjectGauges() {
        if (!telemetryEnabled) {
            return;
        }
        int dirty = 0;
        for (GameObject object : activeObjs.values()) {
            if (object.isDirty()) {
                dirty++;
            }
        }
        updateObjectGauges(dirty);
    }

    private void updateObjectGauges(long dirtyObjects) {
        if (!telemetryEnabled) {
            return;
        }
        telemetry.set(TelemetryGauge.ACTIVE_OBJECT_COUNT, activeObjs.size());
        telemetry.set(TelemetryGauge.RELEASED_RETAINED_OBJECT_COUNT, releasedObjs.size());
        telemetry.set(TelemetryGauge.OBJECT_COUNT, activeObjs.size() + releasedObjs.size());
        telemetry.set(TelemetryGauge.DIRTY_OBJECT_COUNT, dirtyObjects);
        telemetry.set(TelemetryGauge.DIRTY_ROOT_COUNT, dirtyRoots.size());
    }
}

@FunctionalInterface
interface ReleasedObjectReferenceFactory {

    ReleasedObjectReference create(long objectId, GameObject object,
            ReferenceQueue<GameObject> queue, Class<?> type);
}

final class ReleasedObjectReference extends SoftReference<GameObject> {

    private final long objectId;
    private final Class<?> type;

    ReleasedObjectReference(long objectId, GameObject object,
            ReferenceQueue<GameObject> queue, Class<?> type) {
        super(object, queue);
        this.objectId = objectId;
        this.type = type;
    }

    long objectId() {
        return objectId;
    }

    Class<?> type() {
        return type;
    }
}
