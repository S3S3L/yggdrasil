package io.github.s3s3l.yggdrasil.game.core.telemetry;

/**
 * Immutable values for all monotonic telemetry counters.
 */
public record TelemetryCounters(
        long objectsCreated,
        long objectsActivated,
        long objectsReleased,
        long objectsReclaimed,
        long dirtyObjects,
        long resourcesAcquired,
        long resourcesReleased,
        long cacheHits,
        long cacheMisses,
        long cacheEvictions,
        long slowFrames) {

    public static final TelemetryCounters ZERO = new TelemetryCounters(
            0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
}
