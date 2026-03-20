package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * UI 树形列表节点
 * 用于 UITreeList 中的单个节点
 */
public class UITreeNode extends UIComponent {

    private String text;
    private UIColor textColor;
    private boolean expanded;               // 是否展开
    private boolean selectable;             // 是否可选择
    private boolean selected;               // 是否被选中
    private int level;                      // 层级深度
    private Object data;                    // 附加数据
    private List<UITreeNode> children;      // 子节点列表
    private UITreeNode parent;              // 父节点
    private TreeNodeListener listener;      // 节点监听器
    private boolean hasChildren;            // 是否有子节点（即使子节点未加载）

    /**
     * 树形节点监听器接口
     */
    public interface TreeNodeListener {
        /**
         * 当节点展开状态改变时调用
         *
         * @param node 节点
         * @param isExpanded 是否展开
         */
        void onExpandedChanged(UITreeNode node, boolean isExpanded);

        /**
         * 当节点被选中时调用
         *
         * @param node 节点
         * @param isSelected 是否选中
         */
        void onSelectedChanged(UITreeNode node, boolean isSelected);

        /**
         * 当节点被点击时调用
         *
         * @param node 节点
         */
        void onClick(UITreeNode node);
    }

    public UITreeNode() {
        this("");
    }

    public UITreeNode(String text) {
        this.text = text;
        this.textColor = UIColor.WHITE;
        this.expanded = false;
        this.selectable = true;
        this.selected = false;
        this.level = 0;
        this.children = new ArrayList<>();
        this.hasChildren = false;
    }

    public UITreeNode(String text, Object data) {
        this(text);
        this.data = data;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UIColor getTextColor() {
        return textColor;
    }

    public void setTextColor(UIColor textColor) {
        this.textColor = textColor;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        boolean changed = this.expanded != expanded;
        this.expanded = expanded;
        if (changed && listener != null) {
            listener.onExpandedChanged(this, expanded);
        }
    }

    /**
     * 切换展开/收起状态
     */
    public void toggle() {
        setExpanded(!expanded);
    }

    /**
     * 展开节点
     */
    public void expand() {
        setExpanded(true);
    }

    /**
     * 收起节点
     */
    public void collapse() {
        setExpanded(false);
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        if (selectable) {
            boolean changed = this.selected != selected;
            this.selected = selected;
            if (changed && listener != null) {
                listener.onSelectedChanged(this, selected);
            }
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 获取子节点列表
     *
     * @return 子节点列表
     */
    public List<UITreeNode> getChildNodes() {
        return children;
    }

    public void setChildren(List<UITreeNode> children) {
        this.children = children != null ? new ArrayList<>(children) : new ArrayList<>();
        updateHasChildren();
        // 设置子节点的层级
        for (UITreeNode child : this.children) {
            child.setLevel(this.level + 1);
            child.setParent(this);
        }
    }

    public UITreeNode getParent() {
        return parent;
    }

    public void setParent(UITreeNode parent) {
        this.parent = parent;
        if (parent != null) {
            this.level = parent.level + 1;
        }
    }

    public TreeNodeListener getListener() {
        return listener;
    }

    public void setListener(TreeNodeListener listener) {
        this.listener = listener;
    }

    public boolean hasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    /**
     * 添加子节点
     *
     * @param child 子节点
     */
    public void addChild(UITreeNode child) {
        if (child != null) {
            children.add(child);
            child.setLevel(this.level + 1);
            child.setParent(this);
            updateHasChildren();
        }
    }

    /**
     * 在指定位置添加子节点
     *
     * @param index 位置索引
     * @param child 子节点
     */
    public void addChild(int index, UITreeNode child) {
        if (child != null) {
            index = Math.max(0, Math.min(index, children.size()));
            children.add(index, child);
            child.setLevel(this.level + 1);
            child.setParent(this);
            updateHasChildren();
        }
    }

    /**
     * 移除子节点
     *
     * @param child 子节点
     */
    public void removeChild(UITreeNode child) {
        if (children.remove(child)) {
            child.setParent(null);
            updateHasChildren();
        }
    }

    /**
     * 移除指定位置的子节点
     *
     * @param index 位置索引
     */
    public void removeChildAt(int index) {
        if (index >= 0 && index < children.size()) {
            UITreeNode child = children.remove(index);
            child.setParent(null);
            updateHasChildren();
        }
    }

    /**
     * 清除所有子节点
     */
    public void clearChildren() {
        for (UITreeNode child : children) {
            child.setParent(null);
        }
        children.clear();
        updateHasChildren();
    }

    /**
     * 获取子节点数量
     *
     * @return 子节点数量
     */
    public int getChildCount() {
        return children.size();
    }

    /**
     * 获取指定位置的子节点
     *
     * @param index 位置索引
     * @return 子节点
     */
    public UITreeNode getChildAt(int index) {
        if (index >= 0 && index < children.size()) {
            return children.get(index);
        }
        return null;
    }

    /**
     * 查找指定文本的子节点
     *
     * @param text 文本
     * @return 找到的节点，如果没有则返回 null
     */
    public UITreeNode findChildByText(String text) {
        for (UITreeNode child : children) {
            if (text != null && text.equals(child.getText())) {
                return child;
            }
        }
        return null;
    }

    /**
     * 递归查找指定文本的节点（包括子节点）
     *
     * @param text 文本
     * @return 找到的节点，如果没有则返回 null
     */
    public UITreeNode findNodeByText(String text) {
        if (text != null && text.equals(this.text)) {
            return this;
        }
        for (UITreeNode child : children) {
            UITreeNode found = child.findNodeByText(text);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    /**
     * 递归查找指定数据的节点
     *
     * @param data 数据
     * @return 找到的节点，如果没有则返回 null
     */
    public UITreeNode findNodeByData(Object data) {
        if (data != null && data.equals(this.data)) {
            return this;
        }
        for (UITreeNode child : children) {
            UITreeNode found = child.findNodeByData(data);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    /**
     * 更新是否有子节点状态
     */
    private void updateHasChildren() {
        this.hasChildren = !children.isEmpty();
    }

    @Override
    public void onClick() {
        if (listener != null) {
            listener.onClick(this);
        }
        if (selectable) {
            setSelected(true);
        }
        // 如果有子节点，点击切换展开状态
        if (hasChildren) {
            toggle();
        }
    }

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        // 渲染背景（选中状态高亮）
        renderBackground();

        // 渲染展开/收起图标
        renderExpandIcon();

        // 渲染文本
        renderText();
    }

    /**
     * 渲染背景
     */
    protected void renderBackground() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染展开/收起图标
     */
    protected void renderExpandIcon() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染文本
     */
    protected void renderText() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 获取缩进偏移量
     *
     * @param indentSize 每级缩进大小
     * @return 缩进偏移量
     */
    public float getIndentOffset(float indentSize) {
        return level * indentSize;
    }

    /**
     * 获取所有可见的子节点（递归）
     *
     * @return 可见子节点列表
     */
    public List<UITreeNode> getVisibleChildren() {
        List<UITreeNode> result = new ArrayList<>();
        if (expanded) {
            for (UITreeNode child : children) {
                result.add(child);
                result.addAll(child.getVisibleChildren());
            }
        }
        return result;
    }

    /**
     * 展开所有子节点
     */
    public void expandAll() {
        setExpanded(true);
        for (UITreeNode child : children) {
            child.expandAll();
        }
    }

    /**
     * 收起所有子节点
     */
    public void collapseAll() {
        setExpanded(false);
        for (UITreeNode child : children) {
            child.collapseAll();
        }
    }
}
