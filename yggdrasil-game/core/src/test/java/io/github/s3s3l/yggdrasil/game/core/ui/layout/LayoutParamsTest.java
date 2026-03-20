package io.github.s3s3l.yggdrasil.game.core.ui.layout;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 布局参数单元测试
 */
class LayoutParamsTest {

    private LayoutParams layoutParams;

    @BeforeEach
    void setUp() {
        layoutParams = new LayoutParams();
    }

    @Test
    void testDefaultValues() {
        assertEquals(LayoutParams.WRAP_CONTENT, layoutParams.getWidth());
        assertEquals(LayoutParams.WRAP_CONTENT, layoutParams.getHeight());
        assertEquals(0, layoutParams.getMarginLeft());
        assertEquals(0, layoutParams.getMarginRight());
        assertEquals(0, layoutParams.getMarginTop());
        assertEquals(0, layoutParams.getMarginBottom());
        assertEquals(0, layoutParams.getPaddingLeft());
        assertEquals(0, layoutParams.getPaddingRight());
        assertEquals(0, layoutParams.getPaddingTop());
        assertEquals(0, layoutParams.getPaddingBottom());
    }

    @Test
    void testSetWidth() {
        layoutParams.setWidth(100);
        assertEquals(100, layoutParams.getWidth());
    }

    @Test
    void testSetHeight() {
        layoutParams.setHeight(200);
        assertEquals(200, layoutParams.getHeight());
    }

    @Test
    void testSetMargins() {
        layoutParams.setMargins(10, 20, 30, 40);
        assertEquals(10, layoutParams.getMarginLeft());
        assertEquals(20, layoutParams.getMarginTop());
        assertEquals(30, layoutParams.getMarginRight());
        assertEquals(40, layoutParams.getMarginBottom());
    }

    @Test
    void testSetMarginLeft() {
        layoutParams.setMarginLeft(15);
        assertEquals(15, layoutParams.getMarginLeft());
    }

    @Test
    void testSetMarginRight() {
        layoutParams.setMarginRight(25);
        assertEquals(25, layoutParams.getMarginRight());
    }

    @Test
    void testSetMarginTop() {
        layoutParams.setMarginTop(35);
        assertEquals(35, layoutParams.getMarginTop());
    }

    @Test
    void testSetMarginBottom() {
        layoutParams.setMarginBottom(45);
        assertEquals(45, layoutParams.getMarginBottom());
    }

    @Test
    void testSetPadding() {
        layoutParams.setPaddings(5, 10, 15, 20);
        assertEquals(5, layoutParams.getPaddingLeft());
        assertEquals(10, layoutParams.getPaddingTop());
        assertEquals(15, layoutParams.getPaddingRight());
        assertEquals(20, layoutParams.getPaddingBottom());
    }

    @Test
    void testSetPaddingLeft() {
        layoutParams.setPaddingLeft(8);
        assertEquals(8, layoutParams.getPaddingLeft());
    }

    @Test
    void testSetPaddingRight() {
        layoutParams.setPaddingRight(18);
        assertEquals(18, layoutParams.getPaddingRight());
    }

    @Test
    void testSetPaddingTop() {
        layoutParams.setPaddingTop(28);
        assertEquals(28, layoutParams.getPaddingTop());
    }

    @Test
    void testSetPaddingBottom() {
        layoutParams.setPaddingBottom(38);
        assertEquals(38, layoutParams.getPaddingBottom());
    }

    @Test
    void testMatchParentConstant() {
        layoutParams.setWidth(LayoutParams.MATCH_PARENT);
        layoutParams.setHeight(LayoutParams.MATCH_PARENT);
        assertEquals(-1, layoutParams.getWidth());
        assertEquals(-1, layoutParams.getHeight());
    }

    @Test
    void testWrapContentConstant() {
        layoutParams.setWidth(LayoutParams.WRAP_CONTENT);
        layoutParams.setHeight(LayoutParams.WRAP_CONTENT);
        assertEquals(-2, layoutParams.getWidth());
        assertEquals(-2, layoutParams.getHeight());
    }
}
