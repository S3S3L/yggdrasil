package io.github.s3s3l.yggdrasil.game.core.telemetry;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Bounded hand-off from the frame owner to one output-owning writer thread.
 */
final class TelemetryPublisher implements AutoCloseable {

    static final int DEFAULT_QUEUE_CAPACITY = 128;
    private static final long CLOSE_TIMEOUT_MILLIS = 2_000L;

    private final ArrayBlockingQueue<TelemetrySnapshot> queue;
    private final List<TelemetrySink> sinks;
    private final TelemetryErrorHandler errorHandler;
    private final ExecutorService writer;
    private final AtomicBoolean closed = new AtomicBoolean();
    private final AtomicLong droppedPublications = new AtomicLong();
    private final AtomicLong outputFailures = new AtomicLong();

    static TelemetryPublisher create(TelemetryConfig config, TelemetryErrorHandler errorHandler) {
        Objects.requireNonNull(config, "config");
        Objects.requireNonNull(errorHandler, "errorHandler");
        List<TelemetrySink> sinks = new ArrayList<>();
        for (String sink : config.sinks()) {
            switch (sink) {
                case "log" -> sinks.add(new LoggingTelemetrySink());
                case "json" -> sinks.add(new JsonLinesTelemetrySink(Path.of(config.output())));
                case "jmx" -> sinks.add(new JmxTelemetrySink());
                default -> throw new IllegalArgumentException("unsupported telemetry sink: " + sink);
            }
        }
        return sinks.isEmpty() ? null
                : new TelemetryPublisher(DEFAULT_QUEUE_CAPACITY, sinks, errorHandler);
    }

    TelemetryPublisher(int queueCapacity, List<? extends TelemetrySink> sinks,
            TelemetryErrorHandler errorHandler) {
        if (queueCapacity <= 0) {
            throw new IllegalArgumentException("queueCapacity must be positive");
        }
        this.queue = new ArrayBlockingQueue<>(queueCapacity);
        this.sinks = List.copyOf(sinks);
        if (this.sinks.isEmpty()) {
            throw new IllegalArgumentException("at least one sink is required");
        }
        this.errorHandler = Objects.requireNonNull(errorHandler, "errorHandler");
        ThreadFactory factory = runnable -> {
            Thread thread = new Thread(runnable, "yggdrasil-telemetry-writer");
            thread.setDaemon(true);
            return thread;
        };
        this.writer = Executors.newSingleThreadExecutor(factory);
        this.writer.execute(this::writeLoop);
    }

    void publish(TelemetrySnapshot snapshot) {
        if (closed.get()) {
            return;
        }
        if (!queue.offer(Objects.requireNonNull(snapshot, "snapshot"))) {
            droppedPublications.incrementAndGet();
        }
    }

    long droppedPublications() {
        return droppedPublications.get();
    }

    long outputFailures() {
        return outputFailures.get();
    }

    @Override
    public void close() {
        if (!closed.compareAndSet(false, true)) {
            return;
        }
        writer.shutdown();
        try {
            if (!writer.awaitTermination(CLOSE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {
                writer.shutdownNow();
                writer.awaitTermination(CLOSE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException interrupted) {
            writer.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void writeLoop() {
        try {
            while (!closed.get() || !queue.isEmpty()) {
                TelemetrySnapshot snapshot;
                try {
                    snapshot = queue.poll(100L, TimeUnit.MILLISECONDS);
                } catch (InterruptedException interrupted) {
                    if (closed.get()) {
                        break;
                    }
                    continue;
                }
                if (snapshot != null) {
                    write(snapshot);
                }
            }
        } finally {
            for (TelemetrySink sink : sinks) {
                try {
                    sink.close();
                } catch (Throwable failure) {
                    recordFailure(failure);
                }
            }
        }
    }

    private void write(TelemetrySnapshot snapshot) {
        for (TelemetrySink sink : sinks) {
            try {
                sink.accept(snapshot);
            } catch (Throwable failure) {
                recordFailure(failure);
            }
        }
    }

    private void recordFailure(Throwable failure) {
        try {
            errorHandler.onFailure(failure);
        } catch (Throwable ignored) {
        } finally {
            outputFailures.incrementAndGet();
        }
    }
}
