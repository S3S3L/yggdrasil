package io.github.s3s3l.yggdrasil.game.core.telemetry;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.util.List;
import java.util.OptionalLong;
import java.util.function.LongSupplier;

/**
 * Caches process-wide management-bean reads so frame processing never samples
 * the JVM more frequently than one second or the configured publication
 * interval, whichever is longer.
 */
final class JvmSampler {

    private static final long MINIMUM_INTERVAL_NANOS = 1_000_000_000L;

    private final LongSupplier nanoTime;
    private final JvmMetricsSource source;
    private final long intervalNanos;
    private boolean sampled;
    private long sampledAtNanos;
    private TelemetryJvmSnapshot latest = TelemetryJvmSnapshot.UNAVAILABLE;

    JvmSampler(LongSupplier nanoTime, long configuredIntervalMillis) {
        this(nanoTime, systemSource(), Math.max(MINIMUM_INTERVAL_NANOS,
                millisecondsToNanos(configuredIntervalMillis)));
    }

    JvmSampler(LongSupplier nanoTime, JvmMetricsSource source, long intervalNanos) {
        this.nanoTime = java.util.Objects.requireNonNull(nanoTime, "nanoTime");
        this.source = java.util.Objects.requireNonNull(source, "source");
        this.intervalNanos = Math.max(1L, intervalNanos);
    }

    synchronized TelemetryJvmSnapshot snapshot() {
        long now = nanoTime.getAsLong();
        if (!sampled || now < sampledAtNanos || now - sampledAtNanos >= intervalNanos) {
            try {
                latest = source.sample();
            } catch (Throwable ignored) {
                latest = TelemetryJvmSnapshot.UNAVAILABLE;
            }
            sampled = true;
            sampledAtNanos = now;
        }
        return latest;
    }

    private static JvmMetricsSource systemSource() {
        MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
        List<GarbageCollectorMXBean> collectors = ManagementFactory.getGarbageCollectorMXBeans();
        ThreadMXBean threads = ManagementFactory.getThreadMXBean();
        return () -> {
            MemoryUsage heap;
            try {
                heap = memory.getHeapMemoryUsage();
            } catch (RuntimeException unavailable) {
                heap = null;
            }
            return new TelemetryJvmSnapshot(
                    heap == null ? OptionalLong.empty() : nonNegative(heap.getUsed()),
                    heap == null ? OptionalLong.empty() : nonNegative(heap.getCommitted()),
                    heap == null ? OptionalLong.empty() : nonNegative(heap.getMax()),
                    collectorTotal(collectors, GarbageCollectorMXBean::getCollectionCount),
                    collectorTotal(collectors, GarbageCollectorMXBean::getCollectionTime),
                    threadValue(threads::getThreadCount),
                    threadValue(threads::getPeakThreadCount));
        };
    }

    private static OptionalLong collectorTotal(List<GarbageCollectorMXBean> collectors,
            java.util.function.ToLongFunction<GarbageCollectorMXBean> reader) {
        long total = 0L;
        for (GarbageCollectorMXBean collector : collectors) {
            long value;
            try {
                value = reader.applyAsLong(collector);
            } catch (RuntimeException unavailable) {
                return OptionalLong.empty();
            }
            if (value < 0L || Long.MAX_VALUE - total < value) {
                return OptionalLong.empty();
            }
            total += value;
        }
        return OptionalLong.of(total);
    }

    private static OptionalLong threadValue(java.util.function.IntSupplier reader) {
        try {
            return nonNegative(reader.getAsInt());
        } catch (RuntimeException unavailable) {
            return OptionalLong.empty();
        }
    }

    private static OptionalLong nonNegative(long value) {
        return value < 0L ? OptionalLong.empty() : OptionalLong.of(value);
    }

    private static long millisecondsToNanos(long milliseconds) {
        return milliseconds > Long.MAX_VALUE / 1_000_000L
                ? Long.MAX_VALUE : milliseconds * 1_000_000L;
    }

    @FunctionalInterface
    interface JvmMetricsSource {
        TelemetryJvmSnapshot sample();
    }
}
