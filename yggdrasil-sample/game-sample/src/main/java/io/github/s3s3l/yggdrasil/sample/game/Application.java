package io.github.s3s3l.yggdrasil.sample.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import io.github.s3s3l.yggdrasil.game.core.scene.SceneConfig;
import io.github.s3s3l.yggdrasil.game.gdx.game.GdxGame;
import io.github.s3s3l.yggdrasil.sample.game.screen.MainScreen;

public class Application {
    public static void main(String[] args) {
        var config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Sample Game");
        config.setWindowedMode(800, 600);
        new Lwjgl3Application(new GdxGame() {
            @Override
            public void create() {
                createAndSet(MainScreen.class, SceneConfig.builder()
                        .type(MainScreen.class)
                        .build());
            }

        }, config);

    }
}
