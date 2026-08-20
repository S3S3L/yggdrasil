package io.github.s3s3l.yggdrasil.game.core.font;

/** A glyph selected by {@link FontSystem}. */
public interface FontGlyph {

    int codePoint();

    FontGlyphMetrics metrics();

    default boolean isMissing() {
        return false;
    }

    default boolean isImage() {
        return false;
    }
}
