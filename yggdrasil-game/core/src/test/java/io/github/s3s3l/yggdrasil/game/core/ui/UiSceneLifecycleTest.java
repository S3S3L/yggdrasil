package io.github.s3s3l.yggdrasil.game.core.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.layout.LayoutStyle;
import io.github.s3s3l.yggdrasil.game.core.layout.LengthUnit;
import io.github.s3s3l.yggdrasil.game.core.ui.draw.FillRectCommand;

class UiSceneLifecycleTest {

    @Test
    void calculatesLayoutBeforeBuildingTheRendererIndependentCommandSnapshot() {
        UiNode root = fixedNode(100, 80);
        UiNode child = fixedNode(20, 10);
        child.setDrawHandler((node, context) ->
                context.add(new FillRectCommand(node.getBounds(), new UiColor(1, 0, 0, 1))));
        root.addChild(child);
        UiScene scene = new UiScene(root, new UiViewport(0, 0, 100, 80, 100, 80));

        scene.calculate(0);
        scene.preRender();

        FillRectCommand command = assertInstanceOf(FillRectCommand.class, scene.getDrawCommands().get(0));
        assertEquals(new UiRect(0, 0, 20, 10), command.getBounds());
    }

    private static UiNode fixedNode(float width, float height) {
        LayoutStyle style = new LayoutStyle();
        style.getBoxModel().getSize().setWidth(LengthUnit.px(width));
        style.getBoxModel().getSize().setHeight(LengthUnit.px(height));
        return new UiNode(null, style);
    }
}
