package io.github.s3s3l.yggdrasil.game.core.telemetry;

/**
 * Frame-confined registry for runtime telemetry and collector SPI hooks.
 */
public interface TelemetryRegistry {

    static TelemetryRegistry create(TelemetryConfig config) {
        return config.level() == TelemetryLevel.OFF
                ? new NoOpTelemetryRegistry(config)
                : new DefaultTelemetryRegistry(config);
    }

    TelemetryConfig config();

    boolean isEnabled();

    void register(TelemetryCollector collector);

    void unregister(TelemetryCollector collector);

    void add(TelemetryMetric metric, long amount);

    default void increment(TelemetryMetric metric) {
        add(metric, 1L);
    }

    void set(TelemetryGauge gauge, long value);

    void beginFrame();

    void beginPhase(TelemetryFramePhase phase);

    void endPhase(TelemetryFramePhase phase);

    void endFrame(boolean completed);

    TelemetrySnapshot snapshot();
}
