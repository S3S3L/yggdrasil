package io.github.s3s3l.yggdrasil.game.gdx.scene;

import com.badlogic.gdx.Game;

import io.github.s3s3l.yggdrasil.game.core.scene.AbstractSceneManager;
import io.github.s3s3l.yggdrasil.game.core.scene.SceneConfig;
import io.github.s3s3l.yggdrasil.game.gdx.runtime.GdxRuntime;

public class GdxSceneManager extends AbstractSceneManager<GdxScene> {
    private final Game game;
    private final GdxRuntime runtime;

    public GdxSceneManager(Game game, GdxRuntime runtime) {
        this.game = game;
        this.runtime = runtime;
    }

    @Override
    public synchronized GdxScene createScene(Class<? extends GdxScene> sceneClass, SceneConfig config) {
        GdxScene scene = super.createScene(sceneClass, config);
        scene.attachRuntime(runtime);
        return scene;
    }

    @Override
    protected void doSetScene(GdxScene scene) {
        game.setScreen(scene);
    }
    
}
