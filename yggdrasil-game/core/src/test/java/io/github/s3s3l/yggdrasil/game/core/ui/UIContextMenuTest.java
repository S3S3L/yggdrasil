package io.github.s3s3l.yggdrasil.game.core.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 上下文菜单组件单元测试
 */
class UIContextMenuTest {

    private UIContextMenu contextMenu;
    private UIMenuItem item1;
    private UIMenuItem item2;

    @BeforeEach
    void setUp() {
        item1 = new UIMenuItem("Copy");
        item2 = new UIMenuItem("Paste");
        contextMenu = new UIContextMenu(Arrays.asList(item1, item2));
        contextMenu.setId(1L);
    }

    @Test
    void testContextMenuCreation() {
        assertNotNull(contextMenu);
        assertEquals(2, contextMenu.getItemCount());
        assertFalse(contextMenu.isVisible());
        assertEquals(150, contextMenu.getMinWidth(), 0.01f);
        assertEquals(25, contextMenu.getItemHeight(), 0.01f);
    }

    @Test
    void testDefaultConstructor() {
        UIContextMenu menu = new UIContextMenu();
        assertNotNull(menu);
        assertEquals(0, menu.getItemCount());
    }

    @Test
    void testNullItems() {
        UIContextMenu menu = new UIContextMenu(null);
        assertNotNull(menu);
        assertEquals(0, menu.getItemCount());
    }

    @Test
    void testGetItems() {
        List<UIMenuItem> items = contextMenu.getItems();
        assertEquals(2, items.size());
    }

    @Test
    void testSetItems() {
        UIMenuItem newItem = new UIMenuItem("Cut");
        contextMenu.setItems(Arrays.asList(newItem));
        assertEquals(1, contextMenu.getItemCount());
    }

    @Test
    void testAddItem() {
        UIMenuItem newItem = new UIMenuItem("Delete");
        contextMenu.addItem(newItem);
        assertEquals(3, contextMenu.getItemCount());
    }

    @Test
    void testAddItemAt() {
        UIMenuItem newItem = new UIMenuItem("Select All");
        contextMenu.addItem(1, newItem);
        assertEquals(3, contextMenu.getItemCount());
        assertEquals(newItem, contextMenu.getItemAt(1));
    }

    @Test
    void testRemoveItem() {
        contextMenu.removeItem(item1);
        assertEquals(1, contextMenu.getItemCount());
    }

    @Test
    void testRemoveItemAt() {
        contextMenu.removeItemAt(0);
        assertEquals(1, contextMenu.getItemCount());
    }

    @Test
    void testClearItems() {
        contextMenu.clearItems();
        assertEquals(0, contextMenu.getItemCount());
        assertFalse(contextMenu.isVisible());
    }

    @Test
    void testGetItemAt() {
        assertEquals(item1, contextMenu.getItemAt(0));
        assertNull(contextMenu.getItemAt(-1));
        assertNull(contextMenu.getItemAt(10));
    }

    @Test
    void testAddItemWithAction() {
        final boolean[] actionCalled = {false};
        UIMenuItem item = contextMenu.addItem("Test", () -> actionCalled[0] = true);
        assertNotNull(item);
        assertEquals(3, contextMenu.getItemCount());
    }

    @Test
    void testAddMenu() {
        List<UIMenuItem> children = Arrays.asList(new UIMenuItem("Sub1"), new UIMenuItem("Sub2"));
        UIMenuItem item = contextMenu.addMenu("Submenu", children);
        assertNotNull(item);
        assertTrue(item.hasSubmenu());
    }

    @Test
    void testAddCheckItem() {
        UIMenuItem checkItem = contextMenu.addCheckItem("Checked", true, null);
        assertTrue(checkItem.isChecked());
    }

    @Test
    void testAddSeparator() {
        contextMenu.addSeparator();
        assertEquals(3, contextMenu.getItemCount());
        assertTrue(contextMenu.getItemAt(2).isSeparator());
    }

    @Test
    void testAddGroup() {
        List<UIMenuItem> group = Arrays.asList(new UIMenuItem("G1"), new UIMenuItem("G2"));
        contextMenu.addGroup(group);
        assertEquals(5, contextMenu.getItemCount()); // 原有 2 个 + 分隔线 1 个 + 新组 2 个
    }

    @Test
    void testAddEmptyGroup() {
        contextMenu.addGroup(null);
        assertEquals(2, contextMenu.getItemCount());
    }

    @Test
    void testSetPosition() {
        contextMenu.setX(100);
        contextMenu.setY(200);
        assertEquals(100, contextMenu.getX(), 0.01f);
        assertEquals(200, contextMenu.getY(), 0.01f);
    }

    @Test
    void testSetVisible() {
        contextMenu.setVisible(true);
        assertTrue(contextMenu.isVisible());
    }

    @Test
    void testSetMinWidth() {
        contextMenu.setMinWidth(200);
        assertEquals(200, contextMenu.getMinWidth(), 0.01f);
    }

    @Test
    void testSetItemHeight() {
        contextMenu.setItemHeight(30);
        assertEquals(30, contextMenu.getItemHeight(), 0.01f);
    }

    @Test
    void testSetBackgroundColor() {
        UIColor color = new UIColor(0.3f, 0.3f, 0.3f, 1f); // DARK_GRAY
        contextMenu.setBackgroundColor(color);
        assertEquals(color, contextMenu.getBackgroundColor());
    }

    @Test
    void testSetHoverColor() {
        UIColor color = UIColor.GRAY;
        contextMenu.setHoverColor(color);
        assertEquals(color, contextMenu.getHoverColor());
    }

    @Test
    void testSetTextColor() {
        UIColor color = UIColor.WHITE;
        contextMenu.setTextColor(color);
        assertEquals(color, contextMenu.getTextColor());
    }

    @Test
    void testSetDisabledTextColor() {
        UIColor color = UIColor.GRAY;
        contextMenu.setDisabledTextColor(color);
        assertEquals(color, contextMenu.getDisabledTextColor());
    }

    @Test
    void testSetSeparatorColor() {
        UIColor color = UIColor.WHITE;
        contextMenu.setSeparatorColor(color);
        assertEquals(color, contextMenu.getSeparatorColor());
    }

    @Test
    void testSetFontSize() {
        contextMenu.setFontSize(16);
        assertEquals(16, contextMenu.getFontSize(), 0.01f);
    }

    @Test
    void testSetShowShortcuts() {
        contextMenu.setShowShortcuts(false);
        assertFalse(contextMenu.isShowShortcuts());
    }

    @Test
    void testShow() {
        final boolean[] opened = {false};
        contextMenu.setListener(new UIContextMenu.MenuListener() {
            @Override
            public void onMenuItemSelected(UIContextMenu menu, UIMenuItem item) {}

            @Override
            public void onMenuOpened(UIContextMenu menu) {
                opened[0] = true;
            }

            @Override
            public void onMenuClosed(UIContextMenu menu) {}
        });

        contextMenu.show();
        assertTrue(contextMenu.isVisible());
        assertTrue(opened[0]);
    }

    @Test
    void testHide() {
        contextMenu.show();
        contextMenu.hide();
        assertFalse(contextMenu.isVisible());
    }

    @Test
    void testToggle() {
        contextMenu.toggle();
        assertTrue(contextMenu.isVisible());
        contextMenu.toggle();
        assertFalse(contextMenu.isVisible());
    }

    @Test
    void testShowAt() {
        contextMenu.showAt(100, 200);
        assertEquals(100, contextMenu.getX(), 0.01f);
        assertEquals(200, contextMenu.getY(), 0.01f);
        assertTrue(contextMenu.isVisible());
    }

    @Test
    void testShowAtComponent() {
        UIComponent target = new UIPanel();
        target.setBounds(50, 50, 100, 50);
        contextMenu.showAt(target);
        assertEquals(target, contextMenu.getTarget());
        assertTrue(contextMenu.isVisible());
    }

    @Test
    void testToggleAt() {
        contextMenu.toggleAt(100, 200);
        assertTrue(contextMenu.isVisible());
        contextMenu.toggleAt(100, 200);
        assertFalse(contextMenu.isVisible());
    }

    @Test
    void testHandleMenuItemClick() {
        final boolean[] actionCalled = {false};
        final UIMenuItem[] selectedItem = {null};

        contextMenu.setListener(new UIContextMenu.MenuListener() {
            @Override
            public void onMenuItemSelected(UIContextMenu menu, UIMenuItem item) {
                selectedItem[0] = item;
            }

            @Override
            public void onMenuOpened(UIContextMenu menu) {}

            @Override
            public void onMenuClosed(UIContextMenu menu) {}
        });

        UIMenuItem clickableItem = new UIMenuItem("Test");
        clickableItem.setAction(() -> actionCalled[0] = true);
        contextMenu.addItem(clickableItem);

        contextMenu.handleMenuItemClick(clickableItem);
        assertTrue(actionCalled[0]);
        assertEquals(clickableItem, selectedItem[0]);
        assertFalse(contextMenu.isVisible()); // 点击后关闭
    }

    @Test
    void testHandleMenuItemClickWithSubmenu() {
        UIMenuItem parentItem = new UIMenuItem("Parent");
        parentItem.addChild(new UIMenuItem("Child"));
        contextMenu.addItem(parentItem);

        contextMenu.show(); // 先显示菜单
        contextMenu.handleMenuItemClick(parentItem);
        assertTrue(contextMenu.isVisible()); // 有子菜单不关闭
    }

    @Test
    void testHandleMenuItemClickDisabled() {
        UIMenuItem disabledItem = new UIMenuItem("Disabled");
        disabledItem.setEnabled(false);
        contextMenu.addItem(disabledItem);

        contextMenu.handleMenuItemClick(disabledItem);
        // 禁用项不响应
    }

    @Test
    void testHandleMenuItemClickSeparator() {
        contextMenu.addSeparator();
        UIMenuItem separator = contextMenu.getItemAt(2);
        contextMenu.handleMenuItemClick(separator);
        // 分隔线不响应
    }

    @Test
    void testHandleMenuItemHover() {
        contextMenu.handleMenuItemHover(item1);
        assertEquals(item1, contextMenu.getHoveredItem());
    }

    @Test
    void testClearHover() {
        contextMenu.handleMenuItemHover(item1);
        contextMenu.clearHover();
        assertNull(contextMenu.getHoveredItem());
    }

    @Test
    void testContainsPoint() {
        contextMenu.showAt(100, 100);
        // 简单测试，实际区域计算可能不同
        assertTrue(contextMenu.containsPoint(150, 150));
    }

    @Test
    void testContainsPointHidden() {
        assertFalse(contextMenu.containsPoint(100, 100));
    }

    @Test
    void testFindItem() {
        UIMenuItem found = contextMenu.findItem("Copy");
        assertNotNull(found);
        assertEquals("Copy", found.getText());
    }

    @Test
    void testFindItemNotFound() {
        UIMenuItem found = contextMenu.findItem("NonExistent");
        assertNull(found);
    }

    @Test
    void testSetTarget() {
        UIComponent target = new UIPanel();
        contextMenu.setTarget(target);
        assertEquals(target, contextMenu.getTarget());
    }

    @Test
    void testRender() {
        contextMenu.show();
        assertDoesNotThrow(() -> contextMenu.render());
    }

    @Test
    void testRenderHidden() {
        contextMenu.setVisibility(UIVisibility.HIDDEN);
        assertDoesNotThrow(() -> contextMenu.render());
    }

    @Test
    void testListener() {
        final UIMenuItem[] selected = {null};
        contextMenu.setListener(new UIContextMenu.MenuListener() {
            @Override
            public void onMenuItemSelected(UIContextMenu menu, UIMenuItem item) {
                selected[0] = item;
            }

            @Override
            public void onMenuOpened(UIContextMenu menu) {}

            @Override
            public void onMenuClosed(UIContextMenu menu) {}
        });

        contextMenu.handleMenuItemClick(item1);
        assertEquals(item1, selected[0]);
    }
}
