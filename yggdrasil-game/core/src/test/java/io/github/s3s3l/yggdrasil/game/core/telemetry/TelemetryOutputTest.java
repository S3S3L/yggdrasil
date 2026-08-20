package io.github.s3s3l.yggdrasil.game.core.telemetry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.junit.jupiter.api.Test;

class TelemetryOutputTest {

    @Test
    void publishesCompletedSnapshotsAtTheConfiguredMillisecondInterval() throws Exception {
        AtomicLong now = new AtomicLong();
        RecordingSink sink = new RecordingSink();
        TelemetryPublisher publisher = publisher(4, sink);
        DefaultTelemetryRegistry registry = new DefaultTelemetryRegistry(config(100L), now::get,
                publisher, null);
        try {
            endFrame(registry, now, 10L);
            await(() -> sink.count.get() == 1);

            endFrame(registry, now, 50_000_000L);
            Thread.sleep(50L);
            assertEquals(1, sink.count.get());

            endFrame(registry, now, 50_000_000L);
            await(() -> sink.count.get() == 2);
            assertEquals(3L, registry.snapshot().endedFrames());
        } finally {
            publisher.close();
        }
    }

    @Test
    void dropsInsteadOfBlockingWhenTheWriterQueueIsFull() throws Exception {
        CountDownLatch entered = new CountDownLatch(1);
        CountDownLatch release = new CountDownLatch(1);
        TelemetrySink blocking = snapshot -> {
            entered.countDown();
            assertTrue(release.await(2L, TimeUnit.SECONDS));
        };
        TelemetryPublisher publisher = publisher(1, blocking);
        try {
            publisher.publish(TelemetrySnapshot.DISABLED);
            assertTrue(entered.await(2L, TimeUnit.SECONDS));
            publisher.publish(TelemetrySnapshot.DISABLED);
            publisher.publish(TelemetrySnapshot.DISABLED);

            assertEquals(1L, publisher.droppedPublications());
        } finally {
            release.countDown();
            publisher.close();
        }
    }

    @Test
    void containsSinkFailuresAndExposesThemToTheErrorHandler() throws Exception {
        AtomicInteger failures = new AtomicInteger();
        TelemetryPublisher publisher = new TelemetryPublisher(2,
                List.of(snapshot -> {
                    throw new IllegalStateException("broken sink");
                }), ignored -> failures.incrementAndGet());
        try {
            publisher.publish(TelemetrySnapshot.DISABLED);
            await(() -> publisher.outputFailures() == 1L);
            assertEquals(1, failures.get());
        } finally {
            publisher.close();
        }
    }

    @Test
    void writesStableJsonLinesAndEscapesStrings() throws Exception {
        Path output = Path.of("target", "telemetry-output-test.jsonl");
        Files.deleteIfExists(output);
        TelemetrySnapshot snapshot = snapshotWithType("quote\" newline\n control\u0001");
        TelemetryPublisher publisher = new TelemetryPublisher(2,
                List.of(new JsonLinesTelemetrySink(output)), ignored -> {
                    throw new AssertionError(ignored);
                });
        try {
            publisher.publish(snapshot);
        } finally {
            publisher.close();
        }

        String json = Files.readString(output).strip();
        assertTrue(json.startsWith("{\"schemaVersion\":2,\"timestamp\":"));
        assertTrue(json.contains("\"endedFrames\":7"));
        assertTrue(json.contains("\"frame\":{\"number\":7"));
        assertTrue(json.contains("\"cumulative\":"));
        assertTrue(json.contains("\"gauges\":"));
        assertTrue(json.contains("\"types\":[{\"id\":\"quote\\\" newline\\n control\\u0001\""));
        assertTrue(json.contains("\"jvm\":null"));
        assertTrue(json.contains("\"droppedPublications\":3"));
        assertFalse(json.contains("quote\" newline\n"));
        assertTrue(TelemetryJson.toJson(snapshotWithType("\uD800"), "timestamp")
                .contains("\\ud800"));
        Files.deleteIfExists(output);
    }

    @Test
    void reportsJsonOutputErrorsWithoutBreakingThePublisher() throws Exception {
        Path output = Path.of("target", "missing-telemetry-parent", "telemetry.jsonl");
        Files.deleteIfExists(output);
        AtomicInteger failures = new AtomicInteger();
        TelemetryPublisher publisher = new TelemetryPublisher(2,
                List.of(new JsonLinesTelemetrySink(output)), ignored -> failures.incrementAndGet());
        try {
            publisher.publish(TelemetrySnapshot.DISABLED);
            await(() -> publisher.outputFailures() == 1L);
            assertEquals(1, failures.get());
        } finally {
            publisher.close();
        }
    }

    @Test
    void registersReadsAndSafelyUnregistersTheStableJmxBean() throws Exception {
        ObjectName objectName = new ObjectName(JmxTelemetrySink.OBJECT_NAME_TEXT);
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        if (server.isRegistered(objectName)) {
            server.unregisterMBean(objectName);
        }
        TelemetryPublisher first = publisher(2, new JmxTelemetrySink());
        TelemetryPublisher conflict = publisher(2, new JmxTelemetrySink());
        try {
            first.publish(snapshotWithType("type"));
            await(() -> server.isRegistered(objectName));
            assertEquals(7L, server.getAttribute(objectName, "FrameNumber"));
            assertEquals(2, server.getAttribute(objectName, "SchemaVersion"));

            conflict.publish(TelemetrySnapshot.DISABLED);
            await(() -> conflict.outputFailures() == 1L);
            conflict.close();
            assertTrue(server.isRegistered(objectName));
        } finally {
            conflict.close();
            first.close();
        }
        await(() -> !server.isRegistered(objectName));
    }

    @Test
    void cachesJvmSamplesAndPreservesUnavailableFields() {
        AtomicLong now = new AtomicLong();
        AtomicInteger samples = new AtomicInteger();
        JvmSampler sampler = new JvmSampler(now::get, () -> {
            samples.incrementAndGet();
            return TelemetryJvmSnapshot.UNAVAILABLE;
        }, 100L);

        assertEquals(TelemetryJvmSnapshot.UNAVAILABLE, sampler.snapshot());
        now.addAndGet(99L);
        sampler.snapshot();
        assertEquals(1, samples.get());
        now.incrementAndGet();
        sampler.snapshot();
        assertEquals(2, samples.get());

        TelemetryJvmSnapshot partial = new TelemetryJvmSnapshot(OptionalLong.of(1L),
                OptionalLong.empty(), OptionalLong.empty(), OptionalLong.empty(),
                OptionalLong.empty(), OptionalLong.empty(), OptionalLong.empty());
        assertTrue(partial.heapUsedBytes().isPresent());
        assertTrue(partial.gcCount().isEmpty());
    }

    @Test
    void closesTheWriterOnlyOnce() {
        AtomicInteger closes = new AtomicInteger();
        TelemetryPublisher publisher = publisher(2, new TelemetrySink() {
            @Override
            public void accept(TelemetrySnapshot snapshot) {
            }

            @Override
            public void close() {
                closes.incrementAndGet();
            }
        });

        publisher.close();
        publisher.close();

        assertEquals(1, closes.get());
    }

    private static TelemetryPublisher publisher(int capacity, TelemetrySink sink) {
        return new TelemetryPublisher(capacity, List.of(sink), ignored -> {
            throw new AssertionError(ignored);
        });
    }

    private static TelemetryConfig config(long intervalMillis) {
        return new TelemetryConfig(TelemetryLevel.BASIC, intervalMillis, "none", "",
                false, Set.of(), 10, 1L);
    }

    private static void endFrame(DefaultTelemetryRegistry registry, AtomicLong now, long elapsed) {
        registry.beginFrame();
        now.addAndGet(elapsed);
        registry.endFrame(true);
    }

    private static TelemetrySnapshot snapshotWithType(String typeId) {
        TelemetryFrameSnapshot frame = new TelemetryFrameSnapshot(7L, 1L, 2L, 3L, 4L, 5L,
                6L, 7L, true, true);
        return new TelemetrySnapshot(7L, frame, TelemetryCounters.ZERO, TelemetryCounters.ZERO,
                TelemetryGauges.ZERO, Optional.empty(),
                List.of(new TelemetryTypeSnapshot(typeId, TelemetryCounters.ZERO,
                        TelemetryCounters.ZERO)), 3L, 0L);
    }

    private static void await(CheckedCondition condition) throws Exception {
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(2L);
        while (!condition.value()) {
            if (System.nanoTime() >= deadline) {
                throw new AssertionError("condition was not met before timeout");
            }
            Thread.sleep(10L);
        }
    }

    @FunctionalInterface
    private interface CheckedCondition {
        boolean value() throws Exception;
    }

    private static final class RecordingSink implements TelemetrySink {

        private final AtomicInteger count = new AtomicInteger();

        @Override
        public void accept(TelemetrySnapshot snapshot) {
            count.incrementAndGet();
        }
    }
}
