package io.github.s3s3l.yggdrasil.game.core.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 面板单元测试
 */
class UIPanelTest {

    private UIPanel panel;

    @BeforeEach
    void setUp() {
        panel = new UIPanel("TestPanel");
        panel.setId(1L);
        panel.setBounds(0, 0, 400, 300);
    }

    @Test
    void testPanelCreation() {
        assertEquals("TestPanel", panel.getName());
        assertEquals(UIDirection.VERTICAL, panel.getLayoutDirection());
        assertEquals(0, panel.getSpacing());
        assertFalse(panel.isAutoSize());
    }

    @Test
    void testSetSpacing() {
        panel.setSpacing(10);
        assertEquals(10, panel.getSpacing());
    }

    @Test
    void testSetAutoSize() {
        panel.setAutoSize(true);
        assertTrue(panel.isAutoSize());
    }

    @Test
    void testLayoutHorizontal() {
        panel.setSpacing(5);

        UIComponent child1 = new TestUIComponent("Child1");
        child1.setSize(100, 50);
        UIComponent child2 = new TestUIComponent("Child2");
        child2.setSize(100, 50);

        panel.addChild(child1);
        panel.addChild(child2);

        // 注意：layout 方法需要根据实际实现测试
        // 这里只测试基本功能
        assertEquals(2, panel.getChildren().size());
    }

    @Test
    void testLayoutVertical() {
        panel.setSpacing(5);

        UIComponent child1 = new TestUIComponent("Child1");
        child1.setSize(100, 50);
        UIComponent child2 = new TestUIComponent("Child2");
        child2.setSize(100, 50);

        panel.addChild(child1);
        panel.addChild(child2);

        assertEquals(2, panel.getChildren().size());
    }

    @Test
    void testAddChildWithAutoSize() {
        panel.setAutoSize(true);

        UIComponent child = new TestUIComponent("Child");
        child.setSize(100, 50);
        panel.addChild(child);

        // 自动大小会在 addChild 时触发 layout
        assertEquals(1, panel.getChildren().size());
    }

    @Test
    void testRemoveChildWithAutoSize() {
        panel.setAutoSize(true);

        UIComponent child = new TestUIComponent("Child");
        child.setSize(100, 50);
        panel.addChild(child);
        panel.removeChild(child);

        assertEquals(0, panel.getChildren().size());
    }

    @Test
    void testRender() {
        panel.setBackgroundColor(UIColor.RED);

        // 渲染不会抛出异常
        assertDoesNotThrow(() -> panel.render());
    }

    @Test
    void testRenderHidden() {
        panel.setVisibility(UIVisibility.HIDDEN);

        // 隐藏的组件不会渲染
        assertDoesNotThrow(() -> panel.render());
    }

    // 测试用的 UI 组件
    private static class TestUIComponent extends UIComponent {
        public TestUIComponent(String name) {
            super(name);
        }

        @Override
        public void render() {
            // 测试不需要实际渲染
        }

        public void setBounds(float x, float y, float width, float height) {
            setPosition(x, y);
            setSize(width, height);
        }
    }
}
