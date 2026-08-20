package io.github.s3s3l.yggdrasil.game.core.telemetry;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;

/**
 * Immutable runtime telemetry configuration.
 *
 * <p>The interval is expressed in milliseconds. It controls completed-frame
 * publication cadence; it is not a frame count.</p>
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
        if (topN <= 0) {
            throw new IllegalArgumentException("topN must be positive");
        }
        if (slowFrameMillis < 0L) {
            throw new IllegalArgumentException("slowFrameMillis must not be negative");
        }
        sink = normalizeSinks(sink);
        output = output.trim();
        if (!output.isEmpty() && !hasSink(sink, "json")) {
            throw new IllegalArgumentException("output is supported only by the json sink");
        }
        if (hasSink(sink, "json") && output.isEmpty()) {
            throw new IllegalArgumentException("json sink requires a non-blank output path");
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

    /**
     * Type counters are deliberately opt-in because resolving type identities
     * is not part of the disabled or basic telemetry path.
     */
    public boolean typeMetricsEnabled() {
        return (level == TelemetryLevel.DETAILED || level == TelemetryLevel.TRACE)
                && types.contains("true");
    }

    /**
     * Returns the normalized built-in sink names in configuration order.
     */
    public Set<String> sinks() {
        if ("none".equals(sink)) {
            return Set.of();
        }
        Set<String> names = new LinkedHashSet<>();
        for (String name : sink.split(",")) {
            names.add(name);
        }
        return Collections.unmodifiableSet(names);
    }

    boolean acceptsType(Class<?> type) {
        if (!typeMetricsEnabled()) {
            return false;
        }
        if (types.size() == 1) {
            return true;
        }
        return types.contains(type.getName());
    }

    private static TelemetryConfig fromPropertySource(Function<String, String> property) {
        TelemetryConfig defaults = defaults();
        return new TelemetryConfig(
                TelemetryLevel.parse(property.apply(TELEMETRY_PROPERTY)).orElse(defaults.level()),
                positiveLong(property.apply(INTERVAL_PROPERTY), defaults.intervalMillis()),
                sink(property.apply(SINK_PROPERTY), defaults.sink()),
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

    private static String sink(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        try {
            return normalizeSinks(value);
        } catch (IllegalArgumentException ignored) {
            return fallback;
        }
    }

    private static String normalizeSinks(String value) {
        Set<String> names = new LinkedHashSet<>();
        for (String part : value.split(",")) {
            String name = part.trim().toLowerCase(java.util.Locale.ROOT);
            if (name.isEmpty()) {
                continue;
            }
            if (!name.equals("none") && !name.equals("log")
                    && !name.equals("json") && !name.equals("jmx")) {
                throw new IllegalArgumentException("unsupported telemetry sink: " + name);
            }
            names.add(name);
        }
        if (names.isEmpty()) {
            throw new IllegalArgumentException("sink must not be blank");
        }
        if (names.contains("none") && names.size() != 1) {
            throw new IllegalArgumentException("none cannot be combined with telemetry sinks");
        }
        return String.join(",", names);
    }

    private static boolean hasSink(String sinks, String name) {
        return sinks.equals(name) || sinks.startsWith(name + ",")
                || sinks.endsWith("," + name) || sinks.contains("," + name + ",");
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
