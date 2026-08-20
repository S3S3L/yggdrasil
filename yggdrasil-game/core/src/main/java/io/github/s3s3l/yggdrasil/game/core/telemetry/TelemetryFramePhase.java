package io.github.s3s3l.yggdrasil.game.core.telemetry;

/**
 * Named portions of a runtime frame.
 */
public enum TelemetryFramePhase {
    CALLBACKS,
    CALCULATE,
    PRE_RENDER,
    BEFORE_RENDER,
    RENDER
}
