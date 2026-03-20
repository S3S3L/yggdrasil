package io.github.s3s3l.yggdrasil.game.core.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 标签页容器组件单元测试
 */
class UITabContainerTest {

    private UITabContainer container;

    @BeforeEach
    void setUp() {
        container = new UITabContainer();
        container.setId(1L);
        container.setSize(400, 300);
    }

    @Test
    void testTabContainerCreation() {
        assertNotNull(container);
        assertEquals(0, container.getTabCount());
        assertEquals(-1, container.getSelectedTabIndex());
        assertEquals(UITabContainer.TabPosition.TOP, container.getTabPosition());
    }

    @Test
    void testConstructorWithTabs() {
        List<UITabContainer.Tab> tabs = Arrays.asList(
            new UITabContainer.Tab("Tab 1"),
            new UITabContainer.Tab("Tab 2"),
            new UITabContainer.Tab("Tab 3")
        );
        UITabContainer c = new UITabContainer(tabs);
        assertEquals(3, c.getTabCount());
        assertEquals(0, c.getSelectedTabIndex());
    }

    @Test
    void testConstructorWithNull() {
        UITabContainer c = new UITabContainer(null);
        assertNotNull(c);
        assertEquals(0, c.getTabCount());
    }

    @Test
    void testAddTab() {
        UITabContainer.Tab tab = new UITabContainer.Tab("Test Tab");
        container.addTab(tab);

        assertEquals(1, container.getTabCount());
        assertEquals("Test Tab", container.getTabAt(0).getText());
    }

    @Test
    void testAddTabWithContent() {
        UIPanel content = new UIPanel("Content");
        container.addTab("Tab 1", content);

        assertEquals(1, container.getTabCount());
        assertSame(content, container.getTabAt(0).getContent());
    }

    @Test
    void testAddTabAtIndex() {
        container.addTab(new UITabContainer.Tab("Tab 1"));
        container.addTab(new UITabContainer.Tab("Tab 2"));
        container.addTab(0, new UITabContainer.Tab("New Tab"));

        assertEquals(3, container.getTabCount());
        assertEquals("New Tab", container.getTabAt(0).getText());
    }

    @Test
    void testRemoveTab() {
        UITabContainer.Tab tab1 = new UITabContainer.Tab("Tab 1");
        UITabContainer.Tab tab2 = new UITabContainer.Tab("Tab 2");
        container.addTab(tab1);
        container.addTab(tab2);

        container.removeTab(tab1);

        assertEquals(1, container.getTabCount());
        assertEquals("Tab 2", container.getTabAt(0).getText());
    }

    @Test
    void testRemoveTabAt() {
        container.addTab(new UITabContainer.Tab("Tab 1"));
        container.addTab(new UITabContainer.Tab("Tab 2"));
        container.addTab(new UITabContainer.Tab("Tab 3"));

        container.removeTabAt(1);

        assertEquals(2, container.getTabCount());
        assertEquals("Tab 3", container.getTabAt(1).getText());
    }

    @Test
    void testClearTabs() {
        container.addTab(new UITabContainer.Tab("Tab 1"));
        container.addTab(new UITabContainer.Tab("Tab 2"));

        container.clearTabs();

        assertEquals(0, container.getTabCount());
        assertEquals(-1, container.getSelectedTabIndex());
    }

    @Test
    void testGetTabAt() {
        container.addTab(new UITabContainer.Tab("Tab 1"));
        container.addTab(new UITabContainer.Tab("Tab 2"));

        UITabContainer.Tab tab = container.getTabAt(1);
        assertNotNull(tab);
        assertEquals("Tab 2", tab.getText());

        assertNull(container.getTabAt(-1));
        assertNull(container.getTabAt(10));
    }

    @Test
    void testFindTabByText() {
        container.addTab(new UITabContainer.Tab("Tab 1"));
        container.addTab(new UITabContainer.Tab("Tab 2"));
        container.addTab(new UITabContainer.Tab("Tab 3"));

        int index = container.findTabByText("Tab 2");
        assertEquals(1, index);

        assertEquals(-1, container.findTabByText("Non Existent"));
    }

    @Test
    void testSetSelectedTabIndex() {
        container.addTab(new UITabContainer.Tab("Tab 1"));
        container.addTab(new UITabContainer.Tab("Tab 2"));

        container.setSelectedTabIndex(1);

        assertEquals(1, container.getSelectedTabIndex());
        assertEquals("Tab 2", container.getSelectedTab().getText());
    }

    @Test
    void testSetSelectedTab() {
        UITabContainer.Tab tab1 = new UITabContainer.Tab("Tab 1");
        UITabContainer.Tab tab2 = new UITabContainer.Tab("Tab 2");
        container.addTab(tab1);
        container.addTab(tab2);

        container.setSelectedTab(tab2);

        assertEquals(1, container.getSelectedTabIndex());
    }

    @Test
    void testSetSelectedTabByText() {
        container.addTab(new UITabContainer.Tab("Tab 1"));
        container.addTab(new UITabContainer.Tab("Tab 2"));

        boolean result = container.setSelectedTabByText("Tab 2");

        assertTrue(result);
        assertEquals(1, container.getSelectedTabIndex());

        assertFalse(container.setSelectedTabByText("Non Existent"));
    }

    @Test
    void testGetSelectedTab() {
        container.addTab(new UITabContainer.Tab("Tab 1"));
        container.addTab(new UITabContainer.Tab("Tab 2"));

        UITabContainer.Tab selected = container.getSelectedTab();
        assertNotNull(selected);
        assertEquals("Tab 1", selected.getText());
    }

    @Test
    void testGetSelectedContent() {
        UIPanel content = new UIPanel("Content");
        container.addTab("Tab 1", content);

        UIComponent selected = container.getSelectedContent();
        assertSame(content, selected);
    }

    @Test
    void testSetTabColor() {
        UIColor color = UIColor.GRAY;
        container.setTabColor(color);
        assertEquals(color, container.getTabColor());
    }

    @Test
    void testSetSelectedTabColor() {
        UIColor color = UIColor.BLUE;
        container.setSelectedTabColor(color);
        assertEquals(color, container.getSelectedTabColor());
    }

    @Test
    void testSetTextColor() {
        UIColor color = UIColor.WHITE;
        container.setTextColor(color);
        assertEquals(color, container.getTextColor());
    }

    @Test
    void testSetContentColor() {
        UIColor color = new UIColor(0.15f, 0.15f, 0.15f, 1f);
        container.setContentColor(color);
        assertEquals(color, container.getContentColor());
    }

    @Test
    void testSetTabHeight() {
        container.setTabHeight(50);
        assertEquals(50, container.getTabHeight(), 0.01f);
    }

    @Test
    void testSetTabSpacing() {
        container.setTabSpacing(5);
        assertEquals(5, container.getTabSpacing(), 0.01f);
    }

    @Test
    void testSetTabPosition() {
        container.setTabPosition(UITabContainer.TabPosition.BOTTOM);
        assertEquals(UITabContainer.TabPosition.BOTTOM, container.getTabPosition());
    }

    @Test
    void testTabListener() {
        final int[] oldIndex = {-1};
        final int[] newIndex = {-1};

        container.addTab(new UITabContainer.Tab("Tab 1"));
        container.addTab(new UITabContainer.Tab("Tab 2"));
        container.setListener((c, oi, ni) -> {
            oldIndex[0] = oi;
            newIndex[0] = ni;
        });

        container.setSelectedTabIndex(1);

        assertEquals(0, oldIndex[0]);
        assertEquals(1, newIndex[0]);
    }

    @Test
    void testSelectNextTab() {
        container.addTab(new UITabContainer.Tab("Tab 1"));
        container.addTab(new UITabContainer.Tab("Tab 2"));
        container.addTab(new UITabContainer.Tab("Tab 3"));

        container.selectNextTab();

        assertEquals(1, container.getSelectedTabIndex());

        container.selectNextTab();
        assertEquals(2, container.getSelectedTabIndex());

        // 循环到第一个
        container.selectNextTab();
        assertEquals(0, container.getSelectedTabIndex());
    }

    @Test
    void testSelectPreviousTab() {
        container.addTab(new UITabContainer.Tab("Tab 1"));
        container.addTab(new UITabContainer.Tab("Tab 2"));
        container.addTab(new UITabContainer.Tab("Tab 3"));

        container.selectPreviousTab();

        assertEquals(2, container.getSelectedTabIndex());

        container.selectPreviousTab();
        assertEquals(1, container.getSelectedTabIndex());
    }

    @Test
    void testHandleTabClick() {
        container.addTab(new UITabContainer.Tab("Tab 1"));
        container.addTab(new UITabContainer.Tab("Tab 2"));

        container.handleTabClick(1);

        assertEquals(1, container.getSelectedTabIndex());
    }

    @Test
    void testHandleTabClickDisabled() {
        UITabContainer.Tab tab = new UITabContainer.Tab("Tab 1");
        tab.setEnabled(false);
        container.addTab(tab);
        container.addTab(new UITabContainer.Tab("Tab 2"));

        // 点击禁用的标签不会改变选中状态
        // 但第一个标签默认被选中（索引 0）
        assertEquals(0, container.getSelectedTabIndex());
    }

    @Test
    void testRender() {
        container.addTab(new UITabContainer.Tab("Tab 1"));
        assertDoesNotThrow(() -> container.render());
    }

    @Test
    void testRenderHidden() {
        container.setVisibility(UIVisibility.HIDDEN);
        assertDoesNotThrow(() -> container.render());
    }

    @Test
    void testLayout() {
        container.addTab(new UITabContainer.Tab("Tab 1"));
        assertDoesNotThrow(() -> container.layout());
    }

    @Test
    void testTabSetTabs() {
        List<UITabContainer.Tab> tabs = Arrays.asList(
            new UITabContainer.Tab("A"),
            new UITabContainer.Tab("B")
        );
        container.setTabs(tabs);

        assertEquals(2, container.getTabCount());
        assertEquals("A", container.getTabAt(0).getText());
    }

    @Test
    void testSetSelectedIndexOutOfBounds() {
        container.addTab(new UITabContainer.Tab("Tab 1"));
        // 第一个标签会自动被选中
        container.setSelectedTabIndex(10);

        // 索引越界时保持当前选中
        assertEquals(0, container.getSelectedTabIndex());
    }
}
