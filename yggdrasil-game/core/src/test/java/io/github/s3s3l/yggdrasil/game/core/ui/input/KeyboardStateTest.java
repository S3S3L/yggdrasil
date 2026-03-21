package io.github.s3s3l.yggdrasil.game.core.ui.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * KeyboardState 单元测试
 */
class KeyboardStateTest {

    private KeyboardState keyboardState;

    @BeforeEach
    void setUp() {
        keyboardState = new KeyboardState();
    }

    @Test
    void testInitialState() {
        // 初始状态所有按键应该是 UP
        assertFalse(keyboardState.isKeyPressed(65));
        assertFalse(keyboardState.isKeyHeld(65));
        assertFalse(keyboardState.isKeyReleased(65));
        assertTrue(keyboardState.isKeyUp(65));
    }

    @Test
    void testKeyDown() {
        keyboardState.keyDown(65, 1000);

        // 按下后应该是 PRESSED 状态
        assertTrue(keyboardState.isKeyPressed(65));
        assertTrue(keyboardState.isKeyHeld(65));
        assertFalse(keyboardState.isKeyReleased(65));
        assertFalse(keyboardState.isKeyUp(65));
    }

    @Test
    void testKeyUp() {
        keyboardState.keyDown(65, 1000);
        keyboardState.update(1100);  // 更新为 HELD 状态
        keyboardState.keyUp(65);

        // 释放后应该是 RELEASED 状态
        assertFalse(keyboardState.isKeyPressed(65));
        assertFalse(keyboardState.isKeyHeld(65));
        assertTrue(keyboardState.isKeyReleased(65));
        assertFalse(keyboardState.isKeyUp(65));
    }

    @Test
    void testStateTransition() {
        // PRESSED -> HELD -> RELEASED -> UP
        keyboardState.keyDown(65, 1000);
        assertTrue(keyboardState.isKeyPressed(65));

        // 更新后变为 HELD
        keyboardState.update(1100);
        assertFalse(keyboardState.isKeyPressed(65));
        assertTrue(keyboardState.isKeyHeld(65));

        // 释放
        keyboardState.keyUp(65);
        keyboardState.update(1200);
        assertTrue(keyboardState.isKeyUp(65));
    }

    @Test
    void testGetHoldDuration() {
        keyboardState.keyDown(65, 1000);
        keyboardState.update(1500);

        int duration = keyboardState.getHoldDuration(65, 1500);
        assertEquals(500, duration);
    }

    @Test
    void testHoldDurationNotPressed() {
        int duration = keyboardState.getHoldDuration(65, 1000);
        assertEquals(0, duration);
    }

    @Test
    void testKeyHeldAfterUpdate() {
        keyboardState.keyDown(65, 1000);

        // 刚按下时也是 HELD 状态
        assertTrue(keyboardState.isKeyHeld(65));

        // 更新后仍然是 HELD
        keyboardState.update(1100);
        assertTrue(keyboardState.isKeyHeld(65));
    }

    @Test
    void testMultipleKeys() {
        keyboardState.keyDown(65, 1000);
        keyboardState.keyDown(66, 1000);

        assertTrue(keyboardState.isKeyPressed(65));
        assertTrue(keyboardState.isKeyPressed(66));

        keyboardState.keyUp(65);

        assertFalse(keyboardState.isKeyPressed(65));
        assertTrue(keyboardState.isKeyPressed(66));
    }

    @Test
    void testReset() {
        keyboardState.keyDown(65, 1000);
        keyboardState.reset();

        assertTrue(keyboardState.isKeyUp(65));
    }

    @Test
    void testRepeatDelay() {
        keyboardState.setRepeatDelay(200);
        keyboardState.setRepeatInterval(100);

        keyboardState.keyDown(65, 0);

        // 在 repeatDelay 之前，不会触发重复
        keyboardState.update(100);
        assertTrue(keyboardState.isKeyHeld(65));

        // 超过 repeatDelay 后，会触发重复（PRESSED 一帧）
        keyboardState.update(250);
        assertTrue(keyboardState.isKeyPressed(65));
    }

    @Test
    void testGetPressedKeys() {
        keyboardState.keyDown(65, 1000);
        keyboardState.keyDown(66, 1000);
        keyboardState.keyDown(67, 1000);
        keyboardState.keyUp(67);

        var pressedKeys = keyboardState.getPressedKeys();
        assertEquals(2, pressedKeys.size());
        assertTrue(pressedKeys.contains(65));
        assertTrue(pressedKeys.contains(66));
        assertFalse(pressedKeys.contains(67));
    }

    @Test
    void testSetRepeatDelay() {
        keyboardState.setRepeatDelay(1000);
        keyboardState.setRepeatInterval(500);

        keyboardState.keyDown(65, 0);

        // 在 500ms 时，还未达到 repeatDelay
        keyboardState.update(500);
        assertFalse(keyboardState.isKeyPressed(65));

        // 在 1500ms 时，超过 repeatDelay，应该触发重复
        keyboardState.update(1500);
        assertTrue(keyboardState.isKeyPressed(65));
    }
}
