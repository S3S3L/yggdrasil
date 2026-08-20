package io.github.s3s3l.yggdrasil.game.core.telemetry;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Immutable cumulative and last-frame runtime telemetry state.
 */
public record TelemetrySnapshot(
        long endedFrames,
        TelemetryFrameSnapshot frame,
        TelemetryCounters cumulative,
        TelemetryCounters frameCounters,
        TelemetryGauges gauges,
        Optional<TelemetryJvmSnapshot> jvm,
        List<TelemetryTypeSnapshot> typeCounters) {

    public static final TelemetrySnapshot DISABLED = new TelemetrySnapshot(
            0L, TelemetryFrameSnapshot.NONE, TelemetryCounters.ZERO,
            TelemetryCounters.ZERO, TelemetryGauges.ZERO, Optional.empty(), List.of());

    public TelemetrySnapshot {
        Objects.requireNonNull(frame, "frame");
        Objects.requireNonNull(cumulative, "cumulative");
        Objects.requireNonNull(frameCounters, "frameCounters");
        Objects.requireNonNull(gauges, "gauges");
        Objects.requireNonNull(jvm, "jvm");
        typeCounters = List.copyOf(Objects.requireNonNull(typeCounters, "typeCounters"));
    }
}
