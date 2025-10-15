package io.github.s3s3l.yggdrasil.sample.game;

import com.badlogic.gdx.Game;

import io.github.s3s3l.yggdrasil.game.core.scene.SceneConfig;
import io.github.s3s3l.yggdrasil.sample.game.screen.MainScreen;

public class SampleGame extends Game {

    @Override
    public void create() {
        setScreen(new MainScreen(SceneConfig.builder().type(MainScreen.class).build()));
    }

}
