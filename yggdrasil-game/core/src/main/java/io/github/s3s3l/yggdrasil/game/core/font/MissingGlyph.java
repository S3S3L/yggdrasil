package io.github.s3s3l.yggdrasil.game.core.font;

/** A deliberate result when neither an image source nor any font covers a code point. */
public record MissingGlyph(int codePoint) implements FontGlyph {

    public MissingGlyph {
        TextGlyph.requireCodePoint(codePoint);
    }

    @Override
    public FontGlyphMetrics metrics() {
        return FontGlyphMetrics.EMPTY;
    }

    @Override
    public boolean isMissing() {
        return true;
    }
}
