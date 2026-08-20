package io.github.s3s3l.yggdrasil.game.gdx.runtime;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.s3s3l.yggdrasil.game.core.font.FontFallbackPolicy;
import io.github.s3s3l.yggdrasil.game.core.runtime.ResourceScope;
import io.github.s3s3l.yggdrasil.game.gdx.font.GdxFontSourceResolver;
import io.github.s3s3l.yggdrasil.game.gdx.font.GdxFontSystem;
import io.github.s3s3l.yggdrasil.game.gdx.font.GdxImageGlyphRenderer;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;

/**
 * Lazily creates resources in the owning runtime's resource scope.
 */
public final class GdxResources {

    private final GdxRuntime runtime;
    private final ResourceScope resources;
    private SpriteBatch batch;

    GdxResources(GdxRuntime runtime, ResourceScope resources) {
        this.runtime = runtime;
        this.resources = resources;
    }

    public synchronized SpriteBatch batch() {
        requireOpenRuntime();
        runtime.requireRenderThread();
        if (batch == null) {
            SpriteBatch created = new SpriteBatch();
            resources.register(() -> {
                runtime.requireRenderThread();
                created.dispose();
            });
            batch = created;
        }
        return batch;
    }

    public BitmapFont createFont(String fontPath) {
        requireOpenRuntime();
        runtime.requireRenderThread();
        BitmapFont font;
        if (StringUtils.isEmpty(fontPath)) {
            font = new BitmapFont();
        } else {
            font = new BitmapFont(Gdx.files.classpath(fontPath));
        }
        BitmapFont created = font;
        resources.register(() -> {
            runtime.requireRenderThread();
            created.dispose();
        });
        return created;
    }

    /**
     * Creates a lazily populated FreeType font system. Its atlas textures and
     * optional image-glyph textures are owned by this runtime scope.
     */
    public GdxFontSystem createFontSystem(
            FontFallbackPolicy policy, GdxFontSourceResolver sourceResolver) {
        return createFontSystem(policy, sourceResolver, null);
    }

    /**
     * Creates a FreeType font system with an optional LibGDX image/emoji atlas.
     * The image renderer is closed with the font system and must not be
     * registered separately.
     */
    public GdxFontSystem createFontSystem(
            FontFallbackPolicy policy,
            GdxFontSourceResolver sourceResolver,
            GdxImageGlyphRenderer imageGlyphRenderer) {
        requireOpenRuntime();
        runtime.requireRenderThread();
        GdxFontSystem system = new GdxFontSystem(runtime, policy, sourceResolver, imageGlyphRenderer);
        resources.register(() -> {
            runtime.requireRenderThread();
            system.close();
        });
        return system;
    }

    private void requireOpenRuntime() {
        if (runtime.isClosed()) {
            throw new IllegalStateException("Cannot create GL resources after the runtime is closed");
        }
    }
}
