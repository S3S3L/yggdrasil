package io.github.s3s3l.yggdrasil.game.core.font;

import java.util.Objects;

/** A text glyph and the concrete request/face selected by fallback resolution. */
public record TextGlyph(FontRequest request, int codePoint, FontGlyphMetrics metrics) implements FontGlyph {

    public TextGlyph {
        Objects.requireNonNull(request, "request");
        requireCodePoint(codePoint);
        Objects.requireNonNull(metrics, "metrics");
    }

    static void requireCodePoint(int codePoint) {
        if (!Character.isValidCodePoint(codePoint)) {
            throw new IllegalArgumentException("invalid Unicode code point: " + codePoint);
        }
    }
}
