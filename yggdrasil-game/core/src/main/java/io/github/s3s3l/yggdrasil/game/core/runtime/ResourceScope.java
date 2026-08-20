package io.github.s3s3l.yggdrasil.game.core.runtime;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

import io.github.s3s3l.yggdrasil.game.core.telemetry.NoOpTelemetryRegistry;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryGauge;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryMetric;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryRegistry;

/**
 * Owns closeable resources for one runtime and releases them in reverse order.
 */
public final class ResourceScope implements AutoCloseable {

    private final Deque<ManagedResource> resources = new ArrayDeque<>();
    private final TelemetryRegistry telemetry;
    private final boolean telemetryEnabled;
    private int registeredResourceCount;
    private boolean closed;

    /**
     * Creates an independent scope without telemetry.
     */
    public ResourceScope() {
        this(NoOpTelemetryRegistry.INSTANCE);
    }

    /**
     * Creates a scope that reports resource ownership to a runtime registry.
     */
    public ResourceScope(TelemetryRegistry telemetry) {
        this.telemetry = Objects.requireNonNull(telemetry, "telemetry");
        this.telemetryEnabled = telemetry.isEnabled();
    }

    public synchronized <T extends AutoCloseable> T register(T resource) {
        Objects.requireNonNull(resource, "resource");
        if (closed) {
            closeResource(resource);
            return resource;
        }
        resources.push(new ManagedResource(resource, true));
        registeredResourceCount++;
        record(TelemetryMetric.RESOURCES_REGISTERED);
        updateResourceGauge();
        return resource;
    }

    public synchronized boolean isClosed() {
        return closed;
    }

    /**
     * Registers runtime infrastructure that shares this scope's lifetime but
     * is intentionally excluded from application resource metrics.
     */
    synchronized void manage(AutoCloseable resource) {
        Objects.requireNonNull(resource, "resource");
        if (closed) {
            closeUntracked(resource);
            return;
        }
        resources.push(new ManagedResource(resource, false));
    }

    @Override
    public void close() {
        Deque<ManagedResource> toClose;
        synchronized (this) {
            if (closed) {
                return;
            }
            closed = true;
            toClose = new ArrayDeque<>(resources);
            resources.clear();
            registeredResourceCount = 0;
            updateResourceGauge();
        }

        RuntimeException failure = null;
        while (!toClose.isEmpty()) {
            ManagedResource managed = toClose.pop();
            try {
                managed.resource().close();
                if (managed.reportMetrics()) {
                    record(TelemetryMetric.RESOURCES_CLOSED);
                }
            } catch (Exception e) {
                if (managed.reportMetrics()) {
                    record(TelemetryMetric.RESOURCES_CLOSE_FAILED);
                }
                if (failure == null) {
                    failure = new IllegalStateException("Failed to close runtime resources", e);
                } else {
                    failure.addSuppressed(e);
                }
            }
        }
        if (failure != null) {
            throw failure;
        }
    }

    private void closeResource(AutoCloseable resource) {
        try {
            resource.close();
            record(TelemetryMetric.RESOURCES_CLOSED);
        } catch (Exception e) {
            record(TelemetryMetric.RESOURCES_CLOSE_FAILED);
            throw new IllegalStateException("Cannot register a resource with a closed scope", e);
        }
    }

    private void closeUntracked(AutoCloseable resource) {
        try {
            resource.close();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot manage a resource with a closed scope", e);
        }
    }

    private void record(TelemetryMetric metric) {
        if (telemetryEnabled) {
            telemetry.increment(metric);
        }
    }

    private void updateResourceGauge() {
        if (telemetryEnabled) {
            telemetry.set(TelemetryGauge.RESOURCE_COUNT, registeredResourceCount);
        }
    }

    private record ManagedResource(AutoCloseable resource, boolean reportMetrics) {
    }
}
