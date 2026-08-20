package io.github.s3s3l.yggdrasil.game.core.telemetry;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;

/**
 * Immutable runtime telemetry configuration.
 *
 * <p>Sink and JVM options are retained for forward compatibility. This version
 * does not install a built-in sink or collect JVM values.</p>
 */
public record TelemetryConfig(
        TelemetryLevel level,
        long intervalMillis,
        String sink,
        String output,
        boolean jvm,
        Set<String> types,
        int topN,
        long slowFrameMillis) {

    public static final String TELEMETRY_PROPERTY = "ygg.telemetry";
    public static final String INTERVAL_PROPERTY = "ygg.telemetry.interval";
    public static final String SINK_PROPERTY = "ygg.telemetry.sink";
    public static final String OUTPUT_PROPERTY = "ygg.telemetry.output";
    public static final String JVM_PROPERTY = "ygg.telemetry.jvm";
    public static final String TYPES_PROPERTY = "ygg.telemetry.types";
    public static final String TOP_N_PROPERTY = "ygg.telemetry.topN";
    public static final String SLOW_FRAME_MS_PROPERTY = "ygg.telemetry.slowFrameMs";

    public static final long DEFAULT_INTERVAL_MILLIS = 1_000L;
    public static final String DEFAULT_SINK = "none";
    public static final String DEFAULT_OUTPUT = "";
    public static final int DEFAULT_TOP_N = 10;
    public static final long DEFAULT_SLOW_FRAME_MILLIS = 0L;

    public TelemetryConfig {
        Objects.requireNonNull(level, "level");
        Objects.requireNonNull(sink, "sink");
        Objects.requireNonNull(output, "output");
        Objects.requireNonNull(types, "types");
        if (intervalMillis <= 0L) {
            throw new IllegalArgumentException("intervalMillis must be positive");
        }
        if (sink.isBlank()) {
            throw new IllegalArgumentException("sink must not be blank");
        }
        if (topN <= 0) {
            throw new IllegalArgumentException("topN must be positive");
        }
        if (slowFrameMillis < 0L) {
            throw new IllegalArgumentException("slowFrameMillis must not be negative");
        }
        types = Set.copyOf(new LinkedHashSet<>(types));
    }

    public static TelemetryConfig defaults() {
        return new TelemetryConfig(TelemetryLevel.OFF, DEFAULT_INTERVAL_MILLIS,
                DEFAULT_SINK, DEFAULT_OUTPUT, false, Set.of(), DEFAULT_TOP_N,
                DEFAULT_SLOW_FRAME_MILLIS);
    }

    public static TelemetryConfig fromSystemProperties() {
        return fromPropertySource(System::getProperty);
    }

    public static TelemetryConfig fromProperties(Properties properties) {
        Objects.requireNonNull(properties, "properties");
        return fromPropertySource(properties::getProperty);
    }

    private static TelemetryConfig fromPropertySource(Function<String, String> property) {
        TelemetryConfig defaults = defaults();
        return new TelemetryConfig(
                TelemetryLevel.parse(property.apply(TELEMETRY_PROPERTY)).orElse(defaults.level()),
                positiveLong(property.apply(INTERVAL_PROPERTY), defaults.intervalMillis()),
                text(property.apply(SINK_PROPERTY), defaults.sink()),
                text(property.apply(OUTPUT_PROPERTY), defaults.output()),
                bool(property.apply(JVM_PROPERTY), defaults.jvm()),
                types(property.apply(TYPES_PROPERTY)),
                positiveInt(property.apply(TOP_N_PROPERTY), defaults.topN()),
                nonNegativeLong(property.apply(SLOW_FRAME_MS_PROPERTY), defaults.slowFrameMillis()));
    }

    private static long positiveLong(String value, long fallback) {
        try {
            long parsed = Long.parseLong(value);
            return parsed > 0L ? parsed : fallback;
        } catch (NumberFormatException | NullPointerException ignored) {
            return fallback;
        }
    }

    private static long nonNegativeLong(String value, long fallback) {
        try {
            long parsed = Long.parseLong(value);
            return parsed >= 0L ? parsed : fallback;
        } catch (NumberFormatException | NullPointerException ignored) {
            return fallback;
        }
    }

    private static int positiveInt(String value, int fallback) {
        try {
            int parsed = Integer.parseInt(value);
            return parsed > 0 ? parsed : fallback;
        } catch (NumberFormatException | NullPointerException ignored) {
            return fallback;
        }
    }

    private static boolean bool(String value, boolean fallback) {
        if ("true".equalsIgnoreCase(value)) {
            return true;
        }
        if ("false".equalsIgnoreCase(value)) {
            return false;
        }
        return fallback;
    }

    private static String text(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim();
    }

    private static Set<String> types(String value) {
        if (value == null || value.isBlank()) {
            return Set.of();
        }
        Set<String> parsed = new LinkedHashSet<>();
        for (String type : value.split(",")) {
            if (!type.isBlank()) {
                parsed.add(type.trim());
            }
        }
        return parsed;
    }
}
