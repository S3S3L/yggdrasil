package io.github.s3s3l.yggdrasil.game.core.object;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import io.github.s3s3l.yggdrasil.game.core.object.props.Visible;
import lombok.NonNull;

public class GameObjectManager {

    private static final ExecutorService calculateExecutor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors(),
            r -> {
                Thread t = new Thread(r);
                t.setName("Calculate-Thread");
                t.setDaemon(true);
                return t;
            });

    private static final ExecutorService preRenderExecutor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors(),
            r -> {
                Thread t = new Thread(r);
                t.setName("PreRender-Thread");
                t.setDaemon(true);
                return t;
            });

    private final Map<Long, GameObject> activeObjs = new HashMap<>();
    private final Map<Long, SoftReference<GameObject>> releasedObjs = new HashMap<>();
    private final Set<GameObject> dirtyRoots = new HashSet<>();

    public synchronized <T extends GameObject> T create(@NonNull GameObjectConfig config, @NonNull Class<T> type) {
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
     * Calculate all active game objects that are marked as enabled.
     * The calculation process is performed in parallel using multiple threads to
     * improve performance.
     * The method also identifies dirty objects and tracks the root dirty objects to
     * optimize subsequent pre-rendering.
     * 
     * @param delta the time elapsed since the last calculate call
     */
    public synchronized void doCalculate(float delta) {
        try {
            var dirtyObjects = calculateExecutor.invokeAll(activeObjs.values().stream()
                    .filter(GameObject::isEnable)
                    .map(obj -> (Callable<GameObject>) () -> {
                        obj.calculate(delta);
                        return obj;
                    })
                    .toList()).stream().map(t -> {
                        try {
                            return t.get();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return null;
                        } catch (ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    }).filter(GameObject::isDirty).collect(Collectors.toSet());

            // Find dirty roots
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
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Pre-render all active game objects that are marked as dirty.
     * The pre-rendering process is optimized to only pre-render the root dirty
     * objects, avoiding redundant pre-rendering of child objects.
     * This process is performed in parallel using multiple threads to improve
     * performance.
     */
    public synchronized void doPreRender() {
        try {
            preRenderExecutor.invokeAll(dirtyRoots.stream()
                    .filter(obj -> obj.isEnable() && obj.getVisible() == Visible.SHOW)
                    .map(obj -> (Callable<Void>) () -> {
                        obj.preRender();
                        return null;
                    })
                    .toList()).forEach(t -> {
                        try {
                            t.get();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        } catch (ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        dirtyRoots.clear();
    }

    /**
     * Render all active game objects that are marked as visible and enabled.
     * The rendering process is optimized to only render the root objects,
     * avoiding redundant rendering of child objects.
     * This process is performed in a single thread to ensure rendering order.
     */
    public synchronized void doRender() {

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
}
