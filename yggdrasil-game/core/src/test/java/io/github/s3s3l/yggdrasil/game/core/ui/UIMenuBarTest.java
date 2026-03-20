package io.github.s3s3l.yggdrasil.game.core.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 菜单栏组件单元测试
 */
class UIMenuBarTest {

    private UIMenuBar menuBar;
    private UIMenuItem item1;
    private UIMenuItem item2;

    @BeforeEach
    void setUp() {
        item1 = new UIMenuItem("File");
        item2 = new UIMenuItem("Edit");
        menuBar = new UIMenuBar(Arrays.asList(item1, item2));
        menuBar.setId(1L);
    }

    @Test
    void testMenuBarCreation() {
        assertNotNull(menuBar);
        assertEquals(2, menuBar.getItemCount());
        assertEquals(30, menuBar.getItemHeight(), 0.01f);
        assertTrue(menuBar.isShowShortcuts());
    }

    @Test
    void testDefaultConstructor() {
        UIMenuBar bar = new UIMenuBar();
        assertNotNull(bar);
        assertEquals(0, bar.getItemCount());
    }

    @Test
    void testNullItems() {
        UIMenuBar bar = new UIMenuBar(null);
        assertNotNull(bar);
        assertEquals(0, bar.getItemCount());
    }

    @Test
    void testGetItems() {
        List<UIMenuItem> items = menuBar.getItems();
        assertEquals(2, items.size());
    }

    @Test
    void testSetItems() {
        UIMenuItem newItem = new UIMenuItem("View");
        menuBar.setItems(Arrays.asList(newItem));
        assertEquals(1, menuBar.getItemCount());
    }

    @Test
    void testAddItem() {
        UIMenuItem newItem = new UIMenuItem("Help");
        menuBar.addItem(newItem);
        assertEquals(3, menuBar.getItemCount());
    }

    @Test
    void testAddItemAt() {
        UIMenuItem newItem = new UIMenuItem("Insert");
        menuBar.addItem(1, newItem);
        assertEquals(3, menuBar.getItemCount());
        assertEquals(newItem, menuBar.getItemAt(1));
    }

    @Test
    void testRemoveItem() {
        menuBar.removeItem(item1);
        assertEquals(1, menuBar.getItemCount());
    }

    @Test
    void testRemoveItemAt() {
        menuBar.removeItemAt(0);
        assertEquals(1, menuBar.getItemCount());
    }

    @Test
    void testClearItems() {
        menuBar.clearItems();
        assertEquals(0, menuBar.getItemCount());
        assertNull(menuBar.getOpenItem());
    }

    @Test
    void testGetItemAt() {
        assertEquals(item1, menuBar.getItemAt(0));
        assertNull(menuBar.getItemAt(-1));
        assertNull(menuBar.getItemAt(10));
    }

    @Test
    void testFindItemByText() {
        UIMenuItem found = menuBar.findItemByText("File");
        assertNotNull(found);
        assertEquals("File", found.getText());
    }

    @Test
    void testFindItemByTextNotFound() {
        UIMenuItem found = menuBar.findItemByText("NonExistent");
        assertNull(found);
    }

    @Test
    void testSetBackgroundColor() {
        UIColor color = new UIColor(0.2f, 0.2f, 0.2f, 1f); // DARK_GRAY
        menuBar.setBackgroundColor(color);
        assertEquals(color, menuBar.getBackgroundColor());
    }

    @Test
    void testSetHoverColor() {
        UIColor color = UIColor.GRAY;
        menuBar.setHoverColor(color);
        assertEquals(color, menuBar.getHoverColor());
    }

    @Test
    void testSetTextColor() {
        UIColor color = UIColor.WHITE;
        menuBar.setTextColor(color);
        assertEquals(color, menuBar.getTextColor());
    }

    @Test
    void testSetDisabledTextColor() {
        UIColor color = UIColor.GRAY;
        menuBar.setDisabledTextColor(color);
        assertEquals(color, menuBar.getDisabledTextColor());
    }

    @Test
    void testSetItemHeight() {
        menuBar.setItemHeight(40);
        assertEquals(40, menuBar.getItemHeight(), 0.01f);
    }

    @Test
    void testSetItemSpacing() {
        menuBar.setItemSpacing(10);
        assertEquals(10, menuBar.getItemSpacing(), 0.01f);
    }

    @Test
    void testSetFontSize() {
        menuBar.setFontSize(16);
        assertEquals(16, menuBar.getFontSize(), 0.01f);
    }

    @Test
    void testSetShowShortcuts() {
        menuBar.setShowShortcuts(false);
        assertFalse(menuBar.isShowShortcuts());
    }

    @Test
    void testAddMenuItem() {
        final boolean[] actionCalled = {false};
        UIMenuItem item = menuBar.addMenuItem("Test", () -> actionCalled[0] = true);
        assertNotNull(item);
        assertEquals("Test", item.getText());
        assertEquals(3, menuBar.getItemCount());
    }

    @Test
    void testAddMenu() {
        List<UIMenuItem> children = Arrays.asList(new UIMenuItem("Sub1"), new UIMenuItem("Sub2"));
        UIMenuItem item = menuBar.addMenu("Parent", children);
        assertNotNull(item);
        assertTrue(item.hasSubmenu());
        assertEquals(2, item.getChildren().size());
    }

    @Test
    void testAddSeparator() {
        menuBar.addSeparator();
        assertEquals(3, menuBar.getItemCount());
        assertTrue(menuBar.getItemAt(2).isSeparator());
    }

    @Test
    void testHandleMenuItemClick() {
        final boolean[] actionCalled = {false};
        UIMenuItem clickableItem = new UIMenuItem("Click Me");
        clickableItem.setAction(() -> actionCalled[0] = true);
        menuBar.addItem(clickableItem);

        menuBar.handleMenuItemClick(clickableItem);
        assertTrue(actionCalled[0]);
    }

    @Test
    void testHandleMenuItemClickWithSubmenu() {
        UIMenuItem parentItem = new UIMenuItem("Parent");
        parentItem.addChild(new UIMenuItem("Child"));
        menuBar.addItem(parentItem);

        menuBar.handleMenuItemClick(parentItem);
        assertEquals(parentItem, menuBar.getOpenItem());
    }

    @Test
    void testHandleMenuItemClickDisabled() {
        UIMenuItem disabledItem = new UIMenuItem("Disabled");
        disabledItem.setEnabled(false);
        menuBar.addItem(disabledItem);

        menuBar.handleMenuItemClick(disabledItem);
        assertNull(menuBar.getOpenItem());
    }

    @Test
    void testHandleMenuItemClickSeparator() {
        menuBar.addSeparator();
        UIMenuItem separator = menuBar.getItemAt(2);
        menuBar.handleMenuItemClick(separator);
        // 分隔线不响应点击
    }

    @Test
    void testHandleMenuItemHover() {
        menuBar.handleMenuItemHover(item1);
        assertEquals(item1, menuBar.getHoveredItem());
    }

    @Test
    void testOpenMenu() {
        final boolean[] opened = {false};
        menuBar.setListener(new UIMenuBar.MenuListener() {
            @Override
            public void onMenuSelected(UIMenuBar menuBar, UIMenuItem item) {}

            @Override
            public void onMenuOpened(UIMenuBar menuBar, UIMenuItem item) {
                opened[0] = true;
            }

            @Override
            public void onMenuClosed(UIMenuBar menuBar) {}
        });

        menuBar.openMenu(item1);
        assertEquals(item1, menuBar.getOpenItem());
        assertTrue(opened[0]);
    }

    @Test
    void testCloseMenu() {
        menuBar.openMenu(item1);
        menuBar.closeMenu();
        assertNull(menuBar.getOpenItem());
    }

    @Test
    void testToggleMenu() {
        menuBar.toggleMenu(item1);
        assertEquals(item1, menuBar.getOpenItem());
        menuBar.toggleMenu(item1);
        assertNull(menuBar.getOpenItem());
    }

    @Test
    void testClearHover() {
        menuBar.handleMenuItemHover(item1);
        menuBar.clearHover();
        assertNull(menuBar.getHoveredItem());
    }

    @Test
    void testIsHorizontal() {
        assertTrue(menuBar.isHorizontal());
    }

    @Test
    void testGetRequiredWidth() {
        float width = menuBar.getRequiredWidth();
        assertTrue(width > 0);
    }

    @Test
    void testGetRequiredHeight() {
        float height = menuBar.getRequiredHeight();
        assertEquals(30, height, 0.01f);
    }

    @Test
    void testRender() {
        assertDoesNotThrow(() -> menuBar.render());
    }

    @Test
    void testRenderHidden() {
        menuBar.setVisibility(UIVisibility.HIDDEN);
        assertDoesNotThrow(() -> menuBar.render());
    }

    @Test
    void testListener() {
        final UIMenuItem[] selected = {null};
        menuBar.setListener(new UIMenuBar.MenuListener() {
            @Override
            public void onMenuSelected(UIMenuBar menuBar, UIMenuItem item) {
                selected[0] = item;
            }

            @Override
            public void onMenuOpened(UIMenuBar menuBar, UIMenuItem item) {}

            @Override
            public void onMenuClosed(UIMenuBar menuBar) {}
        });

        UIMenuItem clickableItem = new UIMenuItem("Test");
        menuBar.addItem(clickableItem);
        menuBar.handleMenuItemClick(clickableItem);
        assertEquals(clickableItem, selected[0]);
    }
}
