package io.github.s3s3l.yggdrasil.game.gdx.runtime;

import java.util.Objects;
import java.util.function.Consumer;

import io.github.s3s3l.yggdrasil.game.core.basic.Renderable;
import io.github.s3s3l.yggdrasil.game.core.runtime.GameRuntime;

/**
 * LibGDX adapter for an instance-owned {@link GameRuntime}.
 * GL resources and commands are permitted only while a frame is running.
 */
public final class GdxRuntime implements AutoCloseable {

    private final GameRuntime runtime;
    private final GdxResources resources;
    private volatile Thread renderThread;

    public GdxRuntime() {
        this(new GameRuntime());
    }

    public GdxRuntime(Consumer<Throwable> callbackFailureHandler) {
        this(new GameRuntime(callbackFailureHandler));
    }

    public GdxRuntime(GameRuntime runtime) {
        this.runtime = Objects.requireNonNull(runtime, "runtime");
        this.resources = new GdxResources(this, runtime.resources());
    }

    public GameRuntime core() {
        return runtime;
    }

    public GdxResources resources() {
        return resources;
    }

    public boolean post(Runnable callback) {
        return runtime.post(callback);
    }

    /**
     * Runs a complete frame on the LibGDX render thread.
     */
    public boolean runFrame(Renderable renderable, float delta, Runnable beforeRender) {
        bindRenderThread();
        return runtime.runFrame(renderable, delta, beforeRender);
    }

    public void requireRenderThread() {
        if (renderThread == null || renderThread != Thread.currentThread()) {
            throw new IllegalStateException("GL operations must run on the LibGDX render thread");
        }
    }

    public boolean isClosed() {
        return runtime.isClosed();
    }

    @Override
    public void close() {
        if (renderThread != null) {
            requireRenderThread();
        }
        runtime.close();
    }

    private synchronized void bindRenderThread() {
        Thread current = Thread.currentThread();
        if (renderThread == null) {
            renderThread = current;
        } else if (renderThread != current) {
            throw new IllegalStateException("LibGDX runtime is bound to a different render thread");
        }
    }
}
