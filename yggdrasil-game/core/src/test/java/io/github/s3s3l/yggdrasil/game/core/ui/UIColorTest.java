package io.github.s3s3l.yggdrasil.game.core.ui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 颜色单元测试
 */
class UIColorTest {

    @Test
    void testColorCreation() {
        UIColor color = new UIColor(1, 0, 0, 1);

        assertEquals(1, color.getR());
        assertEquals(0, color.getG());
        assertEquals(0, color.getB());
        assertEquals(1, color.getA());
    }

    @Test
    void testDefaultConstructor() {
        UIColor color = new UIColor();

        assertEquals(1, color.getR());
        assertEquals(1, color.getG());
        assertEquals(1, color.getB());
        assertEquals(1, color.getA());
    }

    @Test
    void testRGBConstructor() {
        UIColor color = new UIColor(1, 0, 0);

        assertEquals(1, color.getR());
        assertEquals(0, color.getG());
        assertEquals(0, color.getB());
        assertEquals(1, color.getA());
    }

    @Test
    void testCopyConstructor() {
        UIColor original = new UIColor(0.5f, 0.5f, 0.5f, 0.8f);
        UIColor copy = new UIColor(original);

        assertEquals(original.getR(), copy.getR());
        assertEquals(original.getG(), copy.getG());
        assertEquals(original.getB(), copy.getB());
        assertEquals(original.getA(), copy.getA());
    }

    @Test
    void testFromRGB() {
        UIColor color = UIColor.fromRGB(255, 128, 64);

        assertEquals(1, color.getR());
        assertEquals(128 / 255f, color.getG(), 0.001f);
        assertEquals(64 / 255f, color.getB(), 0.001f);
    }

    @Test
    void testFromRGBA() {
        UIColor color = UIColor.fromRGBA(255, 128, 64, 128);

        assertEquals(1, color.getR());
        assertEquals(128 / 255f, color.getG(), 0.001f);
        assertEquals(64 / 255f, color.getB(), 0.001f);
        assertEquals(128 / 255f, color.getA(), 0.001f);
    }

    @Test
    void testFromHex() {
        UIColor color = UIColor.fromHex("#FF8040");

        assertEquals(1, color.getR());
        assertEquals(128 / 255f, color.getG(), 0.001f);
        assertEquals(64 / 255f, color.getB(), 0.001f);
    }

    @Test
    void testFromHexWithAlpha() {
        UIColor color = UIColor.fromHex("#FF804080");

        assertEquals(1, color.getR());
        assertEquals(128 / 255f, color.getG(), 0.001f);
        assertEquals(64 / 255f, color.getB(), 0.001f);
        assertEquals(128 / 255f, color.getA(), 0.001f);
    }

    @Test
    void testFromHexWithoutHash() {
        UIColor color = UIColor.fromHex("FF8040");

        assertEquals(1, color.getR());
        assertEquals(128 / 255f, color.getG(), 0.001f);
        assertEquals(64 / 255f, color.getB(), 0.001f);
    }

    @Test
    void testToHex() {
        UIColor color = UIColor.fromRGBA(255, 128, 64, 128);
        String hex = color.toHex();

        assertEquals("#FF804080", hex);
    }

    @Test
    void testSet() {
        UIColor color = new UIColor();
        color.set(0.5f, 0.5f, 0.5f, 0.5f);

        assertEquals(0.5f, color.getR());
        assertEquals(0.5f, color.getG());
        assertEquals(0.5f, color.getB());
        assertEquals(0.5f, color.getA());
    }

    @Test
    void testSetFromColor() {
        UIColor color1 = new UIColor(0.5f, 0.5f, 0.5f, 0.5f);
        UIColor color2 = new UIColor();
        color2.set(color1);

        assertEquals(color1.getR(), color2.getR());
        assertEquals(color1.getG(), color2.getG());
        assertEquals(color1.getB(), color2.getB());
        assertEquals(color1.getA(), color2.getA());
    }

    @Test
    void testLerp() {
        UIColor color1 = UIColor.BLACK;
        UIColor color2 = UIColor.WHITE;

        UIColor result = color1.lerp(color2, 0.5f);

        assertEquals(0.5f, result.getR(), 0.001f);
        assertEquals(0.5f, result.getG(), 0.001f);
        assertEquals(0.5f, result.getB(), 0.001f);
        assertEquals(1, result.getA());
    }

    @Test
    void testLighten() {
        UIColor color = new UIColor(0.5f, 0.5f, 0.5f, 1);
        UIColor lightened = color.lighten(0.3f);

        assertEquals(0.8f, lightened.getR(), 0.001f);
        assertEquals(0.8f, lightened.getG(), 0.001f);
        assertEquals(0.8f, lightened.getB(), 0.001f);
    }

    @Test
    void testDarken() {
        UIColor color = new UIColor(0.5f, 0.5f, 0.5f, 1);
        UIColor darkened = color.darken(0.3f);

        assertEquals(0.2f, darkened.getR(), 0.001f);
        assertEquals(0.2f, darkened.getG(), 0.001f);
        assertEquals(0.2f, darkened.getB(), 0.001f);
    }

    @Test
    void testLightenClamp() {
        UIColor color = new UIColor(0.9f, 0.9f, 0.9f, 1);
        UIColor lightened = color.lighten(0.3f);

        assertEquals(1, lightened.getR());
        assertEquals(1, lightened.getG());
        assertEquals(1, lightened.getB());
    }

    @Test
    void testDarkenClamp() {
        UIColor color = new UIColor(0.1f, 0.1f, 0.1f, 1);
        UIColor darkened = color.darken(0.3f);

        assertEquals(0, darkened.getR());
        assertEquals(0, darkened.getG());
        assertEquals(0, darkened.getB());
    }

    @Test
    void testEquals() {
        UIColor color1 = new UIColor(0.5f, 0.5f, 0.5f, 1);
        UIColor color2 = new UIColor(0.5f, 0.5f, 0.5f, 1);
        UIColor color3 = new UIColor(0.5f, 0.5f, 0.5f, 0.5f);

        assertEquals(color1, color2);
        assertNotEquals(color1, color3);
    }

    @Test
    void testPredefinedColors() {
        assertNotNull(UIColor.WHITE);
        assertNotNull(UIColor.BLACK);
        assertNotNull(UIColor.RED);
        assertNotNull(UIColor.GREEN);
        assertNotNull(UIColor.BLUE);
        assertNotNull(UIColor.YELLOW);
        assertNotNull(UIColor.CYAN);
        assertNotNull(UIColor.MAGENTA);
        assertNotNull(UIColor.GRAY);
        assertNotNull(UIColor.TRANSPARENT);
    }
}
