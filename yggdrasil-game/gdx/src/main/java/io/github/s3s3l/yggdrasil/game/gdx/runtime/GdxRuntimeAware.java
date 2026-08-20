package io.github.s3s3l.yggdrasil.game.gdx.runtime;

/**
 * Receives the instance-owned runtime before it performs rendering.
 */
public interface GdxRuntimeAware {

    void attachRuntime(GdxRuntime runtime);
}
