package io.github.s3s3l.yggdrasil.game.core.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 列表组件单元测试
 */
class UIListTest {

    private UIList list;

    @BeforeEach
    void setUp() {
        list = new UIList();
        list.setId(1L);
        list.setSize(200, 300);
    }

    @Test
    void testListCreation() {
        assertNotNull(list);
        assertEquals(0, list.getItemCount());
        assertEquals(-1, list.getSelectedIndex());
        assertEquals(UIDirection.VERTICAL, list.getLayoutDirection());
        assertFalse(list.isAutoSize());
    }

    @Test
    void testConstructorWithItems() {
        List<UIListItem> items = Arrays.asList(
            new UIListItem("Item 1"),
            new UIListItem("Item 2"),
            new UIListItem("Item 3")
        );
        UIList uiList = new UIList(items);
        assertEquals(3, uiList.getItemCount());
    }

    @Test
    void testConstructorWithNull() {
        UIList uiList = new UIList(null);
        assertNotNull(uiList);
        assertEquals(0, uiList.getItemCount());
    }

    @Test
    void testAddItem() {
        UIListItem item = new UIListItem("Test Item");
        list.addItem(item);

        assertEquals(1, list.getItemCount());
        assertEquals("Test Item", list.getItemAt(0).getText());
    }

    @Test
    void testAddItemMultiple() {
        list.addItem(new UIListItem("Item 1"));
        list.addItem(new UIListItem("Item 2"));
        list.addItem(new UIListItem("Item 3"));

        assertEquals(3, list.getItemCount());
    }

    @Test
    void testAddItemAt() {
        list.addItem(new UIListItem("Item 1"));
        list.addItem(new UIListItem("Item 2"));
        list.addItem(0, new UIListItem("New Item"));

        assertEquals(3, list.getItemCount());
        assertEquals("New Item", list.getItemAt(0).getText());
    }

    @Test
    void testAddItemAtOutOfBounds() {
        list.addItem(new UIListItem("Item 1"));
        list.addItem(100, new UIListItem("New Item"));

        assertEquals(2, list.getItemCount());
        assertEquals("New Item", list.getItemAt(1).getText());
    }

    @Test
    void testRemoveItem() {
        UIListItem item = new UIListItem("Test Item");
        list.addItem(item);
        list.removeItem(item);

        assertEquals(0, list.getItemCount());
    }

    @Test
    void testRemoveItemAt() {
        list.addItem(new UIListItem("Item 1"));
        list.addItem(new UIListItem("Item 2"));
        list.addItem(new UIListItem("Item 3"));

        list.removeItemAt(1);

        assertEquals(2, list.getItemCount());
        assertEquals("Item 3", list.getItemAt(1).getText());
    }

    @Test
    void testClearItems() {
        list.addItem(new UIListItem("Item 1"));
        list.addItem(new UIListItem("Item 2"));

        list.clearItems();

        assertEquals(0, list.getItemCount());
        assertEquals(-1, list.getSelectedIndex());
    }

    @Test
    void testGetItemAt() {
        UIListItem item = new UIListItem("Test Item");
        list.addItem(item);

        assertSame(item, list.getItemAt(0));
        assertNull(list.getItemAt(-1));
        assertNull(list.getItemAt(10));
    }

    @Test
    void testFindItemByText() {
        list.addItem(new UIListItem("Item 1"));
        list.addItem(new UIListItem("Item 2"));
        list.addItem(new UIListItem("Item 3"));

        UIListItem found = list.findItemByText("Item 2");
        assertNotNull(found);
        assertEquals("Item 2", found.getText());
    }

    @Test
    void testFindItemByTextNotFound() {
        list.addItem(new UIListItem("Item 1"));
        UIListItem found = list.findItemByText("Non Existent");
        assertNull(found);
    }

    @Test
    void testFindItemByData() {
        UIListItem item = new UIListItem("Test", "TestData");
        list.addItem(item);

        UIListItem found = list.findItemByData("TestData");
        assertNotNull(found);
        assertSame(item, found);
    }

    @Test
    void testFindItemByDataNotFound() {
        list.addItem(new UIListItem("Test"));
        UIListItem found = list.findItemByData("Non Existent");
        assertNull(found);
    }

    @Test
    void testSetSelectedIndex() {
        list.addItem(new UIListItem("Item 1"));
        list.addItem(new UIListItem("Item 2"));
        list.addItem(new UIListItem("Item 3"));

        list.setSelectedIndex(1);

        assertEquals(1, list.getSelectedIndex());
        assertTrue(list.getSelectedItem().isSelected());
    }

    @Test
    void testSetSelectedIndexOutOfBounds() {
        list.addItem(new UIListItem("Item 1"));

        list.setSelectedIndex(-1);
        assertEquals(-1, list.getSelectedIndex());

        list.setSelectedIndex(10);
        assertEquals(-1, list.getSelectedIndex());
    }

    @Test
    void testSetSelectedByText() {
        list.addItem(new UIListItem("Item 1"));
        list.addItem(new UIListItem("Item 2"));

        // 确保初始状态没有选中项
        list.clearSelection();

        boolean result = list.setSelectedByText("Item 2");

        assertTrue(result);
        assertEquals(1, list.getSelectedIndex());
        assertEquals("Item 2", list.getSelectedItem().getText());
    }

    @Test
    void testSetSelectedByTextNotFound() {
        list.addItem(new UIListItem("Item 1"));
        boolean result = list.setSelectedByText("Non Existent");
        assertFalse(result);
    }

    @Test
    void testSetSelectedByData() {
        list.addItem(new UIListItem("Item 1", "Data1"));
        list.addItem(new UIListItem("Item 2", "Data2"));

        // 确保初始状态没有选中项
        list.clearSelection();

        boolean result = list.setSelectedByData("Data2");

        assertTrue(result);
        assertEquals(1, list.getSelectedIndex());
        assertEquals("Item 2", list.getSelectedItem().getText());
    }

    @Test
    void testSetSelectedByDataNotFound() {
        list.addItem(new UIListItem("Item 1"));
        boolean result = list.setSelectedByData("Non Existent");
        assertFalse(result);
    }

    @Test
    void testGetSelectedItem() {
        list.addItem(new UIListItem("Item 1"));
        list.addItem(new UIListItem("Item 2"));
        list.setSelectedIndex(0);

        UIListItem selected = list.getSelectedItem();
        assertNotNull(selected);
        assertEquals("Item 1", selected.getText());
    }

    @Test
    void testGetSelectedItemNull() {
        list.addItem(new UIListItem("Item 1"));
        assertNull(list.getSelectedItem());
    }

    @Test
    void testClearSelection() {
        list.addItem(new UIListItem("Item 1"));
        list.setSelectedIndex(0);

        list.clearSelection();

        assertEquals(-1, list.getSelectedIndex());
    }

    @Test
    void testSetLayoutDirection() {
        list.setLayoutDirection(UIDirection.HORIZONTAL);
        assertEquals(UIDirection.HORIZONTAL, list.getLayoutDirection());
    }

    @Test
    void testSetItemSpacing() {
        list.setItemSpacing(10);
        assertEquals(10, list.getItemSpacing());
    }

    @Test
    void testSetItemHeight() {
        list.setItemHeight(50);
        assertEquals(50, list.getItemHeight());
    }

    @Test
    void testSetAutoSize() {
        list.setAutoSize(true);
        assertTrue(list.isAutoSize());
    }

    @Test
    void testSetSelectedColor() {
        UIColor color = UIColor.BLUE;
        list.setSelectedColor(color);
        assertEquals(color, list.getSelectedColor());
    }

    @Test
    void testSetItemColor() {
        UIColor color = UIColor.GRAY;
        list.setItemColor(color);
        assertEquals(color, list.getItemColor());
    }

    @Test
    void testItemClickListener() {
        final boolean[] clicked = {false};
        final int[] capturedIndex = {-1};

        list.setListener((l, item, index) -> {
            clicked[0] = true;
            capturedIndex[0] = index;
        });

        list.addItem(new UIListItem("Item 1"));
        list.getItemAt(0).onClick();

        assertTrue(clicked[0]);
        assertEquals(0, capturedIndex[0]);
    }

    @Test
    void testListenerNullByDefault() {
        assertNull(list.getListener());
    }

    @Test
    void testSetListener() {
        UIList.ItemClickListener listener = (l, item, index) -> {};
        list.setListener(listener);
        assertNotNull(list.getListener());
    }

    @Test
    void testRender() {
        list.addItem(new UIListItem("Item 1"));
        list.addItem(new UIListItem("Item 2"));

        assertDoesNotThrow(() -> list.render());
    }

    @Test
    void testRenderHidden() {
        list.setVisibility(UIVisibility.HIDDEN);
        assertDoesNotThrow(() -> list.render());
    }

    @Test
    void testLayoutItems() {
        list.addItem(new UIListItem("Item 1"));
        list.addItem(new UIListItem("Item 2"));
        list.addItem(new UIListItem("Item 3"));

        // 不应抛出异常
        assertDoesNotThrow(() -> list.layoutItems());
    }

    @Test
    void testAddChild() {
        UIListItem item = new UIListItem("Test");
        list.addChild(item);

        assertEquals(1, list.getItemCount());
    }

    @Test
    void testAddChildNonListItem() {
        UIPanel panel = new UIPanel("Test Panel");
        list.addChild(panel);

        // 非 UIListItem 应作为普通子组件添加
        assertEquals(0, list.getItemCount());
        assertEquals(1, list.getChildren().size());
    }

    @Test
    void testSelectedIndexAutoAdjustOnRemove() {
        list.addItem(new UIListItem("Item 1"));
        list.addItem(new UIListItem("Item 2"));
        list.addItem(new UIListItem("Item 3"));

        list.setSelectedIndex(1);
        list.removeItemAt(1);

        // 选中项被移除，应该清空选中
        assertEquals(-1, list.getSelectedIndex());
    }

    @Test
    void testSelectedIndexAdjustWhenRemovingBefore() {
        list.addItem(new UIListItem("Item 1"));
        list.addItem(new UIListItem("Item 2"));
        list.addItem(new UIListItem("Item 3"));

        list.setSelectedIndex(2);
        list.removeItemAt(0);

        // 删除的是选中项之前的项，索引应该减 1
        assertEquals(1, list.getSelectedIndex());
    }
}
