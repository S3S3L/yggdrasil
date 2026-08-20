package io.github.s3s3l.yggdrasil.game.core.scene;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.s3s3l.yggdrasil.game.core.utils.IdGenerator;

/**
 * 
 * Instance-owned scene manager.
 * 
 * @author s3s3l
 */
public abstract class AbstractSceneManager<T extends Scene> implements SceneManager<T> {

    private final Map<Long, T> scenes = new ConcurrentHashMap<>();
    private volatile long currentSceneId = -1L;

    @Override
    public synchronized T createScene(Class<? extends T> sceneClass, SceneConfig config) {
        try {
            T scene = sceneClass.getDeclaredConstructor(SceneConfig.class).newInstance(config);
            scene.setId(IdGenerator.nextId());
            scenes.put(scene.getId(), scene);
            return scene;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create scene instance", e);
        }
    }

    @Override
    public synchronized T createAndSet(Class<? extends T> sceneClass, SceneConfig config) {
        T scene = createScene(sceneClass, config);
        setScene(scene.getId());
        return scene;
    }

    @Override
    public synchronized void setScene(long sceneId) {
        var scene = scenes.get(sceneId);
        if (scene == null) {
            throw new IllegalArgumentException("Scene with id " + sceneId + " does not exist");
        }
        this.currentSceneId = sceneId;

        doSetScene(scene);
    }

    @Override
    public T getCurrentScene() {
        return scenes.get(currentSceneId);
    }

    @Override
    public synchronized void destroyScene(long sceneId) {
        if (currentSceneId == sceneId) {
            throw new IllegalStateException("Cannot destroy the current active scene");
        }
        var scene = scenes.remove(sceneId);
        if (scene != null) {
            scene.finalizeResource();
        }
    }

    protected abstract void doSetScene(T scene);

    @Override
    public synchronized void close() {
        for (T scene : scenes.values()) {
            scene.finalizeResource();
        }
        scenes.clear();
        currentSceneId = -1L;
    }
}
