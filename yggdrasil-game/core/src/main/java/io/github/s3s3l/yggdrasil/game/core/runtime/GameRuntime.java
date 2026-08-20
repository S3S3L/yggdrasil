package io.github.s3s3l.yggdrasil.game.core.runtime;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Consumer;

import io.github.s3s3l.yggdrasil.game.core.basic.Renderable;

/**
 * Instance-owned runtime state, resource ownership, and UI callback queue.
 */
public final class GameRuntime implements AutoCloseable {

    private final Object monitor = new Object();
    private final ResourceScope resources = new ResourceScope();
    private final Queue<Runnable> callbacks = new ArrayDeque<>();
    private final Consumer<Throwable> callbackFailureHandler;
    private boolean closed;

    public GameRuntime() {
        this(ignored -> {
        });
    }

    public GameRuntime(Consumer<Throwable> callbackFailureHandler) {
        this.callbackFailureHandler = Objects.requireNonNull(callbackFailureHandler, "callbackFailureHandler");
    }

    public ResourceScope resources() {
        return resources;
    }

    /**
     * Queues work for the render/UI owner. Returns false once the runtime closes.
     */
    public boolean post(Runnable callback) {
        Objects.requireNonNull(callback, "callback");
        synchronized (monitor) {
            if (closed) {
                return false;
            }
            callbacks.offer(callback);
            return true;
        }
    }

    /**
     * Runs callbacks and the complete calculate, pre-render, render frame sequence.
     * The caller owns the thread on which this method is invoked.
     *
     * @return false if the runtime was already closed
     */
    public boolean runFrame(Renderable renderable, float delta) {
        return runFrame(renderable, delta, () -> {
        });
    }

    /**
     * Runs an action after pre-render and before render. Frontends may use this for
     * render-thread-only setup such as clearing a framebuffer.
     */
    public boolean runFrame(Renderable renderable, float delta, Runnable beforeRender) {
        Objects.requireNonNull(renderable, "renderable");
        Objects.requireNonNull(beforeRender, "beforeRender");
        synchronized (monitor) {
            if (closed) {
                return false;
            }
            drainCallbacks();
            renderable.calculate(delta);
            renderable.preRender();
            beforeRender.run();
            renderable.render();
            return true;
        }
    }

    public boolean isClosed() {
        synchronized (monitor) {
            return closed;
        }
    }

    @Override
    public void close() {
        synchronized (monitor) {
            if (closed) {
                return;
            }
            closed = true;
            callbacks.clear();
        }
        resources.close();
    }

    private void drainCallbacks() {
        Runnable callback;
        while ((callback = callbacks.poll()) != null) {
            try {
                callback.run();
            } catch (Throwable failure) {
                try {
                    callbackFailureHandler.accept(failure);
                } catch (Throwable ignored) {
                }
            }
        }
    }
}
