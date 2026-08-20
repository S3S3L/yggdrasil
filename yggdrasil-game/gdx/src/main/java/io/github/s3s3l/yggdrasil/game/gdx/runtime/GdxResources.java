package io.github.s3s3l.yggdrasil.game.gdx.runtime;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.s3s3l.yggdrasil.game.core.runtime.ResourceScope;
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

    private void requireOpenRuntime() {
        if (runtime.isClosed()) {
            throw new IllegalStateException("Cannot create GL resources after the runtime is closed");
        }
    }
}
