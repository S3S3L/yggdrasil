package io.github.s3s3l.yggdrasil.game.core.telemetry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.RecordComponent;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class TelemetryFrameBenchmarkTest {

    @Test
    void writesCompleteSnapshotForTheUntimedDiagnosticWorkload() throws Exception {
        TelemetrySnapshot snapshot = TelemetryFrameBenchmark.runDiagnosticWorkload();

        assertEquals(3L, snapshot.endedFrames());
        assertTrue(snapshot.frame().completed());
        assertEquals(1L, snapshot.cumulative().objectsCreated());
        assertEquals(1L, snapshot.cumulative().objectsActivated());
        assertEquals(2L, snapshot.cumulative().objectsReleased());
        assertTrue(snapshot.cumulative().objectsCalculated() >= 3L);
        assertTrue(snapshot.cumulative().objectsPreRendered() >= 3L);
        assertTrue(snapshot.cumulative().objectsRendered() >= 3L);
        assertTrue(snapshot.cumulative().objectsDirtyMarked() >= 3L);
        assertTrue(snapshot.cumulative().objectDirtyRoots() >= 3L);
        assertTrue(snapshot.cumulative().objectsDirtyPreRendered() >= 3L);
        assertEquals(2L, snapshot.cumulative().uiDrawSnapshotBuilds());
        assertEquals(1L, snapshot.cumulative().uiDrawSnapshotSkips());
        assertTrue(snapshot.cumulative().uiFrontendDrawCommandsSubmitted() >= 3L);
        assertEquals(2L, snapshot.cumulative().resourcesRegistered());
        assertEquals(2L, snapshot.cumulative().resourcesClosed());
        assertTrue(snapshot.cumulative().objectsReclaimed() >= 0L,
                "soft-reference reclamation is intentionally asynchronous");
        assertEquals(1L, snapshot.gauges().objectCount());
        assertEquals(0L, snapshot.gauges().activeObjectCount());
        assertEquals(1L, snapshot.gauges().releasedRetainedObjectCount());
        assertEquals(0L, snapshot.gauges().resourceCount());
        assertFalse(snapshot.typeCounters().isEmpty());
        assertTrue(snapshot.jvm().isPresent());

        Path output = Path.of("target", "telemetry-benchmark-test", "nested", "snapshot.json");
        Files.deleteIfExists(output);
        TelemetryFrameBenchmark.writeSnapshot(output, snapshot);
        String json = Files.readString(output);

        assertTrue(json.startsWith("{\"schemaVersion\":2,\"timestamp\":"));
        assertTrue(json.contains("\"endedFrames\":3"));
        assertTrue(json.contains("\"droppedPublications\":"));
        assertTrue(json.contains("\"outputFailures\":"));
        assertRecordFields(json, snapshot.cumulative());
        assertRecordFields(json, snapshot.frameCounters());
        assertRecordFields(json, snapshot.gauges());
        assertRecordFields(json, snapshot.jvm().orElseThrow());
        assertRecordFields(json, snapshot.typeCounters().getFirst().cumulative());
        assertRecordFields(json, snapshot.typeCounters().getFirst().frameCounters());

        Files.deleteIfExists(output);
        Files.deleteIfExists(output.getParent());
        Files.deleteIfExists(output.getParent().getParent());
    }

    private static void assertRecordFields(String json, Record value) {
        for (RecordComponent field : value.getClass().getRecordComponents()) {
            String serialized = switch (field.getName()) {
                case "frameNumber" -> "number";
                case "callbackNanos" -> "callbacksNanos";
                default -> field.getName();
            };
            assertTrue(json.contains("\"" + serialized + "\":"),
                    () -> "Missing serialized field: " + field.getName());
        }
    }
}
