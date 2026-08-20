package io.github.s3s3l.yggdrasil.game.gdx.runtime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.basic.Renderable;
import io.github.s3s3l.yggdrasil.game.core.font.FontFallbackPolicy;
import io.github.s3s3l.yggdrasil.game.core.font.FontHandle;
import io.github.s3s3l.yggdrasil.game.gdx.font.GdxFontSystem;

class GdxRuntimeTest {

    @Test
    void runsAFrameWithoutCreatingGlResources() {
        List<String> calls = new ArrayList<>();
        GdxRuntime runtime = new GdxRuntime();

        assertTrue(runtime.runFrame(new RecordingRenderable(calls), 0f, () -> calls.add("beforeRender")));
        runtime.close();
        runtime.close();

        assertEquals(List.of("calculate", "preRender", "beforeRender", "render"), calls);
        assertFalse(runtime.post(() -> calls.add("late")));
    }

    @Test
    void rejectsGlResourcesOutsideAnActiveRenderFrame() {
        GdxRuntime runtime = new GdxRuntime();

        assertThrows(IllegalStateException.class, () -> runtime.resources().batch());
        assertThrows(IllegalStateException.class, () -> runtime.resources().createFontSystem(
                FontFallbackPolicy.primaryOnly(), request -> null));

        runtime.close();
    }

    @Test
    void scopesLazyFontSystemsAndRejectsThemAfterRuntimeClosure() {
        GdxRuntime runtime = new GdxRuntime();
        AtomicReference<GdxFontSystem> fonts = new AtomicReference<>();

        assertTrue(runtime.runFrame(new RecordingRenderable(new ArrayList<>()), 0f,
                () -> fonts.set(runtime.resources().createFontSystem(
                        FontFallbackPolicy.primaryOnly(), request -> null))));
        runtime.close();

        assertTrue(fonts.get().isClosed());
        assertThrows(IllegalStateException.class, () -> fonts.get().resolve(FontHandle.DEFAULT, 'A'));
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
            calls.add("calculate");
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
