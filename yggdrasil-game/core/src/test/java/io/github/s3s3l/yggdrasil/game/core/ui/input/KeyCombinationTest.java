package io.github.s3s3l.yggdrasil.game.core.ui.input;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * KeyCombination 单元测试
 */
class KeyCombinationTest {

    @Test
    void testCreateNoModifier() {
        KeyCombination combo = new KeyCombination(65);

        assertEquals(65, combo.getMainKey());
        assertEquals(0, combo.getModifierCount());
        assertFalse(combo.hasModifiers());
    }

    @Test
    void testCreateSingleModifier() {
        KeyCombination combo = new KeyCombination(65, KeyCombination.Modifier.CTRL);

        assertEquals(65, combo.getMainKey());
        assertEquals(1, combo.getModifierCount());
        assertTrue(combo.hasModifiers());
        assertTrue(combo.hasModifier(KeyCombination.Modifier.CTRL));
    }

    @Test
    void testCreateMultipleModifiers() {
        KeyCombination combo = new KeyCombination(
                65,
                KeyCombination.Modifier.CTRL,
                KeyCombination.Modifier.SHIFT
        );

        assertEquals(65, combo.getMainKey());
        assertEquals(2, combo.getModifierCount());
        assertTrue(combo.hasModifier(KeyCombination.Modifier.CTRL));
        assertTrue(combo.hasModifier(KeyCombination.Modifier.SHIFT));
        assertFalse(combo.hasModifier(KeyCombination.Modifier.ALT));
    }

    @Test
    void testCreateWithSet() {
        Set<KeyCombination.Modifier> modifiers = EnumSet.of(
                KeyCombination.Modifier.CTRL,
                KeyCombination.Modifier.ALT
        );
        KeyCombination combo = new KeyCombination(65, modifiers);

        assertEquals(65, combo.getMainKey());
        assertEquals(2, combo.getModifierCount());
        assertTrue(combo.hasModifier(KeyCombination.Modifier.CTRL));
        assertTrue(combo.hasModifier(KeyCombination.Modifier.ALT));
    }

    @Test
    void testGetModifiers() {
        KeyCombination combo = new KeyCombination(
                65,
                KeyCombination.Modifier.CTRL,
                KeyCombination.Modifier.SHIFT
        );

        Set<KeyCombination.Modifier> modifiers = combo.getModifiers();
        assertEquals(2, modifiers.size());
        assertTrue(modifiers.contains(KeyCombination.Modifier.CTRL));
        assertTrue(modifiers.contains(KeyCombination.Modifier.SHIFT));
    }

    @Test
    void testGetModifiersReturnsUnmodifiable() {
        KeyCombination combo = new KeyCombination(65, KeyCombination.Modifier.CTRL);

        assertThrows(UnsupportedOperationException.class, () -> {
            combo.getModifiers().add(KeyCombination.Modifier.ALT);
        });
    }

    @Test
    void testMatches() {
        KeyCombination combo = new KeyCombination(
                65,
                KeyCombination.Modifier.CTRL,
                KeyCombination.Modifier.SHIFT
        );

        Set<KeyCombination.Modifier> correctModifiers = EnumSet.of(
                KeyCombination.Modifier.CTRL,
                KeyCombination.Modifier.SHIFT
        );

        Set<KeyCombination.Modifier> wrongModifiers = EnumSet.of(
                KeyCombination.Modifier.CTRL
        );

        assertTrue(combo.matches(65, correctModifiers));
        assertFalse(combo.matches(66, correctModifiers));
        assertFalse(combo.matches(65, wrongModifiers));
    }

    @Test
    void testEqualsAndHashCode() {
        KeyCombination combo1 = new KeyCombination(
                65,
                KeyCombination.Modifier.CTRL,
                KeyCombination.Modifier.SHIFT
        );

        KeyCombination combo2 = new KeyCombination(
                65,
                KeyCombination.Modifier.CTRL,
                KeyCombination.Modifier.SHIFT
        );

        KeyCombination combo3 = new KeyCombination(66, KeyCombination.Modifier.CTRL);

        assertEquals(combo1, combo2);
        assertNotEquals(combo1, combo3);
        assertEquals(combo1.hashCode(), combo2.hashCode());
    }

    @Test
    void testToString() {
        KeyCombination combo = new KeyCombination(
                65,
                KeyCombination.Modifier.CTRL,
                KeyCombination.Modifier.SHIFT
        );

        String result = combo.toString();
        assertTrue(result.contains("Ctrl"));
        assertTrue(result.contains("Shift"));
        assertTrue(result.contains("65"));
    }

    @Test
    void testToStringNoModifier() {
        KeyCombination combo = new KeyCombination(65);

        String result = combo.toString();
        assertEquals("Key65", result);
    }

    @Test
    void testCreateStaticMethod() {
        KeyCombination combo = KeyCombination.create(65, true, false, true);

        assertEquals(65, combo.getMainKey());
        assertEquals(2, combo.getModifierCount());
        assertTrue(combo.hasModifier(KeyCombination.Modifier.CTRL));
        assertTrue(combo.hasModifier(KeyCombination.Modifier.SHIFT));
        assertFalse(combo.hasModifier(KeyCombination.Modifier.ALT));
    }

    @Test
    void testCreateStaticMethodNoModifiers() {
        KeyCombination combo = KeyCombination.create(65, false, false, false);

        assertEquals(65, combo.getMainKey());
        assertEquals(0, combo.getModifierCount());
    }

    @Test
    void testCreateStaticMethodAllModifiers() {
        KeyCombination combo = KeyCombination.create(65, true, true, true);

        assertEquals(65, combo.getMainKey());
        assertEquals(3, combo.getModifierCount());
        assertTrue(combo.hasModifier(KeyCombination.Modifier.CTRL));
        assertTrue(combo.hasModifier(KeyCombination.Modifier.ALT));
        assertTrue(combo.hasModifier(KeyCombination.Modifier.SHIFT));
    }
}
