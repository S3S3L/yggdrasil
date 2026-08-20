package io.github.s3s3l.yggdrasil.game.core.font;

/**
 * Backend bridge for glyph coverage and measurement. Implementations may
 * allocate atlases, but callers decide the thread on which it is invoked.
 */
public interface FontGlyphProvider extends AutoCloseable {

    boolean hasGlyph(FontRequest request, int codePoint);

    FontGlyphMetrics metrics(FontRequest request, int codePoint);

    /** Discards backend atlas/cache state for a new DPI generation. */
    default void rebuild(float dpiScale) {
    }

    @Override
    default void close() {
    }
}
