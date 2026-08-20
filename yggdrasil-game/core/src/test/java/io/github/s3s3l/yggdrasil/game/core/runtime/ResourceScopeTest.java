package io.github.s3s3l.yggdrasil.game.core.runtime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryConfig;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryLevel;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryRegistry;

class ResourceScopeTest {

    @Test
    void closesInReverseOrderAndContinuesAfterFailure() {
        List<String> calls = new ArrayList<>();
        ResourceScope scope = new ResourceScope();
        scope.register(() -> calls.add("first"));
        scope.register(() -> {
            calls.add("broken");
            throw new IllegalStateException("close failure");
        });
        scope.register(() -> calls.add("last"));

        IllegalStateException failure = assertThrows(IllegalStateException.class, scope::close);
        scope.close();

        assertEquals(List.of("last", "broken", "first"), calls);
        assertEquals("close failure", failure.getCause().getMessage());
    }

    @Test
    void immediatelyClosesResourcesRegisteredAfterClosure() {
        List<String> calls = new ArrayList<>();
        ResourceScope scope = new ResourceScope();
        scope.close();

        scope.register(() -> calls.add("late"));

        assertEquals(List.of("late"), calls);
    }

    @Test
    void recordsRegistrationCloseFailuresAndDoesNotDoubleCountScopeClosure() {
        TelemetryRegistry telemetry = TelemetryRegistry.create(new TelemetryConfig(
                TelemetryLevel.BASIC, 100L, "none", "", false, Set.of(), 10, 0L));
        ResourceScope scope = new ResourceScope(telemetry);
        scope.register(() -> {
        });
        scope.register(() -> {
            throw new IllegalStateException("broken");
        });

        telemetry.beginFrame();
        assertThrows(IllegalStateException.class, scope::close);
        scope.close();
        telemetry.endFrame(true);

        assertEquals(2L, telemetry.snapshot().cumulative().resourcesRegistered());
        assertEquals(1L, telemetry.snapshot().frameCounters().resourcesClosed());
        assertEquals(1L, telemetry.snapshot().frameCounters().resourcesCloseFailed());
        assertEquals(0L, telemetry.snapshot().gauges().resourceCount());
    }
}
