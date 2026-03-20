package io.github.s3s3l.yggdrasil.game.core.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 滑动条组件单元测试
 */
class UISliderTest {

    private UISlider slider;

    @BeforeEach
    void setUp() {
        slider = new UISlider(50, 0, 100);
        slider.setId(1L);
    }

    @Test
    void testSliderCreation() {
        assertNotNull(slider);
        assertEquals(50, slider.getValue());
        assertEquals(0, slider.getMinValue());
        assertEquals(100, slider.getMaxValue());
        assertEquals(0, slider.getStepSize());
        assertTrue(slider.isHorizontal());
        assertFalse(slider.isDragging());
    }

    @Test
    void testDefaultConstructor() {
        UISlider s = new UISlider();
        assertNotNull(s);
        assertEquals(0, s.getValue());
        assertEquals(0, s.getMinValue());
        assertEquals(100, s.getMaxValue());
    }

    @Test
    void testSetValue() {
        slider.setValue(75);
        assertEquals(75, slider.getValue());
    }

    @Test
    void testSetValueClamped() {
        slider.setValue(150);
        assertEquals(100, slider.getValue());

        slider.setValue(-10);
        assertEquals(0, slider.getValue());
    }

    @Test
    void testSetStepSize() {
        slider.setStepSize(5);
        slider.setValue(73);
        // 应该舍入到最接近 5 的倍数
        assertEquals(75, slider.getValue());
    }

    @Test
    void testSetMinValue() {
        slider.setMinValue(20);
        assertEquals(20, slider.getMinValue());
        assertEquals(50, slider.getValue());

        slider.setMinValue(60);
        assertEquals(60, slider.getMinValue());
        assertEquals(60, slider.getValue()); // 值被钳制到新的最小值
    }

    @Test
    void testSetMaxValue() {
        slider.setMaxValue(80);
        assertEquals(80, slider.getMaxValue());
        assertEquals(50, slider.getValue());

        slider.setMaxValue(40);
        assertEquals(40, slider.getMaxValue());
        assertEquals(40, slider.getValue()); // 值被钳制到新的最大值
    }

    @Test
    void testSetHorizontal() {
        slider.setHorizontal(false);
        assertFalse(slider.isHorizontal());
    }

    @Test
    void testSetFillColor() {
        UIColor color = UIColor.RED;
        slider.setFillColor(color);
        assertEquals(color, slider.getFillColor());
    }

    @Test
    void testSetBackgroundColor() {
        UIColor color = UIColor.GRAY;
        slider.setBackgroundColor(color);
        assertEquals(color, slider.getBackgroundColor());
    }

    @Test
    void testSetKnobColor() {
        UIColor color = UIColor.YELLOW;
        slider.setKnobColor(color);
        assertEquals(color, slider.getKnobColor());
    }

    @Test
    void testSetKnobSize() {
        slider.setKnobSize(30);
        assertEquals(30, slider.getKnobSize());
    }

    @Test
    void testSetShowLabels() {
        slider.setShowLabels(true);
        assertTrue(slider.isShowLabels());
    }

    @Test
    void testGetProgressPercent() {
        slider.setValue(50);
        assertEquals(0.5f, slider.getProgressPercent(), 0.01f);

        slider.setValue(0);
        assertEquals(0, slider.getProgressPercent(), 0.01f);

        slider.setValue(100);
        assertEquals(1, slider.getProgressPercent(), 0.01f);
    }

    @Test
    void testSetProgressPercent() {
        slider.setProgressPercent(0.25f);
        assertEquals(25, slider.getValue());

        slider.setProgressPercent(0.75f);
        assertEquals(75, slider.getValue());
    }

    @Test
    void testSetProgressPercentClamped() {
        slider.setProgressPercent(-0.5f);
        assertEquals(0, slider.getValue());

        slider.setProgressPercent(1.5f);
        assertEquals(100, slider.getValue());
    }

    @Test
    void testSliderListener() {
        final float[] capturedValue = {-1};
        slider.setListener(new UISlider.SliderListener() {
            @Override
            public void onValueChanged(UISlider slider, float newValue) {
                capturedValue[0] = newValue;
            }

            @Override
            public void onDragStart(UISlider slider) {}

            @Override
            public void onDragEnd(UISlider slider, float newValue) {}
        });

        slider.setValue(75);
        assertEquals(75, capturedValue[0], 0.01f);
    }

    @Test
    void testDragStart() {
        final boolean[] dragStarted = {false};
        slider.setListener(new UISlider.SliderListener() {
            @Override
            public void onValueChanged(UISlider slider, float newValue) {}

            @Override
            public void onDragStart(UISlider slider) {
                dragStarted[0] = true;
            }

            @Override
            public void onDragEnd(UISlider slider, float newValue) {}
        });

        slider.startDrag(50, 0);
        assertTrue(dragStarted[0]);
        assertTrue(slider.isDragging());
    }

    @Test
    void testDragEnd() {
        final boolean[] dragEnded = {false};
        slider.setListener(new UISlider.SliderListener() {
            @Override
            public void onValueChanged(UISlider slider, float newValue) {}

            @Override
            public void onDragStart(UISlider slider) {}

            @Override
            public void onDragEnd(UISlider slider, float newValue) {
                dragEnded[0] = true;
            }
        });

        slider.startDrag(50, 0);
        slider.endDrag(75, 0);
        assertTrue(dragEnded[0]);
        assertFalse(slider.isDragging());
    }

    @Test
    void testDrag() {
        slider.setBounds(0, 0, 100, 20);
        slider.startDrag(50, 0);
        slider.drag(75, 0);

        assertTrue(slider.isDragging());
        assertEquals(75, slider.getValue(), 1);
    }

    @Test
    void testRender() {
        assertDoesNotThrow(() -> slider.render());
    }

    @Test
    void testRenderHidden() {
        slider.setVisibility(UIVisibility.HIDDEN);
        assertDoesNotThrow(() -> slider.render());
    }

    @Test
    void testGetKnobPosition() {
        slider.setBounds(0, 0, 100, 20);
        slider.setKnobSize(10);
        slider.setValue(50);

        float knobPos = slider.getKnobPosition();
        assertTrue(knobPos >= 0 && knobPos <= 90);
    }

    @Test
    void testSetVertical() {
        slider.setHorizontal(false);
        slider.setBounds(0, 0, 20, 100);
        slider.setValue(50);

        float knobPos = slider.getKnobPosition();
        assertTrue(knobPos >= 0 && knobPos <= 90);
    }
}
