package io.github.s3s3l.yggdrasil.game.core.telemetry;

import java.util.Objects;
import java.util.OptionalLong;

/**
 * Low-frequency JVM values. An empty field is explicitly unavailable from the
 * current JVM rather than being indistinguishable from a zero value.
 */
public record TelemetryJvmSnapshot(
        OptionalLong heapUsedBytes,
        OptionalLong heapCommittedBytes,
        OptionalLong heapMaxBytes,
        OptionalLong gcCount,
        OptionalLong gcTimeMillis,
        OptionalLong liveThreadCount,
        OptionalLong peakThreadCount) {

    public static final TelemetryJvmSnapshot UNAVAILABLE = new TelemetryJvmSnapshot(
            OptionalLong.empty(), OptionalLong.empty(), OptionalLong.empty(),
            OptionalLong.empty(), OptionalLong.empty(), OptionalLong.empty(),
            OptionalLong.empty());

    public TelemetryJvmSnapshot {
        Objects.requireNonNull(heapUsedBytes, "heapUsedBytes");
        Objects.requireNonNull(heapCommittedBytes, "heapCommittedBytes");
        Objects.requireNonNull(heapMaxBytes, "heapMaxBytes");
        Objects.requireNonNull(gcCount, "gcCount");
        Objects.requireNonNull(gcTimeMillis, "gcTimeMillis");
        Objects.requireNonNull(liveThreadCount, "liveThreadCount");
        Objects.requireNonNull(peakThreadCount, "peakThreadCount");
    }
}
