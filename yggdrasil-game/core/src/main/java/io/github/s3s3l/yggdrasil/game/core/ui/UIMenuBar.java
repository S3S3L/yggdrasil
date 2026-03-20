package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * UI 菜单栏组件
 * 顶部水平菜单栏，支持多级下拉菜单，支持菜单项图标和快捷键显示
 */
public class UIMenuBar extends UIComponent {

    private List<UIMenuItem> items;                  // 菜单项列表
    private UIColor backgroundColor;                 // 背景颜色
    private UIColor hoverColor;                      // 悬停颜色
    private UIColor textColor;                       // 文本颜色
    private UIColor disabledTextColor;               // 禁用文本颜色
    private float itemHeight;                        // 菜单项高度
    private float itemSpacing;                       // 菜单项间距
    private float fontSize;                          // 字体大小
    private boolean showShortcuts;                   // 显示快捷键
    private MenuListener listener;                   // 菜单监听器
    private UIMenuItem hoveredItem;                  // 当前悬停的菜单项
    private UIMenuItem openItem;                     // 当前打开的子菜单项

    /**
     * 菜单监听器接口
     */
    public interface MenuListener {
        /**
         * 当菜单项被选中时调用
         *
         * @param menuBar 菜单栏
         * @param item 被选中的菜单项
         */
        void onMenuSelected(UIMenuBar menuBar, UIMenuItem item);

        /**
         * 当菜单打开时调用
         *
         * @param menuBar 菜单栏
         * @param item 打开的菜单项
         */
        void onMenuOpened(UIMenuBar menuBar, UIMenuItem item);

        /**
         * 当菜单关闭时调用
         *
         * @param menuBar 菜单栏
         */
        void onMenuClosed(UIMenuBar menuBar);
    }

    public UIMenuBar() {
        this(new ArrayList<>());
    }

    public UIMenuBar(List<UIMenuItem> items) {
        this.items = new ArrayList<>();
        this.backgroundColor = new UIColor(0.2f, 0.2f, 0.2f, 1f);
        this.hoverColor = new UIColor(0.3f, 0.3f, 0.3f, 1f);
        this.textColor = UIColor.WHITE;
        this.disabledTextColor = UIColor.GRAY;
        this.itemHeight = 30;
        this.itemSpacing = 0;
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
        if (openItem == item) {
            openItem = null;
        }
    }

    public void removeItemAt(int index) {
        if (index >= 0 && index < items.size()) {
            UIMenuItem removed = items.remove(index);
            if (openItem == removed) {
                openItem = null;
            }
        }
    }

    public void clearItems() {
        items.clear();
        openItem = null;
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

    public UIMenuItem findItemByText(String text) {
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

    public float getItemHeight() {
        return itemHeight;
    }

    public void setItemHeight(float itemHeight) {
        this.itemHeight = itemHeight;
    }

    public float getItemSpacing() {
        return itemSpacing;
    }

    public void setItemSpacing(float itemSpacing) {
        this.itemSpacing = itemSpacing;
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

    /**
     * 添加带动作的菜单项
     *
     * @param text 菜单文本
     * @param action 点击动作
     * @return 创建的菜单项
     */
    public UIMenuItem addMenuItem(String text, Runnable action) {
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
     * 添加分隔线
     */
    public void addSeparator() {
        addItem(UIMenuItem.separator());
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

        if (item.hasSubmenu()) {
            // 打开/关闭子菜单
            if (openItem == item) {
                closeMenu();
            } else {
                openMenu(item);
            }
        } else {
            // 执行动作
            item.doAction();
            if (listener != null) {
                listener.onMenuSelected(this, item);
            }
            closeMenu();
        }
    }

    /**
     * 处理菜单项悬停
     *
     * @param item 悬停的菜单项
     */
    public void handleMenuItemHover(UIMenuItem item) {
        hoveredItem = item;
        // 如果有打开的子菜单且悬停到另一个有子菜单的项，打开新的子菜单
        if (openItem != null && item != null && item.hasSubmenu() && item.isEnabled()) {
            openMenu(item);
        }
    }

    /**
     * 打开菜单
     *
     * @param item 要打开的菜单项
     */
    public void openMenu(UIMenuItem item) {
        if (item == null || !item.isEnabled()) {
            return;
        }
        openItem = item;
        if (listener != null) {
            listener.onMenuOpened(this, item);
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (openItem != null) {
            openItem = null;
            if (listener != null) {
                listener.onMenuClosed(this);
            }
        }
    }

    /**
     * 切换菜单
     *
     * @param item 菜单项
     */
    public void toggleMenu(UIMenuItem item) {
        if (openItem == item) {
            closeMenu();
        } else {
            openMenu(item);
        }
    }

    /**
     * 获取当前打开的菜单项
     *
     * @return 打开的菜单项，如果没有则返回 null
     */
    public UIMenuItem getOpenItem() {
        return openItem;
    }

    /**
     * 获取当前悬停的菜单项
     *
     * @return 悬停的菜单项，如果没有则返回 null
     */
    public UIMenuItem getHoveredItem() {
        return hoveredItem;
    }

    /**
     * 清除悬停状态
     */
    public void clearHover() {
        hoveredItem = null;
    }

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        // 渲染菜单栏背景
        renderMenuBarBackground();

        // 渲染菜单项
        renderMenuItems();

        // 渲染打开的子菜单
        if (openItem != null) {
            renderSubmenu(openItem);
        }
    }

    /**
     * 渲染菜单栏背景
     */
    protected void renderMenuBarBackground() {
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
     * 获取菜单栏的方向（水平）
     *
     * @return 水平方向
     */
    public boolean isHorizontal() {
        return true;
    }

    /**
     * 计算菜单栏所需的宽度
     *
     * @return 所需宽度
     */
    public float getRequiredWidth() {
        // 简单计算：所有菜单项宽度之和 + 间距
        // 实际实现可能需要根据字体和图标计算
        return items.size() * (100 + itemSpacing);
    }

    /**
     * 计算菜单栏所需的高度
     *
     * @return 所需高度
     */
    public float getRequiredHeight() {
        return itemHeight;
    }
}
