package io.github.s3s3l.yggdrasil.game.core.telemetry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.Test;

class DefaultTelemetryRegistryTest {

    @Test
    void resetsFrameCountersWhileCumulativeCountersRemainMonotonic() {
        AtomicLong now = new AtomicLong();
        DefaultTelemetryRegistry registry = registry(now);

        registry.add(TelemetryMetric.OBJECTS_CREATED, 4L);
        beginAndEndFrame(registry, now, 10L);
        assertEquals(4L, registry.snapshot().cumulative().objectsCreated());
        assertEquals(0L, registry.snapshot().frameCounters().objectsCreated());

        registry.beginFrame();
        registry.add(TelemetryMetric.OBJECTS_CREATED, 2L);
        registry.add(TelemetryMetric.CACHE_HITS, 3L);
        now.addAndGet(20L);
        registry.endFrame(true);

        TelemetrySnapshot second = registry.snapshot();
        assertEquals(2L, second.frame().frameNumber());
        assertEquals(6L, second.cumulative().objectsCreated());
        assertEquals(2L, second.frameCounters().objectsCreated());
        assertEquals(3L, second.frameCounters().cacheHits());
        assertEquals(2L, second.endedFrames());
    }

    @Test
    void reportsPhaseTimingSlowFramesAndCollectorsWithoutMetricMaps() {
        AtomicLong now = new AtomicLong();
        TelemetryConfig config = new TelemetryConfig(TelemetryLevel.BASIC, 100L,
                "none", "", false, java.util.Set.of(), 10, 1L);
        DefaultTelemetryRegistry registry = new DefaultTelemetryRegistry(config, now::get);
        List<TelemetrySnapshot> collected = new ArrayList<>();
        registry.register(snapshot -> {
            throw new IllegalStateException("collector failure must not affect the frame");
        });
        registry.register(collected::add);

        registry.beginFrame();
        now.addAndGet(5L);
        registry.beginPhase(TelemetryFramePhase.CALCULATE);
        now.addAndGet(7L);
        registry.endPhase(TelemetryFramePhase.CALCULATE);
        now.addAndGet(1_000_000L);
        registry.endFrame(true);

        TelemetrySnapshot snapshot = registry.snapshot();
        assertEquals(1L, snapshot.frame().frameNumber());
        assertEquals(7L, snapshot.frame().calculateNanos());
        assertTrue(snapshot.frame().slow());
        assertEquals(1L, snapshot.cumulative().slowFrames());
        assertEquals(1L, snapshot.frameCounters().slowFrames());
        assertEquals(List.of(snapshot), collected);
        assertFalse(snapshot.jvm().isPresent());
    }

    @Test
    void rejectsNestedAndMismatchedFrameBoundaries() {
        DefaultTelemetryRegistry registry = registry(new AtomicLong());

        assertThrows(IllegalStateException.class, () -> registry.endFrame(true));
        assertThrows(IllegalStateException.class,
                () -> registry.beginPhase(TelemetryFramePhase.RENDER));
        registry.beginFrame();
        assertThrows(IllegalStateException.class, registry::beginFrame);
        registry.beginPhase(TelemetryFramePhase.CALCULATE);
        assertThrows(IllegalStateException.class,
                () -> registry.endPhase(TelemetryFramePhase.RENDER));
        assertThrows(IllegalStateException.class, () -> registry.endFrame(false));
        registry.endPhase(TelemetryFramePhase.CALCULATE);
        registry.endFrame(false);
        assertFalse(registry.snapshot().frame().completed());
        assertEquals(1L, registry.snapshot().endedFrames());
    }

    @Test
    void disabledRegistryIsAReusableNoOp() {
        TelemetryConfig config = TelemetryConfig.defaults();
        TelemetryRegistry registry = TelemetryRegistry.create(config);

        assertTrue(registry instanceof NoOpTelemetryRegistry);
        assertEquals(config, registry.config());
        registry.increment(TelemetryMetric.OBJECTS_CREATED);
        registry.beginFrame();
        registry.beginPhase(TelemetryFramePhase.RENDER);
        registry.endPhase(TelemetryFramePhase.RENDER);
        registry.endFrame(true);

        assertFalse(registry.isEnabled());
        assertEquals(TelemetrySnapshot.DISABLED, registry.snapshot());
    }

    private static DefaultTelemetryRegistry registry(AtomicLong now) {
        return new DefaultTelemetryRegistry(new TelemetryConfig(TelemetryLevel.BASIC, 100L,
                "none", "", false, java.util.Set.of(), 10, 0L), now::get);
    }

    private static void beginAndEndFrame(DefaultTelemetryRegistry registry, AtomicLong now, long elapsed) {
        registry.beginFrame();
        now.addAndGet(elapsed);
        registry.endFrame(true);
    }
}
