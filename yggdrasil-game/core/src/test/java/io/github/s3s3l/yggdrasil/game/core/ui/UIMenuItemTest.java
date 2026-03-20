package io.github.s3s3l.yggdrasil.game.core.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 菜单项组件单元测试
 */
class UIMenuItemTest {

    private UIMenuItem menuItem;

    @BeforeEach
    void setUp() {
        menuItem = new UIMenuItem("Test Item");
    }

    @Test
    void testMenuItemCreation() {
        assertNotNull(menuItem);
        assertEquals("Test Item", menuItem.getText());
        assertNull(menuItem.getIcon());
        assertNull(menuItem.getShortcut());
        assertTrue(menuItem.isEnabled());
        assertFalse(menuItem.isChecked());
        assertFalse(menuItem.isSeparator());
        assertTrue(menuItem.getChildren().isEmpty());
    }

    @Test
    void testDefaultConstructor() {
        UIMenuItem item = new UIMenuItem();
        assertNotNull(item);
        assertEquals("", item.getText());
    }

    @Test
    void testSetText() {
        menuItem.setText("New Text");
        assertEquals("New Text", menuItem.getText());
    }

    @Test
    void testSetIcon() {
        menuItem.setIcon("icon_path");
        assertEquals("icon_path", menuItem.getIcon());
    }

    @Test
    void testSetShortcut() {
        menuItem.setShortcut("Ctrl+S");
        assertEquals("Ctrl+S", menuItem.getShortcut());
    }

    @Test
    void testSetChildren() {
        List<UIMenuItem> children = Arrays.asList(new UIMenuItem("Child 1"), new UIMenuItem("Child 2"));
        menuItem.setChildren(children);
        assertEquals(2, menuItem.getChildren().size());
    }

    @Test
    void testSetNullChildren() {
        menuItem.setChildren(null);
        assertTrue(menuItem.getChildren().isEmpty());
    }

    @Test
    void testAddChild() {
        UIMenuItem child = new UIMenuItem("Child");
        menuItem.addChild(child);
        assertEquals(1, menuItem.getChildren().size());
    }

    @Test
    void testRemoveChild() {
        UIMenuItem child = new UIMenuItem("Child");
        menuItem.addChild(child);
        menuItem.removeChild(child);
        assertTrue(menuItem.getChildren().isEmpty());
    }

    @Test
    void testClearChildren() {
        menuItem.addChild(new UIMenuItem("Child 1"));
        menuItem.addChild(new UIMenuItem("Child 2"));
        menuItem.clearChildren();
        assertTrue(menuItem.getChildren().isEmpty());
    }

    @Test
    void testHasChildren() {
        assertFalse(menuItem.hasChildren());
        menuItem.addChild(new UIMenuItem("Child"));
        assertTrue(menuItem.hasChildren());
    }

    @Test
    void testSetEnabled() {
        menuItem.setEnabled(false);
        assertFalse(menuItem.isEnabled());
    }

    @Test
    void testSetChecked() {
        final boolean[] stateChanged = {false};
        menuItem.setListener(new UIMenuItem.MenuListener() {
            @Override
            public void onMenuItemClicked(UIMenuItem item) {}

            @Override
            public void onMenuItemStateChanged(UIMenuItem item, boolean checked) {
                stateChanged[0] = true;
            }
        });

        menuItem.setChecked(true);
        assertTrue(menuItem.isChecked());
        assertTrue(stateChanged[0]);
    }

    @Test
    void testToggleChecked() {
        menuItem.setChecked(false);
        menuItem.toggleChecked();
        assertTrue(menuItem.isChecked());
        menuItem.toggleChecked();
        assertFalse(menuItem.isChecked());
    }

    @Test
    void testSetSeparator() {
        menuItem.setSeparator(true);
        assertTrue(menuItem.isSeparator());
    }

    @Test
    void testSeparatorFactory() {
        UIMenuItem separator = UIMenuItem.separator();
        assertTrue(separator.isSeparator());
        assertFalse(separator.isEnabled());
        assertEquals("", separator.getText());
    }

    @Test
    void testSetAction() {
        final boolean[] actionCalled = {false};
        menuItem.setAction(() -> actionCalled[0] = true);
        menuItem.doAction();
        assertTrue(actionCalled[0]);
    }

    @Test
    void testSetListener() {
        final boolean[] clicked = {false};
        menuItem.setListener(new UIMenuItem.MenuListener() {
            @Override
            public void onMenuItemClicked(UIMenuItem item) {
                clicked[0] = true;
            }

            @Override
            public void onMenuItemStateChanged(UIMenuItem item, boolean checked) {}
        });

        menuItem.doAction();
        assertTrue(clicked[0]);
    }

    @Test
    void testSetUserData() {
        menuItem.setUserData("test data");
        assertEquals("test data", menuItem.getUserData());
    }

    @Test
    void testHasSubmenu() {
        assertFalse(menuItem.hasSubmenu());
        menuItem.addChild(new UIMenuItem("Child"));
        assertTrue(menuItem.hasSubmenu());
    }

    @Test
    void testDoAction() {
        final boolean[] actionCalled = {false};
        menuItem.setAction(() -> actionCalled[0] = true);
        menuItem.doAction();
        assertTrue(actionCalled[0]);
    }

    @Test
    void testDoActionNoAction() {
        // 没有设置 action，不抛异常
        assertDoesNotThrow(() -> menuItem.doAction());
    }

    @Test
    void testGetDepth() {
        assertEquals(0, menuItem.getDepth());
    }

    @Test
    void testGetDepthWithChildren() {
        UIMenuItem child = new UIMenuItem("Child");
        UIMenuItem grandchild = new UIMenuItem("Grandchild");
        child.addChild(grandchild);
        menuItem.addChild(child);

        assertEquals(2, menuItem.getDepth());
    }

    @Test
    void testFindItem() {
        UIMenuItem child1 = new UIMenuItem("Child 1");
        UIMenuItem child2 = new UIMenuItem("Child 2");
        menuItem.addChild(child1);
        menuItem.addChild(child2);

        UIMenuItem found = menuItem.findItem("Child 1");
        assertNotNull(found);
        assertEquals("Child 1", found.getText());
    }

    @Test
    void testFindItemNotFound() {
        UIMenuItem found = menuItem.findItem("NonExistent");
        assertNull(found);
    }

    @Test
    void testFindItemInGrandchildren() {
        UIMenuItem child = new UIMenuItem("Child");
        UIMenuItem grandchild = new UIMenuItem("Grandchild");
        child.addChild(grandchild);
        menuItem.addChild(child);

        UIMenuItem found = menuItem.findItem("Grandchild");
        assertNotNull(found);
        assertEquals("Grandchild", found.getText());
    }

    @Test
    void testFindItemSelf() {
        UIMenuItem found = menuItem.findItem("Test Item");
        assertNotNull(found);
        assertEquals(menuItem, found);
    }

    @Test
    void testGetAllItems() {
        UIMenuItem child1 = new UIMenuItem("Child 1");
        UIMenuItem child2 = new UIMenuItem("Child 2");
        menuItem.addChild(child1);
        menuItem.addChild(child2);

        List<UIMenuItem> allItems = menuItem.getAllItems();
        assertEquals(3, allItems.size()); // 包括自己
    }

    @Test
    void testGetAllItemsWithGrandchildren() {
        UIMenuItem child = new UIMenuItem("Child");
        UIMenuItem grandchild = new UIMenuItem("Grandchild");
        child.addChild(grandchild);
        menuItem.addChild(child);

        List<UIMenuItem> allItems = menuItem.getAllItems();
        assertEquals(3, allItems.size()); // 包括自己和所有后代
    }

    @Test
    void testGetAllItemsExcludesSeparator() {
        UIMenuItem separator = UIMenuItem.separator();
        menuItem.addChild(separator);

        List<UIMenuItem> allItems = menuItem.getAllItems();
        assertFalse(allItems.contains(separator));
    }
}
