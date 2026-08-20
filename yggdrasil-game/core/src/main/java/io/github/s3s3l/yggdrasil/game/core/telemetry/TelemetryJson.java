package io.github.s3s3l.yggdrasil.game.core.telemetry;

import java.util.OptionalLong;

/**
 * Fixed-schema JSON Lines serializer deliberately kept dependency-free.
 */
final class TelemetryJson {

    static final int SCHEMA_VERSION = 1;

    private TelemetryJson() {
    }

    static String toJson(TelemetrySnapshot snapshot, String timestamp) {
        StringBuilder json = new StringBuilder(1_024);
        json.append('{');
        field(json, "schemaVersion", SCHEMA_VERSION);
        json.append(',');
        stringField(json, "timestamp", timestamp);
        json.append(',');
        json.append("\"frame\":");
        frame(json, snapshot.frame(), snapshot.frameCounters());
        json.append(',');
        json.append("\"cumulative\":");
        counters(json, snapshot.cumulative());
        json.append(',');
        json.append("\"gauges\":");
        gauges(json, snapshot.gauges());
        json.append(',');
        json.append("\"types\":[");
        for (int index = 0; index < snapshot.typeCounters().size(); index++) {
            if (index != 0) {
                json.append(',');
            }
            TelemetryTypeSnapshot type = snapshot.typeCounters().get(index);
            json.append('{');
            stringField(json, "id", type.typeId());
            json.append(',');
            json.append("\"cumulative\":");
            counters(json, type.cumulative());
            json.append(',');
            json.append("\"frame\":");
            counters(json, type.frameCounters());
            json.append('}');
        }
        json.append("],");
        json.append("\"jvm\":");
        if (snapshot.jvm().isEmpty()) {
            json.append("null");
        } else {
            jvm(json, snapshot.jvm().get());
        }
        json.append(',');
        field(json, "droppedPublications", snapshot.droppedPublications());
        json.append(',');
        field(json, "outputFailures", snapshot.outputFailures());
        json.append('}');
        return json.toString();
    }

    private static void frame(StringBuilder json, TelemetryFrameSnapshot frame,
            TelemetryCounters counters) {
        json.append('{');
        field(json, "number", frame.frameNumber());
        json.append(',');
        field(json, "startedNanos", frame.startedNanos());
        json.append(',');
        field(json, "durationNanos", frame.durationNanos());
        json.append(',');
        field(json, "completed", frame.completed());
        json.append(',');
        field(json, "slow", frame.slow());
        json.append(',');
        json.append("\"phases\":{");
        field(json, "callbacksNanos", frame.callbackNanos());
        json.append(',');
        field(json, "calculateNanos", frame.calculateNanos());
        json.append(',');
        field(json, "preRenderNanos", frame.preRenderNanos());
        json.append(',');
        field(json, "beforeRenderNanos", frame.beforeRenderNanos());
        json.append(',');
        field(json, "renderNanos", frame.renderNanos());
        json.append("},\"counters\":");
        counters(json, counters);
        json.append('}');
    }

    private static void counters(StringBuilder json, TelemetryCounters value) {
        json.append('{');
        field(json, "objectsCreated", value.objectsCreated());
        json.append(',');
        field(json, "objectsActivated", value.objectsActivated());
        json.append(',');
        field(json, "objectsReleased", value.objectsReleased());
        json.append(',');
        field(json, "objectsReclaimed", value.objectsReclaimed());
        json.append(',');
        field(json, "dirtyObjects", value.dirtyObjects());
        json.append(',');
        field(json, "resourcesAcquired", value.resourcesAcquired());
        json.append(',');
        field(json, "resourcesReleased", value.resourcesReleased());
        json.append(',');
        field(json, "cacheHits", value.cacheHits());
        json.append(',');
        field(json, "cacheMisses", value.cacheMisses());
        json.append(',');
        field(json, "cacheEvictions", value.cacheEvictions());
        json.append(',');
        field(json, "slowFrames", value.slowFrames());
        json.append(',');
        field(json, "objectsCalculated", value.objectsCalculated());
        json.append(',');
        field(json, "objectsPreRendered", value.objectsPreRendered());
        json.append(',');
        field(json, "objectsRendered", value.objectsRendered());
        json.append(',');
        field(json, "objectsDirtyMarked", value.objectsDirtyMarked());
        json.append(',');
        field(json, "objectDirtyRoots", value.objectDirtyRoots());
        json.append(',');
        field(json, "objectsDirtyPreRendered", value.objectsDirtyPreRendered());
        json.append(',');
        field(json, "uiLayoutPasses", value.uiLayoutPasses());
        json.append(',');
        field(json, "uiLayoutSkips", value.uiLayoutSkips());
        json.append(',');
        field(json, "uiDrawSnapshotBuilds", value.uiDrawSnapshotBuilds());
        json.append(',');
        field(json, "uiDrawSnapshotSkips", value.uiDrawSnapshotSkips());
        json.append(',');
        field(json, "uiCommandSnapshotNodeVisits", value.uiCommandSnapshotNodeVisits());
        json.append(',');
        field(json, "uiHitTestNodeVisits", value.uiHitTestNodeVisits());
        json.append(',');
        field(json, "uiDrawCommandsBuilt", value.uiDrawCommandsBuilt());
        json.append(',');
        field(json, "uiChildOrderCacheBuilds", value.uiChildOrderCacheBuilds());
        json.append(',');
        field(json, "uiFrontendDrawCommandsSubmitted", value.uiFrontendDrawCommandsSubmitted());
        json.append(',');
        field(json, "uiFrontendDrawNodesSubmitted", value.uiFrontendDrawNodesSubmitted());
        json.append(',');
        field(json, "uiFrontendDrawBatchesSubmitted", value.uiFrontendDrawBatchesSubmitted());
        json.append(',');
        field(json, "resourcesRegistered", value.resourcesRegistered());
        json.append(',');
        field(json, "resourcesClosed", value.resourcesClosed());
        json.append(',');
        field(json, "resourcesCloseFailed", value.resourcesCloseFailed());
        json.append('}');
    }

    private static void gauges(StringBuilder json, TelemetryGauges value) {
        json.append('{');
        field(json, "objectCount", value.objectCount());
        json.append(',');
        field(json, "activeObjectCount", value.activeObjectCount());
        json.append(',');
        field(json, "dirtyObjectCount", value.dirtyObjectCount());
        json.append(',');
        field(json, "resourceCount", value.resourceCount());
        json.append(',');
        field(json, "cacheEntryCount", value.cacheEntryCount());
        json.append(',');
        field(json, "releasedRetainedObjectCount", value.releasedRetainedObjectCount());
        json.append(',');
        field(json, "dirtyRootCount", value.dirtyRootCount());
        json.append(',');
        field(json, "uiSceneCount", value.uiSceneCount());
        json.append(',');
        field(json, "uiNodeCount", value.uiNodeCount());
        json.append(',');
        field(json, "uiDirtySceneCount", value.uiDirtySceneCount());
        json.append(',');
        field(json, "uiDirtyNodeCount", value.uiDirtyNodeCount());
        json.append('}');
    }

    private static void jvm(StringBuilder json, TelemetryJvmSnapshot value) {
        json.append('{');
        optionalField(json, "heapUsedBytes", value.heapUsedBytes());
        json.append(',');
        optionalField(json, "heapCommittedBytes", value.heapCommittedBytes());
        json.append(',');
        optionalField(json, "heapMaxBytes", value.heapMaxBytes());
        json.append(',');
        optionalField(json, "gcCount", value.gcCount());
        json.append(',');
        optionalField(json, "gcTimeMillis", value.gcTimeMillis());
        json.append(',');
        optionalField(json, "liveThreadCount", value.liveThreadCount());
        json.append(',');
        optionalField(json, "peakThreadCount", value.peakThreadCount());
        json.append('}');
    }

    private static void optionalField(StringBuilder json, String name, OptionalLong value) {
        json.append('"').append(name).append("\":");
        if (value.isPresent()) {
            json.append(value.getAsLong());
        } else {
            json.append("null");
        }
    }

    private static void stringField(StringBuilder json, String name, String value) {
        json.append('"').append(name).append("\":\"");
        appendEscaped(json, value);
        json.append('"');
    }

    private static void field(StringBuilder json, String name, long value) {
        json.append('"').append(name).append("\":").append(value);
    }

    private static void field(StringBuilder json, String name, boolean value) {
        json.append('"').append(name).append("\":").append(value);
    }

    private static void appendEscaped(StringBuilder json, String value) {
        for (int index = 0; index < value.length(); index++) {
            char character = value.charAt(index);
            switch (character) {
                case '"' -> json.append("\\\"");
                case '\\' -> json.append("\\\\");
                case '\b' -> json.append("\\b");
                case '\f' -> json.append("\\f");
                case '\n' -> json.append("\\n");
                case '\r' -> json.append("\\r");
                case '\t' -> json.append("\\t");
                default -> {
                    boolean unpairedSurrogate = Character.isHighSurrogate(character)
                            ? index + 1 == value.length()
                                    || !Character.isLowSurrogate(value.charAt(index + 1))
                            : Character.isLowSurrogate(character)
                                    && (index == 0
                                    || !Character.isHighSurrogate(value.charAt(index - 1)));
                    if (character < 0x20 || unpairedSurrogate) {
                        appendUnicodeEscape(json, character);
                    } else {
                        json.append(character);
                    }
                }
            }
        }
    }

    private static void appendUnicodeEscape(StringBuilder json, char character) {
        json.append("\\u");
        String hex = Integer.toHexString(character);
        json.append("0000", 0, 4 - hex.length()).append(hex);
    }
}
