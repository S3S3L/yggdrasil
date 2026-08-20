package io.github.s3s3l.yggdrasil.game.core.object;

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
import lombok.NonNull;

public class GameObjectManager implements AutoCloseable {

    private final Map<Long, GameObject> activeObjs = new HashMap<>();
    private final Map<Long, SoftReference<GameObject>> releasedObjs = new HashMap<>();
    private final Set<GameObject> dirtyRoots = new HashSet<>();
    private final EventDispatcher eventDispatcher = EventCenter.get();
    private final GameTaskScheduler taskScheduler;
    private boolean closed;

    /**
     * Creates a manager with deterministic, caller-thread scheduling.
     */
    public GameObjectManager() {
        this(GameTaskScheduler.serial());
    }

    /**
     * Creates a manager with an explicitly selected non-GL work scheduler.
     */
    public GameObjectManager(GameTaskScheduler taskScheduler) {
        this.taskScheduler = java.util.Objects.requireNonNull(taskScheduler, "taskScheduler");
    }

    public synchronized <T extends GameObject> T create(@NonNull GameObjectConfig config, @NonNull Class<T> type) {
        if (closed) {
            throw new IllegalStateException("Cannot create game objects after the manager is closed");
        }
        if (config.getType() != null && !config.getType().isAssignableFrom(type)) {
            throw new IllegalArgumentException("Config type " + config.getType().getName()
                    + " does not match the requested type " + type.getName());
        }
        try {
            T obj = type.getDeclaredConstructor(GameObjectConfig.class).newInstance(config);
            obj.afterPropertiesSet();
            activeObjs.put(obj.getId(), obj);
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
        obj.setEnable(false);
        activeObjs.remove(obj.getId());
        releasedObjs.put(obj.getId(), new SoftReference<GameObject>(obj));
    }

    /**
     * Release all active game objects. All active objects will be moved to released
     * objects.
     */
    public synchronized void releaseAll() {
        for (GameObject obj : activeObjs.values()) {
            obj.setEnable(false);
            releasedObjs.put(obj.getId(), new SoftReference<GameObject>(obj));
        }
        activeObjs.clear();
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

        releasedObjs.computeIfPresent(obj.getId(), (k, v) -> {
            if (v.get() == null) {
                throw new IllegalArgumentException("GameObject has been garbage collected: " + obj);
            }
            if (obj == v.get()) {
                obj.setEnable(true);
                activeObjs.put(obj.getId(), obj);
                return null;
            }

            throw new IllegalArgumentException("GameObject does not match the released object: " + obj);
        });
    }

    /**
     * Calculates enabled objects using the configured non-GL scheduler.
     */
    public synchronized void doCalculate(float delta) {
        if (closed) {
            return;
        }
        var candidates = activeObjs.values().stream()
                .filter(GameObject::isEnable)
                .toList();
        taskScheduler.execute(candidates.stream()
                .<Runnable>map(obj -> () -> obj.calculate(delta))
                .toList());
        var dirtyObjects = candidates.stream()
                .filter(GameObject::isDirty)
                .collect(java.util.stream.Collectors.toSet());

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
                dirtyRoots.add(dirtyObject);
            }
        }
    }

    /**
     * Pre-renders dirty visible objects using the configured non-GL scheduler.
     */
    public synchronized void doPreRender() {
        if (closed) {
            return;
        }
        List<Runnable> tasks = dirtyRoots.stream()
                .filter(obj -> obj.isEnable() && obj.getVisible() == Visible.SHOW)
                .<Runnable>map(obj -> obj::preRender)
                .toList();
        taskScheduler.execute(tasks);
        dirtyRoots.clear();
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

        for (GameObject obj : activeObjs.values()) {
            if (obj.isEnable() && obj.getVisible() == Visible.SHOW && obj.parent() == null) {
                obj.render();
            }
        }
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
}
