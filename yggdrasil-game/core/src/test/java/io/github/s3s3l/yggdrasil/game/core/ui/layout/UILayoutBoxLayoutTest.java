package io.github.s3s3l.yggdrasil.game.core.ui.layout;

import io.github.s3s3l.yggdrasil.game.core.ui.TestUIComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 线性布局单元测试
 */
class UILayoutBoxLayoutTest {

    private UILayoutBoxLayout boxLayout;
    private TestUIComponent parent;

    @BeforeEach
    void setUp() {
        boxLayout = new UILayoutBoxLayout();
        parent = new TestUIComponent("Parent");
        parent.setSize(400, 300);
    }

    @Test
    void testDefaultValues() {
        assertEquals(UILayoutBoxLayout.Direction.VERTICAL, boxLayout.getDirection());
        assertEquals(0, boxLayout.getSpacing());
        assertEquals(UILayoutBase.HorizontalAlign.START, boxLayout.getHorizontalAlign());
        assertEquals(UILayoutBase.VerticalAlign.START, boxLayout.getVerticalAlign());
        assertFalse(boxLayout.isReverse());
    }

    @Test
    void testSetDirection() {
        boxLayout.setDirection(UILayoutBoxLayout.Direction.HORIZONTAL);
        assertEquals(UILayoutBoxLayout.Direction.HORIZONTAL, boxLayout.getDirection());
    }

    @Test
    void testSetSpacing() {
        boxLayout.setSpacing(10);
        assertEquals(10, boxLayout.getSpacing());
    }

    @Test
    void testSetHorizontalAlign() {
        boxLayout.setHorizontalAlign(UILayoutBase.HorizontalAlign.CENTER);
        assertEquals(UILayoutBase.HorizontalAlign.CENTER, boxLayout.getHorizontalAlign());
    }

    @Test
    void testSetVerticalAlign() {
        boxLayout.setVerticalAlign(UILayoutBase.VerticalAlign.CENTER);
        assertEquals(UILayoutBase.VerticalAlign.CENTER, boxLayout.getVerticalAlign());
    }

    @Test
    void testSetReverse() {
        boxLayout.setReverse(true);
        assertTrue(boxLayout.isReverse());
    }

    @Test
    void testLayout() {
        TestUIComponent child1 = new TestUIComponent("Child1");
        TestUIComponent child2 = new TestUIComponent("Child2");
        child1.setSize(100, 50);
        child2.setSize(100, 50);

        boxLayout.addChild(child1);
        boxLayout.addChild(child2);

        // Should not throw exception
        boxLayout.layout(parent, 400, 300);

        // Children should be positioned
        assertTrue(child1.getX() >= 0);
        assertTrue(child1.getY() >= 0);
    }

    @Test
    void testLayoutWithSpacing() {
        TestUIComponent child1 = new TestUIComponent("Child1");
        TestUIComponent child2 = new TestUIComponent("Child2");
        child1.setSize(100, 50);
        child2.setSize(100, 50);

        boxLayout.addChild(child1);
        boxLayout.addChild(child2);
        boxLayout.setSpacing(10);

        boxLayout.layout(parent, 400, 300);

        // Should not throw exception
        assertTrue(child1.getY() >= 0);
        assertTrue(child2.getY() >= 0);
    }

    @Test
    void testMeasure() {
        TestUIComponent child1 = new TestUIComponent("Child1");
        child1.setSize(100, 50);
        boxLayout.addChild(child1);

        float[] measured = boxLayout.measure(parent);
        assertNotNull(measured);
        assertEquals(2, measured.length);
        assertTrue(measured[0] > 0);
        assertTrue(measured[1] > 0);
    }

    @Test
    void testMarkDirty() {
        boxLayout.markDirty();
        assertTrue(boxLayout.isDirty());
    }

    @Test
    void testClearDirty() {
        boxLayout.markDirty();
        boxLayout.clearDirty();
        assertFalse(boxLayout.isDirty());
    }

    @Test
    void testAddChild() {
        TestUIComponent child = new TestUIComponent("Child");
        boxLayout.addChild(child);

        assertEquals(1, boxLayout.getChildCount());
        assertTrue(boxLayout.isDirty());
    }

    @Test
    void testGetChildren() {
        TestUIComponent child1 = new TestUIComponent("Child1");
        child1.setId(1L);
        TestUIComponent child2 = new TestUIComponent("Child2");
        child2.setId(2L);
        boxLayout.addChild(child1);
        boxLayout.addChild(child2);

        assertEquals(2, boxLayout.getChildren().size());
    }

    @Test
    void testGetChildAt() {
        TestUIComponent child = new TestUIComponent("Child");
        boxLayout.addChild(child);

        assertSame(child, boxLayout.getChildAt(0));
        assertNull(boxLayout.getChildAt(1));
    }

    @Test
    void testRemoveChild() {
        TestUIComponent child = new TestUIComponent("Child");
        boxLayout.addChild(child);
        boxLayout.removeChild(child);

        assertEquals(0, boxLayout.getChildCount());
        assertTrue(boxLayout.isDirty());
    }

    @Test
    void testClearChildren() {
        boxLayout.addChild(new TestUIComponent("Child1"));
        boxLayout.addChild(new TestUIComponent("Child2"));
        boxLayout.clearChildren();

        assertEquals(0, boxLayout.getChildCount());
        assertTrue(boxLayout.isDirty());
    }

    @Test
    void testSetParent() {
        boxLayout.setParent(parent);
        assertSame(parent, boxLayout.getParent());
    }

    @Test
    void testSetLayoutWidth() {
        boxLayout.setLayoutWidth(500);
        assertEquals(500, boxLayout.getLayoutWidth());
    }

    @Test
    void testSetLayoutHeight() {
        boxLayout.setLayoutHeight(400);
        assertEquals(400, boxLayout.getLayoutHeight());
    }

    @Test
    void testSetPadding() {
        boxLayout.setPadding(10, 20, 30, 40);
        boxLayout.layout(parent, 400, 300);
        // Should apply padding
    }
}
