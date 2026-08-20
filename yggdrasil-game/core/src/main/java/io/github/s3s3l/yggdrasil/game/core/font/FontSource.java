package io.github.s3s3l.yggdrasil.game.core.font;

import java.util.Objects;

/**
 * Backend-neutral description of a font face.
 *
 * <p>{@code location} is an application-defined asset identifier. A backend
 * resolves it to a file, URL, or platform font without exposing that detail to
 * core UI code. {@code faceIndex} selects a face in a font collection.</p>
 */
public record FontSource(String id, String location, int faceIndex) {

    public FontSource {
        requireText(id, "id");
        requireText(location, "location");
        if (faceIndex < 0) {
            throw new IllegalArgumentException("faceIndex must be non-negative");
        }
    }

    public FontSource(String location) {
        this(location, location, 0);
    }

    private static void requireText(String value, String name) {
        Objects.requireNonNull(value, name);
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
    }
}
