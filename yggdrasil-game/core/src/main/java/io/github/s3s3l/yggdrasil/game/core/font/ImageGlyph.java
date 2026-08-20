package io.github.s3s3l.yggdrasil.game.core.font;

import java.util.Objects;

/**
 * An image-backed glyph, typically emoji. Its id is resolved by a frontend
 * image atlas; core never owns or references a GPU texture.
 */
public record ImageGlyph(String id, int codePoint, FontGlyphMetrics metrics) implements FontGlyph {

    public ImageGlyph {
        Objects.requireNonNull(id, "id");
        if (id.isBlank()) {
            throw new IllegalArgumentException("id must not be blank");
        }
        TextGlyph.requireCodePoint(codePoint);
        Objects.requireNonNull(metrics, "metrics");
    }

    @Override
    public boolean isImage() {
        return true;
    }
}
