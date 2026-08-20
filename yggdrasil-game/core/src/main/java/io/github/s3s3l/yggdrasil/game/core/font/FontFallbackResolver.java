package io.github.s3s3l.yggdrasil.game.core.font;

import java.util.Optional;

/**
 * Optional platform resolver for a font selected dynamically per code point.
 *
 * <p>Core has no macOS or native dependency. A CoreText integration can
 * implement this SPI and be supplied by an application; {@link #none()} keeps
 * fallback deterministic on every other platform.</p>
 */
@FunctionalInterface
public interface FontFallbackResolver extends AutoCloseable {

    Optional<FontSource> resolve(FontRequest request, int codePoint);

    static FontFallbackResolver none() {
        return (request, codePoint) -> Optional.empty();
    }

    @Override
    default void close() {
    }
}
