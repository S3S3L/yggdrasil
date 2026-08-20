package io.github.s3s3l.yggdrasil.game.core.runtime;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Consumer;

import io.github.s3s3l.yggdrasil.game.core.basic.Renderable;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryConfig;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryBootstrap;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryErrorHandler;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryFramePhase;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryRegistry;

/**
 * Instance-owned runtime state, resource ownership, and UI callback queue.
 */
public final class GameRuntime implements AutoCloseable {

    private final Object monitor = new Object();
    private final ResourceScope resources;
    private final Queue<Runnable> callbacks = new ArrayDeque<>();
    private final Consumer<Throwable> callbackFailureHandler;
    private final TelemetryConfig telemetryConfig;
    private final TelemetryRegistry telemetry;
    private boolean closed;

    public GameRuntime() {
        this(ignored -> {
        }, TelemetryBootstrap.fromSystemProperties());
    }

    public GameRuntime(Consumer<Throwable> callbackFailureHandler) {
        this(callbackFailureHandler, TelemetryBootstrap.fromSystemProperties());
    }

    public GameRuntime(TelemetryConfig telemetryConfig) {
        this(ignored -> {
        }, telemetryConfig);
    }

    public GameRuntime(Consumer<Throwable> callbackFailureHandler, TelemetryConfig telemetryConfig) {
        this(callbackFailureHandler, telemetryConfig, TelemetryErrorHandler.stderr());
    }

    public GameRuntime(Consumer<Throwable> callbackFailureHandler, TelemetryConfig telemetryConfig,
            TelemetryErrorHandler telemetryErrorHandler) {
        this.callbackFailureHandler = Objects.requireNonNull(callbackFailureHandler, "callbackFailureHandler");
        this.telemetryConfig = Objects.requireNonNull(telemetryConfig, "telemetryConfig");
        this.telemetry = TelemetryBootstrap.create(telemetryConfig,
                Objects.requireNonNull(telemetryErrorHandler, "telemetryErrorHandler"));
        this.resources = new ResourceScope(telemetry);
        this.resources.manage(telemetry);
    }

    public GameRuntime(Consumer<Throwable> callbackFailureHandler, TelemetryRegistry telemetry) {
        this.callbackFailureHandler = Objects.requireNonNull(callbackFailureHandler, "callbackFailureHandler");
        this.telemetry = Objects.requireNonNull(telemetry, "telemetry");
        this.telemetryConfig = telemetry.config();
        this.resources = new ResourceScope(telemetry);
        this.resources.manage(telemetry);
    }

    public ResourceScope resources() {
        return resources;
    }

    public TelemetryConfig telemetryConfig() {
        return telemetryConfig;
    }

    public TelemetryRegistry telemetry() {
        return telemetry;
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
            if (!telemetry.isEnabled()) {
                drainCallbacks();
                renderable.calculate(delta);
                renderable.preRender();
                beforeRender.run();
                renderable.render();
                return true;
            }
            telemetry.beginFrame();
            boolean completed = false;
            try {
                telemetry.beginPhase(TelemetryFramePhase.CALLBACKS);
                try {
                    drainCallbacks();
                } finally {
                    telemetry.endPhase(TelemetryFramePhase.CALLBACKS);
                }
                telemetry.beginPhase(TelemetryFramePhase.CALCULATE);
                try {
                    renderable.calculate(delta);
                } finally {
                    telemetry.endPhase(TelemetryFramePhase.CALCULATE);
                }
                telemetry.beginPhase(TelemetryFramePhase.PRE_RENDER);
                try {
                    renderable.preRender();
                } finally {
                    telemetry.endPhase(TelemetryFramePhase.PRE_RENDER);
                }
                telemetry.beginPhase(TelemetryFramePhase.BEFORE_RENDER);
                try {
                    beforeRender.run();
                } finally {
                    telemetry.endPhase(TelemetryFramePhase.BEFORE_RENDER);
                }
                telemetry.beginPhase(TelemetryFramePhase.RENDER);
                try {
                    renderable.render();
                } finally {
                    telemetry.endPhase(TelemetryFramePhase.RENDER);
                }
                completed = true;
                return true;
            } finally {
                telemetry.endFrame(completed);
            }
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
