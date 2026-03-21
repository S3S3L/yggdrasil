package io.github.s3s3l.yggdrasil.game.core.ui.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * InputManagerEx 单元测试
 */
class InputManagerExTest {

    private InputManagerEx inputManager;

    @BeforeEach
    void setUp() {
        InputManagerEx.resetInstance();
        inputManager = InputManagerEx.getInstance();
    }

    @Test
    void testGetInstance() {
        assertNotNull(inputManager);
    }

    @Test
    void testResetInstance() {
        InputManagerEx.resetInstance();
        InputManagerEx another = InputManagerEx.getInstance();
        assertNotSame(inputManager, another);
    }

    @Test
    void testGetKeyboardState() {
        assertNotNull(inputManager.getKeyboardState());
    }

    @Test
    void testGetShortcutManager() {
        assertNotNull(inputManager.getShortcutManager());
    }

    @Test
    void testGetLegacyManager() {
        assertNotNull(inputManager.getLegacyManager());
    }

    @Test
    void testKeyDown() {
        inputManager.keyDown(65, 1000);

        assertTrue(inputManager.isKeyPressed(65));
        assertTrue(inputManager.isKeyHeld(65));
    }

    @Test
    void testKeyUp() {
        inputManager.keyDown(65, 1000);
        inputManager.update();
        inputManager.keyUp(65);

        assertTrue(inputManager.isKeyReleased(65));
        assertFalse(inputManager.isKeyHeld(65));
    }

    @Test
    void testGetHoldDuration() {
        inputManager.keyDown(65, 1000);
        inputManager.update();

        int duration = inputManager.getHoldDuration(65);
        assertTrue(duration >= 0);
    }

    @Test
    void testUpdate() {
        inputManager.keyDown(65, 1000);

        // 更新前是 PRESSED
        assertTrue(inputManager.isKeyPressed(65));

        // 更新后变为 HELD
        inputManager.update();
        assertFalse(inputManager.isKeyPressed(65));
        assertTrue(inputManager.isKeyHeld(65));
    }

    @Test
    void testRegisterShortcut() {
        boolean[] triggered = new boolean[]{false};
        inputManager.registerShortcut("test", 65, () -> triggered[0] = true);

        inputManager.keyDown(65, 1000);
        inputManager.update();

        assertTrue(triggered[0]);
    }

    @Test
    void testRegisterShortcutWithModifiers() {
        boolean[] triggered = new boolean[]{false};
        inputManager.registerShortcut("ctrl+a", 65, EnumSet.of(KeyCombination.Modifier.CTRL), () -> triggered[0] = true);

        // 按下 Ctrl
        inputManager.keyDown(131, 1000);
        // 按下 A
        inputManager.keyDown(65, 1000);
        inputManager.update();

        assertTrue(triggered[0]);
    }

    @Test
    void testUnregisterShortcut() {
        boolean[] triggered = new boolean[]{false};
        inputManager.registerShortcut("test", 65, () -> triggered[0] = true);
        inputManager.unregisterShortcut("test");

        inputManager.keyDown(65, 1000);
        inputManager.update();

        assertFalse(triggered[0]);
    }

    @Test
    void testMapAction() {
        inputManager.mapAction("jump", 32);  // 空格键
        assertTrue(inputManager.isActionPressed("jump"));
    }

    @Test
    void testClear() {
        inputManager.keyDown(65, 1000);
        inputManager.clear();

        assertTrue(inputManager.isKeyUp(65));
    }

    @Test
    void testGetPressedModifiers() {
        // 按下 Ctrl
        inputManager.keyDown(131, 1000);

        var modifiers = inputManager.getPressedModifiers();
        assertTrue(modifiers.contains(KeyCombination.Modifier.CTRL));
    }

    @Test
    void testMultipleKeys() {
        inputManager.keyDown(65, 1000);
        inputManager.keyDown(66, 1000);

        assertTrue(inputManager.isKeyPressed(65));
        assertTrue(inputManager.isKeyPressed(66));

        inputManager.keyUp(65);
        inputManager.update();

        assertFalse(inputManager.isKeyPressed(65));
        assertTrue(inputManager.isKeyPressed(66));
    }
}
