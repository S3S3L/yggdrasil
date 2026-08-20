package io.github.s3s3l.yggdrasil.game.core.telemetry;

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
        Optional<TelemetryJvmSnapshot> jvm) {

    public static final TelemetrySnapshot DISABLED = new TelemetrySnapshot(
            0L, TelemetryFrameSnapshot.NONE, TelemetryCounters.ZERO,
            TelemetryCounters.ZERO, TelemetryGauges.ZERO, Optional.empty());

    public TelemetrySnapshot {
        Objects.requireNonNull(frame, "frame");
        Objects.requireNonNull(cumulative, "cumulative");
        Objects.requireNonNull(frameCounters, "frameCounters");
        Objects.requireNonNull(gauges, "gauges");
        Objects.requireNonNull(jvm, "jvm");
    }
}
