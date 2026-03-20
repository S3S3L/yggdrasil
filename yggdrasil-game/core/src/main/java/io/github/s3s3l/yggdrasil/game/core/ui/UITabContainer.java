package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * UI 标签页容器组件
 * 多标签页容器，支持标签切换和内容面板
 */
public class UITabContainer extends UIComponent {

    private List<Tab> tabs;                   // 标签列表
    private int selectedTabIndex;             // 选中的标签索引
    private UIColor tabColor;                 // 标签颜色
    private UIColor selectedTabColor;         // 选中标签颜色
    private UIColor textColor;                // 文本颜色
    private UIColor contentColor;             // 内容区域颜色
    private float tabHeight;                  // 标签高度
    private float tabSpacing;                 // 标签间距
    private TabPosition tabPosition;          // 标签位置
    private TabListener listener;             // 标签切换监听器

    /**
     * 标签位置枚举
     */
    public enum TabPosition {
        /** 上方 */
        TOP,
        /** 下方 */
        BOTTOM,
        /** 左侧 */
        LEFT,
        /** 右侧 */
        RIGHT
    }

    /**
     * 标签切换监听器接口
     */
    public interface TabListener {
        /**
         * 当标签切换时调用
         *
         * @param container 标签容器
         * @param oldIndex 旧标签索引
         * @param newIndex 新标签索引
         */
        void onTabChanged(UITabContainer container, int oldIndex, int newIndex);
    }

    /**
     * 标签数据类
     */
    public static class Tab {
        private String text;
        private UIComponent content;
        private Object data;
        private boolean enabled;
        private TabButton button;

        public Tab(String text) {
            this(text, null);
        }

        public Tab(String text, UIComponent content) {
            this.text = text;
            this.content = content;
            this.enabled = true;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public UIComponent getContent() {
            return content;
        }

        public void setContent(UIComponent content) {
            this.content = content;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public TabButton getButton() {
            return button;
        }

        public void setButton(TabButton button) {
            this.button = button;
        }
    }

    public UITabContainer() {
        this(new ArrayList<>());
    }

    public UITabContainer(List<Tab> tabs) {
        this.tabs = tabs != null ? new ArrayList<>(tabs) : new ArrayList<>();
        this.selectedTabIndex = -1;
        this.tabColor = new UIColor(0.2f, 0.2f, 0.2f, 1f);
        this.selectedTabColor = UIColor.BLUE;
        this.textColor = UIColor.WHITE;
        this.contentColor = new UIColor(0.15f, 0.15f, 0.15f, 1f);
        this.tabHeight = 40;
        this.tabSpacing = 2;
        this.tabPosition = TabPosition.TOP;
        // 默认选中第一个标签
        if (!this.tabs.isEmpty()) {
            this.selectedTabIndex = 0;
        }
    }

    public List<Tab> getTabs() {
        return new ArrayList<>(tabs);
    }

    public void setTabs(List<Tab> tabs) {
        this.tabs = tabs != null ? new ArrayList<>(tabs) : new ArrayList<>();
        if (selectedTabIndex >= this.tabs.size()) {
            selectedTabIndex = Math.max(0, this.tabs.size() - 1);
        }
    }

    /**
     * 添加标签
     *
     * @param tab 标签
     */
    public void addTab(Tab tab) {
        if (tab != null) {
            tabs.add(tab);
            if (selectedTabIndex < 0) {
                selectedTabIndex = 0;
            }
        }
    }

    /**
     * 添加标签
     *
     * @param text 标签文本
     * @param content 内容组件
     */
    public void addTab(String text, UIComponent content) {
        addTab(new Tab(text, content));
    }

    /**
     * 在指定位置添加标签
     *
     * @param index 位置索引
     * @param tab 标签
     */
    public void addTab(int index, Tab tab) {
        if (tab != null) {
            index = Math.max(0, Math.min(index, tabs.size()));
            tabs.add(index, tab);
        }
    }

    /**
     * 移除标签
     *
     * @param tab 标签
     */
    public void removeTab(Tab tab) {
        int index = tabs.indexOf(tab);
        if (index >= 0) {
            tabs.remove(index);
            if (index == selectedTabIndex) {
                selectedTabIndex = Math.max(0, tabs.size() - 1);
            } else if (index < selectedTabIndex) {
                selectedTabIndex--;
            }
        }
    }

    /**
     * 移除指定位置的标签
     *
     * @param index 位置索引
     */
    public void removeTabAt(int index) {
        if (index >= 0 && index < tabs.size()) {
            tabs.remove(index);
            if (index == selectedTabIndex) {
                selectedTabIndex = Math.max(0, tabs.size() - 1);
            } else if (index < selectedTabIndex) {
                selectedTabIndex--;
            }
        }
    }

    /**
     * 清除所有标签
     */
    public void clearTabs() {
        tabs.clear();
        selectedTabIndex = -1;
    }

    /**
     * 获取标签数量
     *
     * @return 标签数量
     */
    public int getTabCount() {
        return tabs.size();
    }

    /**
     * 获取指定位置的标签
     *
     * @param index 位置索引
     * @return 标签
     */
    public Tab getTabAt(int index) {
        if (index >= 0 && index < tabs.size()) {
            return tabs.get(index);
        }
        return null;
    }

    /**
     * 查找指定文本的标签
     *
     * @param text 文本
     * @return 标签索引，未找到返回 -1
     */
    public int findTabByText(String text) {
        for (int i = 0; i < tabs.size(); i++) {
            if (text != null && text.equals(tabs.get(i).getText())) {
                return i;
            }
        }
        return -1;
    }

    public int getSelectedTabIndex() {
        return selectedTabIndex;
    }

    /**
     * 设置选中的标签索引
     *
     * @param index 标签索引
     */
    public void setSelectedTabIndex(int index) {
        if (index >= 0 && index < tabs.size() && index != selectedTabIndex) {
            int oldIndex = selectedTabIndex;
            selectedTabIndex = index;
            if (listener != null) {
                listener.onTabChanged(this, oldIndex, index);
            }
        }
    }

    /**
     * 设置选中的标签
     *
     * @param tab 标签
     */
    public void setSelectedTab(Tab tab) {
        int index = tabs.indexOf(tab);
        setSelectedTabIndex(index);
    }

    /**
     * 通过文本设置选中的标签
     *
     * @param text 标签文本
     * @return 是否成功设置
     */
    public boolean setSelectedTabByText(String text) {
        int index = findTabByText(text);
        if (index >= 0) {
            setSelectedTabIndex(index);
            return true;
        }
        return false;
    }

    /**
     * 获取选中的标签
     *
     * @return 选中的标签，如果没有则返回 null
     */
    public Tab getSelectedTab() {
        if (selectedTabIndex >= 0 && selectedTabIndex < tabs.size()) {
            return tabs.get(selectedTabIndex);
        }
        return null;
    }

    /**
     * 获取选中标签的内容组件
     *
     * @return 内容组件，如果没有则返回 null
     */
    public UIComponent getSelectedContent() {
        Tab selectedTab = getSelectedTab();
        if (selectedTab != null) {
            return selectedTab.getContent();
        }
        return null;
    }

    public UIColor getTabColor() {
        return tabColor;
    }

    public void setTabColor(UIColor tabColor) {
        this.tabColor = tabColor;
    }

    public UIColor getSelectedTabColor() {
        return selectedTabColor;
    }

    public void setSelectedTabColor(UIColor selectedTabColor) {
        this.selectedTabColor = selectedTabColor;
    }

    public UIColor getTextColor() {
        return textColor;
    }

    public void setTextColor(UIColor textColor) {
        this.textColor = textColor;
    }

    public UIColor getContentColor() {
        return contentColor;
    }

    public void setContentColor(UIColor contentColor) {
        this.contentColor = contentColor;
    }

    public float getTabHeight() {
        return tabHeight;
    }

    public void setTabHeight(float tabHeight) {
        this.tabHeight = tabHeight;
    }

    public float getTabSpacing() {
        return tabSpacing;
    }

    public void setTabSpacing(float tabSpacing) {
        this.tabSpacing = tabSpacing;
    }

    public TabPosition getTabPosition() {
        return tabPosition;
    }

    public void setTabPosition(TabPosition tabPosition) {
        this.tabPosition = tabPosition;
    }

    public TabListener getListener() {
        return listener;
    }

    public void setListener(TabListener listener) {
        this.listener = listener;
    }

    /**
     * 选择下一个标签
     */
    public void selectNextTab() {
        if (tabs.size() > 0) {
            int nextIndex = (selectedTabIndex + 1) % tabs.size();
            setSelectedTabIndex(nextIndex);
        }
    }

    /**
     * 选择上一个标签
     */
    public void selectPreviousTab() {
        if (tabs.size() > 0) {
            int prevIndex = selectedTabIndex - 1;
            if (prevIndex < 0) {
                prevIndex = tabs.size() - 1;
            }
            setSelectedTabIndex(prevIndex);
        }
    }

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        // 渲染内容区域
        renderContentArea();

        // 渲染标签
        for (int i = 0; i < tabs.size(); i++) {
            renderTab(i);
        }

        // 渲染选中标签的内容
        UIComponent content = getSelectedContent();
        if (content != null) {
            content.render();
        }
    }

    /**
     * 渲染内容区域
     */
    protected void renderContentArea() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染标签
     *
     * @param index 标签索引
     */
    protected void renderTab(int index) {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 处理标签点击
     *
     * @param index 标签索引
     */
    public void handleTabClick(int index) {
        Tab tab = getTabAt(index);
        if (tab != null && tab.isEnabled()) {
            setSelectedTabIndex(index);
        }
    }

    /**
     * 布局标签和內容
     */
    public void layout() {
        // 根据 tabPosition 计算标签和内容区域的位置和大小
        float contentX = getX();
        float contentY = getY();
        float contentWidth = getWidth();
        float contentHeight = getHeight();

        switch (tabPosition) {
            case TOP:
                contentY += tabHeight;
                contentHeight -= tabHeight;
                break;
            case BOTTOM:
                contentHeight -= tabHeight;
                break;
            case LEFT:
                contentX += tabHeight;
                contentWidth -= tabHeight;
                break;
            case RIGHT:
                contentWidth -= tabHeight;
                break;
        }

        // 设置内容区域大小
        // 实际布局由渲染层实现
    }
}
