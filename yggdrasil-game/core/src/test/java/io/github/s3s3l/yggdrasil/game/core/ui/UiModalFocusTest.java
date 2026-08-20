package io.github.s3s3l.yggdrasil.game.core.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.layout.LayoutResult;
import io.github.s3s3l.yggdrasil.game.core.ui.input.UiEventPhase;
import io.github.s3s3l.yggdrasil.game.core.ui.input.UiEventType;
import io.github.s3s3l.yggdrasil.game.core.ui.input.UiInputEvent;

class UiModalFocusTest {

    @Test
    void modalBlocksBackgroundPointerAndReceivesFocusedKeys() {
        UiNode root = node("root", 0, 0, 100, 100);
        UiNode background = node("background", 0, 0, 100, 100);
        background.setFocusable(true);
        UiNode modal = node("modal", 20, 20, 60, 60);
        UiNode modalField = node("field", 30, 30, 20, 20);
        modalField.setFocusable(true);
        root.addChild(background);
        root.addChild(modal);

        AtomicInteger backgroundPointer = new AtomicInteger();
        AtomicInteger modalPointer = new AtomicInteger();
        AtomicInteger modalKeys = new AtomicInteger();
        background.addEventListener(UiEventType.POINTER_DOWN, UiEventPhase.TARGET, event -> backgroundPointer.incrementAndGet());
        modal.addEventListener(UiEventType.POINTER_DOWN, UiEventPhase.TARGET, event -> modalPointer.incrementAndGet());
        modalField.addEventListener(UiEventType.KEY_DOWN, UiEventPhase.TARGET, event -> modalKeys.incrementAndGet());
        modal.addChild(modalField);
        UiScene scene = new UiScene(root, new UiViewport(0, 0, 100, 100, 100, 100));

        scene.focus().requestFocus(background);
        scene.showModal(modal);
        scene.dispatch(UiInputEvent.pointer(UiEventType.POINTER_DOWN, new UiPoint(5, 5), 0, 0));
        scene.dispatch(UiInputEvent.key(UiEventType.KEY_DOWN, 12));

        assertEquals(0, backgroundPointer.get());
        assertEquals(1, modalPointer.get());
        assertEquals(1, modalKeys.get());
        assertSame(modalField, scene.focus().getFocusedNode());
    }

    @Test
    void restoresPriorFocusAndDetachesAModalAttachedByTheScene() {
        UiNode root = node("root", 0, 0, 100, 100);
        UiNode background = node("background", 0, 0, 100, 100);
        background.setFocusable(true);
        UiNode modal = node("modal", 20, 20, 60, 60);
        modal.setFocusable(true);
        root.addChild(background);
        UiScene scene = new UiScene(root, new UiViewport(0, 0, 100, 100, 100, 100));
        scene.focus().requestFocus(background);

        scene.showModal(modal);

        assertSame(modal, scene.focus().getFocusedNode());
        assertTrue(scene.dismissModal(modal));
        assertSame(background, scene.focus().getFocusedNode());
        assertEquals(1, root.uiChildren().size());
    }

    private static UiNode node(String name, float x, float y, float width, float height) {
        UiNode node = new UiNode();
        node.setName(name);
        node.setLayoutResult(new LayoutResult(x, y, width, height, width, height));
        return node;
    }
}
