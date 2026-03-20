package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * UI 树形列表组件
 * 用于显示和管理树形结构的节点列表
 */
public class UITreeList extends UIComponent {

    private List<UITreeNode> roots;               // 根节点列表
    private float indentSize;                     // 每级缩进大小
    private float rowHeight;                      // 每行高度
    private UIColor textColor;                    // 文本颜色
    private UIColor selectedColor;                // 选中背景颜色
    private UIColor hoverColor;                   // 悬停背景颜色
    private TreeListListener listener;            // 监听器
    private UITreeNode hoveredNode;               // 当前悬停的节点
    private boolean showLines;                    // 是否显示连接线

    /**
     * 树形列表监听器接口
     */
    public interface TreeListListener {
        /**
         * 当节点被选中时调用
         *
         * @param node 被选中的节点
         */
        void onNodeSelected(UITreeNode node);

        /**
         * 当节点展开状态改变时调用
         *
         * @param node 节点
         * @param isExpanded 是否展开
         */
        void onNodeExpanded(UITreeNode node, boolean isExpanded);

        /**
         * 当节点被双击时调用
         *
         * @param node 节点
         */
        void onNodeDoubleClicked(UITreeNode node);
    }

    public UITreeList() {
        this.roots = new ArrayList<>();
        this.indentSize = 20;
        this.rowHeight = 30;
        this.textColor = UIColor.WHITE;
        this.selectedColor = new UIColor(0.2f, 0.4f, 0.6f, 0.8f);
        this.hoverColor = new UIColor(0.3f, 0.3f, 0.3f, 0.5f);
        this.showLines = true;
    }

    public List<UITreeNode> getRoots() {
        return roots;
    }

    public void setRoots(List<UITreeNode> roots) {
        this.roots = roots != null ? new ArrayList<>(roots) : new ArrayList<>();
        updateNodeLevels();
    }

    public float getIndentSize() {
        return indentSize;
    }

    public void setIndentSize(float indentSize) {
        this.indentSize = indentSize;
    }

    public float getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(float rowHeight) {
        this.rowHeight = rowHeight;
    }

    public UIColor getTextColor() {
        return textColor;
    }

    public void setTextColor(UIColor textColor) {
        this.textColor = textColor;
    }

    public UIColor getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(UIColor selectedColor) {
        this.selectedColor = selectedColor;
    }

    public UIColor getHoverColor() {
        return hoverColor;
    }

    public void setHoverColor(UIColor hoverColor) {
        this.hoverColor = hoverColor;
    }

    public TreeListListener getListener() {
        return listener;
    }

    public void setListener(TreeListListener listener) {
        this.listener = listener;
    }

    public boolean isShowLines() {
        return showLines;
    }

    public void setShowLines(boolean showLines) {
        this.showLines = showLines;
    }

    /**
     * 添加根节点
     *
     * @param node 节点
     */
    public void addRoot(UITreeNode node) {
        if (node != null) {
            roots.add(node);
            node.setLevel(0);
            node.setParent(null);
            updateChildLevelsRecursive(node, 1);
        }
    }

    /**
     * 在指定位置添加根节点
     *
     * @param index 位置索引
     * @param node 节点
     */
    public void addRoot(int index, UITreeNode node) {
        if (node != null) {
            index = Math.max(0, Math.min(index, roots.size()));
            roots.add(index, node);
            node.setLevel(0);
            node.setParent(null);
        }
    }

    /**
     * 移除根节点
     *
     * @param node 节点
     */
    public void removeRoot(UITreeNode node) {
        if (roots.remove(node)) {
            node.setParent(null);
        }
    }

    /**
     * 移除指定位置的根节点
     *
     * @param index 位置索引
     */
    public void removeRootAt(int index) {
        if (index >= 0 && index < roots.size()) {
            UITreeNode node = roots.remove(index);
            node.setParent(null);
        }
    }

    /**
     * 清除所有根节点
     */
    public void clear() {
        for (UITreeNode node : roots) {
            node.setParent(null);
        }
        roots.clear();
    }

    /**
     * 获取根节点数量
     *
     * @return 根节点数量
     */
    public int getRootCount() {
        return roots.size();
    }

    /**
     * 获取指定位置的根节点
     *
     * @param index 位置索引
     * @return 根节点
     */
    public UITreeNode getRootAt(int index) {
        if (index >= 0 && index < roots.size()) {
            return roots.get(index);
        }
        return null;
    }

    /**
     * 查找指定文本的节点
     *
     * @param text 文本
     * @return 找到的节点，如果没有则返回 null
     */
    public UITreeNode findNodeByText(String text) {
        for (UITreeNode root : roots) {
            UITreeNode found = root.findNodeByText(text);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    /**
     * 查找指定数据的节点
     *
     * @param data 数据
     * @return 找到的节点，如果没有则返回 null
     */
    public UITreeNode findNodeByData(Object data) {
        for (UITreeNode root : roots) {
            UITreeNode found = root.findNodeByData(data);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    /**
     * 获取所有可见的节点（包括根节点）
     *
     * @return 可见节点列表
     */
    public List<UITreeNode> getAllVisibleNodes() {
        List<UITreeNode> result = new ArrayList<>();
        for (UITreeNode root : roots) {
            result.add(root);
            result.addAll(root.getVisibleChildren());
        }
        return result;
    }

    /**
     * 获取指定节点在可见节点列表中的索引
     *
     * @param node 节点
     * @return 索引，如果节点不可见则返回 -1
     */
    public int getVisibleIndex(UITreeNode node) {
        List<UITreeNode> visibleNodes = getAllVisibleNodes();
        for (int i = 0; i < visibleNodes.size(); i++) {
            if (visibleNodes.get(i) == node) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 展开所有节点
     */
    public void expandAll() {
        for (UITreeNode root : roots) {
            root.expandAll();
        }
    }

    /**
     * 收起所有节点
     */
    public void collapseAll() {
        for (UITreeNode root : roots) {
            root.collapseAll();
        }
    }

    /**
     * 选中指定节点
     *
     * @param node 节点
     */
    public void selectNode(UITreeNode node) {
        if (node != null && node.isSelectable()) {
            // 取消其他节点的选择
            deselectAll();
            node.setSelected(true);
            if (listener != null) {
                listener.onNodeSelected(node);
            }
        }
    }

    /**
     * 取消所有节点的选择
     */
    public void deselectAll() {
        for (UITreeNode root : roots) {
            deselectNodeRecursive(root);
        }
    }

    /**
     * 递归取消节点选择
     *
     * @param node 节点
     */
    private void deselectNodeRecursive(UITreeNode node) {
        node.setSelected(false);
        for (UITreeNode child : node.getChildNodes()) {
            deselectNodeRecursive(child);
        }
    }

    /**
     * 更新所有节点的层级
     */
    private void updateNodeLevels() {
        for (UITreeNode root : roots) {
            root.setLevel(0);
            updateChildLevelsRecursive(root, 1);
        }
    }

    /**
     * 递归更新子节点层级
     *
     * @param node 节点
     * @param level 层级
     */
    private void updateChildLevelsRecursive(UITreeNode node, int level) {
        for (UITreeNode child : node.getChildNodes()) {
            child.setLevel(level);
            child.setParent(node);
            updateChildLevelsRecursive(child, level + 1);
        }
    }

    /**
     * 处理节点点击
     *
     * @param node 被点击的节点
     */
    protected void onNodeClick(UITreeNode node) {
        if (node != null) {
            selectNode(node);
            node.onClick();
        }
    }

    /**
     * 处理节点双击
     *
     * @param node 被双击的节点
     */
    protected void onNodeDoubleClick(UITreeNode node) {
        if (node != null && listener != null) {
            listener.onNodeDoubleClicked(node);
        }
    }

    /**
     * 获取节点对应的行 Y 坐标
     *
     * @param node 节点
     * @return Y 坐标
     */
    public float getNodeY(UITreeNode node) {
        int index = getVisibleIndex(node);
        if (index >= 0) {
            return getY() + getHeight() - (index + 1) * rowHeight;
        }
        return getY();
    }

    /**
     * 获取指定 Y 坐标处的节点
     *
     * @param y Y 坐标
     * @return 节点，如果没有则返回 null
     */
    public UITreeNode getNodeAtY(float y) {
        List<UITreeNode> visibleNodes = getAllVisibleNodes();
        // 计算行索引 - 使用与 getNodeY 相反的公式
        // getNodeY: y = getY() + getHeight() - (index + 1) * rowHeight
        // 反推：index = (getY() + getHeight() - y) / rowHeight - 1
        int index = (int) ((getY() + getHeight() - y) / rowHeight) - 1;
        if (index >= 0 && index < visibleNodes.size()) {
            return visibleNodes.get(index);
        }
        return null;
    }

    /**
     * 处理点击事件
     *
     * @param x X 坐标
     * @param y Y 坐标
     */
    public void handleClick(float x, float y) {
        // 获取点击位置的节点
        UITreeNode node = getNodeAtY(y);
        if (node != null) {
            onNodeClick(node);
        }
    }

    /**
     * 处理双击事件
     *
     * @param x X 坐标
     * @param y Y 坐标
     */
    public void handleDoubleClick(float x, float y) {
        UITreeNode node = getNodeAtY(y);
        if (node != null) {
            onNodeDoubleClick(node);
        }
    }

    /**
     * 处理鼠标移动事件
     *
     * @param x X 坐标
     * @param y Y 坐标
     */
    public void handleMouseMove(float x, float y) {
        UITreeNode node = getNodeAtY(y);
        if (node != hoveredNode) {
            hoveredNode = node;
        }
    }

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        // 渲染背景
        renderBackground();

        // 渲染所有可见节点
        renderNodes();

        // 渲染连接线
        if (showLines) {
            renderLines();
        }
    }

    /**
     * 渲染背景
     */
    protected void renderBackground() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染所有节点
     */
    protected void renderNodes() {
        List<UITreeNode> visibleNodes = getAllVisibleNodes();
        float y = getY() + getHeight() - rowHeight;

        for (UITreeNode node : visibleNodes) {
            // 计算节点的渲染位置（带缩进）
            float indentOffset = node.getIndentOffset(indentSize);
            float x = getX() + indentOffset;

            // 渲染节点（实际渲染由游戏引擎实现）
            renderNode(node, x, y);

            y -= rowHeight;
        }
    }

    /**
     * 渲染单个节点
     *
     * @param node 节点
     * @param x X 坐标
     * @param y Y 坐标
     */
    protected void renderNode(UITreeNode node, float x, float y) {
        // 实际渲染由游戏引擎实现
        // 包括背景（选中/悬停）、展开/收起图标、文本等
    }

    /**
     * 渲染连接线
     */
    protected void renderLines() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 获取推荐的宽度
     *
     * @return 推荐宽度
     */
    public float getPreferredWidth() {
        float maxWidth = 0;
        List<UITreeNode> visibleNodes = getAllVisibleNodes();

        for (UITreeNode node : visibleNodes) {
            float indentOffset = node.getIndentOffset(indentSize);
            float textWidth = node.getText().length() * 10; // 估算文本宽度
            float totalWidth = indentOffset + textWidth + 20; // 加上图标和边距
            maxWidth = Math.max(maxWidth, totalWidth);
        }

        return maxWidth;
    }

    /**
     * 获取推荐的高度
     *
     * @return 推荐高度
     */
    public float getPreferredHeight() {
        return getAllVisibleNodes().size() * rowHeight;
    }
}
