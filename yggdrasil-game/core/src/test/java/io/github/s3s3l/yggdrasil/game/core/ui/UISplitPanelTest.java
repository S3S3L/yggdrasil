package io.github.s3s3l.yggdrasil.game.core.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 分割面板组件单元测试
 */
class UISplitPanelTest {

    private UISplitPanel panel;

    @BeforeEach
    void setUp() {
        panel = new UISplitPanel();
        panel.setId(1L);
        panel.setSize(400, 300);
    }

    @Test
    void testSplitPanelCreation() {
        assertNotNull(panel);
        assertEquals(UISplitPanel.SplitOrientation.VERTICAL, panel.getOrientation());
        assertEquals(0.5f, panel.getSplitPosition(), 0.01f);
        assertTrue(panel.isUseProportional());
        assertTrue(panel.isDraggable());
    }

    @Test
    void testConstructorWithOrientation() {
        UISplitPanel p = new UISplitPanel(UISplitPanel.SplitOrientation.HORIZONTAL);
        assertEquals(UISplitPanel.SplitOrientation.HORIZONTAL, p.getOrientation());
    }

    @Test
    void testSetOrientation() {
        panel.setOrientation(UISplitPanel.SplitOrientation.HORIZONTAL);
        assertEquals(UISplitPanel.SplitOrientation.HORIZONTAL, panel.getOrientation());
    }

    @Test
    void testSetFirstComponent() {
        UIComponent component = new UIPanel("First");
        panel.setFirstComponent(component);

        assertSame(component, panel.getFirstComponent());
        assertSame(panel, component.getParent());
    }

    @Test
    void testSetSecondComponent() {
        UIComponent component = new UIPanel("Second");
        panel.setSecondComponent(component);

        assertSame(component, panel.getSecondComponent());
        assertSame(panel, component.getParent());
    }

    @Test
    void testSetSplitPosition() {
        panel.setSplitPosition(0.3f);
        assertEquals(0.3f, panel.getSplitPosition(), 0.01f);
    }

    @Test
    void testSetSplitPositionWithListener() {
        final float[] newPosition = {-1};
        panel.setListener(new UISplitPanel.SplitPanelListener() {
            @Override
            public void onSplitPositionChanged(UISplitPanel panel, float pos) {
                newPosition[0] = pos;
            }

            @Override
            public void onDragStarted(UISplitPanel panel) {}

            @Override
            public void onDragEnded(UISplitPanel panel) {}
        });

        panel.setSplitPosition(0.7f);

        assertEquals(0.7f, newPosition[0], 0.01f);
    }

    @Test
    void testSetUseProportional() {
        panel.setUseProportional(false);
        assertFalse(panel.isUseProportional());
    }

    @Test
    void testSetMinFirstSize() {
        panel.setMinFirstSize(100);
        assertEquals(100, panel.getMinFirstSize(), 0.01f);
    }

    @Test
    void testSetMinSecondSize() {
        panel.setMinSecondSize(100);
        assertEquals(100, panel.getMinSecondSize(), 0.01f);
    }

    @Test
    void testSetSplitterWidth() {
        panel.setSplitterWidth(8);
        assertEquals(8, panel.getSplitterWidth(), 0.01f);
    }

    @Test
    void testSetSplitterColor() {
        UIColor color = UIColor.GRAY;
        panel.setSplitterColor(color);
        assertEquals(color, panel.getSplitterColor());
    }

    @Test
    void testSetDraggable() {
        panel.setDraggable(false);
        assertFalse(panel.isDraggable());
    }

    @Test
    void testGetSplitterPosition_Vertical() {
        panel.setOrientation(UISplitPanel.SplitOrientation.VERTICAL);
        panel.setSplitPosition(0.5f);

        float pos = panel.getSplitterPosition();
        assertEquals(200, pos, 0.01f); // 400 * 0.5
    }

    @Test
    void testGetSplitterPosition_Horizontal() {
        panel.setOrientation(UISplitPanel.SplitOrientation.HORIZONTAL);
        panel.setSplitPosition(0.5f);

        float pos = panel.getSplitterPosition();
        assertEquals(150, pos, 0.01f); // 300 * 0.5
    }

    @Test
    void testSetSplitterPosition_Vertical() {
        panel.setOrientation(UISplitPanel.SplitOrientation.VERTICAL);
        panel.setSplitterPosition(100);

        assertTrue(panel.getSplitPosition() > 0);
        assertTrue(panel.getSplitPosition() < 1);
    }

    @Test
    void testGetFirstComponentBounds_Vertical() {
        panel.setFirstComponent(new UIPanel("First"));
        panel.setOrientation(UISplitPanel.SplitOrientation.VERTICAL);
        panel.setSplitPosition(0.5f);

        float[] bounds = panel.getFirstComponentBounds();
        assertNotNull(bounds);
        assertEquals(4, bounds.length);
        assertEquals(200, bounds[2], 0.01f); // width
    }

    @Test
    void testGetSecondComponentBounds_Vertical() {
        panel.setSecondComponent(new UIPanel("Second"));
        panel.setOrientation(UISplitPanel.SplitOrientation.VERTICAL);
        panel.setSplitPosition(0.5f);

        float[] bounds = panel.getSecondComponentBounds();
        assertNotNull(bounds);
        assertEquals(panel.getX() + 200 + 4, bounds[0], 0.01f); // x = splitter + width
    }

    @Test
    void testGetSplitterBounds() {
        panel.setOrientation(UISplitPanel.SplitOrientation.VERTICAL);
        panel.setSplitPosition(0.5f);

        float[] bounds = panel.getSplitterBounds();
        assertNotNull(bounds);
        assertEquals(4, bounds[2], 0.01f); // splitter width
        assertEquals(300, bounds[3], 0.01f); // splitter height
    }

    @Test
    void testIsClickOnSplitter() {
        panel.setOrientation(UISplitPanel.SplitOrientation.VERTICAL);
        panel.setSplitPosition(0.5f);

        float[] bounds = panel.getSplitterBounds();
        assertTrue(panel.isClickOnSplitter(bounds[0] + 2, bounds[1] + 100));
        assertFalse(panel.isClickOnSplitter(panel.getX(), panel.getY()));
    }

    @Test
    void testSwapComponents() {
        UIComponent first = new UIPanel("First");
        UIComponent second = new UIPanel("Second");
        panel.setFirstComponent(first);
        panel.setSecondComponent(second);

        panel.swapComponents();

        assertSame(second, panel.getFirstComponent());
        assertSame(first, panel.getSecondComponent());
    }

    @Test
    void testResetSplitPosition() {
        panel.setSplitPosition(0.3f);
        panel.resetSplitPosition();

        assertEquals(0.5f, panel.getSplitPosition(), 0.01f);
    }

    @Test
    void testUpdate() {
        UIComponent first = new UIPanel("First");
        UIComponent second = new UIPanel("Second");
        panel.setFirstComponent(first);
        panel.setSecondComponent(second);

        panel.update(0.016f);

        // 子组件应该被更新位置和大小
        assertTrue(first.getWidth() > 0);
        assertTrue(second.getWidth() > 0);
    }

    @Test
    void testRender() {
        panel.setFirstComponent(new UIPanel("First"));
        panel.setSecondComponent(new UIPanel("Second"));
        assertDoesNotThrow(() -> panel.render());
    }

    @Test
    void testRenderHidden() {
        panel.setVisibility(UIVisibility.HIDDEN);
        assertDoesNotThrow(() -> panel.render());
    }

    @Test
    void testGetPreferredWidth_Vertical() {
        panel.setOrientation(UISplitPanel.SplitOrientation.VERTICAL);
        panel.setFirstComponent(new UIPanel("First"));
        panel.setSecondComponent(new UIPanel("Second"));

        float width = panel.getPreferredWidth();
        assertTrue(width > 0);
    }

    @Test
    void testGetPreferredHeight_Vertical() {
        panel.setOrientation(UISplitPanel.SplitOrientation.VERTICAL);
        panel.setFirstComponent(new UIPanel("First"));
        panel.setSecondComponent(new UIPanel("Second"));
        panel.getFirstComponent().setSize(200, 100);
        panel.getSecondComponent().setSize(200, 150);

        float height = panel.getPreferredHeight();
        assertEquals(150, height, 0.01f); // max(100, 150)
    }

    @Test
    void testIsDragging_Default() {
        assertFalse(panel.isDragging());
    }

    @Test
    void testHandleMouseDown_OnSplitter() {
        panel.setDraggable(true);
        panel.setOrientation(UISplitPanel.SplitOrientation.VERTICAL);
        panel.setSplitPosition(0.5f);

        float[] bounds = panel.getSplitterBounds();
        panel.handleMouseDown(bounds[0] + 2, bounds[1] + 100);

        assertTrue(panel.isDragging());
    }

    @Test
    void testHandleMouseUp() {
        panel.setDragging(true);
        panel.handleMouseUp(0, 0);

        assertFalse(panel.isDragging());
    }

    @Test
    void testHandleMouseMove_Dragging() {
        panel.setOrientation(UISplitPanel.SplitOrientation.VERTICAL);
        panel.setDragging(true);

        panel.handleMouseMove(150, 150);

        // 分割位置应该被更新
        assertTrue(panel.getSplitPosition() > 0);
    }

    @Test
    void testSetSplitPositionClamped() {
        panel.setMinFirstSize(100);
        panel.setMinSecondSize(100);
        panel.setOrientation(UISplitPanel.SplitOrientation.VERTICAL);

        // 尝试设置过小的位置
        panel.setSplitterPosition(10);

        // 应该被限制在最小值
        assertTrue(panel.getSplitterPosition() >= 100);
    }

    @Test
    void testGetFirstComponentBounds_Null() {
        float[] bounds = panel.getFirstComponentBounds();
        assertNotNull(bounds);
        assertEquals(4, bounds.length);
    }

    @Test
    void testGetSecondComponentBounds_Null() {
        float[] bounds = panel.getSecondComponentBounds();
        assertNotNull(bounds);
        assertEquals(4, bounds.length);
    }
}
