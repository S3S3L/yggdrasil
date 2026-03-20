package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * UI 菜单项
 * 用于菜单栏和上下文菜单的菜单项
 */
public class UIMenuItem {

    private String text;                           // 菜单文本
    private String icon;                           // 图标（资源路径或标识）
    private String shortcut;                       // 快捷键显示
    private List<UIMenuItem> children;             // 子菜单项
    private boolean enabled;                       // 是否启用
    private boolean checked;                       // 是否选中（复选菜单项）
    private boolean isSeparator;                   // 是否是分隔线
    private Runnable action;                       // 点击动作
    private MenuListener listener;                 // 菜单项监听器
    private Object userData;                       // 用户数据

    /**
     * 菜单项监听器接口
     */
    public interface MenuListener {
        /**
         * 当菜单项被点击时调用
         *
         * @param item 被点击的菜单项
         */
        void onMenuItemClicked(UIMenuItem item);

        /**
         * 当菜单项状态改变时调用
         *
         * @param item 菜单项
         * @param checked 新的选中状态
         */
        void onMenuItemStateChanged(UIMenuItem item, boolean checked);
    }

    /**
     * 创建普通菜单项
     */
    public UIMenuItem() {
        this("");
    }

    /**
     * 创建带文本的菜单项
     *
     * @param text 菜单文本
     */
    public UIMenuItem(String text) {
        this.text = text;
        this.icon = null;
        this.shortcut = null;
        this.children = new ArrayList<>();
        this.enabled = true;
        this.checked = false;
        this.isSeparator = false;
    }

    /**
     * 创建分隔线菜单项
     *
     * @return 分隔线菜单项
     */
    public static UIMenuItem separator() {
        UIMenuItem item = new UIMenuItem("");
        item.isSeparator = true;
        item.enabled = false;
        return item;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getShortcut() {
        return shortcut;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    public List<UIMenuItem> getChildren() {
        return children;
    }

    public void setChildren(List<UIMenuItem> children) {
        this.children = children != null ? new ArrayList<>(children) : new ArrayList<>();
    }

    public void addChild(UIMenuItem child) {
        if (child != null) {
            children.add(child);
        }
    }

    public void removeChild(UIMenuItem child) {
        children.remove(child);
    }

    public void clearChildren() {
        children.clear();
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        boolean oldChecked = this.checked;
        this.checked = checked;
        if (listener != null && oldChecked != this.checked) {
            listener.onMenuItemStateChanged(this, this.checked);
        }
    }

    /**
     * 切换选中状态
     */
    public void toggleChecked() {
        setChecked(!checked);
    }

    public boolean isSeparator() {
        return isSeparator;
    }

    public void setSeparator(boolean separator) {
        isSeparator = separator;
    }

    public Runnable getAction() {
        return action;
    }

    public void setAction(Runnable action) {
        this.action = action;
    }

    public MenuListener getListener() {
        return listener;
    }

    public void setListener(MenuListener listener) {
        this.listener = listener;
    }

    public Object getUserData() {
        return userData;
    }

    public void setUserData(Object userData) {
        this.userData = userData;
    }

    /**
     * 检查是否有子菜单
     *
     * @return true 如果有子菜单
     */
    public boolean hasSubmenu() {
        return !children.isEmpty();
    }

    /**
     * 执行菜单项的动作
     */
    public void doAction() {
        if (action != null) {
            action.run();
        }
        if (listener != null) {
            listener.onMenuItemClicked(this);
        }
    }

    /**
     * 获取菜单项深度（嵌套层级）
     *
     * @return 深度
     */
    public int getDepth() {
        return getMaxChildDepth(children, 0);
    }

    private int getMaxChildDepth(List<UIMenuItem> items, int level) {
        if (items == null || items.isEmpty()) {
            return level;
        }
        int maxDepth = level;
        for (UIMenuItem child : items) {
            int childDepth = getMaxChildDepth(child.children, level + 1);
            maxDepth = Math.max(maxDepth, childDepth);
        }
        return maxDepth;
    }

    /**
     * 查找菜单项（递归）
     *
     * @param text 菜单文本
     * @return 找到的菜单项，如果没有则返回 null
     */
    public UIMenuItem findItem(String text) {
        if (this.text != null && this.text.equals(text)) {
            return this;
        }
        for (UIMenuItem child : children) {
            UIMenuItem found = child.findItem(text);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    /**
     * 获取所有菜单项（扁平化，包括子菜单）
     *
     * @return 所有菜单项列表
     */
    public List<UIMenuItem> getAllItems() {
        List<UIMenuItem> result = new ArrayList<>();
        collectItems(this, result);
        return result;
    }

    private void collectItems(UIMenuItem item, List<UIMenuItem> result) {
        if (item != null && !item.isSeparator) {
            result.add(item);
            for (UIMenuItem child : item.children) {
                collectItems(child, result);
            }
        }
    }
}
