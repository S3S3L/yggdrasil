package io.github.s3s3l.yggdrasil.game.core.telemetry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.LongSupplier;

/**
 * Fixed-schema, frame-confined telemetry registry.
 */
public final class DefaultTelemetryRegistry implements TelemetryRegistry {

    private final TelemetryConfig config;
    private final LongSupplier nanoTime;
    private final List<TelemetryCollector> collectors = new ArrayList<>();
    private final MutableCounters cumulative = new MutableCounters();
    private final MutableCounters currentFrameCounters = new MutableCounters();
    private final MutableGauges gauges = new MutableGauges();
    private final TypeCounters typeCounters;

    private long nextFrameNumber;
    private long endedFrames;
    private boolean frameActive;
    private long frameStartedNanos;
    private TelemetryFramePhase activePhase;
    private long phaseStartedNanos;
    private long callbackNanos;
    private long calculateNanos;
    private long preRenderNanos;
    private long beforeRenderNanos;
    private long renderNanos;
    private TelemetryFrameSnapshot lastFrame = TelemetryFrameSnapshot.NONE;
    private TelemetryCounters lastFrameCounters = TelemetryCounters.ZERO;

    public DefaultTelemetryRegistry(TelemetryConfig config) {
        this(config, System::nanoTime);
    }

    DefaultTelemetryRegistry(TelemetryConfig config, LongSupplier nanoTime) {
        this.config = Objects.requireNonNull(config, "config");
        if (config.level() == TelemetryLevel.OFF) {
            throw new IllegalArgumentException("DefaultTelemetryRegistry requires an enabled configuration");
        }
        this.nanoTime = Objects.requireNonNull(nanoTime, "nanoTime");
        this.typeCounters = config.typeMetricsEnabled() ? new TypeCounters(config.topN()) : null;
    }

    @Override
    public TelemetryConfig config() {
        return config;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isTypeMetricsEnabled() {
        return typeCounters != null;
    }

    @Override
    public void register(TelemetryCollector collector) {
        collectors.add(Objects.requireNonNull(collector, "collector"));
    }

    @Override
    public void unregister(TelemetryCollector collector) {
        collectors.remove(collector);
    }

    @Override
    public void add(TelemetryMetric metric, long amount) {
        Objects.requireNonNull(metric, "metric");
        if (amount < 0L) {
            throw new IllegalArgumentException("amount must not be negative");
        }
        cumulative.add(metric, amount);
        if (frameActive) {
            currentFrameCounters.add(metric, amount);
        }
    }

    @Override
    public void addType(TelemetryMetric metric, Class<?> type, long amount) {
        Objects.requireNonNull(metric, "metric");
        Objects.requireNonNull(type, "type");
        if (amount < 0L) {
            throw new IllegalArgumentException("amount must not be negative");
        }
        if (typeCounters != null && config.acceptsType(type)) {
            typeCounters.add(metric, type, amount, frameActive);
        }
    }

    @Override
    public void set(TelemetryGauge gauge, long value) {
        Objects.requireNonNull(gauge, "gauge");
        if (value < 0L) {
            throw new IllegalArgumentException("value must not be negative");
        }
        gauges.set(gauge, value);
    }

    @Override
    public void beginFrame() {
        if (frameActive) {
            throw new IllegalStateException("A telemetry frame is already active");
        }
        currentFrameCounters.clear();
        if (typeCounters != null) {
            typeCounters.clearFrame();
        }
        callbackNanos = 0L;
        calculateNanos = 0L;
        preRenderNanos = 0L;
        beforeRenderNanos = 0L;
        renderNanos = 0L;
        frameStartedNanos = nanoTime.getAsLong();
        nextFrameNumber++;
        frameActive = true;
    }

    @Override
    public void beginPhase(TelemetryFramePhase phase) {
        Objects.requireNonNull(phase, "phase");
        requireActiveFrame();
        if (activePhase != null) {
            throw new IllegalStateException("Telemetry phase " + activePhase + " is already active");
        }
        activePhase = phase;
        phaseStartedNanos = nanoTime.getAsLong();
    }

    @Override
    public void endPhase(TelemetryFramePhase phase) {
        Objects.requireNonNull(phase, "phase");
        requireActiveFrame();
        if (activePhase != phase) {
            throw new IllegalStateException("Telemetry phase " + phase + " is not active");
        }
        long elapsed = elapsedSince(phaseStartedNanos);
        switch (phase) {
            case CALLBACKS -> callbackNanos += elapsed;
            case CALCULATE -> calculateNanos += elapsed;
            case PRE_RENDER -> preRenderNanos += elapsed;
            case BEFORE_RENDER -> beforeRenderNanos += elapsed;
            case RENDER -> renderNanos += elapsed;
        }
        activePhase = null;
    }

    @Override
    public void endFrame(boolean completed) {
        requireActiveFrame();
        if (activePhase != null) {
            throw new IllegalStateException("Telemetry phase " + activePhase + " was not ended");
        }
        long duration = elapsedSince(frameStartedNanos);
        boolean slow = config.slowFrameMillis() > 0L
                && duration >= millisecondsToNanos(config.slowFrameMillis());
        if (slow) {
            cumulative.slowFrames++;
            currentFrameCounters.slowFrames++;
        }
        lastFrame = new TelemetryFrameSnapshot(nextFrameNumber, frameStartedNanos, duration,
                callbackNanos, calculateNanos, preRenderNanos, beforeRenderNanos,
                renderNanos, completed, slow);
        lastFrameCounters = currentFrameCounters.snapshot();
        frameActive = false;
        endedFrames++;

        if (!collectors.isEmpty()) {
            TelemetrySnapshot snapshot = snapshot();
            int count = collectors.size();
            for (int index = 0; index < count; index++) {
                try {
                    collectors.get(index).collect(snapshot);
                } catch (Throwable ignored) {
                }
            }
        }
    }

    @Override
    public TelemetrySnapshot snapshot() {
        TelemetryFrameSnapshot frame = frameActive
                ? new TelemetryFrameSnapshot(nextFrameNumber, frameStartedNanos,
                        elapsedSince(frameStartedNanos), callbackNanos, calculateNanos,
                        preRenderNanos, beforeRenderNanos, renderNanos, false, false)
                : lastFrame;
        TelemetryCounters frameCounters = frameActive
                ? currentFrameCounters.snapshot()
                : lastFrameCounters;
        return new TelemetrySnapshot(endedFrames, frame, cumulative.snapshot(), frameCounters,
                gauges.snapshot(), Optional.empty(),
                typeCounters == null ? List.of() : typeCounters.snapshot());
    }

    private void requireActiveFrame() {
        if (!frameActive) {
            throw new IllegalStateException("No telemetry frame is active");
        }
    }

    private long elapsedSince(long startedNanos) {
        return Math.max(0L, nanoTime.getAsLong() - startedNanos);
    }

    private static long millisecondsToNanos(long milliseconds) {
        return milliseconds > Long.MAX_VALUE / 1_000_000L
                ? Long.MAX_VALUE
                : milliseconds * 1_000_000L;
    }

    private static final class MutableCounters {

        private long objectsCreated;
        private long objectsActivated;
        private long objectsReleased;
        private long objectsReclaimed;
        private long dirtyObjects;
        private long resourcesAcquired;
        private long resourcesReleased;
        private long cacheHits;
        private long cacheMisses;
        private long cacheEvictions;
        private long slowFrames;
        private long objectsCalculated;
        private long objectsPreRendered;
        private long objectsRendered;
        private long objectsDirtyMarked;
        private long objectDirtyRoots;
        private long objectsDirtyPreRendered;
        private long uiLayoutPasses;
        private long uiLayoutSkips;
        private long uiDrawSnapshotBuilds;
        private long uiDrawSnapshotSkips;
        private long uiCommandSnapshotNodeVisits;
        private long uiHitTestNodeVisits;
        private long uiDrawCommandsBuilt;
        private long uiChildOrderCacheBuilds;
        private long resourcesRegistered;
        private long resourcesClosed;
        private long resourcesCloseFailed;

        private void add(TelemetryMetric metric, long amount) {
            switch (metric) {
                case OBJECTS_CREATED -> objectsCreated += amount;
                case OBJECTS_ACTIVATED -> objectsActivated += amount;
                case OBJECTS_RELEASED -> objectsReleased += amount;
                case OBJECTS_RECLAIMED -> objectsReclaimed += amount;
                case DIRTY_OBJECTS -> dirtyObjects += amount;
                case RESOURCES_ACQUIRED -> resourcesAcquired += amount;
                case RESOURCES_RELEASED -> resourcesReleased += amount;
                case CACHE_HITS -> cacheHits += amount;
                case CACHE_MISSES -> cacheMisses += amount;
                case CACHE_EVICTIONS -> cacheEvictions += amount;
                case OBJECTS_CALCULATED -> objectsCalculated += amount;
                case OBJECTS_PRE_RENDERED -> objectsPreRendered += amount;
                case OBJECTS_RENDERED -> objectsRendered += amount;
                case OBJECTS_DIRTY_MARKED -> objectsDirtyMarked += amount;
                case OBJECT_DIRTY_ROOTS -> objectDirtyRoots += amount;
                case OBJECTS_DIRTY_PRE_RENDERED -> objectsDirtyPreRendered += amount;
                case UI_LAYOUT_PASSES -> uiLayoutPasses += amount;
                case UI_LAYOUT_SKIPS -> uiLayoutSkips += amount;
                case UI_DRAW_SNAPSHOT_BUILDS -> uiDrawSnapshotBuilds += amount;
                case UI_DRAW_SNAPSHOT_SKIPS -> uiDrawSnapshotSkips += amount;
                case UI_COMMAND_SNAPSHOT_NODE_VISITS -> uiCommandSnapshotNodeVisits += amount;
                case UI_HIT_TEST_NODE_VISITS -> uiHitTestNodeVisits += amount;
                case UI_DRAW_COMMANDS_BUILT -> uiDrawCommandsBuilt += amount;
                case UI_CHILD_ORDER_CACHE_BUILDS -> uiChildOrderCacheBuilds += amount;
                case RESOURCES_REGISTERED -> {
                    resourcesAcquired += amount;
                    resourcesRegistered += amount;
                }
                case RESOURCES_CLOSED -> {
                    resourcesReleased += amount;
                    resourcesClosed += amount;
                }
                case RESOURCES_CLOSE_FAILED -> resourcesCloseFailed += amount;
            }
        }

        private void clear() {
            objectsCreated = 0L;
            objectsActivated = 0L;
            objectsReleased = 0L;
            objectsReclaimed = 0L;
            dirtyObjects = 0L;
            resourcesAcquired = 0L;
            resourcesReleased = 0L;
            cacheHits = 0L;
            cacheMisses = 0L;
            cacheEvictions = 0L;
            slowFrames = 0L;
            objectsCalculated = 0L;
            objectsPreRendered = 0L;
            objectsRendered = 0L;
            objectsDirtyMarked = 0L;
            objectDirtyRoots = 0L;
            objectsDirtyPreRendered = 0L;
            uiLayoutPasses = 0L;
            uiLayoutSkips = 0L;
            uiDrawSnapshotBuilds = 0L;
            uiDrawSnapshotSkips = 0L;
            uiCommandSnapshotNodeVisits = 0L;
            uiHitTestNodeVisits = 0L;
            uiDrawCommandsBuilt = 0L;
            uiChildOrderCacheBuilds = 0L;
            resourcesRegistered = 0L;
            resourcesClosed = 0L;
            resourcesCloseFailed = 0L;
        }

        private TelemetryCounters snapshot() {
            return new TelemetryCounters(objectsCreated, objectsActivated, objectsReleased,
                    objectsReclaimed, dirtyObjects, resourcesAcquired, resourcesReleased,
                    cacheHits, cacheMisses, cacheEvictions, slowFrames,
                    objectsCalculated, objectsPreRendered, objectsRendered,
                    objectsDirtyMarked, objectDirtyRoots, objectsDirtyPreRendered,
                    uiLayoutPasses, uiLayoutSkips, uiDrawSnapshotBuilds,
                    uiDrawSnapshotSkips, uiCommandSnapshotNodeVisits,
                    uiHitTestNodeVisits, uiDrawCommandsBuilt, uiChildOrderCacheBuilds,
                    resourcesRegistered, resourcesClosed, resourcesCloseFailed);
        }
    }

    private static final class MutableGauges {

        private long objectCount;
        private long activeObjectCount;
        private long dirtyObjectCount;
        private long resourceCount;
        private long cacheEntryCount;
        private long releasedRetainedObjectCount;
        private long dirtyRootCount;
        private long uiSceneCount;
        private long uiNodeCount;
        private long uiDirtySceneCount;
        private long uiDirtyNodeCount;

        private void set(TelemetryGauge gauge, long value) {
            switch (gauge) {
                case OBJECT_COUNT -> objectCount = value;
                case ACTIVE_OBJECT_COUNT -> activeObjectCount = value;
                case DIRTY_OBJECT_COUNT -> dirtyObjectCount = value;
                case RESOURCE_COUNT -> resourceCount = value;
                case CACHE_ENTRY_COUNT -> cacheEntryCount = value;
                case RELEASED_RETAINED_OBJECT_COUNT -> releasedRetainedObjectCount = value;
                case DIRTY_ROOT_COUNT -> dirtyRootCount = value;
                case UI_SCENE_COUNT -> uiSceneCount = value;
                case UI_NODE_COUNT -> uiNodeCount = value;
                case UI_DIRTY_SCENE_COUNT -> uiDirtySceneCount = value;
                case UI_DIRTY_NODE_COUNT -> uiDirtyNodeCount = value;
            }
        }

        private TelemetryGauges snapshot() {
            return new TelemetryGauges(objectCount, activeObjectCount, dirtyObjectCount,
                    resourceCount, cacheEntryCount, releasedRetainedObjectCount,
                    dirtyRootCount, uiSceneCount, uiNodeCount, uiDirtySceneCount,
                    uiDirtyNodeCount);
        }
    }

    private static final class TypeCounters {

        private static final ClassValue<TypeKey> TYPE_KEYS = new ClassValue<>() {
            @Override
            protected TypeKey computeValue(Class<?> type) {
                return new TypeKey(type.getName());
            }
        };

        private final int capacity;
        private final Map<TypeKey, MutableCounters> cumulative = new LinkedHashMap<>();
        private final Map<TypeKey, MutableCounters> currentFrame = new LinkedHashMap<>();

        private TypeCounters(int capacity) {
            this.capacity = capacity;
        }

        private void add(TelemetryMetric metric, Class<?> type, long amount, boolean frameActive) {
            TypeKey key = TYPE_KEYS.get(type);
            MutableCounters cumulativeCounters = cumulative.get(key);
            if (cumulativeCounters == null) {
                if (cumulative.size() == capacity) {
                    return;
                }
                cumulativeCounters = new MutableCounters();
                cumulative.put(key, cumulativeCounters);
                currentFrame.put(key, new MutableCounters());
            }
            cumulativeCounters.add(metric, amount);
            if (frameActive) {
                currentFrame.get(key).add(metric, amount);
            }
        }

        private void clearFrame() {
            for (MutableCounters counters : currentFrame.values()) {
                counters.clear();
            }
        }

        private List<TelemetryTypeSnapshot> snapshot() {
            List<TelemetryTypeSnapshot> snapshots = new ArrayList<>(cumulative.size());
            for (Map.Entry<TypeKey, MutableCounters> entry : cumulative.entrySet()) {
                snapshots.add(new TelemetryTypeSnapshot(entry.getKey().id,
                        entry.getValue().snapshot(), currentFrame.get(entry.getKey()).snapshot()));
            }
            return List.copyOf(snapshots);
        }
    }

    private record TypeKey(String id) {
    }
}
