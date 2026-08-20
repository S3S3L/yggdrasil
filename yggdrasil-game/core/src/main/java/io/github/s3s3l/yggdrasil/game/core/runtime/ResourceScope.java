package io.github.s3s3l.yggdrasil.game.core.runtime;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

/**
 * Owns closeable resources for one runtime and releases them in reverse order.
 */
public final class ResourceScope implements AutoCloseable {

    private final Deque<AutoCloseable> resources = new ArrayDeque<>();
    private boolean closed;

    public synchronized <T extends AutoCloseable> T register(T resource) {
        Objects.requireNonNull(resource, "resource");
        if (closed) {
            closeResource(resource);
            return resource;
        }
        resources.push(resource);
        return resource;
    }

    public synchronized boolean isClosed() {
        return closed;
    }

    @Override
    public void close() {
        Deque<AutoCloseable> toClose;
        synchronized (this) {
            if (closed) {
                return;
            }
            closed = true;
            toClose = new ArrayDeque<>(resources);
            resources.clear();
        }

        RuntimeException failure = null;
        while (!toClose.isEmpty()) {
            try {
                toClose.pop().close();
            } catch (Exception e) {
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

    private static void closeResource(AutoCloseable resource) {
        try {
            resource.close();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot register a resource with a closed scope", e);
        }
    }
}
