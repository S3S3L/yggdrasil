package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * UI 上下文菜单组件
 * 右键弹出菜单，支持菜单分组和分隔线，支持复选菜单项，支持禁用菜单项
 */
public class UIContextMenu extends UIComponent {

    private List<UIMenuItem> items;                  // 菜单项列表
    private float x, y;                              // 弹出位置
    private boolean visible;                         // 是否可见
    private float minWidth;                          // 最小宽度
    private float itemHeight;                        // 菜单项高度
    private UIColor backgroundColor;                 // 背景颜色
    private UIColor hoverColor;                      // 悬停颜色
    private UIColor textColor;                       // 文本颜色
    private UIColor disabledTextColor;               // 禁用文本颜色
    private UIColor separatorColor;                  // 分隔线颜色
    private float fontSize;                          // 字体大小
    private boolean showShortcuts;                   // 显示快捷键
    private MenuListener listener;                   // 菜单监听器
    private UIMenuItem hoveredItem;                  // 当前悬停的菜单项
    private UIComponent target;                      // 触发菜单的目标组件

    /**
     * 菜单监听器接口
     */
    public interface MenuListener {
        /**
         * 当菜单项被选中时调用
         *
         * @param menu 上下文菜单
         * @param item 被选中的菜单项
         */
        void onMenuItemSelected(UIContextMenu menu, UIMenuItem item);

        /**
         * 当菜单打开时调用
         *
         * @param menu 上下文菜单
         */
        void onMenuOpened(UIContextMenu menu);

        /**
         * 当菜单关闭时调用
         *
         * @param menu 上下文菜单
         */
        void onMenuClosed(UIContextMenu menu);
    }

    public UIContextMenu() {
        this(new ArrayList<>());
    }

    public UIContextMenu(List<UIMenuItem> items) {
        this.items = new ArrayList<>();
        this.x = 0;
        this.y = 0;
        this.visible = false;
        this.minWidth = 150;
        this.itemHeight = 25;
        this.backgroundColor = new UIColor(0.25f, 0.25f, 0.25f, 0.95f);
        this.hoverColor = new UIColor(0.35f, 0.35f, 0.35f, 1f);
        this.textColor = UIColor.WHITE;
        this.disabledTextColor = UIColor.GRAY;
        this.separatorColor = new UIColor(0.3f, 0.3f, 0.3f, 1f);
        this.fontSize = 14;
        this.showShortcuts = true;
        if (items != null) {
            this.items.addAll(items);
        }
    }

    public List<UIMenuItem> getItems() {
        return new ArrayList<>(items);
    }

    public void setItems(List<UIMenuItem> items) {
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
    }

    public void addItem(UIMenuItem item) {
        if (item != null) {
            items.add(item);
        }
    }

    public void addItem(int index, UIMenuItem item) {
        if (item != null) {
            items.add(index, item);
        }
    }

    public void removeItem(UIMenuItem item) {
        items.remove(item);
    }

    public void removeItemAt(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
        }
    }

    public void clearItems() {
        items.clear();
        hide();
    }

    public int getItemCount() {
        return items.size();
    }

    public UIMenuItem getItemAt(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        }
        return null;
    }

    /**
     * 添加菜单项
     *
     * @param text 菜单文本
     * @param action 点击动作
     * @return 创建的菜单项
     */
    public UIMenuItem addItem(String text, Runnable action) {
        UIMenuItem item = new UIMenuItem(text);
        item.setAction(action);
        addItem(item);
        return item;
    }

    /**
     * 添加带子菜单的菜单项
     *
     * @param text 菜单文本
     * @param children 子菜单项
     * @return 创建的菜单项
     */
    public UIMenuItem addMenu(String text, List<UIMenuItem> children) {
        UIMenuItem item = new UIMenuItem(text);
        item.setChildren(children);
        addItem(item);
        return item;
    }

    /**
     * 添加复选菜单项
     *
     * @param text 菜单文本
     * @param checked 初始选中状态
     * @param action 点击动作
     * @return 创建的菜单项
     */
    public UIMenuItem addCheckItem(String text, boolean checked, Runnable action) {
        UIMenuItem item = new UIMenuItem(text);
        item.setChecked(checked);
        item.setAction(action);
        addItem(item);
        return item;
    }

    /**
     * 添加分隔线
     */
    public void addSeparator() {
        addItem(UIMenuItem.separator());
    }

    /**
     * 添加分组菜单项
     *
     * @param groupItems 分组内的菜单项
     */
    public void addGroup(List<UIMenuItem> groupItems) {
        if (groupItems == null || groupItems.isEmpty()) {
            return;
        }
        // 如果不是第一个分组，先添加分隔线
        if (!items.isEmpty()) {
            addSeparator();
        }
        for (UIMenuItem item : groupItems) {
            addItem(item);
        }
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public float getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(float minWidth) {
        this.minWidth = minWidth;
    }

    public float getItemHeight() {
        return itemHeight;
    }

    public void setItemHeight(float itemHeight) {
        this.itemHeight = itemHeight;
    }

    public UIColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(UIColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public UIColor getHoverColor() {
        return hoverColor;
    }

    public void setHoverColor(UIColor hoverColor) {
        this.hoverColor = hoverColor;
    }

    public UIColor getTextColor() {
        return textColor;
    }

    public void setTextColor(UIColor textColor) {
        this.textColor = textColor;
    }

    public UIColor getDisabledTextColor() {
        return disabledTextColor;
    }

    public void setDisabledTextColor(UIColor disabledTextColor) {
        this.disabledTextColor = disabledTextColor;
    }

    public UIColor getSeparatorColor() {
        return separatorColor;
    }

    public void setSeparatorColor(UIColor separatorColor) {
        this.separatorColor = separatorColor;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public boolean isShowShortcuts() {
        return showShortcuts;
    }

    public void setShowShortcuts(boolean showShortcuts) {
        this.showShortcuts = showShortcuts;
    }

    public MenuListener getListener() {
        return listener;
    }

    public void setListener(MenuListener listener) {
        this.listener = listener;
    }

    public UIMenuItem getHoveredItem() {
        return hoveredItem;
    }

    public UIComponent getTarget() {
        return target;
    }

    public void setTarget(UIComponent target) {
        this.target = target;
    }

    /**
     * 在指定位置显示菜单
     *
     * @param x X 坐标
     * @param y Y 坐标
     */
    public void showAt(float x, float y) {
        this.x = x;
        this.y = y;
        show();
    }

    /**
     * 在目标组件位置显示菜单
     *
     * @param target 目标组件
     */
    public void showAt(UIComponent target) {
        this.target = target;
        if (target != null) {
            this.x = target.getX();
            this.y = target.getY() + target.getHeight();
        }
        show();
    }

    /**
     * 显示菜单
     */
    public void show() {
        visible = true;
        hoveredItem = null;
        if (listener != null) {
            listener.onMenuOpened(this);
        }
    }

    /**
     * 隐藏菜单
     */
    public void hide() {
        visible = false;
        hoveredItem = null;
        if (listener != null) {
            listener.onMenuClosed(this);
        }
    }

    /**
     * 切换菜单显示/隐藏
     */
    public void toggle() {
        if (visible) {
            hide();
        } else {
            show();
        }
    }

    /**
     * 切换菜单显示/隐藏在指定位置
     *
     * @param x X 坐标
     * @param y Y 坐标
     */
    public void toggleAt(float x, float y) {
        if (visible) {
            hide();
        } else {
            showAt(x, y);
        }
    }

    /**
     * 处理菜单项点击
     *
     * @param item 被点击的菜单项
     */
    public void handleMenuItemClick(UIMenuItem item) {
        if (item == null || !item.isEnabled() || item.isSeparator()) {
            return;
        }

        // 执行动作
        item.doAction();

        if (listener != null) {
            listener.onMenuItemSelected(this, item);
        }

        // 关闭菜单（除非是有子菜单）
        if (!item.hasSubmenu()) {
            hide();
        }
    }

    /**
     * 处理菜单项悬停
     *
     * @param item 悬停的菜单项
     */
    public void handleMenuItemHover(UIMenuItem item) {
        hoveredItem = item;
    }

    /**
     * 清除悬停状态
     */
    public void clearHover() {
        hoveredItem = null;
    }

    /**
     * 检查点是否在菜单内
     *
     * @param x X 坐标
     * @param y Y 坐标
     * @return 是否在菜单内
     */
    public boolean containsPoint(float x, float y) {
        if (!visible) {
            return false;
        }
        // 简单实现，实际需要计算菜单区域
        float menuWidth = Math.max(minWidth, getMaxItemWidth());
        float menuHeight = items.size() * itemHeight;
        return x >= this.x && x <= this.x + menuWidth &&
               y >= this.y && y <= this.y + menuHeight;
    }

    /**
     * 获取最大菜单项宽度
     *
     * @return 最大宽度
     */
    private float getMaxItemWidth() {
        float maxWidth = 0;
        for (UIMenuItem item : items) {
            // 简单估算，实际实现可能需要根据文本和图标计算
            float itemWidth = 100 + (item.hasSubmenu() ? 20 : 0) +
                             (item.getShortcut() != null ? 40 : 0);
            maxWidth = Math.max(maxWidth, itemWidth);
        }
        return maxWidth;
    }

    @Override
    public void render() {
        if (!visible) {
            return;
        }

        // 渲染菜单背景
        renderBackground();

        // 渲染菜单项
        renderMenuItems();

        // 渲染子菜单
        if (hoveredItem != null && hoveredItem.hasSubmenu()) {
            renderSubmenu(hoveredItem);
        }
    }

    /**
     * 渲染菜单背景
     */
    protected void renderBackground() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染菜单项
     */
    protected void renderMenuItems() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染子菜单
     *
     * @param item 父菜单项
     */
    protected void renderSubmenu(UIMenuItem item) {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 查找菜单项（递归）
     *
     * @param text 菜单文本
     * @return 找到的菜单项，如果没有则返回 null
     */
    public UIMenuItem findItem(String text) {
        for (UIMenuItem item : items) {
            UIMenuItem found = findItemRecursive(item, text);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private UIMenuItem findItemRecursive(UIMenuItem item, String text) {
        if (item.getText() != null && item.getText().equals(text)) {
            return item;
        }
        for (UIMenuItem child : item.getChildren()) {
            UIMenuItem found = findItemRecursive(child, text);
            if (found != null) {
                return found;
            }
        }
        return null;
    }
}
