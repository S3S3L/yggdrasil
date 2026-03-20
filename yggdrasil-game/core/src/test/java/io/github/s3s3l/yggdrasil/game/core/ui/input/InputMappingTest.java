package io.github.s3s3l.yggdrasil.game.core.ui.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 输入映射单元测试
 */
class InputMappingTest {

    private InputMapping mapping;

    @BeforeEach
    void setUp() {
        mapping = new InputMapping("testAction");
    }

    @Test
    void testGetActionName() {
        assertEquals("testAction", mapping.getActionName());
    }

    @Test
    void testAddKeyBinding() {
        mapping.addKeyBinding(1);
        mapping.addKeyBinding(2);

        List<InputMapping.KeyBinding> bindings = mapping.getKeyBindings();
        assertEquals(2, bindings.size());
    }

    @Test
    void testAddGestureBinding() {
        mapping.addGestureBinding(Gesture.SWIPE_UP);
        mapping.addGestureBinding(Gesture.DOUBLE_TAP);

        List<InputMapping.GestureBinding> bindings = mapping.getGestureBindings();
        assertEquals(2, bindings.size());
    }

    @Test
    void testRemoveKeyBinding() {
        mapping.addKeyBinding(1);
        mapping.addKeyBinding(2);

        mapping.removeKeyBinding(1);

        List<InputMapping.KeyBinding> bindings = mapping.getKeyBindings();
        assertEquals(1, bindings.size());
        assertFalse(mapping.hasKeyBinding(1));
    }

    @Test
    void testRemoveGestureBinding() {
        mapping.addGestureBinding(Gesture.SWIPE_UP);
        mapping.addGestureBinding(Gesture.SWIPE_DOWN);

        mapping.removeGestureBinding(Gesture.SWIPE_UP);

        List<InputMapping.GestureBinding> bindings = mapping.getGestureBindings();
        assertEquals(1, bindings.size());
        assertFalse(mapping.hasGestureBinding(Gesture.SWIPE_UP));
    }

    @Test
    void testHasKeyBinding() {
        mapping.addKeyBinding(65); // KeyEvent.KEYCODE_A
        assertTrue(mapping.hasKeyBinding(65));
        assertFalse(mapping.hasKeyBinding(66));
    }

    @Test
    void testHasGestureBinding() {
        mapping.addGestureBinding(Gesture.TAP);
        assertTrue(mapping.hasGestureBinding(Gesture.TAP));
        assertFalse(mapping.hasGestureBinding(Gesture.SWIPE_UP));
    }

    @Test
    void testGetKeyBindingsReturnsCopy() {
        mapping.addKeyBinding(1);
        List<InputMapping.KeyBinding> bindings = mapping.getKeyBindings();
        bindings.clear();

        // 原始绑定不受影响
        assertEquals(1, mapping.getKeyBindings().size());
    }

    @Test
    void testGetGestureBindingsReturnsCopy() {
        mapping.addGestureBinding(Gesture.TAP);
        List<InputMapping.GestureBinding> bindings = mapping.getGestureBindings();
        bindings.clear();

        // 原始绑定不受影响
        assertEquals(1, mapping.getGestureBindings().size());
    }

    @Test
    void testClear() {
        mapping.addKeyBinding(1);
        mapping.addKeyBinding(2);
        mapping.addGestureBinding(Gesture.TAP);

        mapping.clear();

        assertTrue(mapping.getKeyBindings().isEmpty());
        assertTrue(mapping.getGestureBindings().isEmpty());
    }

    @Test
    void testKeyBindingEquals() {
        InputMapping.KeyBinding a = new InputMapping.KeyBinding(65);
        InputMapping.KeyBinding b = new InputMapping.KeyBinding(65);
        InputMapping.KeyBinding c = new InputMapping.KeyBinding(66);

        assertEquals(a, b);
        assertNotEquals(a, c);
    }

    @Test
    void testKeyBindingHashCode() {
        InputMapping.KeyBinding a = new InputMapping.KeyBinding(65);
        InputMapping.KeyBinding b = new InputMapping.KeyBinding(65);

        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testGestureBindingEquals() {
        InputMapping.GestureBinding a = new InputMapping.GestureBinding(Gesture.TAP);
        InputMapping.GestureBinding b = new InputMapping.GestureBinding(Gesture.TAP);
        InputMapping.GestureBinding c = new InputMapping.GestureBinding(Gesture.SWIPE_UP);

        assertEquals(a, b);
        assertNotEquals(a, c);
    }

    @Test
    void testGestureBindingHashCode() {
        InputMapping.GestureBinding a = new InputMapping.GestureBinding(Gesture.TAP);
        InputMapping.GestureBinding b = new InputMapping.GestureBinding(Gesture.TAP);

        assertEquals(a.hashCode(), b.hashCode());
    }
}
