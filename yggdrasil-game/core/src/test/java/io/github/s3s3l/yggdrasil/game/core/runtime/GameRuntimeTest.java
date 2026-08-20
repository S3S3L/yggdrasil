package io.github.s3s3l.yggdrasil.game.core.runtime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.basic.Renderable;

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

    private static final class RecordingRenderable implements Renderable {

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
}
