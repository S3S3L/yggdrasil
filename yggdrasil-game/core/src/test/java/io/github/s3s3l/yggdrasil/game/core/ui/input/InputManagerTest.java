package io.github.s3s3l.yggdrasil.game.core.ui.input;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 输入管理器单元测试
 */
class InputManagerTest {

    @BeforeEach
    void setUp() {
        InputManager.resetInstance();
    }

    @AfterEach
    void tearDown() {
        InputManager.resetInstance();
    }

    @Test
    void testGetInstance() {
        InputManager manager1 = InputManager.getInstance();
        InputManager manager2 = InputManager.getInstance();
        assertSame(manager1, manager2);
    }

    @Test
    void testMapActionToKey() {
        InputManager manager = InputManager.getInstance();
        manager.mapAction("jump", 32); // space key

        InputMapping mapping = manager.getMapping("jump");
        assertNotNull(mapping);
        assertTrue(mapping.hasKeyBinding(32));
    }

    @Test
    void testMapActionToGesture() {
        InputManager manager = InputManager.getInstance();
        manager.mapAction("select", Gesture.TAP);

        InputMapping mapping = manager.getMapping("select");
        assertNotNull(mapping);
        assertTrue(mapping.hasGestureBinding(Gesture.TAP));
    }

    @Test
    void testUnmapAction() {
        InputManager manager = InputManager.getInstance();
        manager.mapAction("test", 65);
        manager.mapAction("test", Gesture.TAP);

        manager.unmapAction("test");

        assertNull(manager.getMapping("test"));
        assertNull(manager.getActionByKey(65));
        assertNull(manager.getActionByGesture(Gesture.TAP));
    }

    @Test
    void testIsActionPressed() {
        InputManager manager = InputManager.getInstance();
        manager.mapAction("fire", 70);

        assertFalse(manager.isActionPressed("fire"));
        manager.setActionState("fire", true);
        assertTrue(manager.isActionPressed("fire"));
    }

    @Test
    void testSetActionState() {
        InputManager manager = InputManager.getInstance();
        manager.mapAction("move", 77);

        manager.setActionState("move", true);
        assertTrue(manager.isActionPressed("move"));

        manager.setActionState("move", false);
        assertFalse(manager.isActionPressed("move"));
    }

    @Test
    void testGetActionByKey() {
        InputManager manager = InputManager.getInstance();
        manager.mapAction("jump", 32);

        assertEquals("jump", manager.getActionByKey(32));
        assertNull(manager.getActionByKey(999));
    }

    @Test
    void testGetActionByGesture() {
        InputManager manager = InputManager.getInstance();
        manager.mapAction("select", Gesture.DOUBLE_TAP);

        assertEquals("select", manager.getActionByGesture(Gesture.DOUBLE_TAP));
        assertNull(manager.getActionByGesture(Gesture.TAP));
    }

    @Test
    void testGetActions() {
        InputManager manager = InputManager.getInstance();
        manager.mapAction("action1", 1);
        manager.mapAction("action2", 2);

        Set<String> actions = manager.getActions();
        assertEquals(2, actions.size());
        assertTrue(actions.contains("action1"));
        assertTrue(actions.contains("action2"));
    }

    @Test
    void testKeyDown() {
        InputManager manager = InputManager.getInstance();
        manager.mapAction("fire", 70);

        manager.keyDown(70);
        assertTrue(manager.isActionPressed("fire"));
    }

    @Test
    void testKeyUp() {
        InputManager manager = InputManager.getInstance();
        manager.mapAction("fire", 70);

        manager.keyDown(70);
        manager.keyUp(70);
        assertFalse(manager.isActionPressed("fire"));
    }

    @Test
    void testHandleGesture() {
        InputManager manager = InputManager.getInstance();
        manager.mapAction("select", Gesture.TAP);

        manager.handleGesture(Gesture.TAP);
        // 手势触发后状态自动重置
        assertFalse(manager.isActionPressed("select"));
    }

    @Test
    void testAddListener() {
        InputManager manager = InputManager.getInstance();
        TestListener listener = new TestListener();
        manager.addListener(listener);

        manager.mapAction("test", 65);
        manager.keyDown(65);

        assertTrue(listener.called);
        assertEquals("test", listener.lastAction);
    }

    @Test
    void testRemoveListener() {
        InputManager manager = InputManager.getInstance();
        TestListener listener = new TestListener();
        manager.addListener(listener);
        manager.removeListener(listener);

        manager.mapAction("test", 65);
        manager.keyDown(65);

        assertFalse(listener.called);
    }

    @Test
    void testClearAll() {
        InputManager manager = InputManager.getInstance();
        manager.mapAction("action1", 1);
        manager.mapAction("action2", Gesture.TAP);

        manager.clearAll();

        assertTrue(manager.getActions().isEmpty());
        assertNull(manager.getActionByKey(1));
    }

    @Test
    void testMultipleKeyBindingsToSameAction() {
        InputManager manager = InputManager.getInstance();
        manager.mapAction("move", 65);
        manager.mapAction("move", 68);

        assertEquals("move", manager.getActionByKey(65));
        assertEquals("move", manager.getActionByKey(68));
    }

    private static class TestListener implements InputManager.InputListener {
        boolean called = false;
        String lastAction = null;

        @Override
        public void onActionPressed(String actionName) {
            called = true;
            lastAction = actionName;
        }
    }
}
