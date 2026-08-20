package io.github.s3s3l.yggdrasil.game.gdx.game;

import com.badlogic.gdx.Game;

import io.github.s3s3l.yggdrasil.game.core.scene.SceneConfig;
import io.github.s3s3l.yggdrasil.game.core.scene.SceneManager;
import io.github.s3s3l.yggdrasil.game.gdx.scene.GdxScene;
import io.github.s3s3l.yggdrasil.game.gdx.scene.GdxSceneManager;
import io.github.s3s3l.yggdrasil.game.gdx.runtime.GdxRuntime;

public abstract class GdxGame extends Game implements SceneManager<GdxScene> {
    protected final GdxRuntime runtime;
    protected final GdxSceneManager sceneManager;

    /**
     * @deprecated Global current-game lookup was removed. Keep the runtime and pass
     *             it to the code that needs it instead.
     */
    @Deprecated
    public static GdxGame get() {
        throw new UnsupportedOperationException("Global GdxGame lookup was removed; use an instance runtime instead");
    }

    public GdxGame() {
        this(new GdxRuntime());
    }

    protected GdxGame(GdxRuntime runtime) {
        this.runtime = runtime;
        this.sceneManager = new GdxSceneManager(this, runtime);
    }

    public GdxRuntime runtime() {
        return runtime;
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

    @Override
    public void dispose() {
        try {
            sceneManager.close();
        } finally {
            try {
                super.dispose();
            } finally {
                runtime.close();
            }
        }
    }
}
