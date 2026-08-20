package io.github.s3s3l.yggdrasil.game.core.font;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Owns fallback resolution and one DPI generation of glyph coverage/metric
 * caches. It is backend-neutral; a frontend registers it in its resource scope
 * and invokes it on the thread required by its {@link FontGlyphProvider}.
 */
public final class FontSystem implements AutoCloseable {

    private final FontFallbackPolicy policy;
    private final FontGlyphProvider glyphProvider;
    private final ImageGlyphSource imageGlyphSource;
    private final Map<FontGlyphKey, Boolean> coverage = new HashMap<>();
    private final Map<FontGlyphKey, TextGlyph> textGlyphs = new HashMap<>();
    private final Map<FontGlyphKey, ImageGlyph> imageGlyphs = new HashMap<>();
    private float dpiScale = 1f;
    private boolean closed;

    public FontSystem(FontFallbackPolicy policy, FontGlyphProvider glyphProvider) {
        this(policy, glyphProvider, null);
    }

    public FontSystem(
            FontFallbackPolicy policy,
            FontGlyphProvider glyphProvider,
            ImageGlyphSource imageGlyphSource) {
        this.policy = Objects.requireNonNull(policy, "policy");
        this.glyphProvider = Objects.requireNonNull(glyphProvider, "glyphProvider");
        this.imageGlyphSource = imageGlyphSource;
    }

    public synchronized FontGlyph resolve(FontHandle handle, int codePoint) {
        Objects.requireNonNull(handle, "handle");
        return resolve(handle.request(), codePoint);
    }

    public synchronized FontGlyph resolve(FontRequest primary, int codePoint) {
        Objects.requireNonNull(primary, "primary");
        TextGlyph.requireCodePoint(codePoint);
        requireOpen();

        FontGlyphKey primaryKey = new FontGlyphKey(primary, codePoint);
        if (imageGlyphSource != null) {
            ImageGlyph cached = imageGlyphs.get(primaryKey);
            if (cached != null) {
                return cached;
            }
            Optional<ImageGlyph> imageGlyph = imageGlyphSource.resolve(primary, codePoint);
            if (imageGlyph.isPresent()) {
                imageGlyphs.put(primaryKey, imageGlyph.get());
                return imageGlyph.get();
            }
        }

        for (FontRequest candidate : policy.candidates(primary, codePoint)) {
            FontGlyphKey key = new FontGlyphKey(candidate, codePoint);
            if (!coverage.computeIfAbsent(key, ignored -> glyphProvider.hasGlyph(candidate, codePoint))) {
                continue;
            }
            return textGlyphs.computeIfAbsent(key,
                    ignored -> new TextGlyph(candidate, codePoint, glyphProvider.metrics(candidate, codePoint)));
        }
        return new MissingGlyph(codePoint);
    }

    /**
     * Invalidates all source/code-point cache entries and asks the backend to
     * rebuild its atlas generation when DPI changes.
     *
     * @return whether a rebuild occurred
     */
    public synchronized boolean updateDpiScale(float requestedDpiScale) {
        requireOpen();
        float safeDpiScale = validDpiScale(requestedDpiScale) ? requestedDpiScale : 1f;
        if (Float.compare(dpiScale, safeDpiScale) == 0) {
            return false;
        }
        dpiScale = safeDpiScale;
        clearCaches();
        glyphProvider.rebuild(dpiScale);
        if (imageGlyphSource != null) {
            imageGlyphSource.rebuild(dpiScale);
        }
        return true;
    }

    /** Explicitly invalidates cached coverage and metrics at the current DPI. */
    public synchronized void invalidate() {
        requireOpen();
        clearCaches();
        glyphProvider.rebuild(dpiScale);
        if (imageGlyphSource != null) {
            imageGlyphSource.rebuild(dpiScale);
        }
    }

    /** Call after changing a handle's point size to rebuild all cached atlases. */
    public synchronized void invalidateForFontSizeChange() {
        invalidate();
    }

    public synchronized float dpiScale() {
        return dpiScale;
    }

    public synchronized int cachedCoverageCount() {
        return coverage.size();
    }

    public synchronized int cachedGlyphCount() {
        return textGlyphs.size() + imageGlyphs.size();
    }

    public synchronized boolean isClosed() {
        return closed;
    }

    @Override
    public synchronized void close() {
        if (closed) {
            return;
        }
        closed = true;
        clearCaches();
        RuntimeException failure = null;
        try {
            if (imageGlyphSource != null) {
                imageGlyphSource.close();
            }
        } catch (Exception exception) {
            failure = new IllegalStateException("Failed to close image glyph source", exception);
        }
        try {
            glyphProvider.close();
        } catch (Exception exception) {
            if (failure == null) {
                failure = new IllegalStateException("Failed to close font glyph provider", exception);
            } else {
                failure.addSuppressed(exception);
            }
        }
        try {
            policy.close();
        } catch (RuntimeException exception) {
            if (failure == null) {
                failure = exception;
            } else {
                failure.addSuppressed(exception);
            }
        }
        if (failure != null) {
            throw failure;
        }
    }

    private void clearCaches() {
        coverage.clear();
        textGlyphs.clear();
        imageGlyphs.clear();
    }

    private void requireOpen() {
        if (closed) {
            throw new IllegalStateException("FontSystem is closed");
        }
    }

    private static boolean validDpiScale(float scale) {
        return Float.isFinite(scale) && scale > 0f;
    }
}
