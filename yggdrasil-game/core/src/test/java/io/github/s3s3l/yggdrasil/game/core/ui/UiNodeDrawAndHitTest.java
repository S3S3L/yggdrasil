package io.github.s3s3l.yggdrasil.game.core.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.font.FontHandle;
import io.github.s3s3l.yggdrasil.game.core.font.FontRequest;
import io.github.s3s3l.yggdrasil.game.core.font.FontSource;
import io.github.s3s3l.yggdrasil.game.core.font.FontStyle;
import io.github.s3s3l.yggdrasil.game.core.layout.LayoutResult;
import io.github.s3s3l.yggdrasil.game.core.ui.draw.DrawCommand;
import io.github.s3s3l.yggdrasil.game.core.ui.draw.FillRectCommand;
import io.github.s3s3l.yggdrasil.game.core.ui.draw.PopClipCommand;
import io.github.s3s3l.yggdrasil.game.core.ui.draw.PushClipCommand;
import io.github.s3s3l.yggdrasil.game.core.ui.draw.TextCommand;

class UiNodeDrawAndHitTest {

    @Test
    void drawsEqualZChildrenInInsertionOrderAndEmitsClipPair() {
        UiNode root = node("root", 0, 0, 100, 100);
        root.setClipChildren(true);
        UiNode first = node("first", 0, 0, 10, 10);
        UiNode second = node("second", 10, 0, 10, 10);
        first.setDrawHandler((node, context) -> context.add(new TextCommand("first", new UiPoint(0, 0), white())));
        second.setDrawHandler((node, context) -> context.add(new TextCommand("second", new UiPoint(0, 0), white())));
        root.addChild(first);
        root.addChild(second);
        UiScene scene = new UiScene(root, new UiViewport(0, 0, 100, 100, 100, 100));

        scene.preRender();
        List<DrawCommand> commands = scene.getDrawCommands();

        assertInstanceOf(PushClipCommand.class, commands.get(0));
        assertEquals("first", ((TextCommand) commands.get(1)).getText());
        assertEquals("second", ((TextCommand) commands.get(2)).getText());
        assertSame(PopClipCommand.INSTANCE, commands.get(3));
    }

    @Test
    void clipsChildHitTestingToParentContentBounds() {
        UiNode root = node("root", 0, 0, 100, 100);
        UiNode clipped = node("clipped", 20, 20, 20, 20);
        clipped.setClipChildren(true);
        UiNode overflowing = node("overflowing", 0, 0, 80, 80);
        root.addChild(clipped);
        clipped.addChild(overflowing);

        assertNull(clipped.hitTest(10, 10));
        assertSame(root, root.hitTest(10, 10));
        assertSame(overflowing, root.hitTest(25, 25));
    }

    @Test
    void choosesHigherZIndexAtOverlappingHit() {
        UiNode root = node("root", 0, 0, 100, 100);
        UiNode low = node("low", 10, 10, 50, 50);
        UiNode high = node("high", 10, 10, 50, 50);
        high.setZIndex(1);
        root.addChild(low);
        root.addChild(high);

        assertSame(high, root.hitTest(20, 20));
    }

    @Test
    void keepsFontHandleInBackendIndependentTextCommands() {
        FontHandle handle = new FontHandle("title",
                new FontRequest(new FontSource("title.ttf"), FontStyle.BOLD, 20f));
        TextCommand command = new TextCommand("title", handle, new UiPoint(4, 12), white());

        assertSame(handle, command.getFont());
    }

    private static UiColor white() {
        return new UiColor(1, 1, 1, 1);
    }

    private static UiNode node(String name, float x, float y, float width, float height) {
        UiNode node = new UiNode();
        node.setName(name);
        node.setLayoutResult(new LayoutResult(x, y, width, height, width, height));
        return node;
    }
}
