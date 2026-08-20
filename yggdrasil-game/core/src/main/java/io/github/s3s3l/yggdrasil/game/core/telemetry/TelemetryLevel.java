package io.github.s3s3l.yggdrasil.game.core.telemetry;

import java.util.Locale;
import java.util.Optional;

/**
 * The amount of telemetry collected by a runtime.
 */
public enum TelemetryLevel {
    OFF,
    BASIC,
    DETAILED,
    TRACE;

    public static Optional<TelemetryLevel> parse(String value) {
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(valueOf(value.trim().toUpperCase(Locale.ROOT)));
        } catch (IllegalArgumentException ignored) {
            return Optional.empty();
        }
    }
}
