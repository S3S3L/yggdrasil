package io.github.s3s3l.yggdrasil.game.gdx.scene;

import com.badlogic.gdx.Game;

import io.github.s3s3l.yggdrasil.game.core.scene.AbstractSceneManager;

public class GdxSceneManager extends AbstractSceneManager<GdxScene> {
    private final Game game;

    public GdxSceneManager(Game game) {
        this.game = game;
    }

    @Override
    protected void doSetScene(GdxScene scene) {
        game.setScreen(scene);
    }
    
}
