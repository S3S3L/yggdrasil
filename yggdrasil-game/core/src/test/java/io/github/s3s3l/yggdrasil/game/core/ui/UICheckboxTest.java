package io.github.s3s3l.yggdrasil.game.core.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 多选框组件单元测试
 */
class UICheckboxTest {

    private UICheckbox checkbox;

    @BeforeEach
    void setUp() {
        checkbox = new UICheckbox("Test Checkbox");
        checkbox.setId(1L);
    }

    @Test
    void testCheckboxCreation() {
        assertNotNull(checkbox);
        assertEquals("Test Checkbox", checkbox.getText());
        assertEquals(UIColor.WHITE, checkbox.getTextColor());
        assertEquals(16, checkbox.getFontSize());
        assertFalse(checkbox.isChecked());
        assertFalse(checkbox.isRadio());
        assertEquals(UICheckbox.CheckState.UNCHECKED, checkbox.getState());
    }

    @Test
    void testDefaultConstructor() {
        UICheckbox cb = new UICheckbox();
        assertNotNull(cb);
        assertNull(cb.getText());
    }

    @Test
    void testSetText() {
        checkbox.setText("New Text");
        assertEquals("New Text", checkbox.getText());
    }

    @Test
    void testSetTextColor() {
        UIColor color = UIColor.RED;
        checkbox.setTextColor(color);
        assertEquals(color, checkbox.getTextColor());
    }

    @Test
    void testSetFontSize() {
        checkbox.setFontSize(24);
        assertEquals(24, checkbox.getFontSize());
    }

    @Test
    void testSetChecked() {
        checkbox.setChecked(true);
        assertTrue(checkbox.isChecked());
        assertEquals(UICheckbox.CheckState.CHECKED, checkbox.getState());
    }

    @Test
    void testSetUnchecked() {
        checkbox.setChecked(true);
        checkbox.setChecked(false);
        assertFalse(checkbox.isChecked());
        assertEquals(UICheckbox.CheckState.UNCHECKED, checkbox.getState());
    }

    @Test
    void testSetRadio() {
        checkbox.setRadio(true);
        assertTrue(checkbox.isRadio());

        checkbox.setRadio(false);
        assertFalse(checkbox.isRadio());
    }

    @Test
    void testToggle() {
        assertFalse(checkbox.isChecked());

        checkbox.toggle();
        assertTrue(checkbox.isChecked());

        checkbox.toggle();
        assertFalse(checkbox.isChecked());
    }

    @Test
    void testSetState() {
        checkbox.setState(UICheckbox.CheckState.CHECKED);
        assertEquals(UICheckbox.CheckState.CHECKED, checkbox.getState());
        assertTrue(checkbox.isChecked());

        checkbox.setState(UICheckbox.CheckState.UNCHECKED);
        assertEquals(UICheckbox.CheckState.UNCHECKED, checkbox.getState());
        assertFalse(checkbox.isChecked());
    }

    @Test
    void testSetIndeterminate() {
        checkbox.setIndeterminate(true);
        assertEquals(UICheckbox.CheckState.INDETERMINATE, checkbox.getState());
        assertFalse(checkbox.isChecked());
    }

    @Test
    void testSetIndeterminateOnRadio() {
        checkbox.setRadio(true);
        checkbox.setChecked(true);
        checkbox.setIndeterminate(true);
        // 单选模式下不确定状态无效
        assertTrue(checkbox.isChecked());
    }

    @Test
    void testChangeListener() {
        final boolean[] listenerCalled = {false};
        final boolean[] capturedIsChecked = {false};

        checkbox.setListener((cb, isChecked) -> {
            listenerCalled[0] = true;
            capturedIsChecked[0] = isChecked;
        });

        checkbox.setChecked(true);

        assertTrue(listenerCalled[0]);
        assertTrue(capturedIsChecked[0]);
    }

    @Test
    void testChangeListenerOnToggle() {
        final int[] callCount = {0};

        checkbox.setListener((cb, isChecked) -> callCount[0]++);

        checkbox.toggle();
        assertEquals(1, callCount[0]);

        checkbox.toggle();
        assertEquals(2, callCount[0]);
    }

    @Test
    void testSetListener() {
        UICheckbox.ChangeListener listener = (cb, isChecked) -> {};
        checkbox.setListener(listener);
        assertNotNull(checkbox.getListener());
    }

    @Test
    void testListenerNullByDefault() {
        assertNull(checkbox.getListener());
    }

    @Test
    void testOnClick() {
        final boolean[] clicked = {false};
        checkbox.setListener((cb, isChecked) -> clicked[0] = true);

        checkbox.onClick();
        assertTrue(clicked[0]);
        assertTrue(checkbox.isChecked());
    }

    @Test
    void testRender() {
        // 渲染不应抛出异常
        assertDoesNotThrow(() -> checkbox.render());
    }

    @Test
    void testRenderHidden() {
        checkbox.setVisibility(UIVisibility.HIDDEN);
        assertDoesNotThrow(() -> checkbox.render());
    }

    @Test
    void testSetVisibility() {
        checkbox.setVisibility(UIVisibility.HIDDEN);
        assertFalse(checkbox.isVisible());

        checkbox.setVisibility(UIVisibility.VISIBLE);
        assertTrue(checkbox.isVisible());
    }

    @Test
    void testSetBounds() {
        checkbox.setBounds(10, 20, 150, 30);

        assertEquals(10, checkbox.getX());
        assertEquals(20, checkbox.getY());
        assertEquals(150, checkbox.getWidth());
        assertEquals(30, checkbox.getHeight());
    }

    @Test
    void testSetBackgroundColor() {
        UIColor bgColor = UIColor.GRAY;
        checkbox.setBackgroundColor(bgColor);
        assertEquals(bgColor, checkbox.getBackgroundColor());
    }

    @Test
    void testSetEnabled() {
        checkbox.setEnabled(false);
        assertFalse(checkbox.isEnabled());

        checkbox.setEnabled(true);
        assertTrue(checkbox.isEnabled());
    }

    @Test
    void testSetInteractable() {
        checkbox.setInteractable(false);
        assertFalse(checkbox.isInteractable());
    }

    @Test
    void testCheckState_UNCHECKED() {
        checkbox.setChecked(false);
        assertEquals(UICheckbox.CheckState.UNCHECKED, checkbox.getState());
    }

    @Test
    void testCheckState_CHECKED() {
        checkbox.setChecked(true);
        assertEquals(UICheckbox.CheckState.CHECKED, checkbox.getState());
    }

    @Test
    void testCheckState_INDETERMINATE() {
        checkbox.setState(UICheckbox.CheckState.INDETERMINATE);
        assertEquals(UICheckbox.CheckState.INDETERMINATE, checkbox.getState());
    }
}
