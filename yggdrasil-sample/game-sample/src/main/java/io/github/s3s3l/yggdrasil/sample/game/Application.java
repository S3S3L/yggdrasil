package io.github.s3s3l.yggdrasil.sample.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class Application {
    public static void main(String[] args) {
        var config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Sample Game");
        config.setWindowedMode(800, 600);
        new Lwjgl3Application(new SampleGame(), config);
    }
}
