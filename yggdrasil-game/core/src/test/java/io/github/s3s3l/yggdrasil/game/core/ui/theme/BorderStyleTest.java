package io.github.s3s3l.yggdrasil.game.core.ui.theme;

import io.github.s3s3l.yggdrasil.game.core.ui.UIColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 边框样式单元测试
 */
class BorderStyleTest {

    @Test
    void testCreateDefault() {
        BorderStyle border = new BorderStyle(1f, UIColor.BLACK, 0f);
        assertEquals(1f, border.getWidth());
        assertEquals(UIColor.BLACK, border.getColor());
        assertEquals(0f, border.getRadius());
    }

    @Test
    void testCreateWithRadius() {
        BorderStyle border = new BorderStyle(2f, UIColor.RED, 5f);
        assertEquals(2f, border.getWidth());
        assertEquals(UIColor.RED, border.getColor());
        assertEquals(5f, border.getRadius());
    }

    @Test
    void testHasBorder() {
        BorderStyle border = new BorderStyle(1f, UIColor.BLACK, 0f);
        assertTrue(border.hasBorder());

        border.setWidth(0);
        assertFalse(border.hasBorder());
    }

    @Test
    void testHasRoundedCorners() {
        BorderStyle border = new BorderStyle(1f, UIColor.BLACK, 5f);
        assertTrue(border.hasRoundedCorners());

        border.setRadius(0);
        assertFalse(border.hasRoundedCorners());
    }

    @Test
    void testCopy() {
        BorderStyle original = new BorderStyle(2f, UIColor.BLUE, 8f);
        BorderStyle copy = original.copy();

        assertEquals(original.getWidth(), copy.getWidth());
        assertEquals(original.getColor(), copy.getColor());
        assertEquals(original.getRadius(), copy.getRadius());
        assertNotSame(original, copy);
    }

    @Test
    void testEquals() {
        BorderStyle a = new BorderStyle(2f, UIColor.GREEN, 4f);
        BorderStyle b = new BorderStyle(2f, UIColor.GREEN, 4f);
        assertEquals(a, b);
    }

    @Test
    void testHashCode() {
        BorderStyle border = new BorderStyle(2f, UIColor.GREEN, 4f);
        BorderStyle same = new BorderStyle(2f, UIColor.GREEN, 4f);
        assertEquals(border.hashCode(), same.hashCode());
    }

    @Test
    void testSetters() {
        BorderStyle border = new BorderStyle(1f, UIColor.BLACK, 0f);
        border.setWidth(5f);
        border.setColor(UIColor.WHITE);
        border.setRadius(10f);

        assertEquals(5f, border.getWidth());
        assertEquals(UIColor.WHITE, border.getColor());
        assertEquals(10f, border.getRadius());
    }

    @Test
    void testToString() {
        BorderStyle border = new BorderStyle(2f, UIColor.RED, 5f);
        String str = border.toString();
        assertTrue(str.contains("BorderStyle"));
        assertTrue(str.contains("2.0"));
        assertTrue(str.contains("5.0"));
    }
}
