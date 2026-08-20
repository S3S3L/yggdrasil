package io.github.s3s3l.yggdrasil.game.gdx.font;

import java.util.Objects;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.s3s3l.yggdrasil.game.core.font.FontFallbackPolicy;
import io.github.s3s3l.yggdrasil.game.core.font.FontGlyph;
import io.github.s3s3l.yggdrasil.game.core.font.FontHandle;
import io.github.s3s3l.yggdrasil.game.core.font.FontSystem;
import io.github.s3s3l.yggdrasil.game.core.font.ImageGlyph;
import io.github.s3s3l.yggdrasil.game.core.font.ImageGlyphSource;
import io.github.s3s3l.yggdrasil.game.core.font.TextGlyph;
import io.github.s3s3l.yggdrasil.game.core.ui.UiColor;
import io.github.s3s3l.yggdrasil.game.gdx.runtime.GdxRuntime;

/**
 * Render-thread adapter that resolves core {@link FontHandle}s to FreeType
 * atlases. It is resource-scoped by {@code GdxResources}; no static font or
 * texture is created.
 */
public final class GdxFontSystem implements AutoCloseable {

    private final GdxRuntime runtime;
    private final GdxFreeTypeFontGlyphProvider freeType;
    private final GdxImageGlyphRenderer imageRenderer;
    private final FontSystem fonts;
    private boolean closed;

    public GdxFontSystem(
            GdxRuntime runtime,
            FontFallbackPolicy policy,
            GdxFontSourceResolver sourceResolver) {
        this(runtime, policy, sourceResolver, null);
    }

    public GdxFontSystem(
            GdxRuntime runtime,
            FontFallbackPolicy policy,
            GdxFontSourceResolver sourceResolver,
            ImageGlyphSource imageGlyphSource) {
        this.runtime = Objects.requireNonNull(runtime, "runtime");
        freeType = new GdxFreeTypeFontGlyphProvider(runtime, sourceResolver);
        imageRenderer = imageGlyphSource instanceof GdxImageGlyphRenderer renderer ? renderer : null;
        fonts = new FontSystem(Objects.requireNonNull(policy, "policy"), freeType, imageGlyphSource);
    }

    public FontSystem core() {
        return fonts;
    }

    public FontGlyph resolve(FontHandle handle, int codePoint) {
        requireUsable();
        return fonts.resolve(handle, codePoint);
    }

    public boolean updateDpiScale(float dpiScale) {
        requireUsable();
        return fonts.updateDpiScale(dpiScale);
    }

    /** Releases the current atlas generation after a logical point-size change. */
    public void invalidateForFontSizeChange() {
        requireUsable();
        fonts.invalidateForFontSizeChange();
    }

    /** Draws fallback runs by their selected atlas without exposing BitmapFont to core UI. */
    public void draw(
            SpriteBatch batch, FontHandle handle, String text, float x, float baseline, UiColor color) {
        requireUsable();
        Objects.requireNonNull(batch, "batch");
        Objects.requireNonNull(handle, "handle");
        Objects.requireNonNull(text, "text");
        Objects.requireNonNull(color, "color");
        float cursor = x;
        for (int offset = 0; offset < text.length();) {
            int codePoint = text.codePointAt(offset);
            FontGlyph glyph = fonts.resolve(handle, codePoint);
            if (glyph instanceof TextGlyph textGlyph) {
                BitmapFont font = freeType.font(textGlyph.request());
                font.setColor(color.red(), color.green(), color.blue(), color.alpha());
                font.draw(batch, new String(Character.toChars(codePoint)), cursor, baseline);
            } else if (glyph instanceof ImageGlyph imageGlyph) {
                if (imageRenderer == null) {
                    throw new IllegalStateException("Image glyph resolved without a LibGDX image renderer");
                }
                imageRenderer.draw(batch, imageGlyph, cursor, baseline, color);
            }
            cursor += glyph.metrics().advanceX();
            offset += Character.charCount(codePoint);
        }
    }

    public boolean isClosed() {
        return closed;
    }

    @Override
    public void close() {
        runtime.requireRenderThread();
        if (closed) {
            return;
        }
        closed = true;
        fonts.close();
    }

    private void requireUsable() {
        runtime.requireRenderThread();
        if (closed || runtime.isClosed()) {
            throw new IllegalStateException("Cannot use GdxFontSystem after runtime closure");
        }
    }
}
