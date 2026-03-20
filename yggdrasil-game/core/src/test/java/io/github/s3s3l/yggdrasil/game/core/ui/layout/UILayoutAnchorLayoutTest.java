package io.github.s3s3l.yggdrasil.game.core.ui.layout;

import io.github.s3s3l.yggdrasil.game.core.ui.TestUIComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 锚点布局单元测试
 */
class UILayoutAnchorLayoutTest {

    private UILayoutAnchorLayout anchorLayout;
    private TestUIComponent parent;

    @BeforeEach
    void setUp() {
        anchorLayout = new UILayoutAnchorLayout();
        parent = new TestUIComponent("Parent");
        parent.setSize(400, 300);
    }

    @Test
    void testDefaultValues() {
        assertNotNull(anchorLayout);
    }

    @Test
    void testSetAnchor() {
        TestUIComponent child = new TestUIComponent("Child");
        anchorLayout.addChild(child);

        anchorLayout.setAnchor(child, UILayoutAnchorLayout.Anchor.TOP_LEFT, 10, 20);

        UILayoutAnchorLayout.AnchorParams params = anchorLayout.getAnchorParams(child);
        assertEquals(UILayoutAnchorLayout.Anchor.TOP_LEFT, params.anchor);
        assertEquals(10, params.offsetX);
        assertEquals(20, params.offsetY);
    }

    @Test
    void testSetAnchorParams() {
        TestUIComponent child = new TestUIComponent("Child");
        anchorLayout.addChild(child);

        UILayoutAnchorLayout.AnchorParams params = new UILayoutAnchorLayout.AnchorParams(
            UILayoutAnchorLayout.Anchor.MIDDLE_CENTER, 5, 10
        );
        anchorLayout.setAnchorParams(child, params);

        assertSame(params, anchorLayout.getAnchorParams(child));
    }

    @Test
    void testRemoveAnchor() {
        TestUIComponent child = new TestUIComponent("Child");
        anchorLayout.addChild(child);
        anchorLayout.setAnchor(child, UILayoutAnchorLayout.Anchor.TOP_LEFT, 0, 0);

        anchorLayout.removeAnchor(child);

        assertNull(anchorLayout.getAnchorParams(child));
    }

    @Test
    void testClearAnchors() {
        TestUIComponent child1 = new TestUIComponent("Child1");
        TestUIComponent child2 = new TestUIComponent("Child2");
        anchorLayout.addChild(child1);
        anchorLayout.addChild(child2);

        anchorLayout.setAnchor(child1, UILayoutAnchorLayout.Anchor.TOP_LEFT, 0, 0);
        anchorLayout.setAnchor(child2, UILayoutAnchorLayout.Anchor.TOP_RIGHT, 0, 0);

        anchorLayout.clearAnchors();

        assertNull(anchorLayout.getAnchorParams(child1));
        assertNull(anchorLayout.getAnchorParams(child2));
    }

    @Test
    void testLayoutTopLeft() {
        TestUIComponent child = new TestUIComponent("Child");
        child.setSize(100, 50);
        anchorLayout.addChild(child);
        anchorLayout.setAnchor(child, UILayoutAnchorLayout.Anchor.TOP_LEFT, 10, 20);

        anchorLayout.layout(parent, 400, 300);

        assertEquals(10, child.getX(), 0.01f);
        assertEquals(20, child.getY(), 0.01f);
    }

    @Test
    void testLayoutTopCenter() {
        TestUIComponent child = new TestUIComponent("Child");
        child.setSize(100, 50);
        anchorLayout.addChild(child);
        anchorLayout.setAnchor(child, UILayoutAnchorLayout.Anchor.TOP_CENTER, 0, 0);

        anchorLayout.layout(parent, 400, 300);

        assertEquals(150, child.getX(), 0.01f);
        assertEquals(0, child.getY(), 0.01f);
    }

    @Test
    void testLayoutMiddleCenter() {
        TestUIComponent child = new TestUIComponent("Child");
        child.setSize(100, 50);
        anchorLayout.addChild(child);
        anchorLayout.setAnchor(child, UILayoutAnchorLayout.Anchor.MIDDLE_CENTER, 0, 0);

        anchorLayout.layout(parent, 400, 300);

        assertEquals(150, child.getX(), 0.01f);
        assertEquals(125, child.getY(), 0.01f);
    }

    @Test
    void testLayoutBottomRight() {
        TestUIComponent child = new TestUIComponent("Child");
        child.setSize(100, 50);
        anchorLayout.addChild(child);
        anchorLayout.setAnchor(child, UILayoutAnchorLayout.Anchor.BOTTOM_RIGHT, 0, 0);

        anchorLayout.layout(parent, 400, 300);

        assertEquals(300, child.getX(), 0.01f);
        assertEquals(250, child.getY(), 0.01f);
    }

    @Test
    void testAnchorParamsCreation() {
        UILayoutAnchorLayout.AnchorParams params = new UILayoutAnchorLayout.AnchorParams();
        assertEquals(UILayoutAnchorLayout.Anchor.TOP_LEFT, params.getAnchor());
        assertEquals(0, params.getOffsetX());
        assertEquals(0, params.getOffsetY());
    }

    @Test
    void testAnchorParamsSetters() {
        UILayoutAnchorLayout.AnchorParams params = new UILayoutAnchorLayout.AnchorParams();
        params.setAnchor(UILayoutAnchorLayout.Anchor.BOTTOM_LEFT);
        params.setOffsetX(15);
        params.setOffsetY(25);

        assertEquals(UILayoutAnchorLayout.Anchor.BOTTOM_LEFT, params.getAnchor());
        assertEquals(15, params.getOffsetX());
        assertEquals(25, params.getOffsetY());
    }

    @Test
    void testMeasure() {
        TestUIComponent child = new TestUIComponent("Child");
        child.setSize(100, 50);
        anchorLayout.addChild(child);

        float[] measured = anchorLayout.measure(parent);
        assertNotNull(measured);
        assertEquals(2, measured.length);
    }

    @Test
    void testMarkDirty() {
        anchorLayout.markDirty();
        assertTrue(anchorLayout.isDirty());
    }

    @Test
    void testClearDirty() {
        anchorLayout.markDirty();
        anchorLayout.clearDirty();
        assertFalse(anchorLayout.isDirty());
    }

    @Test
    void testAddChild() {
        TestUIComponent child = new TestUIComponent("Child");
        anchorLayout.addChild(child);

        assertEquals(1, anchorLayout.getChildCount());
    }

    @Test
    void testRemoveChild() {
        TestUIComponent child = new TestUIComponent("Child");
        anchorLayout.addChild(child);
        anchorLayout.removeChild(child);

        assertEquals(0, anchorLayout.getChildCount());
    }

    @Test
    void testClearChildren() {
        anchorLayout.addChild(new TestUIComponent("Child1"));
        anchorLayout.addChild(new TestUIComponent("Child2"));
        anchorLayout.clearChildren();

        assertEquals(0, anchorLayout.getChildCount());
    }

    @Test
    void testGetChildAt() {
        TestUIComponent child = new TestUIComponent("Child");
        anchorLayout.addChild(child);

        assertSame(child, anchorLayout.getChildAt(0));
        assertNull(anchorLayout.getChildAt(1));
    }

    @Test
    void testSetParent() {
        anchorLayout.setParent(parent);
        assertSame(parent, anchorLayout.getParent());
    }

    @Test
    void testSetLayoutWidth() {
        anchorLayout.setLayoutWidth(500);
        assertEquals(500, anchorLayout.getLayoutWidth());
    }

    @Test
    void testSetLayoutHeight() {
        anchorLayout.setLayoutHeight(400);
        assertEquals(400, anchorLayout.getLayoutHeight());
    }

    @Test
    void testSetPadding() {
        anchorLayout.setPadding(10, 20, 30, 40);
        anchorLayout.layout(parent, 400, 300);
    }
}
