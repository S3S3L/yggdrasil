package io.github.s3s3l.yggdrasil.game.core.layout;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 布局节点，持有样式、计算结果以及树形关系。
 */
public class LayoutNode extends AbstractLayout {
    /** 父节点 */
    private LayoutNode parent;
    /** 子节点集合，保序 */
    private final Set<LayoutNode> children = new LinkedHashSet<>();
    private final Set<LayoutNode> childrenView = Collections.unmodifiableSet(children);
    /** 原始样式 */
    private LayoutStyle style = new LayoutStyle();
    /** 计算后的样式（像素值） */
    private ComputedStyle computedStyle;
    /** 布局计算结果 */
    private LayoutResult layoutResult;
    /** 调试/标识用名称 */
    private String name;

    public LayoutNode() {
    }

    public LayoutNode(LayoutStyle style) {
        this.style = style == null ? new LayoutStyle() : style;
    }

    public LayoutNode(String name, LayoutStyle style) {
        this.name = name;
        this.style = style == null ? new LayoutStyle() : style;
    }

    @Override
    public LayoutNode parent() {
        return parent;
    }

    @Override
    public void setParent(Layout parent) {
        if (parent != null && !(parent instanceof LayoutNode)) {
            throw new IllegalArgumentException("parent must be LayoutNode");
        }
        this.parent = (LayoutNode) parent;
    }

    @Override
    public Set<Layout> children() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(children));
    }

    @Override
    public void addChild(Layout child) {
        if (child == null) {
            return;
        }
        if (!(child instanceof LayoutNode)) {
            throw new IllegalArgumentException("child must be LayoutNode");
        }
        LayoutNode node = (LayoutNode) child;
        if (node == this || isDescendantOf(node)) {
            throw new IllegalArgumentException("a layout node cannot contain itself or an ancestor");
        }
        if (node.parent != null && node.parent != this) {
            node.parent.removeChild(node);
        }
        children.add(node);
        node.setParent(this);
    }

    public void addChild(LayoutNode child) {
        addChild((Layout) child);
    }

    /**
     * Removes a child and clears its parent when this node owns it.
     */
    public void removeChild(Layout child) {
        if (!(child instanceof LayoutNode)) {
            return;
        }
        LayoutNode node = (LayoutNode) child;
        if (children.remove(node) && node.parent == this) {
            node.setParent(null);
        }
    }

    public void removeChild(LayoutNode child) {
        removeChild((Layout) child);
    }

    private boolean isDescendantOf(LayoutNode possibleAncestor) {
        for (LayoutNode node = this; node != null; node = node.parent) {
            if (node == possibleAncestor) {
                return true;
            }
        }
        return false;
    }

    public Set<LayoutNode> childNodes() {
        return childrenView;
    }

    /** Ordered live child view for layout-node subclasses that avoid snapshot allocation during traversal. */
    protected final Set<LayoutNode> childNodesInOrder() {
        return childrenView;
    }

    public LayoutStyle getStyle() {
        return style;
    }

    public void setStyle(LayoutStyle style) {
        this.style = style == null ? new LayoutStyle() : style;
    }

    public ComputedStyle getComputedStyle() {
        return computedStyle;
    }

    public void setComputedStyle(ComputedStyle computedStyle) {
        this.computedStyle = computedStyle;
    }

    public LayoutResult getLayoutResult() {
        return layoutResult;
    }

    public void setLayoutResult(LayoutResult layoutResult) {
        this.layoutResult = layoutResult;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
