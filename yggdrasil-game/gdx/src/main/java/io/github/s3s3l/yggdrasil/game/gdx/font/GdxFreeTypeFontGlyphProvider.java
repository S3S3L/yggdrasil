package io.github.s3s3l.yggdrasil.game.gdx.font;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.Hinting;

import io.github.s3s3l.yggdrasil.game.core.font.FontBounds;
import io.github.s3s3l.yggdrasil.game.core.font.FontGlyphMetrics;
import io.github.s3s3l.yggdrasil.game.core.font.FontGlyphProvider;
import io.github.s3s3l.yggdrasil.game.core.font.FontMetrics;
import io.github.s3s3l.yggdrasil.game.core.font.FontRequest;
import io.github.s3s3l.yggdrasil.game.gdx.runtime.GdxRuntime;

/**
 * Render-thread FreeType implementation with one incremental atlas per
 * concrete request and DPI generation.
 *
 * <p>FreeType face loading, bitmap-font texture upload, incremental glyph
 * upload, and disposal are all guarded by {@link GdxRuntime#requireRenderThread()}.
 * The constructor is deliberately GL-free.</p>
 */
public final class GdxFreeTypeFontGlyphProvider implements FontGlyphProvider {

    private final GdxRuntime runtime;
    private final GdxFontSourceResolver sourceResolver;
    private final Map<FontRequest, FontResource> resources = new HashMap<>();
    private float dpiScale = 1f;
    private boolean closed;

    public GdxFreeTypeFontGlyphProvider(GdxRuntime runtime, GdxFontSourceResolver sourceResolver) {
        this.runtime = Objects.requireNonNull(runtime, "runtime");
        this.sourceResolver = Objects.requireNonNull(sourceResolver, "sourceResolver");
    }

    @Override
    public synchronized boolean hasGlyph(FontRequest request, int codePoint) {
        requireUsable();
        if (!Character.isBmpCodePoint(codePoint)) {
            return false;
        }
        return resource(request).generator.hasGlyph(codePoint);
    }

    @Override
    public synchronized FontGlyphMetrics metrics(FontRequest request, int codePoint) {
        requireUsable();
        if (!Character.isBmpCodePoint(codePoint)) {
            throw new IllegalArgumentException("LibGDX BitmapFont cannot rasterize supplementary code points");
        }
        FontResource resource = resource(request);
        BitmapFont.Glyph glyph = resource.font.getData().getGlyph((char) codePoint);
        if (glyph == null) {
            throw new IllegalArgumentException(String.format(
                    "Font source '%s' does not cover U+%04X", request.source().id(), codePoint));
        }
        float logicalScale = resource.logicalScale;
        BitmapFont.BitmapFontData data = resource.font.getData();
        FontMetrics fontMetrics = new FontMetrics(
                data.ascent * logicalScale,
                data.descent * logicalScale,
                data.lineHeight * logicalScale,
                -data.ascent * logicalScale);
        return new FontGlyphMetrics(
                glyph.xadvance * logicalScale,
                0f,
                new FontBounds(
                        glyph.xoffset * logicalScale,
                        glyph.yoffset * logicalScale,
                        glyph.width * logicalScale,
                        glyph.height * logicalScale),
                fontMetrics);
    }

    /** Returns the atlas font selected by {@link io.github.s3s3l.yggdrasil.game.core.font.FontSystem}. */
    public synchronized BitmapFont font(FontRequest request) {
        requireUsable();
        return resource(request).font;
    }

    @Override
    public synchronized void rebuild(float requestedDpiScale) {
        requireRenderThread();
        if (closed) {
            return;
        }
        dpiScale = validDpiScale(requestedDpiScale) ? requestedDpiScale : 1f;
        disposeResources();
    }

    @Override
    public synchronized void close() {
        requireRenderThread();
        if (closed) {
            return;
        }
        closed = true;
        disposeResources();
    }

    private FontResource resource(FontRequest request) {
        Objects.requireNonNull(request, "request");
        return resources.computeIfAbsent(request, this::createResource);
    }

    private FontResource createResource(FontRequest request) {
        requireRenderThread();
        FileHandle file = Objects.requireNonNull(sourceResolver.resolve(request),
                "sourceResolver returned null for " + request.source().id());
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(file, request.source().faceIndex());
        try {
            int physicalSize = Math.max(1, Math.round(request.pointSize() * dpiScale));
            FreeTypeFontParameter parameter = new FreeTypeFontParameter();
            parameter.size = physicalSize;
            parameter.incremental = true;
            parameter.hinting = Hinting.AutoSlight;
            parameter.minFilter = TextureFilter.Linear;
            parameter.magFilter = TextureFilter.Linear;
            parameter.genMipMaps = false;
            BitmapFont font = generator.generateFont(parameter);
            float logicalScale = request.pointSize() / physicalSize;
            font.getData().setScale(logicalScale);
            return new FontResource(generator, font, logicalScale);
        } catch (RuntimeException failure) {
            generator.dispose();
            throw failure;
        }
    }

    private void disposeResources() {
        RuntimeException failure = null;
        for (FontResource resource : resources.values()) {
            try {
                resource.font.dispose();
            } catch (RuntimeException exception) {
                failure = append(failure, exception);
            }
            try {
                resource.generator.dispose();
            } catch (RuntimeException exception) {
                failure = append(failure, exception);
            }
        }
        resources.clear();
        if (failure != null) {
            throw failure;
        }
    }

    private void requireUsable() {
        requireRenderThread();
        if (closed || runtime.isClosed()) {
            throw new IllegalStateException("Cannot use FreeType font resources after runtime closure");
        }
    }

    private void requireRenderThread() {
        runtime.requireRenderThread();
    }

    private static boolean validDpiScale(float scale) {
        return Float.isFinite(scale) && scale > 0f;
    }

    private static RuntimeException append(RuntimeException prior, RuntimeException next) {
        if (prior == null) {
            return next;
        }
        prior.addSuppressed(next);
        return prior;
    }

    private record FontResource(
            FreeTypeFontGenerator generator,
            BitmapFont font,
            float logicalScale) {
    }
}
