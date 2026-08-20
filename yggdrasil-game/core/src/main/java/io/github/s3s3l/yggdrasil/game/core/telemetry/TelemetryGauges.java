package io.github.s3s3l.yggdrasil.game.core.telemetry;

/**
 * Immutable values for all point-in-time telemetry gauges.
 */
public record TelemetryGauges(
        long objectCount,
        long activeObjectCount,
        long dirtyObjectCount,
        long resourceCount,
        long cacheEntryCount,
        long releasedRetainedObjectCount,
        long dirtyRootCount,
        long uiSceneCount,
        long uiNodeCount,
        long uiDirtySceneCount,
        long uiDirtyNodeCount) {

    public static final TelemetryGauges ZERO = new TelemetryGauges(
            0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
}
