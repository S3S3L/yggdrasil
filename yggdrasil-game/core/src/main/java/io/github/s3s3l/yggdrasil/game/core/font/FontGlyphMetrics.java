package io.github.s3s3l.yggdrasil.game.core.font;

import java.util.Objects;

/** Advance, ink, and line metrics used for backend-independent text alignment. */
public record FontGlyphMetrics(
        float advanceX,
        float advanceY,
        FontBounds inkBounds,
        FontMetrics fontMetrics) {

    public static final FontGlyphMetrics EMPTY =
            new FontGlyphMetrics(0f, 0f, FontBounds.EMPTY, FontMetrics.EMPTY);

    public FontGlyphMetrics {
        if (!Float.isFinite(advanceX) || !Float.isFinite(advanceY)) {
            throw new IllegalArgumentException("glyph advances must be finite");
        }
        Objects.requireNonNull(inkBounds, "inkBounds");
        Objects.requireNonNull(fontMetrics, "fontMetrics");
    }
}
