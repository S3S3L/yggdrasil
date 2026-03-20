package io.github.s3s3l.yggdrasil.game.core.ui.layout;

import io.github.s3s3l.yggdrasil.game.core.ui.TestUIComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 约束布局单元测试
 */
class UILayoutConstraintLayoutTest {

    private UILayoutConstraintLayout constraintLayout;
    private TestUIComponent parent;

    @BeforeEach
    void setUp() {
        constraintLayout = new UILayoutConstraintLayout();
        parent = new TestUIComponent("Parent");
        parent.setSize(400, 300);
    }

    @Test
    void testDefaultValues() {
        assertNotNull(constraintLayout);
    }

    @Test
    void testSetConstraints() {
        TestUIComponent child = new TestUIComponent("Child");
        constraintLayout.addChild(child);

        UILayoutConstraintLayout.ConstraintSet constraints = new UILayoutConstraintLayout.ConstraintSet();
        constraints.centerHorizontally();

        constraintLayout.setConstraints(child, constraints);

        assertSame(constraints, constraintLayout.getConstraints(child));
    }

    @Test
    void testRemoveConstraints() {
        TestUIComponent child = new TestUIComponent("Child");
        constraintLayout.addChild(child);

        UILayoutConstraintLayout.ConstraintSet constraints = new UILayoutConstraintLayout.ConstraintSet();
        constraintLayout.setConstraints(child, constraints);
        constraintLayout.removeConstraints(child);

        assertNull(constraintLayout.getConstraints(child));
    }

    @Test
    void testClearConstraints() {
        TestUIComponent child1 = new TestUIComponent("Child1");
        TestUIComponent child2 = new TestUIComponent("Child2");
        constraintLayout.addChild(child1);
        constraintLayout.addChild(child2);

        constraintLayout.setConstraints(child1, new UILayoutConstraintLayout.ConstraintSet());
        constraintLayout.setConstraints(child2, new UILayoutConstraintLayout.ConstraintSet());

        constraintLayout.clearConstraints();

        assertNull(constraintLayout.getConstraints(child1));
        assertNull(constraintLayout.getConstraints(child2));
    }

    @Test
    void testLayoutBasic() {
        TestUIComponent child = new TestUIComponent("Child");
        child.setSize(100, 50);
        constraintLayout.addChild(child);

        UILayoutConstraintLayout.ConstraintSet constraints = new UILayoutConstraintLayout.ConstraintSet();
        constraints.connectTopToTop(10);
        constraints.connectStartToLeft(20);
        constraintLayout.setConstraints(child, constraints);

        constraintLayout.layout(parent, 400, 300);

        assertTrue(child.getX() >= 0);
        assertTrue(child.getY() >= 0);
    }

    @Test
    void testLayoutCenterHorizontally() {
        TestUIComponent child = new TestUIComponent("Child");
        child.setSize(100, 50);
        constraintLayout.addChild(child);

        UILayoutConstraintLayout.ConstraintSet constraints = new UILayoutConstraintLayout.ConstraintSet();
        constraints.centerHorizontally();
        constraintLayout.setConstraints(child, constraints);

        constraintLayout.layout(parent, 400, 300);

        assertTrue(child.getX() >= 0);
    }

    @Test
    void testLayoutCenterVertically() {
        TestUIComponent child = new TestUIComponent("Child");
        child.setSize(100, 50);
        constraintLayout.addChild(child);

        UILayoutConstraintLayout.ConstraintSet constraints = new UILayoutConstraintLayout.ConstraintSet();
        constraints.centerVertically();
        constraintLayout.setConstraints(child, constraints);

        constraintLayout.layout(parent, 400, 300);

        assertTrue(child.getY() >= 0);
    }

    @Test
    void testConstraintSetBuilder() {
        UILayoutConstraintLayout.ConstraintSet constraints = new UILayoutConstraintLayout.ConstraintSet();

        assertSame(constraints, constraints.centerHorizontally());
        assertSame(constraints, constraints.centerVertically());
        assertSame(constraints, constraints.setWidthPercent(0.5f));
        assertSame(constraints, constraints.setHeightPercent(0.3f));
    }

    @Test
    void testMeasure() {
        TestUIComponent child = new TestUIComponent("Child");
        child.setSize(100, 50);
        constraintLayout.addChild(child);

        float[] measured = constraintLayout.measure(parent);
        assertNotNull(measured);
        assertEquals(2, measured.length);
    }

    @Test
    void testMarkDirty() {
        constraintLayout.markDirty();
        assertTrue(constraintLayout.isDirty());
    }

    @Test
    void testClearDirty() {
        constraintLayout.markDirty();
        constraintLayout.clearDirty();
        assertFalse(constraintLayout.isDirty());
    }

    @Test
    void testAddChild() {
        TestUIComponent child = new TestUIComponent("Child");
        constraintLayout.addChild(child);

        assertEquals(1, constraintLayout.getChildCount());
    }

    @Test
    void testRemoveChild() {
        TestUIComponent child = new TestUIComponent("Child");
        constraintLayout.addChild(child);
        constraintLayout.removeChild(child);

        assertEquals(0, constraintLayout.getChildCount());
    }

    @Test
    void testClearChildren() {
        constraintLayout.addChild(new TestUIComponent("Child1"));
        constraintLayout.addChild(new TestUIComponent("Child2"));
        constraintLayout.clearChildren();

        assertEquals(0, constraintLayout.getChildCount());
    }

    @Test
    void testGetChildAt() {
        TestUIComponent child = new TestUIComponent("Child");
        constraintLayout.addChild(child);

        assertSame(child, constraintLayout.getChildAt(0));
        assertNull(constraintLayout.getChildAt(1));
    }

    @Test
    void testGetChildren() {
        TestUIComponent child1 = new TestUIComponent("Child1");
        child1.setId(1L);
        TestUIComponent child2 = new TestUIComponent("Child2");
        child2.setId(2L);
        constraintLayout.addChild(child1);
        constraintLayout.addChild(child2);

        assertEquals(2, constraintLayout.getChildren().size());
    }

    @Test
    void testSetParent() {
        constraintLayout.setParent(parent);
        assertSame(parent, constraintLayout.getParent());
    }

    @Test
    void testSetLayoutWidth() {
        constraintLayout.setLayoutWidth(500);
        assertEquals(500, constraintLayout.getLayoutWidth());
    }

    @Test
    void testSetLayoutHeight() {
        constraintLayout.setLayoutHeight(400);
        assertEquals(400, constraintLayout.getLayoutHeight());
    }

    @Test
    void testSetPadding() {
        constraintLayout.setPadding(10, 20, 30, 40);
        constraintLayout.layout(parent, 400, 300);
    }
}
