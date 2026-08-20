package io.github.s3s3l.yggdrasil.game.gdx.font;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.font.FontRequest;
import io.github.s3s3l.yggdrasil.game.core.font.FontSource;
import io.github.s3s3l.yggdrasil.game.core.font.FontStyle;
import io.github.s3s3l.yggdrasil.game.gdx.runtime.GdxRuntime;

class GdxFreeTypeFontGlyphProviderTest {

    @Test
    void isGlFreeAtConstructionAndRejectsGlyphWorkOutsideTheRenderThread() {
        GdxRuntime runtime = new GdxRuntime();
        AtomicBoolean resolverCalled = new AtomicBoolean();
        GdxFreeTypeFontGlyphProvider provider = new GdxFreeTypeFontGlyphProvider(runtime, request -> {
            resolverCalled.set(true);
            return null;
        });

        assertThrows(IllegalStateException.class,
                () -> provider.hasGlyph(request(), 'A'));
        assertFalse(resolverCalled.get());

        runtime.close();
    }

    private static FontRequest request() {
        return new FontRequest(new FontSource("test.ttf"), FontStyle.REGULAR, 16f);
    }
}
