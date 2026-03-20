package io.github.s3s3l.yggdrasil.game.core.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 按钮单元测试
 */
class UIButtonTest {

    private UIButton button;

    @BeforeEach
    void setUp() {
        button = new UIButton("Click Me");
        button.setId(1L);
    }

    @Test
    void testButtonCreation() {
        assertEquals("Click Me", button.getText());
        assertEquals(UIColor.WHITE, button.getTextColor());
        assertEquals(16, button.getFontSize());
        assertFalse(button.isHovered());
        assertFalse(button.isPressed());
    }

    @Test
    void testDefaultConstructor() {
        UIButton btn = new UIButton();

        assertNull(btn.getText());
        assertEquals(UIColor.WHITE, btn.getTextColor());
    }

    @Test
    void testSetText() {
        button.setText("New Text");
        assertEquals("New Text", button.getText());
    }

    @Test
    void testSetTextColor() {
        UIColor color = UIColor.RED;
        button.setTextColor(color);
        assertEquals(color, button.getTextColor());
    }

    @Test
    void testSetFontSize() {
        button.setFontSize(24);
        assertEquals(24, button.getFontSize());
    }

    @Test
    void testSetOnClickListener() {
        final boolean[] clicked = {false};
        button.setOnClickListener(() -> clicked[0] = true);

        button.onClick();
        assertTrue(clicked[0]);
    }

    @Test
    void testSetHovered() {
        button.setHovered(true);
        assertTrue(button.isHovered());
    }

    @Test
    void testSetPressed() {
        button.setPressed(true);
        assertTrue(button.isPressed());
    }

    @Test
    void testOnClickWithoutListener() {
        // 没有监听器时不应抛出异常
        assertDoesNotThrow(() -> button.onClick());
    }

    @Test
    void testRender() {
        // 渲染不应抛出异常
        assertDoesNotThrow(() -> button.render());
    }

    @Test
    void testRenderHidden() {
        button.setVisibility(UIVisibility.HIDDEN);

        // 隐藏的组件不应抛出异常
        assertDoesNotThrow(() -> button.render());
    }
}
