package io.github.s3s3l.yggdrasil.game.gdx.ui.renderer;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.ui.UIColor;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.graphics.Color;

/**
 * GdxUIRenderer 颜色转换测试
 */
class GdxUIRendererTest {

    @Test
    void testColorConversion() {
        UIColor uiColor = new UIColor(1, 0, 0, 1);
        Color gdxColor = GdxUIRenderer.getColor(uiColor);

        assertEquals(1, gdxColor.r, 0.001f);
        assertEquals(0, gdxColor.g, 0.001f);
        assertEquals(0, gdxColor.b, 0.001f);
        assertEquals(1, gdxColor.a, 0.001f);
    }

    @Test
    void testColorConversionWithAlpha() {
        UIColor uiColor = UIColor.fromRGBA(255, 128, 64, 128);
        Color gdxColor = GdxUIRenderer.getColor(uiColor);

        assertEquals(1, gdxColor.r, 0.001f);
        assertEquals(128 / 255f, gdxColor.g, 0.001f);
        assertEquals(64 / 255f, gdxColor.b, 0.001f);
        assertEquals(128 / 255f, gdxColor.a, 0.001f);
    }

    @Test
    void testColorConversionNull() {
        Color gdxColor = GdxUIRenderer.getColor(null);
        assertEquals(Color.WHITE, gdxColor);
    }

    @Test
    void testColorConversionWhite() {
        UIColor uiColor = UIColor.WHITE;
        Color gdxColor = GdxUIRenderer.getColor(uiColor);

        assertEquals(1, gdxColor.r, 0.001f);
        assertEquals(1, gdxColor.g, 0.001f);
        assertEquals(1, gdxColor.b, 0.001f);
        assertEquals(1, gdxColor.a, 0.001f);
    }

    @Test
    void testColorConversionBlack() {
        UIColor uiColor = UIColor.BLACK;
        Color gdxColor = GdxUIRenderer.getColor(uiColor);

        assertEquals(0, gdxColor.r, 0.001f);
        assertEquals(0, gdxColor.g, 0.001f);
        assertEquals(0, gdxColor.b, 0.001f);
        assertEquals(1, gdxColor.a, 0.001f);
    }

    @Test
    void testColorConversionTransparent() {
        UIColor uiColor = UIColor.TRANSPARENT;
        Color gdxColor = GdxUIRenderer.getColor(uiColor);

        assertEquals(0, gdxColor.r, 0.001f);
        assertEquals(0, gdxColor.g, 0.001f);
        assertEquals(0, gdxColor.b, 0.001f);
        assertEquals(0, gdxColor.a, 0.001f);
    }

    @Test
    void testColorConversionGray() {
        UIColor uiColor = UIColor.GRAY;
        Color gdxColor = GdxUIRenderer.getColor(uiColor);

        assertEquals(0.5f, gdxColor.r, 0.001f);
        assertEquals(0.5f, gdxColor.g, 0.001f);
        assertEquals(0.5f, gdxColor.b, 0.001f);
        assertEquals(1, gdxColor.a, 0.001f);
    }
}
