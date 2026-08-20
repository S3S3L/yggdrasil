package io.github.s3s3l.yggdrasil.game.core.telemetry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;

class TelemetryBootstrapTest {

    @Test
    void createsAnEnabledRegistryFromDocumentedSystemProperties() {
        String previous = System.getProperty(TelemetryConfig.TELEMETRY_PROPERTY);
        System.setProperty(TelemetryConfig.TELEMETRY_PROPERTY, "basic");
        try (TelemetryRegistry telemetry = TelemetryBootstrap.fromSystemProperties()) {
            assertEquals(TelemetryLevel.BASIC, telemetry.config().level());
            assertInstanceOf(DefaultTelemetryRegistry.class, telemetry);
        } finally {
            if (previous == null) {
                System.clearProperty(TelemetryConfig.TELEMETRY_PROPERTY);
            } else {
                System.setProperty(TelemetryConfig.TELEMETRY_PROPERTY, previous);
            }
        }
    }
}
