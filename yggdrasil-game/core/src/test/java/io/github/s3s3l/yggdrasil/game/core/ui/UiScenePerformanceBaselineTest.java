package io.github.s3s3l.yggdrasil.game.core.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.layout.LayoutResult;
import io.github.s3s3l.yggdrasil.game.core.layout.LayoutStyle;
import io.github.s3s3l.yggdrasil.game.core.layout.LengthUnit;
import io.github.s3s3l.yggdrasil.game.core.ui.draw.FillRectCommand;

class UiScenePerformanceBaselineTest {

    private static final int FRAMES = 120;

    @Test
    void retainsTheFirstLayoutAndCommandSnapshotAcrossSteadyFrames() {
        CountingNode root = fixedNode(100, 80);
        CountingNode child = fixedNode(10, 10);
        child.setDrawHandler((node, context) ->
                context.add(new FillRectCommand(node.getBounds(), new UiColor(1, 1, 1, 1))));
        root.addChild(child);
        UiScene scene = new UiScene(root, new UiViewport(0, 0, 100, 80, 100, 80));

        int commandSnapshots = 0;
        List<?> previous = null;
        for (int frame = 0; frame < FRAMES; frame++) {
            scene.calculate(0);
            scene.preRender();
            if (scene.getDrawCommands() != previous) {
                commandSnapshots++;
                previous = scene.getDrawCommands();
            }
        }

        assertEquals(4, root.layoutResultWrites + child.layoutResultWrites);
        assertEquals(1, child.drawCalls);
        assertEquals(1, commandSnapshots);
        assertEquals(1, scene.metrics().layoutPasses());
        assertEquals(1, scene.metrics().commandSnapshotBuilds());
        assertEquals(2, scene.metrics().drawnNodeVisits());
        assertEquals(2, scene.metrics().childOrderCacheBuilds());
    }

    private static CountingNode fixedNode(float width, float height) {
        LayoutStyle style = new LayoutStyle();
        style.getBoxModel().getSize().setWidth(LengthUnit.px(width));
        style.getBoxModel().getSize().setHeight(LengthUnit.px(height));
        return new CountingNode(style);
    }

    private static final class CountingNode extends UiNode {

        private int layoutResultWrites;
        private int drawCalls;

        private CountingNode(LayoutStyle style) {
            super(null, style);
        }

        @Override
        public void setLayoutResult(LayoutResult layoutResult) {
            layoutResultWrites++;
            super.setLayoutResult(layoutResult);
        }

        @Override
        public void setDrawHandler(UiNodeDrawHandler drawHandler) {
            super.setDrawHandler((node, context) -> {
                drawCalls++;
                if (drawHandler != null) {
                    drawHandler.draw(node, context);
                }
            });
        }
    }
}
