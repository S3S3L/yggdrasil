package io.github.s3s3l.yggdrasil.game.core.ui.layout;

import io.github.s3s3l.yggdrasil.game.core.ui.TestUIComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 弹性布局单元测试
 */
class UILayoutFlexboxTest {

    private UILayoutFlexbox flexbox;
    private TestUIComponent parent;

    @BeforeEach
    void setUp() {
        flexbox = new UILayoutFlexbox();
        parent = new TestUIComponent("Parent");
        parent.setSize(400, 300);
    }

    @Test
    void testDefaultValues() {
        assertEquals(UILayoutFlexbox.FlexDirection.ROW, flexbox.getFlexDirection());
        assertEquals(UILayoutFlexbox.FlexWrap.WRAP, flexbox.getFlexWrap());
        assertEquals(UILayoutFlexbox.JustifyContent.FLEX_START, flexbox.getJustifyContent());
        assertEquals(UILayoutFlexbox.AlignItems.STRETCH, flexbox.getAlignItems());
        assertEquals(UILayoutFlexbox.AlignContent.STRETCH, flexbox.getAlignContent());
        assertEquals(0, flexbox.getGap());
    }

    @Test
    void testSetFlexDirection() {
        flexbox.setFlexDirection(UILayoutFlexbox.FlexDirection.COLUMN);
        assertEquals(UILayoutFlexbox.FlexDirection.COLUMN, flexbox.getFlexDirection());
    }

    @Test
    void testSetFlexWrap() {
        flexbox.setFlexWrap(UILayoutFlexbox.FlexWrap.NOWRAP);
        assertEquals(UILayoutFlexbox.FlexWrap.NOWRAP, flexbox.getFlexWrap());
    }

    @Test
    void testSetJustifyContent() {
        flexbox.setJustifyContent(UILayoutFlexbox.JustifyContent.CENTER);
        assertEquals(UILayoutFlexbox.JustifyContent.CENTER, flexbox.getJustifyContent());
    }

    @Test
    void testSetAlignItems() {
        flexbox.setAlignItems(UILayoutFlexbox.AlignItems.CENTER);
        assertEquals(UILayoutFlexbox.AlignItems.CENTER, flexbox.getAlignItems());
    }

    @Test
    void testSetAlignContent() {
        flexbox.setAlignContent(UILayoutFlexbox.AlignContent.SPACE_BETWEEN);
        assertEquals(UILayoutFlexbox.AlignContent.SPACE_BETWEEN, flexbox.getAlignContent());
    }

    @Test
    void testSetGap() {
        flexbox.setGap(10);
        assertEquals(10, flexbox.getGap());
    }

    @Test
    void testLayout() {
        TestUIComponent child1 = new TestUIComponent("Child1");
        TestUIComponent child2 = new TestUIComponent("Child2");
        child1.setSize(100, 50);
        child2.setSize(100, 50);

        flexbox.addChild(child1);
        flexbox.addChild(child2);

        // Should not throw exception
        flexbox.layout(parent, 400, 300);

        assertTrue(child1.getX() >= 0);
        assertTrue(child1.getY() >= 0);
    }

    @Test
    void testMarkDirty() {
        flexbox.markDirty();
        assertTrue(flexbox.isDirty());
    }

    @Test
    void testClearDirty() {
        flexbox.markDirty();
        flexbox.clearDirty();
        assertFalse(flexbox.isDirty());
    }

    @Test
    void testAddChild() {
        TestUIComponent child = new TestUIComponent("Child");
        flexbox.addChild(child);

        assertEquals(1, flexbox.getChildCount());
        assertTrue(flexbox.isDirty());
    }

    @Test
    void testRemoveChild() {
        TestUIComponent child = new TestUIComponent("Child");
        flexbox.addChild(child);
        flexbox.removeChild(child);

        assertEquals(0, flexbox.getChildCount());
    }

    @Test
    void testClearChildren() {
        flexbox.addChild(new TestUIComponent("Child1"));
        flexbox.addChild(new TestUIComponent("Child2"));
        flexbox.clearChildren();

        assertEquals(0, flexbox.getChildCount());
    }

    @Test
    void testGetChildAt() {
        TestUIComponent child = new TestUIComponent("Child");
        flexbox.addChild(child);

        assertSame(child, flexbox.getChildAt(0));
        assertNull(flexbox.getChildAt(1));
    }

    @Test
    void testGetChildren() {
        TestUIComponent child1 = new TestUIComponent("Child1");
        child1.setId(1L);
        TestUIComponent child2 = new TestUIComponent("Child2");
        child2.setId(2L);
        flexbox.addChild(child1);
        flexbox.addChild(child2);

        assertEquals(2, flexbox.getChildren().size());
    }

    @Test
    void testSetParent() {
        flexbox.setParent(parent);
        assertSame(parent, flexbox.getParent());
    }

    @Test
    void testSetLayoutWidth() {
        flexbox.setLayoutWidth(500);
        assertEquals(500, flexbox.getLayoutWidth());
    }

    @Test
    void testSetLayoutHeight() {
        flexbox.setLayoutHeight(400);
        assertEquals(400, flexbox.getLayoutHeight());
    }

    @Test
    void testSetPadding() {
        flexbox.setPadding(10, 20, 30, 40);
        flexbox.layout(parent, 400, 300);
    }

    @Test
    void testMeasure() {
        TestUIComponent child1 = new TestUIComponent("Child1");
        child1.setSize(100, 50);
        flexbox.addChild(child1);

        float[] measured = flexbox.measure(parent);
        assertNotNull(measured);
        assertEquals(2, measured.length);
    }
}
