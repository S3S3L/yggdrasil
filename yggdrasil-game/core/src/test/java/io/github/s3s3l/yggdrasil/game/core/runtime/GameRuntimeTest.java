package io.github.s3s3l.yggdrasil.game.core.runtime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.basic.Renderable;
import io.github.s3s3l.yggdrasil.game.core.telemetry.NoOpTelemetryRegistry;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryConfig;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryFramePhase;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryGauge;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryLevel;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryMetric;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryRegistry;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetrySnapshot;

class GameRuntimeTest {

    @Test
    void runsCallbacksAndEveryFramePhaseInOrder() {
        List<String> calls = new ArrayList<>();
        GameRuntime runtime = new GameRuntime();
        runtime.post(() -> calls.add("callback"));

        assertTrue(runtime.runFrame(new RecordingRenderable(calls), 0.25f));

        assertEquals(List.of("callback", "calculate:0.25", "preRender", "render"), calls);
    }

    @Test
    void isolatesCallbackFailuresAndContinuesDraining() {
        List<String> calls = new ArrayList<>();
        List<Throwable> failures = new ArrayList<>();
        GameRuntime runtime = new GameRuntime(failures::add);
        runtime.post(() -> {
            throw new IllegalStateException("broken");
        });
        runtime.post(() -> calls.add("after failure"));

        runtime.runFrame(new RecordingRenderable(calls), 0f);

        assertEquals(List.of("after failure", "calculate:0.0", "preRender", "render"), calls);
        assertEquals(1, failures.size());
        assertEquals("broken", failures.get(0).getMessage());
    }

    @Test
    void discardsQueuedAndSubsequentCallbacksAfterClose() {
        List<String> calls = new ArrayList<>();
        GameRuntime runtime = new GameRuntime();
        runtime.post(() -> calls.add("queued"));

        runtime.close();
        runtime.close();

        assertFalse(runtime.post(() -> calls.add("late")));
        assertFalse(runtime.runFrame(new RecordingRenderable(calls), 0f));
        assertTrue(calls.isEmpty());
    }

    @Test
    void canCreateAndCloseIndependentRuntimesRepeatedly() {
        List<String> calls = new ArrayList<>();

        for (int index = 0; index < 3; index++) {
            GameRuntime runtime = new GameRuntime();
            int runtimeIndex = index;
            runtime.resources().register(() -> calls.add("close-" + runtimeIndex));
            runtime.close();
        }

        assertEquals(List.of("close-0", "close-1", "close-2"), calls);
    }

    @Test
    void explicitTelemetryConfigOverridesSystemProperties() {
        String property = System.getProperty(TelemetryConfig.TELEMETRY_PROPERTY);
        System.setProperty(TelemetryConfig.TELEMETRY_PROPERTY, "trace");
        try {
            GameRuntime runtime = new GameRuntime(TelemetryConfig.defaults());

            assertEquals(TelemetryLevel.OFF, runtime.telemetryConfig().level());
            assertTrue(runtime.telemetry() instanceof NoOpTelemetryRegistry);
        } finally {
            if (property == null) {
                System.clearProperty(TelemetryConfig.TELEMETRY_PROPERTY);
            } else {
                System.setProperty(TelemetryConfig.TELEMETRY_PROPERTY, property);
            }
        }
    }

    @Test
    void completesTelemetryFrameWhenEachFrameStageFails() {
        TelemetryConfig config = basicTelemetry();
        for (FailurePhase phase : FailurePhase.values()) {
            GameRuntime runtime = new GameRuntime(config);

            assertThrows(IllegalStateException.class,
                    () -> runtime.runFrame(new FailingRenderable(phase), 0f, () -> {
                        if (phase == FailurePhase.BEFORE_RENDER) {
                            throw new IllegalStateException(phase.name());
                        }
                    }));

            assertEquals(1L, runtime.telemetry().snapshot().endedFrames(), phase.name());
            assertFalse(runtime.telemetry().snapshot().frame().completed(), phase.name());
            assertTrue(runtime.runFrame(new RecordingRenderable(new ArrayList<>()), 0f), phase.name());
            assertEquals(2L, runtime.telemetry().snapshot().frame().frameNumber(), phase.name());
        }
    }

    @Test
    void completesTelemetryFrameWhenCallbackFails() {
        GameRuntime runtime = new GameRuntime(ignored -> {
        }, basicTelemetry());
        runtime.post(() -> {
            throw new IllegalStateException("callback");
        });

        assertTrue(runtime.runFrame(new RecordingRenderable(new ArrayList<>()), 0f));
        assertEquals(1L, runtime.telemetry().snapshot().endedFrames());
        assertTrue(runtime.telemetry().snapshot().frame().completed());
    }

    @Test
    void rejectsNestedFrameButClosesTheOuterTelemetryFrame() {
        GameRuntime runtime = new GameRuntime(basicTelemetry());
        Renderable nested = new RecordingRenderable(new ArrayList<>()) {
            @Override
            public void calculate(float delta) {
                runtime.runFrame(new RecordingRenderable(new ArrayList<>()), delta);
            }
        };

        assertThrows(IllegalStateException.class, () -> runtime.runFrame(nested, 0f));
        assertEquals(1L, runtime.telemetry().snapshot().endedFrames());
        assertFalse(runtime.telemetry().snapshot().frame().completed());
    }

    @Test
    void neverStartsTelemetryAfterClose() {
        GameRuntime runtime = new GameRuntime(basicTelemetry());
        runtime.close();

        assertFalse(runtime.runFrame(new RecordingRenderable(new ArrayList<>()), 0f));
        assertEquals(0L, runtime.telemetry().snapshot().endedFrames());
    }

    @Test
    void endsTheActivePhaseBeforeCompletingAnExceptionalFrame() {
        List<String> events = new ArrayList<>();
        GameRuntime runtime = new GameRuntime(ignored -> { }, new PhaseTrackingRegistry(events));
        Renderable failing = new RecordingRenderable(new ArrayList<>()) {
            @Override
            public void calculate(float delta) {
                throw new IllegalStateException("calculate");
            }
        };

        assertThrows(IllegalStateException.class, () -> runtime.runFrame(failing, 0f));

        assertEquals(List.of("beginFrame", "begin:CALLBACKS", "end:CALLBACKS",
                "begin:CALCULATE", "end:CALCULATE", "endFrame:false"), events);
        runtime.close();
    }

    private static TelemetryConfig basicTelemetry() {
        Properties properties = new Properties();
        properties.setProperty(TelemetryConfig.TELEMETRY_PROPERTY, "basic");
        return TelemetryConfig.fromProperties(properties);
    }

    private enum FailurePhase {
        CALCULATE,
        PRE_RENDER,
        BEFORE_RENDER,
        RENDER
    }

    private static final class FailingRenderable extends RecordingRenderable {

        private final FailurePhase failurePhase;

        private FailingRenderable(FailurePhase failurePhase) {
            super(new ArrayList<>());
            this.failurePhase = failurePhase;
        }

        @Override
        public void calculate(float delta) {
            fail(FailurePhase.CALCULATE);
        }

        @Override
        public void preRender() {
            fail(FailurePhase.PRE_RENDER);
        }

        @Override
        public void render() {
            fail(FailurePhase.RENDER);
        }

        private void fail(FailurePhase phase) {
            if (failurePhase == phase) {
                throw new IllegalStateException(phase.name());
            }
        }
    }

    private static class RecordingRenderable implements Renderable {

        private final List<String> calls;

        private RecordingRenderable(List<String> calls) {
            this.calls = calls;
        }

        @Override
        public long getId() {
            return 1L;
        }

        @Override
        public void setId(long id) {
        }

        @Override
        public void onWindowResize(int width, int height) {
        }

        @Override
        public void calculate(float delta) {
            calls.add("calculate:" + delta);
        }

        @Override
        public void preRender() {
            calls.add("preRender");
        }

        @Override
        public void render() {
            calls.add("render");
        }

        @Override
        public boolean isDirty() {
            return false;
        }

        @Override
        public void markDirty() {
        }

        @Override
        public void clearDirty() {
        }
    }

    private static final class PhaseTrackingRegistry implements TelemetryRegistry {

        private final List<String> events;

        private PhaseTrackingRegistry(List<String> events) {
            this.events = events;
        }

        @Override
        public TelemetryConfig config() {
            return basicTelemetry();
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public void register(io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryCollector collector) {
        }

        @Override
        public void unregister(io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryCollector collector) {
        }

        @Override
        public void add(TelemetryMetric metric, long amount) {
        }

        @Override
        public void set(TelemetryGauge gauge, long value) {
        }

        @Override
        public void beginFrame() {
            events.add("beginFrame");
        }

        @Override
        public void beginPhase(TelemetryFramePhase phase) {
            events.add("begin:" + phase);
        }

        @Override
        public void endPhase(TelemetryFramePhase phase) {
            events.add("end:" + phase);
        }

        @Override
        public void endFrame(boolean completed) {
            events.add("endFrame:" + completed);
        }

        @Override
        public TelemetrySnapshot snapshot() {
            return TelemetrySnapshot.DISABLED;
        }
    }
}
