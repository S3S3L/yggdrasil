package io.github.s3s3l.yggdrasil.game.core.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 数据绑定单元测试
 */
class DataBindingTest {

    private ObservableProperty<String> property;

    @BeforeEach
    void setUp() {
        property = new ObservableProperty<>("initial");
    }

    @Test
    void testPropertyCreation() {
        assertEquals("initial", property.getValue());
    }

    @Test
    void testSetValue() {
        property.setValue("new value");
        assertEquals("new value", property.getValue());
    }

    @Test
    void testAddChangeListener() {
        final boolean[] changed = {false};
        property.addChangeListener((oldValue, newValue) -> changed[0] = true);

        property.setValue("new value");
        assertTrue(changed[0]);
    }

    @Test
    void testChangeListenerReceivesValues() {
        final String[][] values = {new String[2]};
        property.addChangeListener((oldValue, newValue) -> {
            values[0][0] = oldValue;
            values[0][1] = newValue;
        });

        property.setValue("new value");
        assertEquals("initial", values[0][0]);
        assertEquals("new value", values[0][1]);
    }

    @Test
    void testRemoveChangeListener() {
        final boolean[] changed = {false};
        DataChangeListener<String> listener = (oldValue, newValue) -> changed[0] = true;

        property.addChangeListener(listener);
        property.removeChangeListener(listener);

        property.setValue("new value");
        assertFalse(changed[0]);
    }

    @Test
    void testClearListeners() {
        final boolean[] changed = {false};
        property.addChangeListener((oldValue, newValue) -> changed[0] = true);

        property.clearListeners();
        property.setValue("new value");

        assertFalse(changed[0]);
    }

    @Test
    void testGetListenerCount() {
        assertEquals(0, property.getListenerCount());

        property.addChangeListener((oldValue, newValue) -> {});
        assertEquals(1, property.getListenerCount());

        property.addChangeListener((oldValue, newValue) -> {});
        assertEquals(2, property.getListenerCount());
    }

    @Test
    void testMultipleListeners() {
        final int[] count = {0};
        property.addChangeListener((oldValue, newValue) -> count[0]++);
        property.addChangeListener((oldValue, newValue) -> count[0]++);
        property.addChangeListener((oldValue, newValue) -> count[0]++);

        property.setValue("new value");
        assertEquals(3, count[0]);
    }
}
