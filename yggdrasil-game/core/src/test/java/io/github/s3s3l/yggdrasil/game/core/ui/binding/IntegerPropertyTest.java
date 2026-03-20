package io.github.s3s3l.yggdrasil.game.core.ui.binding;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 整数属性单元测试
 */
class IntegerPropertyTest {

    private IntegerProperty property;

    @BeforeEach
    void setUp() {
        property = new IntegerProperty();
    }

    @Test
    void testDefaultValue() {
        assertEquals(0, property.get());
    }

    @Test
    void testCreateWithValue() {
        IntegerProperty prop = new IntegerProperty(42);
        assertEquals(42, prop.get());
    }

    @Test
    void testCreateFactory() {
        IntegerProperty prop = IntegerProperty.create(100);
        assertEquals(100, prop.get());
    }

    @Test
    void testSetValue() {
        property.setValue(50);
        assertEquals(50, property.get());
    }

    @Test
    void testSetPrimitive() {
        property.set(25);
        assertEquals(25, property.get());
    }

    @Test
    void testAdd() {
        property.setValue(10);
        IntegerBinding result = property.add(5);
        assertEquals(15, result.get());
    }

    @Test
    void testSubtract() {
        property.setValue(10);
        IntegerBinding result = property.subtract(3);
        assertEquals(7, result.get());
    }

    @Test
    void testMultiply() {
        property.setValue(5);
        IntegerBinding result = property.multiply(3);
        assertEquals(15, result.get());
    }

    @Test
    void testDivide() {
        property.setValue(10);
        IntegerBinding result = property.divide(2);
        assertEquals(5, result.get());
    }

    @Test
    void testAddListener() {
        final boolean[] changed = {false};
        property.addListener((ov, oldVal, newVal) -> changed[0] = true);
        property.setValue(10);
        assertTrue(changed[0]);
    }

    @Test
    void testBind() {
        IntegerProperty source = new IntegerProperty(100);
        property.bind(source);
        assertEquals(100, property.get());
        source.setValue(200);
        assertEquals(200, property.get());
    }

    @Test
    void testBidirectionalBinding() {
        IntegerProperty other = new IntegerProperty();
        property.bindBidirectional(other);
        property.setValue(42);
        assertEquals(42, other.get());
        other.setValue(99);
        assertEquals(99, property.get());
    }
}
