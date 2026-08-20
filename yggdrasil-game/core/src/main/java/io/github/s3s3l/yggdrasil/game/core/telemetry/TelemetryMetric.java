package io.github.s3s3l.yggdrasil.game.core.telemetry;

/**
 * Monotonic telemetry counters. Future subsystems record their work through
 * this fixed schema rather than allocating metric-name maps in frame loops.
 */
public enum TelemetryMetric {
    OBJECTS_CREATED,
    OBJECTS_ACTIVATED,
    OBJECTS_RELEASED,
    OBJECTS_RECLAIMED,
    DIRTY_OBJECTS,
    RESOURCES_ACQUIRED,
    RESOURCES_RELEASED,
    CACHE_HITS,
    CACHE_MISSES,
    CACHE_EVICTIONS
}
