package io.github.s3s3l.yggdrasil.game.core.ui.binding;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 字符串属性单元测试
 */
class StringPropertyTest {

    private StringProperty property;

    @BeforeEach
    void setUp() {
        property = new StringProperty();
    }

    @Test
    void testDefaultValue() {
        assertEquals("", property.getValue());
        assertTrue(property.isEmpty());
    }

    @Test
    void testCreateWithValue() {
        StringProperty prop = new StringProperty("Hello");
        assertEquals("Hello", prop.getValue());
        assertFalse(prop.isEmpty());
    }

    @Test
    void testCreateFactory() {
        StringProperty prop = StringProperty.create("World");
        assertEquals("World", prop.getValue());
    }

    @Test
    void testSetValue() {
        property.setValue("Test");
        assertEquals("Test", property.getValue());
    }

    @Test
    void testSetNull() {
        property.setValue(null);
        assertEquals("", property.getValue());
    }

    @Test
    void testLength() {
        property.setValue("Hello");
        assertEquals(5, property.length());
    }

    @Test
    void testIsEmpty() {
        assertTrue(property.isEmpty());
        property.setValue("Not empty");
        assertFalse(property.isEmpty());
    }

    @Test
    void testAddListener() {
        final boolean[] changed = {false};
        property.addListener((ov, oldVal, newVal) -> changed[0] = true);
        property.setValue("New value");
        assertTrue(changed[0]);
    }

    @Test
    void testRemoveListener() {
        ChangeListener<String> listener = (ov, oldVal, newVal) -> {};
        property.addListener(listener);
        property.removeListener(listener);
        // Should not throw
    }

    @Test
    void testBind() {
        StringProperty source = new StringProperty("Source");
        property.bind(source);
        assertEquals("Source", property.getValue());
        source.setValue("Updated");
        assertEquals("Updated", property.getValue());
    }

    @Test
    void testUnbind() {
        StringProperty source = new StringProperty("Source");
        property.bind(source);
        assertEquals("Source", property.getValue());
        property.unbind();
        source.setValue("Updated");
        // After unbind, property should keep its last value from source
        assertEquals("Source", property.getValue());
    }

    @Test
    void testIsBound() {
        assertFalse(property.isBound());
        property.bind(new StringProperty("Source"));
        assertTrue(property.isBound());
    }

    @Test
    void testBidirectionalBinding() {
        StringProperty other = new StringProperty("Other");
        property.bindBidirectional(other);
        property.setValue("New");
        assertEquals("New", other.getValue());
        other.setValue("From other");
        assertEquals("From other", property.getValue());
    }

    @Test
    void testEquals() {
        StringProperty other = new StringProperty("Test");
        property.setValue("Test");
        assertEquals(property, other);
    }

    @Test
    void testHashCode() {
        property.setValue("Test");
        assertEquals("Test".hashCode(), property.hashCode());
    }

    @Test
    void testToString() {
        property.setValue("Hello");
        assertTrue(property.toString().contains("Hello"));
    }
}
