package io.github.s3s3l.yggdrasil.game.core.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 组件单元测试
 */
class UIComponentTest {

    private UIComponent component;

    @BeforeEach
    void setUp() {
        component = new TestUIComponent("TestComponent");
        component.setId(1L);
    }

    @Test
    void testComponentCreation() {
        assertEquals(1L, component.getId());
        assertEquals("TestComponent", component.getName());
        assertEquals(UIAlign.TOP_LEFT, component.getAlign());
        assertEquals(UIAnchor.TOP_LEFT, component.getAnchor());
        assertEquals(UIVisibility.VISIBLE, component.getVisibility());
        assertTrue(component.isEnabled());
        assertTrue(component.isInteractable());
    }

    @Test
    void testSetPosition() {
        component.setPosition(100, 200);

        assertEquals(100, component.getX());
        assertEquals(200, component.getY());
    }

    @Test
    void testSetSize() {
        component.setSize(300, 400);

        assertEquals(300, component.getWidth());
        assertEquals(400, component.getHeight());
    }

    @Test
    void testSetBounds() {
        component.setBounds(10, 20, 300, 400);

        assertEquals(10, component.getX());
        assertEquals(20, component.getY());
        assertEquals(300, component.getWidth());
        assertEquals(400, component.getHeight());
    }

    @Test
    void testAddChild() {
        UIComponent child = new TestUIComponent("Child");
        component.addChild(child);

        assertEquals(1, component.getChildren().size());
        assertSame(component, child.getParent());
    }

    @Test
    void testRemoveChild() {
        UIComponent child = new TestUIComponent("Child");
        component.addChild(child);
        component.removeChild(child);

        assertEquals(0, component.getChildren().size());
        assertNull(child.getParent());
    }

    @Test
    void testRemoveAllChildren() {
        component.addChild(new TestUIComponent("Child1"));
        component.addChild(new TestUIComponent("Child2"));
        component.addChild(new TestUIComponent("Child3"));

        component.removeAllChildren();

        assertEquals(0, component.getChildren().size());
    }

    @Test
    void testFindChildByName() {
        UIComponent child1 = new TestUIComponent("Child1");
        UIComponent child2 = new TestUIComponent("Child2");
        component.addChild(child1);
        component.addChild(child2);

        UIComponent found = component.findChildByName("Child1");
        assertSame(child1, found);
    }

    @Test
    void testFindChildByNameNotFound() {
        component.addChild(new TestUIComponent("Child1"));

        UIComponent found = component.findChildByName("NonExistent");
        assertNull(found);
    }

    @Test
    void testFindChildrenByTag() {
        UIComponent child1 = new TestUIComponent("Child1");
        child1.setTag("group1");
        UIComponent child2 = new TestUIComponent("Child2");
        child2.setTag("group1");
        UIComponent child3 = new TestUIComponent("Child3");
        child3.setTag("group2");

        component.addChild(child1);
        component.addChild(child2);
        component.addChild(child3);

        var found = component.findChildrenByTag("group1");
        assertEquals(2, found.size());
    }

    @Test
    void testFindChildrenByType() {
        UIButton button = new UIButton("Button");
        UILabel label = new UILabel("Label");
        component.addChild(button);
        component.addChild(label);

        var buttons = component.findChildrenByType(UIButton.class);
        assertEquals(1, buttons.size());
        assertSame(button, buttons.get(0));
    }

    @Test
    void testContainsPoint() {
        component.setBounds(0, 0, 100, 100);

        assertTrue(component.containsPoint(50, 50));
        assertTrue(component.containsPoint(0, 0));
        assertTrue(component.containsPoint(100, 100));
        assertFalse(component.containsPoint(-1, 50));
        assertFalse(component.containsPoint(150, 50));
    }

    @Test
    void testFindComponentAtPoint() {
        component.setBounds(0, 0, 200, 200);
        UIComponent child = new TestUIComponent("Child");
        child.setBounds(50, 50, 100, 100);
        component.addChild(child);

        UIComponent found = component.findComponentAtPoint(75, 75);
        assertSame(child, found);

        found = component.findComponentAtPoint(25, 25);
        assertSame(component, found);
    }

    @Test
    void testSetVisibility() {
        component.setVisibility(UIVisibility.HIDDEN);
        assertFalse(component.isVisible());

        component.setVisibility(UIVisibility.VISIBLE);
        assertTrue(component.isVisible());
    }

    @Test
    void testSetOpacity() {
        component.setOpacity(0.5f);
        assertEquals(0.5f, component.getOpacity());

        component.setOpacity(-0.5f);
        assertEquals(0f, component.getOpacity());

        component.setOpacity(1.5f);
        assertEquals(1f, component.getOpacity());
    }

    // 测试用的 UI 组件
    private static class TestUIComponent extends UIComponent {
        public TestUIComponent() {
            super();
        }

        public TestUIComponent(String name) {
            super(name);
        }

        @Override
        public void render() {
            // 测试不需要实际渲染
        }
    }
}
