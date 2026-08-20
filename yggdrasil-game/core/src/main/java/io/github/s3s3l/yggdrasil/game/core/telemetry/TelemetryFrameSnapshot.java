package io.github.s3s3l.yggdrasil.game.core.telemetry;

/**
 * Timing and outcome of one runtime frame.
 */
public record TelemetryFrameSnapshot(
        long frameNumber,
        long startedNanos,
        long durationNanos,
        long callbackNanos,
        long calculateNanos,
        long preRenderNanos,
        long beforeRenderNanos,
        long renderNanos,
        boolean completed,
        boolean slow) {

    public static final TelemetryFrameSnapshot NONE = new TelemetryFrameSnapshot(
            0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, false, false);
}
