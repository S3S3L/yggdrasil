package io.github.s3s3l.yggdrasil.game.core.ui.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ShortcutManager 单元测试
 */
class ShortcutManagerTest {

    private ShortcutManager shortcutManager;
    private KeyboardState keyboardState;
    private boolean[] actionTriggered;

    @BeforeEach
    void setUp() {
        shortcutManager = new ShortcutManager();
        keyboardState = new KeyboardState();
        shortcutManager.setKeyboardState(keyboardState);
        actionTriggered = new boolean[]{false};
    }

    @Test
    void testRegisterSimple() {
        shortcutManager.register("test", 65, () -> actionTriggered[0] = true);

        assertTrue(shortcutManager.isRegistered(new KeyCombination(65)));
    }

    @Test
    void testRegisterWithModifiers() {
        Set<KeyCombination.Modifier> modifiers = EnumSet.of(KeyCombination.Modifier.CTRL);
        shortcutManager.register("ctrl+a", 65, modifiers, () -> actionTriggered[0] = true);

        assertTrue(shortcutManager.isRegistered(
                new KeyCombination(65, KeyCombination.Modifier.CTRL)
        ));
    }

    @Test
    void testUnregisterByName() {
        shortcutManager.register("test", 65, () -> actionTriggered[0] = true);
        shortcutManager.unregister("test");

        assertFalse(shortcutManager.isRegistered(new KeyCombination(65)));
    }

    @Test
    void testUnregisterByCombination() {
        shortcutManager.register("test", 65, () -> actionTriggered[0] = true);
        shortcutManager.unregister(new KeyCombination(65));

        assertFalse(shortcutManager.isRegistered(new KeyCombination(65)));
        assertNull(shortcutManager.getShortcut("test"));
    }

    @Test
    void testGetShortcut() {
        shortcutManager.register("test", 65, () -> actionTriggered[0] = true);

        ShortcutManager.Shortcut shortcut = shortcutManager.getShortcut("test");
        assertNotNull(shortcut);
        assertEquals("test", shortcut.getName());
    }

    @Test
    void testGetAllShortcuts() {
        shortcutManager.register("test1", 65, () -> {});
        shortcutManager.register("test2", 66, () -> {});
        shortcutManager.register("test3", 67, () -> {});

        List<ShortcutManager.Shortcut> shortcuts = shortcutManager.getAllShortcuts();
        assertEquals(3, shortcuts.size());
    }

    @Test
    void testUpdateTriggersShortcut() {
        shortcutManager.register("test", 65, () -> actionTriggered[0] = true);

        // 按下按键
        keyboardState.keyDown(65, 1000);

        // 更新触发
        shortcutManager.update(EnumSet.noneOf(KeyCombination.Modifier.class), 1000);

        assertTrue(actionTriggered[0]);
    }

    @Test
    void testUpdateWithModifiers() {
        Set<KeyCombination.Modifier> modifiers = EnumSet.of(KeyCombination.Modifier.CTRL);
        shortcutManager.register("ctrl+a", 65, modifiers, () -> actionTriggered[0] = true);

        // 按下 Ctrl 和 A
        keyboardState.keyDown(131, 1000);  // Ctrl
        keyboardState.keyDown(65, 1000);   // A

        // 更新时传入 Ctrl 修饰键
        shortcutManager.update(EnumSet.of(KeyCombination.Modifier.CTRL), 1000);

        assertTrue(actionTriggered[0]);
    }

    @Test
    void testUpdateWithWrongModifiers() {
        Set<KeyCombination.Modifier> requiredModifiers = EnumSet.of(KeyCombination.Modifier.CTRL);
        shortcutManager.register("ctrl+a", 65, requiredModifiers, () -> actionTriggered[0] = true);

        // 按下 A 但没有按 Ctrl
        keyboardState.keyDown(65, 1000);

        // 更新时没有传入修饰键
        shortcutManager.update(EnumSet.noneOf(KeyCombination.Modifier.class), 1000);

        assertFalse(actionTriggered[0]);
    }

    @Test
    void testMultipleShortcuts() {
        boolean[] triggered = new boolean[]{false, false};
        shortcutManager.register("test1", 65, () -> triggered[0] = true);
        shortcutManager.register("test2", 66, () -> triggered[1] = true);

        keyboardState.keyDown(65, 1000);
        shortcutManager.update(EnumSet.noneOf(KeyCombination.Modifier.class), 1000);

        assertTrue(triggered[0]);
        assertFalse(triggered[1]);
    }

    @Test
    void testShortcutEquals() {
        ShortcutManager.Shortcut shortcut1 = new ShortcutManager.Shortcut(
                "test", new KeyCombination(65), () -> {}
        );
        ShortcutManager.Shortcut shortcut2 = new ShortcutManager.Shortcut(
                "test", new KeyCombination(65), () -> {}
        );
        ShortcutManager.Shortcut shortcut3 = new ShortcutManager.Shortcut(
                "test2", new KeyCombination(66), () -> {}
        );

        assertEquals(shortcut1, shortcut2);
        assertNotEquals(shortcut1, shortcut3);
    }

    @Test
    void testShortcutToString() {
        ShortcutManager.Shortcut shortcut = new ShortcutManager.Shortcut(
                "test", new KeyCombination(65), () -> {}
        );

        String result = shortcut.toString();
        assertTrue(result.contains("test"));
        assertTrue(result.contains("Key65"));
    }
}
