package io.github.s3s3l.yggdrasil.game.core.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 数字选择器组件单元测试
 */
class UINumberPickerTest {

    private UINumberPicker picker;

    @BeforeEach
    void setUp() {
        picker = new UINumberPicker(50, 0, 100, 1);
        picker.setId(1L);
    }

    @Test
    void testNumberPickerCreation() {
        assertNotNull(picker);
        assertEquals(50, picker.getValue());
        assertEquals(0, picker.getMinValue());
        assertEquals(100, picker.getMaxValue());
        assertEquals(1, picker.getStepSize());
        assertEquals(0, picker.getDecimalPlaces());
        assertTrue(picker.isShowButtons());
        assertTrue(picker.isAllowDirectInput());
    }

    @Test
    void testDefaultConstructor() {
        UINumberPicker p = new UINumberPicker();
        assertNotNull(p);
        assertEquals(0, p.getValue());
        assertEquals(0, p.getMinValue());
        assertEquals(100, p.getMaxValue());
        assertEquals(1, p.getStepSize());
    }

    @Test
    void testSetValue() {
        picker.setValue(75);
        assertEquals(75, picker.getValue());
    }

    @Test
    void testSetValueClamped() {
        picker.setValue(150);
        assertEquals(100, picker.getValue());

        picker.setValue(-10);
        assertEquals(0, picker.getValue());
    }

    @Test
    void testSetValueWithStep() {
        picker.setStepSize(5);
        picker.setValue(73);
        assertEquals(75, picker.getValue()); // 舍入到最接近 5 的倍数
    }

    @Test
    void testSetMinValue() {
        picker.setMinValue(20);
        assertEquals(20, picker.getMinValue());
        assertEquals(50, picker.getValue());

        picker.setMinValue(60);
        assertEquals(60, picker.getMinValue());
        assertEquals(60, picker.getValue());
    }

    @Test
    void testSetMaxValue() {
        picker.setMaxValue(80);
        assertEquals(80, picker.getMaxValue());

        picker.setMaxValue(40);
        assertEquals(40, picker.getMaxValue());
        assertEquals(40, picker.getValue());
    }

    @Test
    void testSetStepSize() {
        picker.setStepSize(0.1);
        assertEquals(0.1, picker.getStepSize());
        assertEquals(1, picker.getDecimalPlaces()); // 自动计算小数位数
    }

    @Test
    void testSetDecimalPlaces() {
        picker.setDecimalPlaces(2);
        assertEquals(2, picker.getDecimalPlaces());
    }

    @Test
    void testSetSuffix() {
        picker.setSuffix("%");
        assertEquals("%", picker.getSuffix());
    }

    @Test
    void testSetNullSuffix() {
        picker.setSuffix(null);
        assertEquals("", picker.getSuffix());
    }

    @Test
    void testSetPrefix() {
        picker.setPrefix("$");
        assertEquals("$", picker.getPrefix());
    }

    @Test
    void testSetTextColor() {
        UIColor color = UIColor.RED;
        picker.setTextColor(color);
        assertEquals(color, picker.getTextColor());
    }

    @Test
    void testSetButtonColor() {
        UIColor color = UIColor.BLUE;
        picker.setButtonColor(color);
        assertEquals(color, picker.getButtonColor());
    }

    @Test
    void testSetBackgroundColor() {
        UIColor color = new UIColor(0.2f, 0.2f, 0.2f, 1f); // DARK_GRAY
        picker.setBackgroundColor(color);
        assertEquals(color, picker.getBackgroundColor());
    }

    @Test
    void testSetFontSize() {
        picker.setFontSize(20);
        assertEquals(20, picker.getFontSize());
    }

    @Test
    void testSetShowButtons() {
        picker.setShowButtons(false);
        assertFalse(picker.isShowButtons());
    }

    @Test
    void testSetAllowDirectInput() {
        picker.setAllowDirectInput(false);
        assertFalse(picker.isAllowDirectInput());
    }

    @Test
    void testStepUp() {
        picker.stepUp();
        assertEquals(51, picker.getValue());
    }

    @Test
    void testStepDown() {
        picker.stepDown();
        assertEquals(49, picker.getValue());
    }

    @Test
    void testStepUpClamped() {
        picker.setValue(100);
        picker.stepUp();
        assertEquals(100, picker.getValue());
    }

    @Test
    void testStepDownClamped() {
        picker.setValue(0);
        picker.stepDown();
        assertEquals(0, picker.getValue());
    }

    @Test
    void testGetDisplayText() {
        picker.setValue(50);
        assertEquals("50", picker.getDisplayText());
    }

    @Test
    void testGetDisplayTextWithPrefixAndSuffix() {
        picker.setPrefix("$");
        picker.setSuffix("kg");
        picker.setValue(50);
        assertEquals("$50 kg", picker.getDisplayText());
    }

    @Test
    void testGetDisplayTextWithDecimals() {
        picker.setStepSize(0.01); // 设置小步长以支持小数
        picker.setDecimalPlaces(2);
        picker.setValue(50.5);
        assertEquals("50.50", picker.getDisplayText());
    }

    @Test
    void testGetProgressPercent() {
        picker.setValue(50);
        assertEquals(0.5f, picker.getProgressPercent(), 0.01f);
    }

    @Test
    void testListener() {
        final double[] oldValue = {-1};
        final double[] newValue = {-1};

        picker.setListener(new UINumberPicker.NumberChangeListener() {
            @Override
            public void onValueChanged(UINumberPicker p, double oldVal, double newVal) {
                oldValue[0] = oldVal;
                newValue[0] = newVal;
            }

            @Override
            public void onDragStart(UINumberPicker p) {}

            @Override
            public void onDragEnd(UINumberPicker p, double value) {}
        });

        picker.setValue(75);
        assertEquals(50, oldValue[0], 0.01);
        assertEquals(75, newValue[0], 0.01);
    }

    @Test
    void testDragStart() {
        final boolean[] dragStarted = {false};
        picker.setListener(new UINumberPicker.NumberChangeListener() {
            @Override
            public void onValueChanged(UINumberPicker p, double oldVal, double newVal) {}

            @Override
            public void onDragStart(UINumberPicker p) {
                dragStarted[0] = true;
            }

            @Override
            public void onDragEnd(UINumberPicker p, double value) {}
        });

        picker.startDrag(0, 0);
        assertTrue(dragStarted[0]);
        assertTrue(picker.isDragging());
    }

    @Test
    void testDragEnd() {
        final boolean[] dragEnded = {false};
        picker.setListener(new UINumberPicker.NumberChangeListener() {
            @Override
            public void onValueChanged(UINumberPicker p, double oldVal, double newVal) {}

            @Override
            public void onDragStart(UINumberPicker p) {}

            @Override
            public void onDragEnd(UINumberPicker p, double value) {
                dragEnded[0] = true;
            }
        });

        picker.startDrag(0, 0);
        picker.endDrag(0, 0);
        assertTrue(dragEnded[0]);
        assertFalse(picker.isDragging());
    }

    @Test
    void testDrag() {
        picker.startDrag(0, 0);
        picker.drag(0, 0, 1); // 向上拖动
        assertEquals(49, picker.getValue()); // 向上拖动应该增加
    }

    @Test
    void testHandleInput() {
        picker.setAllowDirectInput(true);
        picker.handleInput("75");
        assertEquals(75, picker.getValue());
    }

    @Test
    void testHandleInputNotAllowed() {
        picker.setAllowDirectInput(false);
        picker.handleInput("75");
        assertEquals(50, picker.getValue()); // 不变
    }

    @Test
    void testHandleInvalidInput() {
        picker.handleInput("invalid");
        assertEquals(50, picker.getValue()); // 不变
    }

    @Test
    void testOnPlusClick() {
        picker.onPlusClick();
        assertEquals(51, picker.getValue());
    }

    @Test
    void testOnMinusClick() {
        picker.onMinusClick();
        assertEquals(49, picker.getValue());
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
}
