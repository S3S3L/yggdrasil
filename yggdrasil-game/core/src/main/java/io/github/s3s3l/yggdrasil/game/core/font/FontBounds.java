package io.github.s3s3l.yggdrasil.game.core.font;

/** The visible ink bounds of one glyph relative to its draw origin. */
public record FontBounds(float x, float y, float width, float height) {

    public static final FontBounds EMPTY = new FontBounds(0f, 0f, 0f, 0f);

    public FontBounds {
        if (!Float.isFinite(x) || !Float.isFinite(y)
                || !Float.isFinite(width) || !Float.isFinite(height)
                || width < 0f || height < 0f) {
            throw new IllegalArgumentException("font bounds must be finite with non-negative dimensions");
        }
    }
}
