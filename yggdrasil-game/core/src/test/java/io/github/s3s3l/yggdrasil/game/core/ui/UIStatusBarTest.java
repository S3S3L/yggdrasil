package io.github.s3s3l.yggdrasil.game.core.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 状态条组件单元测试
 */
class UIStatusBarTest {

    private UIStatusBar statusBar;
    private UIStatusBarLayer hpLayer;
    private UIStatusBarLayer mpLayer;

    @BeforeEach
    void setUp() {
        hpLayer = new UIStatusBarLayer("HP", UIColor.RED, 100, 100);
        mpLayer = new UIStatusBarLayer("MP", UIColor.BLUE, 50, 50);
        statusBar = new UIStatusBar(Arrays.asList(hpLayer, mpLayer));
        statusBar.setId(1L);
    }

    @Test
    void testStatusBarCreation() {
        assertNotNull(statusBar);
        assertEquals(2, statusBar.getLayerCount());
        assertTrue(statusBar.isShowText());
        assertTrue(statusBar.isShowBorder());
        assertEquals(UIStatusBar.TextPosition.CENTER, statusBar.getTextPosition());
    }

    @Test
    void testDefaultConstructor() {
        UIStatusBar sb = new UIStatusBar();
        assertNotNull(sb);
        assertEquals(0, sb.getLayerCount());
    }

    @Test
    void testNullLayers() {
        UIStatusBar sb = new UIStatusBar(null);
        assertNotNull(sb);
        assertEquals(0, sb.getLayerCount());
    }

    @Test
    void testGetLayers() {
        List<UIStatusBarLayer> layers = statusBar.getLayers();
        assertEquals(2, layers.size());
    }

    @Test
    void testSetLayers() {
        UIStatusBarLayer newLayer = new UIStatusBarLayer("XP", UIColor.GREEN, 0, 100);
        statusBar.setLayers(Arrays.asList(newLayer));
        assertEquals(1, statusBar.getLayerCount());
    }

    @Test
    void testAddLayer() {
        UIStatusBarLayer newLayer = new UIStatusBarLayer("XP", UIColor.GREEN, 0, 100);
        statusBar.addLayer(newLayer);
        assertEquals(3, statusBar.getLayerCount());
    }

    @Test
    void testAddLayerAt() {
        UIStatusBarLayer newLayer = new UIStatusBarLayer("XP", UIColor.GREEN, 0, 100);
        statusBar.addLayer(1, newLayer);
        assertEquals(3, statusBar.getLayerCount());
        assertEquals(newLayer, statusBar.getLayer(1));
    }

    @Test
    void testRemoveLayer() {
        statusBar.removeLayer(hpLayer);
        assertEquals(1, statusBar.getLayerCount());
    }

    @Test
    void testRemoveLayerById() {
        statusBar.removeLayer("HP");
        assertEquals(1, statusBar.getLayerCount());
        assertNull(statusBar.getLayer("HP"));
    }

    @Test
    void testClearLayers() {
        statusBar.clearLayers();
        assertEquals(0, statusBar.getLayerCount());
    }

    @Test
    void testGetLayer() {
        assertEquals(hpLayer, statusBar.getLayer("HP"));
        assertNull(statusBar.getLayer("NonExistent"));
    }

    @Test
    void testGetLayerByIndex() {
        assertEquals(hpLayer, statusBar.getLayer(0));
        assertNull(statusBar.getLayer(-1));
        assertNull(statusBar.getLayer(10));
    }

    @Test
    void testAddLayerWithParams() {
        UIStatusBarLayer layer = statusBar.addLayer("XP", UIColor.GREEN, 0, 100);
        assertNotNull(layer);
        assertEquals("XP", layer.getId());
        assertEquals(3, statusBar.getLayerCount());
    }

    @Test
    void testSetLayerValue() {
        statusBar.setLayerValue("HP", 50);
        assertEquals(50, statusBar.getLayer("HP").getValue());
    }

    @Test
    void testSetLayerValueForNonExistentLayer() {
        statusBar.setLayerValue("NonExistent", 50);
        // 不抛异常
    }

    @Test
    void testSetLayerPercent() {
        statusBar.setLayerPercent("HP", 0.5f);
        assertEquals(50, statusBar.getLayer("HP").getValue());
    }

    @Test
    void testAddLayerValue() {
        statusBar.setLayerValue("HP", 90); // 先设置为 90
        statusBar.addLayerValue("HP", 20);
        assertEquals(100, statusBar.getLayer("HP").getValue()); // 钳制到 100
    }

    @Test
    void testSubtractLayerValue() {
        statusBar.addLayerValue("HP", -20);
        assertEquals(80, statusBar.getLayer("HP").getValue());
    }

    @Test
    void testSetDisplayText() {
        statusBar.setDisplayText("100/100");
        assertEquals("100/100", statusBar.getDisplayText());
    }

    @Test
    void testSetNullDisplayText() {
        statusBar.setDisplayText(null);
        assertEquals("", statusBar.getDisplayText());
    }

    @Test
    void testSetShowText() {
        statusBar.setShowText(false);
        assertFalse(statusBar.isShowText());
    }

    @Test
    void testSetBackgroundColor() {
        UIColor color = new UIColor(0.2f, 0.2f, 0.2f, 1f); // DARK_GRAY
        statusBar.setBackgroundColor(color);
        assertEquals(color, statusBar.getBackgroundColor());
    }

    @Test
    void testSetBorderColor() {
        UIColor color = UIColor.WHITE;
        statusBar.setBorderColor(color);
        assertEquals(color, statusBar.getBorderColor());
    }

    @Test
    void testSetTextColor() {
        UIColor color = UIColor.WHITE;
        statusBar.setTextColor(color);
        assertEquals(color, statusBar.getTextColor());
    }

    @Test
    void testSetBorderWidth() {
        statusBar.setBorderWidth(2);
        assertEquals(2, statusBar.getBorderWidth(), 0.01f);
    }

    @Test
    void testSetCornerRadius() {
        statusBar.setCornerRadius(5);
        assertEquals(5, statusBar.getCornerRadius(), 0.01f);
    }

    @Test
    void testSetTextPosition() {
        statusBar.setTextPosition(UIStatusBar.TextPosition.RIGHT);
        assertEquals(UIStatusBar.TextPosition.RIGHT, statusBar.getTextPosition());
    }

    @Test
    void testSetShowBorder() {
        statusBar.setShowBorder(false);
        assertFalse(statusBar.isShowBorder());
    }

    @Test
    void testAddThresholdWarning() {
        statusBar.addThresholdWarning("HP", 0.3f, UIColor.RED);
        assertEquals(1, statusBar.getThresholdWarnings().size());
    }

    @Test
    void testRemoveThresholdWarning() {
        statusBar.addThresholdWarning("HP", 0.3f, UIColor.RED);
        statusBar.removeThresholdWarning("HP", 0.3f);
        assertEquals(0, statusBar.getThresholdWarnings().size());
    }

    @Test
    void testSetWarningsEnabled() {
        statusBar.setWarningsEnabled(false);
        assertFalse(statusBar.isWarningsEnabled());
    }

    @Test
    void testSetThresholdWarnings() {
        UIStatusBar.ThresholdWarning warning = new UIStatusBar.ThresholdWarning("HP", 0.3f, UIColor.RED);
        statusBar.setThresholdWarnings(Arrays.asList(warning));
        assertEquals(1, statusBar.getThresholdWarnings().size());
    }

    @Test
    void testListener() {
        final float[] oldValue = {-1};
        final float[] newValue = {-1};

        statusBar.setListener(new UIStatusBar.StatusBarListener() {
            @Override
            public void onLayerValueChanged(UIStatusBar sb, UIStatusBarLayer layer, float oldVal, float newVal) {
                oldValue[0] = oldVal;
                newValue[0] = newVal;
            }

            @Override
            public void onThresholdWarning(UIStatusBar sb, UIStatusBarLayer layer, float threshold) {}
        });

        statusBar.setLayerValue("HP", 50);
        assertEquals(100, oldValue[0], 0.01f);
        assertEquals(50, newValue[0], 0.01f);
    }

    @Test
    void testGetComputedDisplayText() {
        assertEquals("100 / 100", statusBar.getComputedDisplayText());
    }

    @Test
    void testGetComputedDisplayTextWithCustomText() {
        statusBar.setDisplayText("Custom");
        assertEquals("Custom", statusBar.getComputedDisplayText());
    }

    @Test
    void testGetTotalProgress() {
        hpLayer.setValue(100);
        mpLayer.setValue(25);
        float total = statusBar.getTotalProgress();
        assertEquals(0.75f, total, 0.01f); // (1.0 + 0.5) / 2
    }

    @Test
    void testHasWarning() {
        statusBar.setLayerValue("HP", 20); // 20%
        statusBar.addThresholdWarning("HP", 0.3f, UIColor.RED);
        assertTrue(statusBar.hasWarning());
    }

    @Test
    void testHasNoWarning() {
        statusBar.setLayerValue("HP", 50); // 50%
        statusBar.addThresholdWarning("HP", 0.3f, UIColor.RED);
        assertFalse(statusBar.hasWarning());
    }

    @Test
    void testHasWarningDisabled() {
        statusBar.setWarningsEnabled(false);
        statusBar.setLayerValue("HP", 20);
        statusBar.addThresholdWarning("HP", 0.3f, UIColor.RED);
        assertFalse(statusBar.hasWarning());
    }

    @Test
    void testUpdate() {
        hpLayer.setAnimated(true);
        hpLayer.setValue(50);
        statusBar.update(0.016f); // 60fps
        // 动画平滑过渡
        assertNotEquals(50, hpLayer.getSmoothedValue()); // 还未到达目标值
    }

    @Test
    void testRender() {
        assertDoesNotThrow(() -> statusBar.render());
    }

    @Test
    void testRenderHidden() {
        statusBar.setVisibility(UIVisibility.HIDDEN);
        assertDoesNotThrow(() -> statusBar.render());
    }

    @Test
    void testLayerSortByRenderOrder() {
        UIStatusBarLayer lowPriority = new UIStatusBarLayer("Low", UIColor.GRAY, 0, 100);
        lowPriority.setRenderOrder(0);
        UIStatusBarLayer highPriority = new UIStatusBarLayer("High", UIColor.WHITE, 0, 100);
        highPriority.setRenderOrder(10);

        UIStatusBar sb = new UIStatusBar(Arrays.asList(lowPriority, highPriority));
        // 渲染顺序应该保持（已排序）
        assertEquals(lowPriority, sb.getLayer(0));
        assertEquals(highPriority, sb.getLayer(1));
    }

    @Test
    void testLayerProgressPercent() {
        hpLayer.setValue(75);
        assertEquals(0.75f, hpLayer.getProgressPercent(), 0.01f);
    }

    @Test
    void testLayerSetValue() {
        hpLayer.setValue(50);
        assertEquals(50, hpLayer.getValue());
    }

    @Test
    void testLayerSetValueClamped() {
        hpLayer.setValue(150);
        assertEquals(100, hpLayer.getValue()); // 钳制到最大值
    }

    @Test
    void testLayerSetProgressPercent() {
        hpLayer.setProgressPercent(0.25f);
        assertEquals(25, hpLayer.getValue());
    }

    @Test
    void testLayerAddValue() {
        hpLayer.setValue(90); // 先设置为 90
        hpLayer.addValue(20);
        assertEquals(100, hpLayer.getValue()); // 钳制到 100
    }

    @Test
    void testLayerSubtractValue() {
        hpLayer.subtractValue(20);
        assertEquals(80, hpLayer.getValue());
    }

    @Test
    void testLayerIsEmpty() {
        hpLayer.setValue(0);
        assertTrue(hpLayer.isEmpty());
    }

    @Test
    void testLayerIsFull() {
        hpLayer.setValue(100);
        assertTrue(hpLayer.isFull());
    }

    @Test
    void testLayerIsBelowThreshold() {
        hpLayer.setValue(20);
        assertTrue(hpLayer.isBelowThreshold(0.3f));
    }

    @Test
    void testLayerAnimated() {
        hpLayer.setAnimated(true);
        assertTrue(hpLayer.isAnimated());
    }

    @Test
    void testLayerAnimationSpeed() {
        hpLayer.setAnimationSpeed(10f);
        assertEquals(10f, hpLayer.getAnimationSpeed(), 0.01f);
    }

    @Test
    void testLayerVisible() {
        hpLayer.setVisible(false);
        assertFalse(hpLayer.isVisible());
    }

    @Test
    void testLayerRenderOrder() {
        hpLayer.setRenderOrder(5);
        assertEquals(5, hpLayer.getRenderOrder());
    }
}
