package io.github.s3s3l.yggdrasil.game.core.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.layout.LayoutStyle;
import io.github.s3s3l.yggdrasil.game.core.layout.LengthUnit;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryConfig;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryLevel;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryRegistry;
import io.github.s3s3l.yggdrasil.game.core.ui.draw.FillRectCommand;

class UiSceneTelemetryTest {

    @Test
    void recordsBuildWorkAndStaticFrameSkipsFromExistingSceneMetrics() {
        TelemetryRegistry telemetry = TelemetryRegistry.create(new TelemetryConfig(
                TelemetryLevel.BASIC, 100L, "none", "", false, Set.of(), 10, 0L));
        UiNode root = fixedNode(100, 80);
        UiNode child = fixedNode(20, 10);
        child.setDrawHandler((node, context) ->
                context.add(new FillRectCommand(node.getBounds(), new UiColor(1, 1, 1, 1))));
        root.addChild(child);
        UiScene scene = new UiScene(root, new UiViewport(0, 0, 100, 80, 100, 80), telemetry);

        telemetry.beginFrame();
        scene.calculate(0);
        scene.preRender();
        scene.hitTest(1, 1);
        scene.render();
        telemetry.endFrame(true);

        var first = telemetry.snapshot().frameCounters();
        assertEquals(1L, first.uiLayoutPasses());
        assertEquals(1L, first.uiDrawSnapshotBuilds());
        assertEquals(2L, first.uiCommandSnapshotNodeVisits());
        assertEquals(2L, first.uiHitTestNodeVisits());
        assertEquals(1L, first.uiDrawCommandsBuilt());
        assertEquals(2L, first.uiChildOrderCacheBuilds());
        assertEquals(1L, telemetry.snapshot().gauges().uiSceneCount());
        assertEquals(2L, telemetry.snapshot().gauges().uiNodeCount());
        assertEquals(0L, telemetry.snapshot().gauges().uiDirtySceneCount());
        assertEquals(0L, telemetry.snapshot().gauges().uiDirtyNodeCount());

        telemetry.beginFrame();
        scene.calculate(0);
        scene.preRender();
        telemetry.endFrame(true);

        var staticFrame = telemetry.snapshot().frameCounters();
        assertEquals(1L, staticFrame.uiLayoutSkips());
        assertEquals(1L, staticFrame.uiDrawSnapshotSkips());
        assertEquals(0L, staticFrame.uiDrawSnapshotBuilds());

        scene.invalidateDrawCommands();
        telemetry.beginFrame();
        scene.calculate(0);
        scene.preRender();
        telemetry.endFrame(true);

        var changed = telemetry.snapshot().frameCounters();
        assertEquals(1L, changed.uiLayoutSkips());
        assertEquals(1L, changed.uiDrawSnapshotBuilds());
        assertEquals(2L, changed.uiCommandSnapshotNodeVisits());
        assertEquals(1L, changed.uiDrawCommandsBuilt());
    }

    @Test
    void maintainsNodeAndDirtyGaugesWhenTheSceneTreeChanges() {
        TelemetryRegistry telemetry = TelemetryRegistry.create(new TelemetryConfig(
                TelemetryLevel.BASIC, 100L, "none", "", false, Set.of(), 10, 0L));
        UiNode root = fixedNode(100, 80);
        UiScene scene = new UiScene(root, new UiViewport(0, 0, 100, 80, 100, 80), telemetry);

        UiNode child = fixedNode(20, 10);
        root.addChild(child);
        assertEquals(2L, telemetry.snapshot().gauges().uiNodeCount());

        scene.calculate(0);
        assertEquals(1L, telemetry.snapshot().gauges().uiDirtySceneCount());
        assertEquals(2L, telemetry.snapshot().gauges().uiDirtyNodeCount());
        root.removeChild(child);
        assertEquals(1L, telemetry.snapshot().gauges().uiNodeCount());
    }

    @Test
    void distinguishesFrontendSubmissionFromRetainedCommandRebuilds() {
        TelemetryRegistry telemetry = TelemetryRegistry.create(new TelemetryConfig(
                TelemetryLevel.BASIC, 100L, "none", "", false, Set.of(), 10, 0L));
        UiFrontendDrawTelemetry frontend = new UiFrontendDrawTelemetry(telemetry);

        telemetry.beginFrame();
        frontend.recordSubmittedDrawCommands(8L);
        frontend.recordSubmittedDrawNodes(5L);
        frontend.recordSubmittedDrawBatches(1L);
        telemetry.endFrame(true);

        var counters = telemetry.snapshot().frameCounters();
        assertEquals(8L, counters.uiFrontendDrawCommandsSubmitted());
        assertEquals(5L, counters.uiFrontendDrawNodesSubmitted());
        assertEquals(1L, counters.uiFrontendDrawBatchesSubmitted());
        assertEquals(0L, counters.uiDrawCommandsBuilt());
    }

    private static UiNode fixedNode(float width, float height) {
        LayoutStyle style = new LayoutStyle();
        style.getBoxModel().getSize().setWidth(LengthUnit.px(width));
        style.getBoxModel().getSize().setHeight(LengthUnit.px(height));
        return new UiNode(null, style);
    }
}
