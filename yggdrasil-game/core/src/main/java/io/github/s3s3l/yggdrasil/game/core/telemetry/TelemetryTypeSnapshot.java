package io.github.s3s3l.yggdrasil.game.core.telemetry;

import java.util.Objects;

/**
 * Bounded per-type counters for a telemetry sink.
 *
 * <p>The type identifier is the class name cached for the observed class. The
 * list in {@link TelemetrySnapshot#typeCounters()} is available only when
 * detailed type telemetry is enabled.</p>
 */
public record TelemetryTypeSnapshot(
        String typeId,
        TelemetryCounters cumulative,
        TelemetryCounters frameCounters) {

    public TelemetryTypeSnapshot {
        Objects.requireNonNull(typeId, "typeId");
        Objects.requireNonNull(cumulative, "cumulative");
        Objects.requireNonNull(frameCounters, "frameCounters");
    }
}
