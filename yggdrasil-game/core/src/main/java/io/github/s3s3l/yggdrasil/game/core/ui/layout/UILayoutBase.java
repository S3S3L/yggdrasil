package io.github.s3s3l.yggdrasil.game.core.ui.layout;

import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * UI 布局基类
 * 所有布局管理器的基础类
 */
public abstract class UILayoutBase implements ILayout {

    protected UIComponent parent;
    protected List<UIComponent> children;
    protected boolean dirty;

    protected float layoutWidth;
    protected float layoutHeight;
    protected float paddingLeft;
    protected float paddingRight;
    protected float paddingTop;
    protected float paddingBottom;

    public UILayoutBase() {
        this.children = new ArrayList<>();
        this.dirty = true;
        this.layoutWidth = 0;
        this.layoutHeight = 0;
        this.paddingLeft = 0;
        this.paddingRight = 0;
        this.paddingTop = 0;
        this.paddingBottom = 0;
    }

    /**
     * 设置父组件
     */
    public void setParent(UIComponent parent) {
        this.parent = parent;
    }

    /**
     * 获取父组件
     */
    public UIComponent getParent() {
        return parent;
    }

    /**
     * 添加子组件
     */
    public void addChild(UIComponent child) {
        if (child != null && !children.contains(child)) {
            children.add(child);
            markDirty();
        }
    }

    /**
     * 移除子组件
     */
    public void removeChild(UIComponent child) {
        if (children.remove(child)) {
            markDirty();
        }
    }

    /**
     * 清除所有子组件
     */
    public void clearChildren() {
        children.clear();
        markDirty();
    }

    /**
     * 获取子组件列表
     */
    public List<UIComponent> getChildren() {
        return new ArrayList<>(children);
    }

    /**
     * 获取子组件数量
     */
    public int getChildCount() {
        return children.size();
    }

    /**
     * 获取指定索引的子组件
     */
    public UIComponent getChildAt(int index) {
        if (index >= 0 && index < children.size()) {
            return children.get(index);
        }
        return null;
    }

    /**
     * 设置布局宽度
     */
    public void setLayoutWidth(float width) {
        this.layoutWidth = width;
        markDirty();
    }

    /**
     * 设置布局高度
     */
    public void setLayoutHeight(float height) {
        this.layoutHeight = height;
        markDirty();
    }

    /**
     * 获取布局宽度
     */
    public float getLayoutWidth() {
        return layoutWidth;
    }

    /**
     * 获取布局高度
     */
    public float getLayoutHeight() {
        return layoutHeight;
    }

    /**
     * 设置内边距
     */
    public void setPadding(float left, float top, float right, float bottom) {
        this.paddingLeft = left;
        this.paddingTop = top;
        this.paddingRight = right;
        this.paddingBottom = bottom;
        markDirty();
    }

    /**
     * 设置统一内边距
     */
    public void setPadding(float padding) {
        setPadding(padding, padding, padding, padding);
    }

    /**
     * 标记布局为脏（需要重新布局）
     */
    public void markDirty() {
        dirty = true;
    }

    /**
     * 清除脏标记
     */
    public void clearDirty() {
        dirty = false;
    }

    /**
     * 检查是否需要重新布局
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * 执行布局
     * @param parent 父组件
     * @param width 布局宽度
     * @param height 布局高度
     */
    @Override
    public void layout(UIComponent parent, float width, float height) {
        this.parent = parent;
        this.layoutWidth = width;
        this.layoutHeight = height;
        onLayout(width, height);
        clearDirty();
    }

    /**
     * 测量子组件所需空间
     * @param parent 父组件
     * @return 所需的尺寸 [width, height]
     */
    @Override
    public float[] measure(UIComponent parent) {
        this.parent = parent;
        return onMeasure();
    }

    /**
     * 执行布局（不设置父组件的简化版本）
     * @param width 布局宽度
     * @param height 布局高度
     */
    public void layout(float width, float height) {
        this.layoutWidth = width;
        this.layoutHeight = height;
        onLayout(width, height);
        clearDirty();
    }

    /**
     * 子类实现具体布局逻辑
     */
    protected abstract void onLayout(float width, float height);

    /**
     * 子类实现测量逻辑
     */
    protected abstract float[] onMeasure();

    /**
     * 获取子组件的布局参数
     */
    protected LayoutParams getChildLayoutParams(UIComponent child) {
        if (child instanceof LayoutParamsProvider) {
            return ((LayoutParamsProvider) child).getLayoutParams();
        }
        return new LayoutParams();
    }

    /**
     * 设置子组件的布局参数
     */
    protected void setChildLayoutParams(UIComponent child, LayoutParams params) {
        if (child instanceof LayoutParamsProvider) {
            ((LayoutParamsProvider) child).setLayoutParams(params);
        }
    }

    /**
     * 布局参数提供者接口
     */
    public interface LayoutParamsProvider {
        LayoutParams getLayoutParams();
        void setLayoutParams(LayoutParams params);
    }

    /**
     * 水平对齐方式
     */
    public enum HorizontalAlign {
        LEFT,
        CENTER,
        RIGHT,
        START,
        END,
        SPACE_BETWEEN,
        SPACE_AROUND,
        SPACE_EVENLY
    }

    /**
     * 垂直对齐方式
     */
    public enum VerticalAlign {
        TOP,
        CENTER,
        BOTTOM,
        START,
        END,
        SPACE_BETWEEN,
        SPACE_AROUND,
        SPACE_EVENLY
    }

    /**
     * 方向
     */
    public enum Direction {
        HORIZONTAL,
        VERTICAL
    }
}
