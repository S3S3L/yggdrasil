package io.github.s3s3l.yggdrasil.game.core.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.ui.input.UiEventPhase;
import io.github.s3s3l.yggdrasil.game.core.ui.input.UiEventType;
import io.github.s3s3l.yggdrasil.game.core.ui.input.UiInputEvent;

class UiEventDispatchTest {

    @Test
    void dispatchesCaptureThenTargetThenBubble() {
        UiNode root = node("root", 0, 0, 100, 100);
        UiNode parent = node("parent", 10, 10, 80, 80);
        UiNode target = node("target", 20, 20, 40, 40);
        root.addChild(parent);
        parent.addChild(target);
        List<String> calls = new ArrayList<>();
        root.addEventListener(UiEventType.POINTER_DOWN, UiEventPhase.CAPTURE,
                event -> calls.add("root-capture"));
        parent.addEventListener(UiEventType.POINTER_DOWN, UiEventPhase.CAPTURE,
                event -> calls.add("parent-capture"));
        target.addEventListener(UiEventType.POINTER_DOWN, UiEventPhase.TARGET,
                event -> calls.add("target"));
        parent.addEventListener(UiEventType.POINTER_DOWN, UiEventPhase.BUBBLE,
                event -> calls.add("parent-bubble"));
        root.addEventListener(UiEventType.POINTER_DOWN, UiEventPhase.BUBBLE,
                event -> calls.add("root-bubble"));

        UiScene scene = scene(root);
        scene.dispatch(UiInputEvent.pointer(UiEventType.POINTER_DOWN, new UiPoint(25, 25), 0, 0));

        assertEquals(List.of("root-capture", "parent-capture", "target", "parent-bubble", "root-bubble"), calls);
    }

    @Test
    void consumingEventStopsFurtherPhases() {
        UiNode root = node("root", 0, 0, 100, 100);
        UiNode target = node("target", 0, 0, 100, 100);
        root.addChild(target);
        List<String> calls = new ArrayList<>();
        root.addEventListener(UiEventType.POINTER_DOWN, UiEventPhase.CAPTURE, event -> {
            calls.add("capture");
            event.consume();
        });
        target.addEventListener(UiEventType.POINTER_DOWN, UiEventPhase.TARGET, event -> calls.add("target"));

        assertTrue(scene(root).dispatch(UiInputEvent.pointer(UiEventType.POINTER_DOWN, new UiPoint(1, 1), 0, 0)));
        assertEquals(List.of("capture"), calls);
    }

    @Test
    void isolatesListenerFailuresAndContinuesTheCurrentPhase() {
        UiNode root = node("root", 0, 0, 100, 100);
        List<Throwable> failures = new ArrayList<>();
        List<String> calls = new ArrayList<>();
        root.addEventListener(UiEventType.KEY_DOWN, UiEventPhase.TARGET,
                event -> {
                    throw new IllegalStateException("broken listener");
                });
        root.addEventListener(UiEventType.KEY_DOWN, UiEventPhase.TARGET, event -> calls.add("after"));

        UiScene scene = new UiScene(root, new UiViewport(0, 0, 100, 100, 100, 100), failures::add);
        scene.dispatch(UiInputEvent.key(UiEventType.KEY_DOWN, 42));

        assertEquals(List.of("after"), calls);
        assertEquals(1, failures.size());
        assertEquals("broken listener", failures.get(0).getMessage());
    }

    private static UiScene scene(UiNode root) {
        return new UiScene(root, new UiViewport(0, 0, 100, 100, 100, 100));
    }

    private static UiNode node(String name, float x, float y, float width, float height) {
        UiNode node = new UiNode();
        node.setName(name);
        node.setLayoutResult(new io.github.s3s3l.yggdrasil.game.core.layout.LayoutResult(x, y, width, height, width, height));
        return node;
    }
}
