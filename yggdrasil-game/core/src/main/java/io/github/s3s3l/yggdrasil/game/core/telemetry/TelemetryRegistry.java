package io.github.s3s3l.yggdrasil.game.core.telemetry;

/**
 * Frame-confined registry for runtime telemetry and collector SPI hooks.
 */
public interface TelemetryRegistry extends AutoCloseable {

    static TelemetryRegistry create(TelemetryConfig config) {
        return create(config, TelemetryErrorHandler.stderr());
    }

    static TelemetryRegistry create(TelemetryConfig config, TelemetryErrorHandler errorHandler) {
        return config.level() == TelemetryLevel.OFF
                ? new NoOpTelemetryRegistry(config)
                : new DefaultTelemetryRegistry(config, errorHandler);
    }

    TelemetryConfig config();

    boolean isEnabled();

    /**
     * Whether bounded per-class counters are enabled for this registry.
     */
    default boolean isTypeMetricsEnabled() {
        return false;
    }

    void register(TelemetryCollector collector);

    void unregister(TelemetryCollector collector);

    void add(TelemetryMetric metric, long amount);

    default void increment(TelemetryMetric metric) {
        add(metric, 1L);
    }

    /**
     * Adds a per-type counter only. Callers also record the aggregate counter.
     */
    default void addType(TelemetryMetric metric, Class<?> type, long amount) {
    }

    default void incrementType(TelemetryMetric metric, Class<?> type) {
        addType(metric, type, 1L);
    }

    void set(TelemetryGauge gauge, long value);

    void beginFrame();

    void beginPhase(TelemetryFramePhase phase);

    void endPhase(TelemetryFramePhase phase);

    void endFrame(boolean completed);

    TelemetrySnapshot snapshot();

    /**
     * Stops asynchronous telemetry output. Implementations must be idempotent.
     */
    @Override
    default void close() {
    }
}
