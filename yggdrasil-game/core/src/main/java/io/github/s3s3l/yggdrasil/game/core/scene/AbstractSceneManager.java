package io.github.s3s3l.yggdrasil.game.core.scene;

import java.util.HashMap;
import java.util.Map;

import io.github.s3s3l.yggdrasil.game.core.utils.IdGenerator;

/**
 * 
 * 场景管理器，单例
 * 
 * @author s3s3l
 */
public abstract class AbstractSceneManager {

    private final Map<Long, Scene> scenes = new HashMap<>();

    public synchronized <T extends Scene> T createScene(Class<T> sceneClass) {
        try {
            T scene = sceneClass.getDeclaredConstructor().newInstance();
            scene.setId(IdGenerator.nextId());
            scenes.put(scene.getId(), scene);
            return scene;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create scene instance", e);
        }
    }

    public synchronized void switchScene(Scene scene) {

    }
}
