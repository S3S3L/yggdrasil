package io.github.s3s3l.yggdrasil.game.core.ui.binding;

import io.github.s3s3l.yggdrasil.game.core.ui.TestUIComponent;
import io.github.s3s3l.yggdrasil.game.core.ui.UIVisibility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 组件绑定集成测试
 */
class UIComponentBindingTest {

    private TestUIComponent component;

    @BeforeEach
    void setUp() {
        component = new TestUIComponent("Test");
    }

    @Test
    void testNameProperty() {
        StringProperty nameProp = component.nameProperty();
        assertEquals("Test", nameProp.getValue());
        nameProp.setValue("NewName");
        assertEquals("NewName", component.getName());
    }

    @Test
    void testEnabledProperty() {
        BooleanProperty enabledProp = component.enabledProperty();
        assertTrue(enabledProp.get());
        enabledProp.setValue(false);
        assertFalse(component.isEnabled());
    }

    @Test
    void testVisibleProperty() {
        BooleanProperty visibleProp = component.visibleProperty();
        assertTrue(visibleProp.get());
        visibleProp.setValue(false);
        assertFalse(component.isVisible());
    }

    @Test
    void testBindName() {
        StringProperty source = new StringProperty("BoundName");
        component.bindName(source);
        assertEquals("BoundName", component.getName());
        source.setValue("UpdatedName");
        assertEquals("UpdatedName", component.getName());
    }

    @Test
    void testBindEnabled() {
        BooleanProperty source = new BooleanProperty(false);
        component.bindEnabled(source);
        assertFalse(component.isEnabled());
        source.setValue(true);
        assertTrue(component.isEnabled());
    }

    @Test
    void testBindVisible() {
        BooleanProperty source = new BooleanProperty(false);
        component.bindVisible(source);
        assertFalse(component.isVisible());
        source.setValue(true);
        assertTrue(component.isVisible());
    }

    @Test
    void testBindNameBidirectional() {
        StringProperty source = new StringProperty("Source");
        component.bindNameBidirectional(source);
        component.setName("Component");
        assertEquals("Component", source.getValue());
        source.setValue("Source2");
        assertEquals("Source2", component.getName());
    }

    @Test
    void testSetEnabledUpdatesProperty() {
        component.setEnabled(false);
        assertFalse(component.enabledProperty().get());
    }

    @Test
    void testSetVisibilityUpdatesProperty() {
        component.setVisibility(UIVisibility.HIDDEN);
        assertFalse(component.visibleProperty().get());
        component.setVisibility(UIVisibility.VISIBLE);
        assertTrue(component.visibleProperty().get());
    }
}
