package io.github.s3s3l.yggdrasil.game.core.ui.binding;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 绑定工具类单元测试
 */
class BindingsTest {

    @Test
    void testCreateStringBinding() {
        StringProperty prop = new StringProperty("Hello");
        StringBinding binding = Bindings.createStringBinding(() -> prop.getValue().toUpperCase(), prop);
        assertEquals("HELLO", binding.getValue());
        prop.setValue("World");
        assertEquals("WORLD", binding.getValue());
    }

    @Test
    void testCreateIntegerBinding() {
        IntegerProperty prop = new IntegerProperty(5);
        IntegerBinding binding = Bindings.createIntegerBinding(() -> prop.get() * 2, prop);
        assertEquals(10, binding.get());
    }

    @Test
    void testCreateBooleanBinding() {
        BooleanProperty prop = new BooleanProperty(true);
        BooleanBinding binding = Bindings.createBooleanBinding(() -> !prop.get(), prop);
        assertFalse(binding.get());
    }

    @Test
    void testFormat() {
        StringProperty name = new StringProperty("John");
        IntegerProperty age = new IntegerProperty(25);
        StringBinding binding = Bindings.format("Name: %s, Age: %d", name, age);
        assertTrue(binding.getValue().contains("John"));
        assertTrue(binding.getValue().contains("25"));
    }

    @Test
    void testConcatenate() {
        StringProperty a = new StringProperty("Hello");
        StringProperty b = new StringProperty("World");
        StringBinding binding = Bindings.concatenate(a, b);
        assertEquals("HelloWorld", binding.getValue());
    }

    @Test
    void testEqual() {
        StringProperty a = new StringProperty("Test");
        StringProperty b = new StringProperty("Test");
        BooleanBinding binding = Bindings.equal(a, b);
        assertTrue(binding.get());
        b.setValue("Different");
        assertFalse(binding.get());
    }

    @Test
    void testGreaterThan() {
        IntegerProperty a = new IntegerProperty(10);
        IntegerProperty b = new IntegerProperty(5);
        BooleanBinding binding = Bindings.greaterThanIntegers(a, b);
        assertTrue(binding.get());
        a.setValue(3);
        assertFalse(binding.get());
    }

    @Test
    void testLessThan() {
        IntegerProperty a = new IntegerProperty(3);
        IntegerProperty b = new IntegerProperty(5);
        BooleanBinding binding = Bindings.lessThanIntegers(a, b);
        assertTrue(binding.get());
    }

    @Test
    void testAnd() {
        BooleanProperty a = new BooleanProperty(true);
        BooleanProperty b = new BooleanProperty(true);
        BooleanBinding binding = Bindings.and(a, b);
        assertTrue(binding.get());
        b.setValue(false);
        assertFalse(binding.get());
    }

    @Test
    void testOr() {
        BooleanProperty a = new BooleanProperty(false);
        BooleanProperty b = new BooleanProperty(true);
        BooleanBinding binding = Bindings.or(a, b);
        assertTrue(binding.get());
        b.setValue(false);
        assertFalse(binding.get());
    }

    @Test
    void testNot() {
        BooleanProperty a = new BooleanProperty(true);
        BooleanBinding binding = Bindings.not(a);
        assertFalse(binding.get());
    }

    @Test
    void testAdd() {
        IntegerProperty a = new IntegerProperty(5);
        IntegerProperty b = new IntegerProperty(3);
        IntegerBinding binding = Bindings.addIntegers(a, b);
        assertEquals(8, binding.get());
    }

    @Test
    void testSubtract() {
        IntegerProperty a = new IntegerProperty(10);
        IntegerProperty b = new IntegerProperty(4);
        IntegerBinding binding = Bindings.subtractIntegers(a, b);
        assertEquals(6, binding.get());
    }

    @Test
    void testMultiply() {
        IntegerProperty a = new IntegerProperty(6);
        IntegerProperty b = new IntegerProperty(7);
        IntegerBinding binding = Bindings.multiplyIntegers(a, b);
        assertEquals(42, binding.get());
    }

    @Test
    void testDivide() {
        IntegerProperty a = new IntegerProperty(20);
        IntegerProperty b = new IntegerProperty(4);
        IntegerBinding binding = Bindings.divideIntegers(a, b);
        assertEquals(5, binding.get());
    }

    @Test
    void testWhenNull() {
        ObjectProperty<String> source = new ObjectProperty<>(null);
        ObjectBinding<String> binding = Bindings.whenNull(source, "Default");
        assertEquals("Default", binding.getValue());
        source.setValue("Value");
        assertEquals("Value", binding.getValue());
    }

    @Test
    void testSelectWhen() {
        BooleanProperty condition = new BooleanProperty(true);
        StringProperty whenTrue = new StringProperty("Yes");
        StringProperty whenFalse = new StringProperty("No");
        ObjectBinding<String> binding = Bindings.selectWhen(condition, whenTrue, whenFalse);
        assertEquals("Yes", binding.getValue());
        condition.setValue(false);
        assertEquals("No", binding.getValue());
    }

    @Test
    void testDoubleAdd() {
        DoubleProperty a = new DoubleProperty(5.5);
        DoubleProperty b = new DoubleProperty(4.5);
        DoubleBinding binding = Bindings.addDoubles(a, b);
        assertEquals(10.0, binding.get());
    }

    @Test
    void testDoubleDivide() {
        DoubleProperty a = new DoubleProperty(10.0);
        DoubleProperty b = new DoubleProperty(2.0);
        DoubleBinding binding = Bindings.divideDoubles(a, b);
        assertEquals(5.0, binding.get());
    }
}
