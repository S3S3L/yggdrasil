package io.github.s3s3l.yggdrasil.game.core.ui.layout;

import io.github.s3s3l.yggdrasil.game.core.ui.TestUIComponent;
import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 流式布局单元测试
 */
class UILayoutFlowLayoutTest {

    private UILayoutFlowLayout flowLayout;
    private TestUIComponent parent;

    @BeforeEach
    void setUp() {
        flowLayout = new UILayoutFlowLayout();
        parent = new TestUIComponent("Parent");
        parent.setSize(400, 300);
    }

    @Test
    void testDefaultValues() {
        assertEquals(UILayoutFlowLayout.FlowDirection.HORIZONTAL, flowLayout.getFlowDirection());
        assertEquals(5, flowLayout.getHorizontalSpacing());
        assertEquals(5, flowLayout.getVerticalSpacing());
    }

    @Test
    void testSetFlowDirection() {
        flowLayout.setFlowDirection(UILayoutFlowLayout.FlowDirection.VERTICAL);
        assertEquals(UILayoutFlowLayout.FlowDirection.VERTICAL, flowLayout.getFlowDirection());
    }

    @Test
    void testSetHorizontalSpacing() {
        flowLayout.setHorizontalSpacing(10);
        assertEquals(10, flowLayout.getHorizontalSpacing());
    }

    @Test
    void testSetVerticalSpacing() {
        flowLayout.setVerticalSpacing(10);
        assertEquals(10, flowLayout.getVerticalSpacing());
    }

    @Test
    void testSetHorizontalAlign() {
        flowLayout.setHorizontalAlign(UILayoutBase.HorizontalAlign.CENTER);
        assertEquals(UILayoutBase.HorizontalAlign.CENTER, flowLayout.getHorizontalAlign());
    }

    @Test
    void testSetVerticalAlign() {
        flowLayout.setVerticalAlign(UILayoutBase.VerticalAlign.CENTER);
        assertEquals(UILayoutBase.VerticalAlign.CENTER, flowLayout.getVerticalAlign());
    }

    @Test
    void testSetAutoFit() {
        flowLayout.setAutoFit(true);
        assertTrue(flowLayout.isAutoFit());
    }

    @Test
    void testLayout() {
        for (int i = 0; i < 5; i++) {
            TestUIComponent child = new TestUIComponent("Child" + i);
            child.setSize(80, 40);
            flowLayout.addChild(child);
        }

        // Should not throw exception
        flowLayout.layout(parent, 400, 300);

        for (UIComponent child : flowLayout.getChildren()) {
            assertTrue(child.getX() >= 0);
            assertTrue(child.getY() >= 0);
        }
    }

    @Test
    void testMeasure() {
        for (int i = 0; i < 4; i++) {
            TestUIComponent child = new TestUIComponent("Child" + i);
            child.setSize(80, 40);
            flowLayout.addChild(child);
        }

        float[] measured = flowLayout.measure(parent);
        assertNotNull(measured);
        assertEquals(2, measured.length);
    }

    @Test
    void testMarkDirty() {
        flowLayout.markDirty();
        assertTrue(flowLayout.isDirty());
    }

    @Test
    void testClearDirty() {
        flowLayout.markDirty();
        flowLayout.clearDirty();
        assertFalse(flowLayout.isDirty());
    }

    @Test
    void testAddChild() {
        TestUIComponent child = new TestUIComponent("Child");
        flowLayout.addChild(child);

        assertEquals(1, flowLayout.getChildCount());
    }

    @Test
    void testRemoveChild() {
        TestUIComponent child = new TestUIComponent("Child");
        flowLayout.addChild(child);
        flowLayout.removeChild(child);

        assertEquals(0, flowLayout.getChildCount());
    }

    @Test
    void testClearChildren() {
        flowLayout.addChild(new TestUIComponent("Child1"));
        flowLayout.addChild(new TestUIComponent("Child2"));
        flowLayout.clearChildren();

        assertEquals(0, flowLayout.getChildCount());
    }

    @Test
    void testGetChildAt() {
        TestUIComponent child = new TestUIComponent("Child");
        flowLayout.addChild(child);

        assertSame(child, flowLayout.getChildAt(0));
        assertNull(flowLayout.getChildAt(1));
    }

    @Test
    void testGetChildren() {
        TestUIComponent child1 = new TestUIComponent("Child1");
        child1.setId(1L);
        TestUIComponent child2 = new TestUIComponent("Child2");
        child2.setId(2L);
        flowLayout.addChild(child1);
        flowLayout.addChild(child2);

        assertEquals(2, flowLayout.getChildren().size());
    }

    @Test
    void testSetParent() {
        flowLayout.setParent(parent);
        assertSame(parent, flowLayout.getParent());
    }

    @Test
    void testSetLayoutWidth() {
        flowLayout.setLayoutWidth(500);
        assertEquals(500, flowLayout.getLayoutWidth());
    }

    @Test
    void testSetLayoutHeight() {
        flowLayout.setLayoutHeight(400);
        assertEquals(400, flowLayout.getLayoutHeight());
    }

    @Test
    void testSetPadding() {
        flowLayout.setPadding(10, 20, 30, 40);
        flowLayout.layout(parent, 400, 300);
    }
}
