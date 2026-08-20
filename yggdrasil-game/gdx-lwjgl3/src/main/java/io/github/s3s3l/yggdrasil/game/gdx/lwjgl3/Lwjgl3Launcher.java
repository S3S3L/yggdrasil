package io.github.s3s3l.yggdrasil.game.gdx.lwjgl3;

import java.util.Objects;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/**
 * Optional desktop entry point. Depending on this artifact is the explicit
 * opt-in for the LWJGL3 backend and desktop native libraries.
 */
public final class Lwjgl3Launcher {

    private Lwjgl3Launcher() {
    }

    public static Lwjgl3ApplicationConfiguration newConfiguration() {
        return new Lwjgl3ApplicationConfiguration();
    }

    public static Lwjgl3Application launch(
            ApplicationListener application,
            Lwjgl3ApplicationConfiguration configuration) {
        return new Lwjgl3Application(
                Objects.requireNonNull(application, "application"),
                Objects.requireNonNull(configuration, "configuration"));
    }
}
