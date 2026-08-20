package io.github.s3s3l.yggdrasil.game.core.object;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.ref.ReferenceQueue;
import java.util.Set;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.config.Props;
import io.github.s3s3l.yggdrasil.game.core.object.props.Visible;
import io.github.s3s3l.yggdrasil.game.core.runtime.GameTaskScheduler;
import io.github.s3s3l.yggdrasil.game.core.snapshot.ObjectSnapshot;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryConfig;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryLevel;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryRegistry;

class GameObjectManagerTelemetryTest {

    @Test
    void recordsLifecyclePhasesAndMergedDirtyRootsWithoutChangingRootRendering() {
        TelemetryRegistry telemetry = telemetry(TelemetryLevel.BASIC, Set.of(), 10);
        GameObjectManager manager = new GameObjectManager(GameTaskScheduler.serial(), telemetry);
        CountingObject root = manager.create(config(), CountingObject.class);
        CountingObject child = manager.create(config(), CountingObject.class);
        root.addChild(child);
        child.setParent(root);

        telemetry.beginFrame();
        manager.doCalculate(0.25f);
        manager.doPreRender();
        manager.doRender();
        telemetry.endFrame(true);

        var frame = telemetry.snapshot().frameCounters();
        assertEquals(2L, frame.objectsCalculated());
        assertEquals(1L, frame.objectsPreRendered());
        assertEquals(1L, frame.objectsRendered());
        assertEquals(2L, frame.dirtyObjects());
        assertEquals(2L, frame.objectsDirtyMarked());
        assertEquals(1L, frame.objectDirtyRoots());
        assertEquals(1L, frame.objectsDirtyPreRendered());
        assertEquals(1, root.preRenderCalls);
        assertEquals(1, root.renderCalls);
        assertEquals(2, child.calculateCalls, "the existing recursive and managed calls are retained");
        assertEquals(0L, telemetry.snapshot().gauges().dirtyRootCount());
        assertEquals(0L, telemetry.snapshot().gauges().dirtyObjectCount());
    }

    @Test
    void recordsStateTransitionsOnceAndRetainsAccurateGauges() {
        TelemetryRegistry telemetry = telemetry(TelemetryLevel.BASIC, Set.of(), 10);
        GameObjectManager manager = new GameObjectManager(GameTaskScheduler.serial(), telemetry);
        CountingObject object = manager.create(config(), CountingObject.class);

        telemetry.beginFrame();
        manager.release(object);
        manager.release(object);
        manager.active(object);
        manager.releaseAll();
        telemetry.endFrame(true);

        var frame = telemetry.snapshot().frameCounters();
        assertEquals(2L, frame.objectsReleased(),
                "the duplicate is ignored while the later release after activation is counted");
        assertEquals(1L, frame.objectsActivated());
        assertEquals(1L, telemetry.snapshot().gauges().releasedRetainedObjectCount());
        assertEquals(0L, telemetry.snapshot().gauges().activeObjectCount());
        assertEquals(1L, telemetry.snapshot().gauges().objectCount());
    }

    @Test
    void drainsOnlyExplicitlyEnqueuedReleasedReferencesWithoutRequestingGc() {
        TelemetryRegistry telemetry = telemetry(TelemetryLevel.BASIC, Set.of(), 10);
        CapturingReferenceFactory references = new CapturingReferenceFactory();
        GameObjectManager manager = new GameObjectManager(GameTaskScheduler.serial(), telemetry,
                new ReferenceQueue<>(), references);
        CountingObject object = manager.create(config(), CountingObject.class);
        manager.release(object);

        references.reference.clear();
        assertTrue(references.reference.enqueue());
        telemetry.beginFrame();
        assertEquals(1, manager.reclaimReleasedObjects());
        telemetry.endFrame(true);

        assertEquals(1L, telemetry.snapshot().frameCounters().objectsReclaimed());
        assertEquals(0L, telemetry.snapshot().gauges().releasedRetainedObjectCount());
    }

    @Test
    void enablesBoundedClassCountersOnlyForDetailedTrueTypeTelemetry() {
        TelemetryRegistry telemetry = telemetry(TelemetryLevel.DETAILED, Set.of("true"), 1);
        GameObjectManager manager = new GameObjectManager(GameTaskScheduler.serial(), telemetry);
        manager.create(config(), CountingObject.class);

        assertTrue(telemetry.isTypeMetricsEnabled());
        assertEquals(1, telemetry.snapshot().typeCounters().size());
        assertEquals(CountingObject.class.getName(), telemetry.snapshot().typeCounters().get(0).typeId());
        assertEquals(1L, telemetry.snapshot().typeCounters().get(0).cumulative().objectsCreated());

        TelemetryRegistry off = TelemetryRegistry.create(TelemetryConfig.defaults());
        GameObjectManager offManager = new GameObjectManager(GameTaskScheduler.serial(), off);
        offManager.create(config(), CountingObject.class);
        assertFalse(off.isTypeMetricsEnabled());
        assertTrue(off.snapshot().typeCounters().isEmpty());
    }

    private static TelemetryRegistry telemetry(TelemetryLevel level, Set<String> types, int topN) {
        return TelemetryRegistry.create(new TelemetryConfig(level, 100L, "none", "", false, types, topN, 0L));
    }

    private static GameObjectConfig config() {
        return new GameObjectConfig(CountingObject.class, new Props());
    }

    public static final class CountingObject extends AbstractGameObject {

        private int calculateCalls;
        private int preRenderCalls;
        private int renderCalls;

        public CountingObject(GameObjectConfig config) {
            super(config);
        }

        @Override
        public void afterPropertiesSet() {
        }

        @Override
        public void onWindowResize(int width, int height) {
        }

        @Override
        protected void doRestore(ObjectSnapshot snapshot) {
        }

        @Override
        protected void doSnapshot(ObjectSnapshot snapshot) {
        }

        @Override
        protected void doCalculate(float delta) {
            calculateCalls++;
            markDirty();
        }

        @Override
        protected void doPreRender() {
            preRenderCalls++;
        }

        @Override
        protected void doRender() {
            renderCalls++;
        }
    }

    private static final class CapturingReferenceFactory implements ReleasedObjectReferenceFactory {

        private ReleasedObjectReference reference;

        @Override
        public ReleasedObjectReference create(long objectId, GameObject object,
                ReferenceQueue<GameObject> queue, Class<?> type) {
            reference = new ReleasedObjectReference(objectId, object, queue, type);
            return reference;
        }
    }
}
