package io.github.s3s3l.yggdrasil.game.core.ui;

import io.github.s3s3l.yggdrasil.game.core.basic.ID;
import io.github.s3s3l.yggdrasil.game.core.ui.animation.AnimationManager;
import io.github.s3s3l.yggdrasil.game.core.ui.animation.UIAction;
import io.github.s3s3l.yggdrasil.game.core.ui.binding.*;
import io.github.s3s3l.yggdrasil.game.core.ui.layout.ILayout;
import io.github.s3s3l.yggdrasil.game.core.ui.layout.LayoutParams;
import io.github.s3s3l.yggdrasil.game.core.ui.theme.UITheme;
import io.github.s3s3l.yggdrasil.game.core.ui.theme.UIStyle;
import io.github.s3s3l.yggdrasil.game.core.ui.theme.UIStyleManager;

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
    private ILayout layout;
    private LayoutParams layoutParams;
    private UIStyle style;
    private String styleName;

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
        this.layoutParams = new LayoutParams();
        this.style = new UIStyle();
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
        if (nameProperty != null) {
            nameProperty.setValue(name);
        }
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
        if (visibleProperty != null) {
            visibleProperty.setValue(visibility == UIVisibility.VISIBLE);
        }
    }

    public boolean isVisible() {
        return visibility == UIVisibility.VISIBLE;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabledProperty != null) {
            enabledProperty.setValue(enabled);
        }
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

    // ==================== 布局系统相关方法 ====================

    /**
     * 获取子组件的布局参数
     *
     * @param child 子组件
     * @return 布局参数
     */
    protected LayoutParams getChildLayoutParams(UIComponent child) {
        if (child == null) {
            return new LayoutParams();
        }
        return child.getLayoutParams();
    }

    /**
     * 设置布局管理器
     *
     * @param layout 布局管理器
     */
    public void setLayout(ILayout layout) {
        this.layout = layout;
        requestLayout();
    }

    /**
     * 获取布局管理器
     *
     * @return 布局管理器
     */
    public ILayout getLayout() {
        return layout;
    }

    /**
     * 获取布局参数
     *
     * @return 布局参数
     */
    public LayoutParams getLayoutParams() {
        return layoutParams;
    }

    /**
     * 设置布局参数
     *
     * @param layoutParams 布局参数
     */
    public void setLayoutParams(LayoutParams layoutParams) {
        this.layoutParams = layoutParams;
        if (parent != null && parent.layout != null) {
            parent.requestLayout();
        }
    }

    /**
     * 请求重新布局
     */
    public void requestLayout() {
        if (layout != null) {
            layout.layout(this, getWidth(), getHeight());
        }
        for (UIComponent child : children) {
            child.requestLayout();
        }
    }

    /**
     * 使布局失效，标记需要重新布局
     */
    public void invalidateLayout() {
        requestLayout();
    }

    /**
     * 添加子组件并应用布局
     */
    public void addChildWithLayout(UIComponent child) {
        children.add(child);
        child.setParent(this);
        if (layout != null) {
            requestLayout();
        }
    }

    /**
     * 移除子组件并重新布局
     */
    public void removeChildWithLayout(UIComponent child) {
        children.remove(child);
        child.setParent(null);
        if (layout != null) {
            requestLayout();
        }
    }

    /**
     * 清除所有子组件并重新布局
     */
    public void clearAllChildren() {
        for (UIComponent child : children) {
            child.setParent(null);
        }
        children.clear();
        if (layout != null) {
            requestLayout();
        }
    }

    /**
     * 更新组件，包含布局更新
     *
     * @param deltaTime 时间增量
     */
    public void updateWithLayout(float deltaTime) {
        if (layout != null) {
            layout.layout(this, getWidth(), getHeight());
        }
        if (animationManager != null) {
            animationManager.update(deltaTime);
        }
        for (UIComponent child : children) {
            child.update(deltaTime);
        }
    }

    // ==================== 数据绑定相关方法 ====================

    private final StringProperty nameProperty = new StringProperty();
    private final BooleanProperty enabledProperty = new BooleanProperty(true);
    private final BooleanProperty visibleProperty = new BooleanProperty(true);
    private boolean namePropertyInitialized = false;
    private boolean enabledPropertyInitialized = false;
    private boolean visiblePropertyInitialized = false;

    /**
     * 获取名称属性
     */
    public StringProperty nameProperty() {
        if (!namePropertyInitialized) {
            namePropertyInitialized = true;
            nameProperty.setValue(name);
            nameProperty.addListener((ov, oldVal, newVal) -> name = newVal);
        }
        return nameProperty;
    }

    /**
     * 获取启用状态属性
     */
    public BooleanProperty enabledProperty() {
        if (!enabledPropertyInitialized) {
            enabledPropertyInitialized = true;
            enabledProperty.setValue(enabled);
            enabledProperty.addListener((ov, oldVal, newVal) -> enabled = newVal);
        }
        return enabledProperty;
    }

    /**
     * 获取可见性属性
     */
    public BooleanProperty visibleProperty() {
        if (!visiblePropertyInitialized) {
            visiblePropertyInitialized = true;
            visibleProperty.setValue(isVisible());
            visibleProperty.addListener((ov, oldVal, newVal) -> {
                setVisibility(newVal ? UIVisibility.VISIBLE : UIVisibility.HIDDEN);
            });
        }
        return visibleProperty;
    }

    /**
     * 绑定名称
     */
    public void bindName(ObservableValue<String> observable) {
        nameProperty().bind(observable);
    }

    /**
     * 绑定启用状态
     */
    public void bindEnabled(ObservableValue<Boolean> observable) {
        enabledProperty().bind(observable);
    }

    /**
     * 绑定可见性
     */
    public void bindVisible(ObservableValue<Boolean> observable) {
        visibleProperty().bind(observable);
    }

    /**
     * 绑定双向名称
     */
    public void bindNameBidirectional(Property<String> property) {
        nameProperty().bindBidirectional(property);
    }

    /**
     * 绑定双向启用状态
     */
    public void bindEnabledBidirectional(Property<Boolean> property) {
        enabledProperty().bindBidirectional(property);
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

    // ==================== 主题相关方法 ====================

    /**
     * 获取组件样式
     *
     * @return 组件样式
     */
    public UIStyle getStyle() {
        return style;
    }

    /**
     * 设置组件样式
     *
     * @param style 样式
     */
    public void setStyle(UIStyle style) {
        if (style != null) {
            this.style = style.copy();
            applyStyleToComponent();
        }
    }

    /**
     * 获取样式名称
     *
     * @return 样式名称
     */
    public String getStyleName() {
        return styleName;
    }

    /**
     * 设置样式名称（从主题中获取）
     *
     * @param styleName 样式名称
     */
    public void setStyleName(String styleName) {
        this.styleName = styleName;
        applyCurrentTheme();
    }

    /**
     * 应用当前主题到组件
     */
    public void applyCurrentTheme() {
        UIStyleManager styleManager = UIStyleManager.getInstance();
        UITheme theme = styleManager.getCurrentTheme();
        if (theme != null && styleName != null) {
            UIStyle themeStyle = theme.getStyle(styleName);
            if (themeStyle != null) {
                this.style = themeStyle.copy();
                applyStyleToComponent();
            }
        }
    }

    /**
     * 应用主题
     *
     * @param theme 主题
     */
    public void applyTheme(UITheme theme) {
        if (theme != null && styleName != null) {
            UIStyle themeStyle = theme.getStyle(styleName);
            if (themeStyle != null) {
                this.style = themeStyle.copy();
                applyStyleToComponent();
            }
        }
    }

    /**
     * 将样式应用到组件属性
     * 子类可以重写以应用特定样式
     */
    protected void applyStyleToComponent() {
        if (style != null) {
            if (style.getBackgroundColor() != null) {
                this.backgroundColor = style.getBackgroundColor();
            }
            if (style.getPadding() != null) {
                this.padding.setX(style.getPadding().getX());
                this.padding.setY(style.getPadding().getY());
                this.padding.setWidth(style.getPadding().getWidth());
                this.padding.setHeight(style.getPadding().getHeight());
            }
            if (style.getMargin() != null) {
                this.margin.setX(style.getMargin().getX());
                this.margin.setY(style.getMargin().getY());
                this.margin.setWidth(style.getMargin().getWidth());
                this.margin.setHeight(style.getMargin().getHeight());
            }
            this.opacity = style.getOpacity();
        }
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
