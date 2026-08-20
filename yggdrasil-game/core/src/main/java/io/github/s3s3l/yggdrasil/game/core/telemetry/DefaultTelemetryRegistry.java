package io.github.s3s3l.yggdrasil.game.core.telemetry;

import java.util.ArrayList;
import java.util.List;
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
                gauges.snapshot(), Optional.empty());
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
        }

        private TelemetryCounters snapshot() {
            return new TelemetryCounters(objectsCreated, objectsActivated, objectsReleased,
                    objectsReclaimed, dirtyObjects, resourcesAcquired, resourcesReleased,
                    cacheHits, cacheMisses, cacheEvictions, slowFrames);
        }
    }

    private static final class MutableGauges {

        private long objectCount;
        private long activeObjectCount;
        private long dirtyObjectCount;
        private long resourceCount;
        private long cacheEntryCount;

        private void set(TelemetryGauge gauge, long value) {
            switch (gauge) {
                case OBJECT_COUNT -> objectCount = value;
                case ACTIVE_OBJECT_COUNT -> activeObjectCount = value;
                case DIRTY_OBJECT_COUNT -> dirtyObjectCount = value;
                case RESOURCE_COUNT -> resourceCount = value;
                case CACHE_ENTRY_COUNT -> cacheEntryCount = value;
            }
        }

        private TelemetryGauges snapshot() {
            return new TelemetryGauges(objectCount, activeObjectCount, dirtyObjectCount,
                    resourceCount, cacheEntryCount);
        }
    }
}
