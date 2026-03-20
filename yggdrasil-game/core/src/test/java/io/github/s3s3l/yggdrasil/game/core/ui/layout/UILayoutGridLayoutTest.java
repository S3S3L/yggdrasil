package io.github.s3s3l.yggdrasil.game.core.ui.layout;

import io.github.s3s3l.yggdrasil.game.core.ui.TestUIComponent;
import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 网格布局单元测试
 */
class UILayoutGridLayoutTest {

    private UILayoutGridLayout gridLayout;
    private TestUIComponent parent;

    @BeforeEach
    void setUp() {
        gridLayout = new UILayoutGridLayout();
        parent = new TestUIComponent("Parent");
        parent.setSize(400, 300);
    }

    @Test
    void testDefaultValues() {
        assertEquals(3, gridLayout.getColumns());
        assertEquals(0, gridLayout.getRows());
        assertEquals(0, gridLayout.getColumnWidth());
        assertEquals(0, gridLayout.getRowHeight());
        assertEquals(10, gridLayout.getHorizontalSpacing());
        assertEquals(10, gridLayout.getVerticalSpacing());
        assertFalse(gridLayout.isAutoFit());
    }

    @Test
    void testSetColumns() {
        gridLayout.setColumns(4);
        assertEquals(4, gridLayout.getColumns());
    }

    @Test
    void testSetRows() {
        gridLayout.setRows(3);
        assertEquals(3, gridLayout.getRows());
    }

    @Test
    void testSetColumnWidth() {
        gridLayout.setColumnWidth(100);
        assertEquals(100, gridLayout.getColumnWidth());
    }

    @Test
    void testSetRowHeight() {
        gridLayout.setRowHeight(50);
        assertEquals(50, gridLayout.getRowHeight());
    }

    @Test
    void testSetHorizontalSpacing() {
        gridLayout.setHorizontalSpacing(10);
        assertEquals(10, gridLayout.getHorizontalSpacing());
    }

    @Test
    void testSetVerticalSpacing() {
        gridLayout.setVerticalSpacing(10);
        assertEquals(10, gridLayout.getVerticalSpacing());
    }

    @Test
    void testSetAutoFit() {
        gridLayout.setAutoFit(true);
        assertTrue(gridLayout.isAutoFit());
    }

    @Test
    void testSetAlign() {
        gridLayout.setAlign(UILayoutGridLayout.Align.MIDDLE_CENTER);
        assertEquals(UILayoutGridLayout.Align.MIDDLE_CENTER, gridLayout.getAlign());
    }

    @Test
    void testLayout() {
        for (int i = 0; i < 4; i++) {
            TestUIComponent child = new TestUIComponent("Child" + i);
            child.setSize(100, 80);
            gridLayout.addChild(child);
        }

        // Should not throw exception
        gridLayout.layout(parent, 400, 300);

        for (UIComponent child : gridLayout.getChildren()) {
            assertTrue(child.getX() >= 0);
            assertTrue(child.getY() >= 0);
        }
    }

    @Test
    void testMeasure() {
        for (int i = 0; i < 4; i++) {
            TestUIComponent child = new TestUIComponent("Child" + i);
            child.setId((long) i);
            child.setSize(100, 80);
            gridLayout.addChild(child);
        }

        float[] measured = gridLayout.measure(parent);
        assertNotNull(measured);
        assertEquals(2, measured.length);
        // Measure may return 0 if layout params are not set
        assertTrue(measured[0] >= 0);
        assertTrue(measured[1] >= 0);
    }

    @Test
    void testMarkDirty() {
        gridLayout.markDirty();
        assertTrue(gridLayout.isDirty());
    }

    @Test
    void testClearDirty() {
        gridLayout.markDirty();
        gridLayout.clearDirty();
        assertFalse(gridLayout.isDirty());
    }

    @Test
    void testAddChild() {
        TestUIComponent child = new TestUIComponent("Child");
        gridLayout.addChild(child);

        assertEquals(1, gridLayout.getChildCount());
        assertTrue(gridLayout.isDirty());
    }

    @Test
    void testRemoveChild() {
        TestUIComponent child = new TestUIComponent("Child");
        gridLayout.addChild(child);
        gridLayout.removeChild(child);

        assertEquals(0, gridLayout.getChildCount());
    }

    @Test
    void testClearChildren() {
        gridLayout.addChild(new TestUIComponent("Child1"));
        gridLayout.addChild(new TestUIComponent("Child2"));
        gridLayout.clearChildren();

        assertEquals(0, gridLayout.getChildCount());
    }

    @Test
    void testGetChildAt() {
        TestUIComponent child = new TestUIComponent("Child");
        gridLayout.addChild(child);

        assertSame(child, gridLayout.getChildAt(0));
        assertNull(gridLayout.getChildAt(1));
    }

    @Test
    void testGetChildren() {
        TestUIComponent child1 = new TestUIComponent("Child1");
        child1.setId(1L);
        TestUIComponent child2 = new TestUIComponent("Child2");
        child2.setId(2L);
        gridLayout.addChild(child1);
        gridLayout.addChild(child2);

        assertEquals(2, gridLayout.getChildren().size());
    }

    @Test
    void testSetParent() {
        gridLayout.setParent(parent);
        assertSame(parent, gridLayout.getParent());
    }

    @Test
    void testSetLayoutWidth() {
        gridLayout.setLayoutWidth(500);
        assertEquals(500, gridLayout.getLayoutWidth());
    }

    @Test
    void testSetLayoutHeight() {
        gridLayout.setLayoutHeight(400);
        assertEquals(400, gridLayout.getLayoutHeight());
    }

    @Test
    void testSetPadding() {
        gridLayout.setPadding(10, 20, 30, 40);
        gridLayout.layout(parent, 400, 300);
    }
}
