package io.github.s3s3l.yggdrasil.game.core.font;

import java.util.Objects;

/** A stable UI-facing handle that a runtime resolves to concrete font resources. */
public record FontHandle(String id, FontRequest request) {

    public static final FontHandle DEFAULT = new FontHandle(
            "default", new FontRequest(new FontSource("default"), FontStyle.REGULAR, 16f));

    public FontHandle {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(request, "request");
        if (id.isBlank()) {
            throw new IllegalArgumentException("id must not be blank");
        }
    }
}
