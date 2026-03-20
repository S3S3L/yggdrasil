package io.github.s3s3l.yggdrasil.game.core.ui.binding;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 布尔属性单元测试
 */
class BooleanPropertyTest {

    private BooleanProperty property;

    @BeforeEach
    void setUp() {
        property = new BooleanProperty();
    }

    @Test
    void testDefaultValue() {
        assertFalse(property.get());
    }

    @Test
    void testCreateWithValue() {
        BooleanProperty prop = new BooleanProperty(true);
        assertTrue(prop.get());
    }

    @Test
    void testCreateFactory() {
        BooleanProperty prop = BooleanProperty.create(true);
        assertTrue(prop.get());
    }

    @Test
    void testSetValue() {
        property.setValue(true);
        assertTrue(property.get());
    }

    @Test
    void testSetPrimitive() {
        property.set(true);
        assertTrue(property.get());
    }

    @Test
    void testNot() {
        property.setValue(true);
        BooleanBinding result = property.not();
        assertFalse(result.get());
    }

    @Test
    void testAnd() {
        BooleanProperty other = new BooleanProperty(true);
        property.setValue(true);
        BooleanBinding result = property.and(other);
        assertTrue(result.get());
    }

    @Test
    void testOr() {
        BooleanProperty other = new BooleanProperty(false);
        property.setValue(true);
        BooleanBinding result = property.or(other);
        assertTrue(result.get());
    }

    @Test
    void testAddListener() {
        final boolean[] changed = {false};
        property.addListener((ov, oldVal, newVal) -> changed[0] = true);
        property.setValue(true);
        assertTrue(changed[0]);
    }

    @Test
    void testBind() {
        BooleanProperty source = new BooleanProperty(true);
        property.bind(source);
        assertTrue(property.get());
    }

    @Test
    void testBidirectionalBinding() {
        BooleanProperty other = new BooleanProperty();
        property.bindBidirectional(other);
        property.setValue(true);
        assertTrue(other.get());
        other.setValue(false);
        assertFalse(property.get());
    }
}
