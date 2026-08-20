package io.github.s3s3l.yggdrasil.game.gdx.font;

import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.s3s3l.yggdrasil.game.core.font.FontRequest;
import io.github.s3s3l.yggdrasil.game.core.font.ImageGlyph;
import io.github.s3s3l.yggdrasil.game.core.ui.UiColor;
import io.github.s3s3l.yggdrasil.game.gdx.runtime.GdxRuntime;

/**
 * Owns textures returned by an emoji/image glyph loader. The loader is invoked
 * only on the render thread; every texture is disposed exactly once on atlas
 * invalidation or runtime shutdown.
 */
public final class GdxImageGlyphSource implements GdxImageGlyphRenderer {

    @FunctionalInterface
    public interface Loader {
        Optional<LoadedImageGlyph> load(FontRequest request, int codePoint, float dpiScale);
    }

    /** A glyph paired with the texture that this source assumes ownership of. */
    public record LoadedImageGlyph(ImageGlyph glyph, Texture texture) {
        public LoadedImageGlyph {
            Objects.requireNonNull(glyph, "glyph");
            Objects.requireNonNull(texture, "texture");
        }
    }

    private final GdxRuntime runtime;
    private final Loader loader;
    private final Map<ImageKey, ImageGlyph> glyphs = new HashMap<>();
    private final Map<String, Texture> textures = new HashMap<>();
    private float dpiScale = 1f;
    private boolean closed;

    public GdxImageGlyphSource(GdxRuntime runtime, Loader loader) {
        this.runtime = Objects.requireNonNull(runtime, "runtime");
        this.loader = Objects.requireNonNull(loader, "loader");
    }

    @Override
    public synchronized Optional<ImageGlyph> resolve(FontRequest request, int codePoint) {
        requireUsable();
        ImageKey key = new ImageKey(request, codePoint);
        ImageGlyph cached = glyphs.get(key);
        if (cached != null) {
            return Optional.of(cached);
        }
        Optional<LoadedImageGlyph> loaded = loader.load(request, codePoint, dpiScale);
        if (loaded.isEmpty()) {
            return Optional.empty();
        }
        LoadedImageGlyph entry = loaded.get();
        Texture existing = textures.putIfAbsent(entry.glyph().id(), entry.texture());
        if (existing != null && existing != entry.texture()) {
            entry.texture().dispose();
        }
        glyphs.put(key, entry.glyph());
        return Optional.of(entry.glyph());
    }

    @Override
    public synchronized void draw(
            SpriteBatch batch, ImageGlyph glyph, float x, float baseline, UiColor color) {
        requireUsable();
        Objects.requireNonNull(batch, "batch");
        Objects.requireNonNull(glyph, "glyph");
        Objects.requireNonNull(color, "color");
        Texture texture = textures.get(glyph.id());
        if (texture == null) {
            throw new IllegalStateException("No owned texture for image glyph " + glyph.id());
        }
        Color previous = new Color(batch.getColor());
        try {
            batch.setColor(color.red(), color.green(), color.blue(), color.alpha());
            var bounds = glyph.metrics().inkBounds();
            batch.draw(texture, x + bounds.x(), baseline + bounds.y(), bounds.width(), bounds.height());
        } finally {
            batch.setColor(previous);
        }
    }

    @Override
    public synchronized void rebuild(float requestedDpiScale) {
        requireRenderThread();
        if (closed) {
            return;
        }
        dpiScale = validDpiScale(requestedDpiScale) ? requestedDpiScale : 1f;
        glyphs.clear();
        disposeTextures();
    }

    @Override
    public synchronized void close() {
        requireRenderThread();
        if (closed) {
            return;
        }
        closed = true;
        glyphs.clear();
        disposeTextures();
    }

    private void disposeTextures() {
        Set<Texture> disposed = Collections.newSetFromMap(new IdentityHashMap<>());
        RuntimeException failure = null;
        for (Texture texture : textures.values()) {
            if (disposed.add(texture)) {
                try {
                    texture.dispose();
                } catch (RuntimeException exception) {
                    if (failure == null) {
                        failure = exception;
                    } else {
                        failure.addSuppressed(exception);
                    }
                }
            }
        }
        textures.clear();
        if (failure != null) {
            throw failure;
        }
    }

    private void requireUsable() {
        requireRenderThread();
        if (closed || runtime.isClosed()) {
            throw new IllegalStateException("Cannot use image glyph textures after runtime closure");
        }
    }

    private void requireRenderThread() {
        runtime.requireRenderThread();
    }

    private static boolean validDpiScale(float scale) {
        return Float.isFinite(scale) && scale > 0f;
    }

    private record ImageKey(FontRequest request, int codePoint) {
    }
}
