package io.github.s3s3l.yggdrasil.game.core.font;

import java.util.Optional;

/**
 * Optional extension point for image/emoji glyphs.
 *
 * <p>An implementation owns any image atlas it creates. The owning
 * {@link FontSystem} calls {@link #rebuild(float)} before a DPI cache
 * generation changes and {@link #close()} at shutdown.</p>
 */
public interface ImageGlyphSource extends AutoCloseable {

    Optional<ImageGlyph> resolve(FontRequest request, int codePoint);

    default void rebuild(float dpiScale) {
    }

    @Override
    default void close() {
    }
}
