package io.github.s3s3l.yggdrasil.game.core.telemetry;

import java.util.Objects;

/**
 * Allocation-free disabled telemetry registry.
 */
public final class NoOpTelemetryRegistry implements TelemetryRegistry {

    public static final NoOpTelemetryRegistry INSTANCE = new NoOpTelemetryRegistry(TelemetryConfig.defaults());

    private final TelemetryConfig config;

    public NoOpTelemetryRegistry(TelemetryConfig config) {
        this.config = Objects.requireNonNull(config, "config");
        if (config.level() != TelemetryLevel.OFF) {
            throw new IllegalArgumentException("NoOpTelemetryRegistry requires an off configuration");
        }
    }

    @Override
    public TelemetryConfig config() {
        return config;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void register(TelemetryCollector collector) {
    }

    @Override
    public void unregister(TelemetryCollector collector) {
    }

    @Override
    public void add(TelemetryMetric metric, long amount) {
    }

    @Override
    public void set(TelemetryGauge gauge, long value) {
    }

    @Override
    public void beginFrame() {
    }

    @Override
    public void beginPhase(TelemetryFramePhase phase) {
    }

    @Override
    public void endPhase(TelemetryFramePhase phase) {
    }

    @Override
    public void endFrame(boolean completed) {
    }

    @Override
    public TelemetrySnapshot snapshot() {
        return TelemetrySnapshot.DISABLED;
    }
}
