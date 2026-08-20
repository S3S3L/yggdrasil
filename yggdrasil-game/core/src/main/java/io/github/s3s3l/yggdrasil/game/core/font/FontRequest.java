package io.github.s3s3l.yggdrasil.game.core.font;

import java.util.Objects;

/** A logical face, style, and point size requested by a UI command. */
public record FontRequest(FontSource source, FontStyle style, float pointSize) {

    public FontRequest {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(style, "style");
        if (!Float.isFinite(pointSize) || pointSize <= 0f) {
            throw new IllegalArgumentException("pointSize must be finite and positive");
        }
    }

    public FontRequest withSource(FontSource fallbackSource) {
        return new FontRequest(fallbackSource, style, pointSize);
    }
}
