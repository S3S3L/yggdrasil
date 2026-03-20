package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * UI 列表组件
 * 垂直/水平布局，支持动态添加/删除项，支持选中项
 */
public class UIList extends UIComponent {

    private List<UIListItem> items;                // 列表项
    private int selectedIndex;                     // 选中项索引
    private UIDirection layoutDirection;           // 布局方向
    private float itemSpacing;                     // 项间距
    private float itemHeight;                      // 项高度（固定高度模式）
    private boolean autoSize;                      // 自动大小
    private UIColor selectedColor;                 // 选中项背景色
    private UIColor itemColor;                     // 列表项背景色
    private ItemClickListener listener;            // 项点击监听器

    /**
     * 列表项点击监听器接口
     */
    public interface ItemClickListener {
        /**
         * 当列表项被点击时调用
         *
         * @param list 列表组件
         * @param item 被点击的列表项
         * @param index 列表项索引
         */
        void onItemClick(UIList list, UIListItem item, int index);
    }

    public UIList() {
        this(null);
    }

    public UIList(List<UIListItem> items) {
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        this.selectedIndex = -1;
        this.layoutDirection = UIDirection.VERTICAL;
        this.itemSpacing = 2;
        this.itemHeight = 40;
        this.autoSize = false;
        this.selectedColor = new UIColor(0.3f, 0.5f, 0.8f, 1f);
        this.itemColor = UIColor.TRANSPARENT;
        this.selectedIndex = -1;
    }

    public List<UIListItem> getItems() {
        return new ArrayList<>(items);
    }

    public void setItems(List<UIListItem> items) {
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        this.selectedIndex = -1;
        layoutItems();
    }

    /**
     * 添加列表项
     *
     * @param item 列表项
     */
    public void addItem(UIListItem item) {
        if (item != null) {
            items.add(item);
            item.setClickListener(createItemClickListener(items.size() - 1));
            layoutItems();
        }
    }

    /**
     * 在指定位置添加列表项
     *
     * @param index 位置索引
     * @param item 列表项
     */
    public void addItem(int index, UIListItem item) {
        if (item != null) {
            index = Math.max(0, Math.min(index, items.size()));
            items.add(index, item);
            item.setClickListener(createItemClickListener(index));
            updateItemClickListeners();
            layoutItems();
        }
    }

    /**
     * 移除列表项
     *
     * @param item 列表项
     */
    public void removeItem(UIListItem item) {
        int index = items.indexOf(item);
        if (index >= 0) {
            items.remove(index);
            updateItemClickListeners();
            layoutItems();
            // 调整选中索引
            adjustSelectedIndexAfterRemove(index);
        }
    }

    /**
     * 移除指定位置的列表项
     *
     * @param index 位置索引
     */
    public void removeItemAt(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            updateItemClickListeners();
            layoutItems();
            adjustSelectedIndexAfterRemove(index);
        }
    }

    /**
     * 移除所有列表项
     */
    public void clearItems() {
        items.clear();
        selectedIndex = -1;
    }

    /**
     * 获取列表项数量
     *
     * @return 列表项数量
     */
    public int getItemCount() {
        return items.size();
    }

    /**
     * 获取指定位置的列表项
     *
     * @param index 位置索引
     * @return 列表项
     */
    public UIListItem getItemAt(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        }
        return null;
    }

    /**
     * 查找指定文本的列表项
     *
     * @param text 文本
     * @return 找到的列表项，如果没有则返回 null
     */
    public UIListItem findItemByText(String text) {
        for (UIListItem item : items) {
            if (text != null && text.equals(item.getText())) {
                return item;
            }
        }
        return null;
    }

    /**
     * 查找指定数据的列表项
     *
     * @param data 数据
     * @return 找到的列表项，如果没有则返回 null
     */
    public UIListItem findItemByData(Object data) {
        for (UIListItem item : items) {
            if (data != null && data.equals(item.getData())) {
                return item;
            }
        }
        return null;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * 设置选中项
     *
     * @param index 选中项索引
     */
    public void setSelectedIndex(int index) {
        if (index >= -1 && index < items.size()) {
            // 取消之前的选中
            if (selectedIndex >= 0 && selectedIndex < items.size()) {
                items.get(selectedIndex).setSelected(false);
            }
            // 选中新项
            selectedIndex = index;
            if (index >= 0) {
                items.get(index).setSelected(true);
            }
        }
    }

    /**
     * 设置选中项（通过文本）
     *
     * @param text 列表项文本
     * @return 是否成功设置
     */
    public boolean setSelectedByText(String text) {
        UIListItem item = findItemByText(text);
        if (item != null) {
            int index = indexOfItem(item);
            setSelectedIndex(index);
            return true;
        }
        return false;
    }

    /**
     * 设置选中项（通过数据）
     *
     * @param data 列表项数据
     * @return 是否成功设置
     */
    public boolean setSelectedByData(Object data) {
        UIListItem item = findItemByData(data);
        if (item != null) {
            int index = indexOfItem(item);
            setSelectedIndex(index);
            return true;
        }
        return false;
    }

    /**
     * 获取选中的列表项
     *
     * @return 选中的列表项，如果没有选中项则返回 null
     */
    public UIListItem getSelectedItem() {
        if (selectedIndex >= 0 && selectedIndex < items.size()) {
            return items.get(selectedIndex);
        }
        return null;
    }

    /**
     * 获取列表项的索引（使用引用相等性）
     *
     * @param item 列表项
     * @return 索引，如果未找到则返回 -1
     */
    private int indexOfItem(UIListItem item) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) == item) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 清空选中项
     */
    public void clearSelection() {
        setSelectedIndex(-1);
    }

    public UIDirection getLayoutDirection() {
        return layoutDirection;
    }

    public void setLayoutDirection(UIDirection layoutDirection) {
        this.layoutDirection = layoutDirection;
        layoutItems();
    }

    public float getItemSpacing() {
        return itemSpacing;
    }

    public void setItemSpacing(float itemSpacing) {
        this.itemSpacing = itemSpacing;
        layoutItems();
    }

    public float getItemHeight() {
        return itemHeight;
    }

    public void setItemHeight(float itemHeight) {
        this.itemHeight = itemHeight;
        layoutItems();
    }

    public boolean isAutoSize() {
        return autoSize;
    }

    public void setAutoSize(boolean autoSize) {
        this.autoSize = autoSize;
        layoutItems();
    }

    public UIColor getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(UIColor selectedColor) {
        this.selectedColor = selectedColor;
    }

    public UIColor getItemColor() {
        return itemColor;
    }

    public void setItemColor(UIColor itemColor) {
        this.itemColor = itemColor;
    }

    public ItemClickListener getListener() {
        return listener;
    }

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        // 渲染背景
        if (getBackgroundColor() != null && getBackgroundColor().getA() > 0) {
            renderBackground();
        }

        // 渲染列表项
        for (UIListItem item : items) {
            item.render();
        }
    }

    /**
     * 渲染背景
     */
    protected void renderBackground() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 布局列表项
     */
    public void layoutItems() {
        if (items.isEmpty()) {
            return;
        }

        float currentX = getBounds().getX() + getPadding().getX();
        float currentY = getBounds().getY() + getPadding().getY();

        float maxWidth = 0;
        float maxHeight = 0;
        float listWidth = getWidth() - getPadding().getX() * 2;
        float listHeight = getHeight() - getPadding().getY() * 2;

        for (UIListItem item : items) {
            // 设置列表项大小
            item.setWidth(listWidth);
            item.setHeight(itemHeight);

            if (layoutDirection == UIDirection.HORIZONTAL) {
                item.setPosition(currentX, currentY);
                currentX += item.getWidth() + itemSpacing;
                maxWidth += item.getWidth() + itemSpacing;
                maxHeight = Math.max(maxHeight, itemHeight);
            } else {
                item.setPosition(currentX, currentY);
                currentY += itemHeight + itemSpacing;
                maxHeight += itemHeight + itemSpacing;
                maxWidth = Math.max(maxWidth, listWidth);
            }
        }

        // 如果启用自动大小，调整列表大小
        if (autoSize) {
            if (layoutDirection == UIDirection.HORIZONTAL) {
                setSize(maxWidth + getPadding().getX() * 2, listHeight);
            } else {
                setSize(listWidth, maxHeight + getPadding().getY() * 2);
            }
        }
    }

    @Override
    public void addChild(UIComponent child) {
        if (child instanceof UIListItem) {
            addItem((UIListItem) child);
        } else {
            super.addChild(child);
        }
    }

    /**
     * 创建列表项点击监听器
     *
     * @param index 列表项索引
     * @return 点击监听器
     */
    private UIListItem.ItemClickListener createItemClickListener(int index) {
        return item -> {
            if (listener != null) {
                listener.onItemClick(UIList.this, item, index);
            }
            setSelectedIndex(index);
        };
    }

    /**
     * 更新所有列表项的点击监听器
     */
    private void updateItemClickListeners() {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setClickListener(createItemClickListener(i));
        }
    }

    /**
     * 移除项后调整选中索引
     */
    private void adjustSelectedIndexAfterRemove(int removedIndex) {
        if (selectedIndex == removedIndex) {
            // 移除的是选中项，清空选中
            selectedIndex = -1;
        } else if (selectedIndex > removedIndex) {
            // 选中项在移除项之后，调整索引
            selectedIndex--;
        }
    }
}
