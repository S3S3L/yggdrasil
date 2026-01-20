package io.github.s3s3l.yggdrasil.game.core.scene;

public interface SceneManager<T extends Scene> {

    T createScene(Class<? extends T> sceneClass, SceneConfig config);

    T createAndSet(Class<? extends T> sceneClass, SceneConfig config);

    void setScene(long sceneId);

    T getCurrentScene();

    void destroyScene(long sceneId);

}