package io.github.s3s3l.yggdrasil.game.core.telemetry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Properties;
import java.util.Set;

import org.junit.jupiter.api.Test;

class TelemetryConfigTest {

    @Test
    void defaultsToDisabledTelemetry() {
        TelemetryConfig config = TelemetryConfig.fromProperties(new Properties());

        assertEquals(TelemetryLevel.OFF, config.level());
        assertEquals(TelemetryConfig.DEFAULT_INTERVAL_MILLIS, config.intervalMillis());
        assertEquals(TelemetryConfig.DEFAULT_SINK, config.sink());
        assertEquals(TelemetryConfig.DEFAULT_OUTPUT, config.output());
        assertFalse(config.jvm());
        assertEquals(Set.of(), config.types());
        assertEquals(TelemetryConfig.DEFAULT_TOP_N, config.topN());
        assertEquals(TelemetryConfig.DEFAULT_SLOW_FRAME_MILLIS, config.slowFrameMillis());
    }

    @Test
    void parsesEverySupportedSystemProperty() {
        Properties properties = new Properties();
        properties.setProperty("ygg.telemetry", "TrAcE");
        properties.setProperty("ygg.telemetry.interval", "250");
        properties.setProperty("ygg.telemetry.sink", "json");
        properties.setProperty("ygg.telemetry.output", "telemetry.json");
        properties.setProperty("ygg.telemetry.jvm", "true");
        properties.setProperty("ygg.telemetry.types", " ui.Node, sprite ,ui.Node ");
        properties.setProperty("ygg.telemetry.topN", "25");
        properties.setProperty("ygg.telemetry.slowFrameMs", "17");

        TelemetryConfig config = TelemetryConfig.fromProperties(properties);

        assertEquals(TelemetryLevel.TRACE, config.level());
        assertEquals(250L, config.intervalMillis());
        assertEquals("json", config.sink());
        assertEquals("telemetry.json", config.output());
        assertTrue(config.jvm());
        assertEquals(Set.of("ui.Node", "sprite"), config.types());
        assertEquals(25, config.topN());
        assertEquals(17L, config.slowFrameMillis());
    }

    @Test
    void ignoresInvalidSystemPropertyValues() {
        Properties properties = new Properties();
        properties.setProperty("ygg.telemetry", "verbose");
        properties.setProperty("ygg.telemetry.interval", "0");
        properties.setProperty("ygg.telemetry.sink", " ");
        properties.setProperty("ygg.telemetry.jvm", "sometimes");
        properties.setProperty("ygg.telemetry.topN", "-1");
        properties.setProperty("ygg.telemetry.slowFrameMs", "-4");

        TelemetryConfig config = TelemetryConfig.fromProperties(properties);

        assertEquals(TelemetryConfig.defaults(), config);
    }

    @Test
    void readsTheRuntimeSystemPropertySource() {
        String previous = System.getProperty(TelemetryConfig.TELEMETRY_PROPERTY);
        System.setProperty(TelemetryConfig.TELEMETRY_PROPERTY, "detailed");
        try {
            assertEquals(TelemetryLevel.DETAILED, TelemetryConfig.fromSystemProperties().level());
        } finally {
            if (previous == null) {
                System.clearProperty(TelemetryConfig.TELEMETRY_PROPERTY);
            } else {
                System.setProperty(TelemetryConfig.TELEMETRY_PROPERTY, previous);
            }
        }
    }
}
