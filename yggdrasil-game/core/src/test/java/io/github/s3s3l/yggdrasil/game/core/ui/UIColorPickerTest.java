package io.github.s3s3l.yggdrasil.game.core.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 颜色选择器组件单元测试
 */
class UIColorPickerTest {

    private UIColorPicker picker;

    @BeforeEach
    void setUp() {
        picker = new UIColorPicker(UIColor.WHITE);
        picker.setId(1L);
    }

    @Test
    void testColorPickerCreation() {
        assertNotNull(picker);
        assertEquals(UIColor.WHITE, picker.getSelectedColor());
        assertEquals(UIColorPicker.ColorMode.RGB, picker.getMode());
        assertTrue(picker.isShowAlpha());
        assertTrue(picker.isShowColorPreview());
        assertFalse(picker.getPresetColors().isEmpty());
    }

    @Test
    void testDefaultConstructor() {
        UIColorPicker p = new UIColorPicker();
        assertNotNull(p);
        assertEquals(UIColor.WHITE, p.getSelectedColor());
    }

    @Test
    void testNullInitialColor() {
        UIColorPicker p = new UIColorPicker(null);
        assertNotNull(p);
        assertEquals(UIColor.WHITE, p.getSelectedColor());
    }

    @Test
    void testSetSelectedColor() {
        UIColor color = UIColor.RED;
        picker.setSelectedColor(color);
        assertEquals(color, picker.getSelectedColor());
    }

    @Test
    void testSetNullSelectedColor() {
        UIColor original = picker.getSelectedColor();
        picker.setSelectedColor(null);
        assertEquals(original, picker.getSelectedColor()); // 不变
    }

    @Test
    void testSetColorRGB() {
        picker.setColor(1f, 0f, 0f);
        assertEquals(1f, picker.getSelectedColor().getR(), 0.01f);
        assertEquals(0f, picker.getSelectedColor().getG(), 0.01f);
        assertEquals(0f, picker.getSelectedColor().getB(), 0.01f);
    }

    @Test
    void testSetColorRGBA() {
        picker.setColor(1f, 0f, 0f, 0.5f);
        assertEquals(1f, picker.getSelectedColor().getR(), 0.01f);
        assertEquals(0.5f, picker.getSelectedColor().getA(), 0.01f);
    }

    @Test
    void testSetColorClamped() {
        picker.setColor(1.5f, -0.5f, 0.5f);
        assertEquals(1f, picker.getSelectedColor().getR(), 0.01f);
        assertEquals(0f, picker.getSelectedColor().getG(), 0.01f);
        assertEquals(0.5f, picker.getSelectedColor().getB(), 0.01f);
    }

    @Test
    void testSetColorHex6Chars() {
        picker.setColor("FF0000");
        assertEquals(1f, picker.getSelectedColor().getR(), 0.01f);
        assertEquals(0f, picker.getSelectedColor().getG(), 0.01f);
        assertEquals(0f, picker.getSelectedColor().getB(), 0.01f);
    }

    @Test
    void testSetColorHexWithHash() {
        picker.setColor("#FF0000");
        assertEquals(1f, picker.getSelectedColor().getR(), 0.01f);
    }

    @Test
    void testSetColorHex8Chars() {
        picker.setColor("FF000080");
        assertEquals(1f, picker.getSelectedColor().getR(), 0.01f);
        assertEquals(0.5f, picker.getSelectedColor().getA(), 0.01f);
    }

    @Test
    void testSetColorNull() {
        UIColor original = picker.getSelectedColor();
        picker.setColor((String) null);
        assertEquals(original, picker.getSelectedColor());
    }

    @Test
    void testSetMode() {
        picker.setMode(UIColorPicker.ColorMode.HSV);
        assertEquals(UIColorPicker.ColorMode.HSV, picker.getMode());
    }

    @Test
    void testSetRGBMode() {
        picker.setMode(UIColorPicker.ColorMode.HSV);
        picker.setRGBMode();
        assertEquals(UIColorPicker.ColorMode.RGB, picker.getMode());
    }

    @Test
    void testSetHSVMode() {
        picker.setHSVMode();
        assertEquals(UIColorPicker.ColorMode.HSV, picker.getMode());
    }

    @Test
    void testSetHue() {
        picker.setHue(180);
        assertEquals(180, picker.getHue(), 0.01f);
    }

    @Test
    void testSetHueNormalized() {
        picker.setHue(400);
        assertEquals(40, picker.getHue(), 0.01f); // 400 % 360 = 40
    }

    @Test
    void testSetNegativeHue() {
        picker.setHue(-90);
        assertEquals(270, picker.getHue(), 0.01f); // -90 + 360 = 270
    }

    @Test
    void testSetSaturation() {
        picker.setSaturation(0.5f);
        assertEquals(0.5f, picker.getSaturation(), 0.01f);
    }

    @Test
    void testSetSaturationClamped() {
        picker.setSaturation(1.5f);
        assertEquals(1f, picker.getSaturation(), 0.01f);
    }

    @Test
    void testSetValue() {
        picker.setValue(0.8f);
        assertEquals(0.8f, picker.getValue(), 0.01f);
    }

    @Test
    void testSetValueClamped() {
        picker.setValue(-0.5f);
        assertEquals(0f, picker.getValue(), 0.01f);
    }

    @Test
    void testSetAlpha() {
        picker.setAlpha(0.5f);
        assertEquals(0.5f, picker.getAlpha(), 0.01f);
    }

    @Test
    void testSetAlphaClamped() {
        picker.setAlpha(1.5f);
        assertEquals(1f, picker.getAlpha(), 0.01f);
    }

    @Test
    void testAddPresetColor() {
        int initialCount = picker.getPresetColors().size();
        picker.addPresetColor(new UIColor(1f, 0.5f, 0f)); // ORANGE
        assertEquals(initialCount + 1, picker.getPresetColors().size());
    }

    @Test
    void testAddDuplicatePresetColor() {
        int initialCount = picker.getPresetColors().size();
        picker.addPresetColor(UIColor.RED); // 已存在
        assertEquals(initialCount, picker.getPresetColors().size());
    }

    @Test
    void testRemovePresetColor() {
        picker.addPresetColor(new UIColor(1f, 0.5f, 0f)); // ORANGE
        picker.removePresetColor(new UIColor(1f, 0.5f, 0f));
        assertFalse(picker.getPresetColors().contains(new UIColor(1f, 0.5f, 0f)));
    }

    @Test
    void testClearPresetColors() {
        picker.clearPresetColors();
        assertTrue(picker.getPresetColors().isEmpty());
    }

    @Test
    void testSetPresetColors() {
        picker.setPresetColors(java.util.Arrays.asList(UIColor.RED, UIColor.BLUE));
        assertEquals(2, picker.getPresetColors().size());
    }

    @Test
    void testSetNullPresetColors() {
        picker.setPresetColors(null);
        assertNotNull(picker.getPresetColors());
    }

    @Test
    void testSelectPresetColor() {
        picker.selectPresetColor(0);
        assertEquals(UIColor.RED, picker.getSelectedColor());
    }

    @Test
    void testSelectInvalidPresetColor() {
        UIColor original = picker.getSelectedColor();
        picker.selectPresetColor(-1);
        assertEquals(original, picker.getSelectedColor());
    }

    @Test
    void testSetShowAlpha() {
        picker.setShowAlpha(false);
        assertFalse(picker.isShowAlpha());
    }

    @Test
    void testSetShowColorPreview() {
        picker.setShowColorPreview(false);
        assertFalse(picker.isShowColorPreview());
    }

    @Test
    void testSetSliderColor() {
        UIColor color = UIColor.GRAY;
        picker.setSliderColor(color);
        assertEquals(color, picker.getSliderColor());
    }

    @Test
    void testListener() {
        final UIColor[] oldColor = {null};
        final UIColor[] newColor = {null};

        picker.setListener((p, oldC, newC) -> {
            oldColor[0] = oldC;
            newColor[0] = newC;
        });

        picker.setColor(UIColor.RED);
        assertNotNull(oldColor[0]);
        assertEquals(UIColor.RED, newColor[0]);
    }

    @Test
    void testGetHexColor() {
        picker.setColor(UIColor.RED);
        assertEquals("#FF0000", picker.getHexColor());
    }

    @Test
    void testGetHexColorWithAlpha() {
        picker.setColor(1f, 0f, 0f, 0.5f);
        String hex = picker.getHexColorWithAlpha();
        assertTrue(hex.startsWith("#"));
        assertEquals(9, hex.length()); // #RRGGBBAA
    }

    @Test
    void testRgbToHsvConversion() {
        picker.setColor(UIColor.RED);
        float hue = picker.getHue();
        float saturation = picker.getSaturation();
        float value = picker.getValue();

        assertEquals(0, hue, 1); // 红色色相为 0
        assertEquals(1f, saturation, 0.01f);
        assertEquals(1f, value, 0.01f);
    }

    @Test
    void testHsvToRgbConversion() {
        picker.setMode(UIColorPicker.ColorMode.HSV);
        picker.setHue(0);
        picker.setSaturation(1);
        picker.setValue(1);

        UIColor result = picker.getSelectedColor();
        assertEquals(1f, result.getR(), 0.01f);
        assertEquals(0f, result.getG(), 0.01f);
        assertEquals(0f, result.getB(), 0.01f);
    }

    @Test
    void testRender() {
        assertDoesNotThrow(() -> picker.render());
    }

    @Test
    void testRenderHidden() {
        picker.setVisibility(UIVisibility.HIDDEN);
        assertDoesNotThrow(() -> picker.render());
    }

    @Test
    void testOnHueSliderChanged() {
        picker.onHueSliderChanged(0.5f);
        assertEquals(180, picker.getHue(), 1);
    }

    @Test
    void testOnSaturationSliderChanged() {
        picker.onSaturationSliderChanged(0.5f);
        assertEquals(0.5f, picker.getSaturation(), 0.01f);
    }

    @Test
    void testOnValueSliderChanged() {
        picker.onValueSliderChanged(0.5f);
        assertEquals(0.5f, picker.getValue(), 0.01f);
    }

    @Test
    void testOnRedSliderChanged() {
        picker.onRedSliderChanged(0.5f);
        assertEquals(0.5f, picker.getSelectedColor().getR(), 0.01f);
    }

    @Test
    void testOnAlphaSliderChanged() {
        picker.onAlphaSliderChanged(0.5f);
        assertEquals(0.5f, picker.getAlpha(), 0.01f);
    }
}
