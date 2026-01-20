package io.github.s3s3l.yggdrasil.game.gdx.game;

import com.badlogic.gdx.Game;

import io.github.s3s3l.yggdrasil.game.core.scene.SceneConfig;
import io.github.s3s3l.yggdrasil.game.core.scene.SceneManager;
import io.github.s3s3l.yggdrasil.game.gdx.scene.GdxScene;
import io.github.s3s3l.yggdrasil.game.gdx.scene.GdxSceneManager;

public abstract class GdxGame extends Game implements SceneManager<GdxScene> {
    protected static GdxGame CURRENT;

    protected final GdxSceneManager sceneManager = new GdxSceneManager(this);

    public static GdxGame get() {
        return CURRENT;
    }

    public GdxGame() {
        CURRENT = this;
    }

    @Override
    public GdxScene createScene(Class<? extends GdxScene> sceneClass, SceneConfig config) {
        return sceneManager.createScene(sceneClass, config);
    }

    @Override
    public GdxScene createAndSet(Class<? extends GdxScene> sceneClass, SceneConfig config) {
        return sceneManager.createAndSet(sceneClass, config);
    }

    @Override
    public void setScene(long sceneId) {
        sceneManager.setScene(sceneId);
    }

    @Override
    public GdxScene getCurrentScene() {
        return sceneManager.getCurrentScene();
    }

    @Override
    public void destroyScene(long sceneId) {
        sceneManager.destroyScene(sceneId);
    }
    
}
