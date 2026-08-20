package io.github.s3s3l.yggdrasil.game.core.telemetry;

/**
 * Reserved strongly typed JVM schema for a future JVM collector.
 */
public record TelemetryJvmSnapshot(
        long heapUsedBytes,
        long heapCommittedBytes,
        long heapMaxBytes,
        long gcCount,
        long gcTimeMillis) {
}
