package io.github.s3s3l.yggdrasil.game.core.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.layout.DisplayType;
import io.github.s3s3l.yggdrasil.game.core.layout.LayoutResult;
import io.github.s3s3l.yggdrasil.game.core.layout.LayoutStyle;
import io.github.s3s3l.yggdrasil.game.core.layout.LengthUnit;
import io.github.s3s3l.yggdrasil.game.core.ui.draw.FillRectCommand;
import io.github.s3s3l.yggdrasil.game.core.ui.draw.TextCommand;

class UiSceneDirtyStateTest {

    @Test
    void invalidatesForStyleBoundsChildrenAndVisibilityChanges() {
        UiNode root = fixedNode(100, 80);
        UiNode child = fillNode(20, 10);
        root.addChild(child);
        UiScene scene = new UiScene(root, new UiViewport(0, 0, 100, 80, 100, 80));

        scene.calculate(0);
        scene.preRender();
        assertEquals(new UiRect(0, 0, 20, 10), fill(scene).getBounds());

        child.getStyle().getBoxModel().getSize().setWidth(LengthUnit.px(30));
        scene.calculate(0);
        scene.preRender();
        assertEquals(new UiRect(0, 0, 30, 10), fill(scene).getBounds());
        assertEquals(2, scene.metrics().layoutPasses());

        child.setLayoutResult(new LayoutResult(4, 5, 30, 10, 30, 10));
        scene.calculate(0);
        scene.preRender();
        assertEquals(new UiRect(4, 5, 30, 10), fill(scene).getBounds());
        assertEquals(2, scene.metrics().layoutPasses(), "manual bounds do not force a layout pass");

        child.getStyle().setDisplay(DisplayType.NONE);
        scene.calculate(0);
        scene.preRender();
        assertEquals(0, scene.getDrawCommands().size());

        UiNode added = fillNode(8, 6);
        root.addChild(added);
        scene.calculate(0);
        scene.preRender();
        assertEquals(new UiRect(0, 0, 8, 6), fill(scene).getBounds());
        assertEquals(4, scene.metrics().layoutPasses());
        assertEquals(5, scene.metrics().commandSnapshotBuilds());
    }

    @Test
    void refreshesDynamicDrawHandlersWithoutRelayoutAndNeverUsesStaleCommands() {
        UiNode root = fixedNode(100, 80);
        UiNode child = fixedNode(20, 10);
        String[] text = {"before"};
        child.setDrawHandler((node, context) ->
                context.add(new TextCommand(text[0], new UiPoint(0, 8), new UiColor(1, 1, 1, 1))));
        root.addChild(child);
        UiScene scene = new UiScene(root, new UiViewport(0, 0, 100, 80, 100, 80));

        scene.calculate(0);
        scene.preRender();
        text[0] = "after";
        scene.invalidateDrawCommands();
        scene.calculate(0);
        scene.preRender();

        TextCommand command = assertInstanceOf(TextCommand.class, scene.getDrawCommands().get(0));
        assertEquals("after", command.getText());
        assertEquals(1, scene.metrics().layoutPasses());
        assertEquals(2, scene.metrics().commandSnapshotBuilds());
    }

    @Test
    void relayoutsAfterResizeAndReusesChildOrderingForHitTesting() {
        UiNode root = fixedNode(100, 80);
        UiNode child = fillNode(20, 10);
        root.addChild(child);
        UiScene scene = new UiScene(root, new UiViewport(0, 0, 100, 80, 100, 80));

        scene.calculate(0);
        scene.preRender();
        long cacheBuilds = scene.metrics().childOrderCacheBuilds();
        scene.hitTest(1, 1);

        assertEquals(cacheBuilds, scene.metrics().childOrderCacheBuilds());
        assertEquals(2, scene.metrics().hitTestNodeVisits());

        scene.onWindowResize(200, 160);
        scene.calculate(0);
        scene.preRender();
        assertEquals(2, scene.metrics().layoutPasses());
        assertEquals(2, scene.metrics().commandSnapshotBuilds());
    }

    private static FillRectCommand fill(UiScene scene) {
        return assertInstanceOf(FillRectCommand.class, scene.getDrawCommands().get(0));
    }

    private static UiNode fillNode(float width, float height) {
        UiNode node = fixedNode(width, height);
        node.setDrawHandler((current, context) ->
                context.add(new FillRectCommand(current.getBounds(), new UiColor(1, 1, 1, 1))));
        return node;
    }

    private static UiNode fixedNode(float width, float height) {
        LayoutStyle style = new LayoutStyle();
        style.getBoxModel().getSize().setWidth(LengthUnit.px(width));
        style.getBoxModel().getSize().setHeight(LengthUnit.px(height));
        return new UiNode(null, style);
    }
}
