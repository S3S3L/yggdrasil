package io.github.s3s3l.yggdrasil.game.core.telemetry;

import java.util.Objects;

/**
 * Creates the telemetry registry and its optional asynchronous output pipeline.
 *
 * <p>Applications normally let {@code GameRuntime} call this bootstrap from
 * {@link TelemetryConfig#fromSystemProperties()}. The runtime registers the
 * result with its {@code ResourceScope}, so callers must not close the returned
 * registry independently while the runtime owns it.</p>
 */
public final class TelemetryBootstrap {

    private TelemetryBootstrap() {
    }

    /**
     * Creates a registry using the documented {@code ygg.telemetry.*} system
     * properties.
     */
    public static TelemetryRegistry fromSystemProperties() {
        return create(TelemetryConfig.fromSystemProperties());
    }

    /**
     * Creates a registry for an explicit configuration.
     */
    public static TelemetryRegistry create(TelemetryConfig config) {
        return create(config, TelemetryErrorHandler.stderr());
    }

    /**
     * Creates a registry and, when configured, its bounded publisher and sinks.
     */
    public static TelemetryRegistry create(
            TelemetryConfig config, TelemetryErrorHandler errorHandler) {
        return TelemetryRegistry.create(Objects.requireNonNull(config, "config"),
                Objects.requireNonNull(errorHandler, "errorHandler"));
    }
}
