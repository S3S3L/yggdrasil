package io.github.s3s3l.yggdrasil.game.core.ui;

import io.github.s3s3l.yggdrasil.game.core.basic.ID;
import io.github.s3s3l.yggdrasil.game.core.ui.animation.AnimationManager;
import io.github.s3s3l.yggdrasil.game.core.ui.animation.UIAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * UI 组件基类
 * 所有 UI 元素的基础类
 */
public abstract class UIComponent implements ID {

    private long numericId;
    private String name;
    private String tag;
    private final UIRect bounds;
    private final UIRect padding;
    private final UIRect margin;
    private UIComponent parent;
    private final List<UIComponent> children;
    private UIAlign align;
    private UIAnchor anchor;
    private UIVisibility visibility;
    private boolean enabled;
    private boolean interactable;
    private UIColor backgroundColor;
    private float opacity;
    private AnimationManager animationManager;

    protected UIComponent() {
        this.bounds = new UIRect();
        this.padding = new UIRect();
        this.margin = new UIRect();
        this.children = new ArrayList<>();
        this.align = UIAlign.TOP_LEFT;
        this.anchor = UIAnchor.TOP_LEFT;
        this.visibility = UIVisibility.VISIBLE;
        this.enabled = true;
        this.interactable = true;
        this.backgroundColor = UIColor.TRANSPARENT;
        this.opacity = 1f;
        this.animationManager = new AnimationManager(this);
    }

    protected UIComponent(String name) {
        this();
        this.name = name;
    }

    @Override
    public long getId() {
        return numericId;
    }

    @Override
    public void setId(long id) {
        this.numericId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public UIRect getBounds() {
        return bounds;
    }

    public float getX() {
        return bounds.getX();
    }

    public void setX(float x) {
        bounds.setX(x);
    }

    public float getY() {
        return bounds.getY();
    }

    public void setY(float y) {
        bounds.setY(y);
    }

    public float getWidth() {
        return bounds.getWidth();
    }

    public void setWidth(float width) {
        bounds.setWidth(width);
    }

    public float getHeight() {
        return bounds.getHeight();
    }

    public void setHeight(float height) {
        bounds.setHeight(height);
    }

    public void setPosition(float x, float y) {
        bounds.setX(x);
        bounds.setY(y);
    }

    public void setSize(float width, float height) {
        bounds.setWidth(width);
        bounds.setHeight(height);
    }

    public void setBounds(float x, float y, float width, float height) {
        bounds.setX(x);
        bounds.setY(y);
        bounds.setWidth(width);
        bounds.setHeight(height);
    }

    public void setBounds(UIRect rect) {
        bounds.setX(rect.getX());
        bounds.setY(rect.getY());
        bounds.setWidth(rect.getWidth());
        bounds.setHeight(rect.getHeight());
    }

    public UIRect getPadding() {
        return padding;
    }

    public UIRect getMargin() {
        return margin;
    }

    public UIComponent getParent() {
        return parent;
    }

    public void setParent(UIComponent parent) {
        this.parent = parent;
    }

    public List<UIComponent> getChildren() {
        return children;
    }

    public void addChild(UIComponent child) {
        children.add(child);
        child.setParent(this);
    }

    public void removeChild(UIComponent child) {
        children.remove(child);
        child.setParent(null);
    }

    public void removeAllChildren() {
        for (UIComponent child : children) {
            child.setParent(null);
        }
        children.clear();
    }

    public UIAlign getAlign() {
        return align;
    }

    public void setAlign(UIAlign align) {
        this.align = align;
    }

    public UIAnchor getAnchor() {
        return anchor;
    }

    public void setAnchor(UIAnchor anchor) {
        this.anchor = anchor;
    }

    public UIVisibility getVisibility() {
        return visibility;
    }

    public void setVisibility(UIVisibility visibility) {
        this.visibility = visibility;
    }

    public boolean isVisible() {
        return visibility == UIVisibility.VISIBLE;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isInteractable() {
        return interactable;
    }

    public void setInteractable(boolean interactable) {
        this.interactable = interactable;
    }

    public UIColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(UIColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = Math.max(0, Math.min(1, opacity));
    }

    /**
     * 检查点是否在组件内
     *
     * @param x 点 X 坐标
     * @param y 点 Y 坐标
     * @return 是否在组件内
     */
    public boolean containsPoint(float x, float y) {
        return bounds.contains(x, y);
    }

    /**
     * 查找包含指定点的子组件
     *
     * @param x 点 X 坐标
     * @param y 点 Y 坐标
     * @return 包含点的组件，如果没有则返回 null
     */
    public UIComponent findComponentAtPoint(float x, float y) {
        if (!isVisible() || !containsPoint(x, y)) {
            return null;
        }

        // 从后向前遍历子组件（后面的组件优先）
        for (int i = children.size() - 1; i >= 0; i--) {
            UIComponent child = children.get(i);
            UIComponent found = child.findComponentAtPoint(x, y);
            if (found != null) {
                return found;
            }
        }

        return this;
    }

    /**
     * 根据名称查找子组件
     *
     * @param name 组件名称
     * @return 找到的组件，如果没有则返回 null
     */
    public UIComponent findChildByName(String name) {
        for (UIComponent child : children) {
            if (name.equals(child.name)) {
                return child;
            }
        }
        return null;
    }

    /**
     * 根据标签查找子组件
     *
     * @param tag 组件标签
     * @return 找到的组件列表
     */
    public List<UIComponent> findChildrenByTag(String tag) {
        List<UIComponent> result = new ArrayList<>();
        for (UIComponent child : children) {
            if (tag.equals(child.tag)) {
                result.add(child);
            }
        }
        return result;
    }

    /**
     * 根据类型查找子组件
     *
     * @param clazz 组件类型
     * @param <T> 组件类型
     * @return 找到的组件列表
     */
    public <T extends UIComponent> List<T> findChildrenByType(Class<T> clazz) {
        List<T> result = new ArrayList<>();
        for (UIComponent child : children) {
            if (clazz.isInstance(child)) {
                result.add(clazz.cast(child));
            }
        }
        return result;
    }

    /**
     * 更新组件
     *
     * @param deltaTime 时间增量
     */
    public void update(float deltaTime) {
        if (animationManager != null) {
            animationManager.update(deltaTime);
        }
        for (UIComponent child : children) {
            child.update(deltaTime);
        }
    }

    // ==================== 动画相关方法 ====================

    /**
     * 添加动画
     *
     * @param action 动画动作
     */
    public void addAction(UIAction action) {
        if (animationManager != null) {
            animationManager.addAction(action);
        }
    }

    /**
     * 移除动画
     *
     * @param action 动画动作
     */
    public void removeAction(UIAction action) {
        if (animationManager != null) {
            animationManager.removeAction(action);
        }
    }

    /**
     * 清除所有动画
     */
    public void clearActions() {
        if (animationManager != null) {
            animationManager.clearActions();
        }
    }

    /**
     * 检查是否有任何动画在运行
     *
     * @return true 如果有动画在运行
     */
    public boolean hasRunningActions() {
        return animationManager != null && animationManager.hasRunningActions();
    }

    /**
     * 获取动画管理器
     *
     * @return 动画管理器
     */
    public AnimationManager getAnimationManager() {
        return animationManager;
    }

    /**
     * 运行动画
     *
     * @param action 动画动作
     */
    public void runAction(UIAction action) {
        addAction(action);
    }

    /**
     * 停止所有动画
     */
    public void stopAllActions() {
        if (animationManager != null) {
            clearActions();
        }
    }

    /**
     * 渲染组件
     */
    public abstract void render();

    /**
     * 组件被点击时调用
     */
    public void onClick() {
        // 子类可以重写
    }

    /**
     * 组件获得焦点时调用
     */
    public void onFocus() {
        // 子类可以重写
    }

    /**
     * 组件失去焦点时调用
     */
    public void onBlur() {
        // 子类可以重写
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UIComponent that = (UIComponent) o;
        return Objects.equals(numericId, that.numericId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numericId);
    }
}
