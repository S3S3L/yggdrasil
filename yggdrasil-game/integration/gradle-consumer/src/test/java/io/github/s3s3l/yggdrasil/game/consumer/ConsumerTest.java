package io.github.s3s3l.yggdrasil.game.consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.font.FontFallbackResolvers;
import io.github.s3s3l.yggdrasil.game.core.runtime.GameRuntime;
import io.github.s3s3l.yggdrasil.game.core.ui.UiViewport;
import io.github.s3s3l.yggdrasil.game.gdx.runtime.GdxRuntime;

class ConsumerTest {

    @Test
    void consumesCoreUiAndGdxAdapterWithoutDesktopBackend() {
        UiViewport viewport = new UiViewport(0, 0, 320, 240, 160, 120);
        assertEquals(80f, viewport.toUi(160, 120).orElseThrow().x());
        assertEquals(0, FontFallbackResolvers.loadInstalled().failures().size());

        try (GameRuntime runtime = new GameRuntime(); GdxRuntime gdxRuntime = new GdxRuntime()) {
            assertEquals(false, runtime.isClosed());
            assertEquals(false, gdxRuntime.isClosed());
        }
    }
}
