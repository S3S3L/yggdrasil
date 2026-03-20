package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * UI 画布
 * UI 渲染的根容器
 */
public class UICanvas extends UIComponent {

    private final List<UIComponent> overlays;
    private UIComponent focusedComponent;
    private boolean enabled;

    public UICanvas() {
        this("Canvas");
    }

    public UICanvas(String name) {
        super(name);
        this.overlays = new ArrayList<>();
        this.enabled = true;
    }

    public List<UIComponent> getOverlays() {
        return overlays;
    }

    public void addOverlay(UIComponent overlay) {
        overlays.add(overlay);
    }

    public void removeOverlay(UIComponent overlay) {
        overlays.remove(overlay);
    }

    public UIComponent getFocusedComponent() {
        return focusedComponent;
    }

    public void setFocusedComponent(UIComponent focusedComponent) {
        if (this.focusedComponent != null) {
            this.focusedComponent.onBlur();
        }
        this.focusedComponent = focusedComponent;
        if (focusedComponent != null) {
            focusedComponent.onFocus();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * 设置画布尺寸
     *
     * @param width 宽度
     * @param height 高度
     */
    public void setCanvasSize(float width, float height) {
        setSize(width, height);
    }

    @Override
    public void update(float deltaTime) {
        if (!enabled) {
            return;
        }

        super.update(deltaTime);

        // 更新 overlays
        for (UIComponent overlay : overlays) {
            overlay.update(deltaTime);
        }
    }

    @Override
    public void render() {
        if (!enabled || !isVisible()) {
            return;
        }

        // 渲染背景（如果有）
        if (getBackgroundColor() != null && getBackgroundColor().getA() > 0) {
            // 渲染背景
        }

        // 渲染子组件
        for (UIComponent child : getChildren()) {
            child.render();
        }

        // 渲染 overlays
        for (UIComponent overlay : overlays) {
            overlay.render();
        }
    }

    /**
     * 处理点击事件
     *
     * @param x 点击 X 坐标
     * @param y 点击 Y 坐标
     * @return 是否处理了点击
     */
    public boolean handleClick(float x, float y) {
        if (!enabled || !isVisible()) {
            return false;
        }

        UIComponent clicked = findComponentAtPoint(x, y);
        if (clicked != null && clicked.isInteractable()) {
            clicked.onClick();
            return true;
        }

        return false;
    }

    /**
     * 处理悬停事件
     *
     * @param x 鼠标 X 坐标
     * @param y 鼠标 Y 坐标
     */
    public void handleHover(float x, float y) {
        if (!enabled || !isVisible()) {
            return;
        }

        // 清除所有按钮的悬停状态
        clearHoverState();

        // 设置新悬停的组件
        UIComponent hovered = findComponentAtPoint(x, y);
        if (hovered instanceof UIButton && hovered.isInteractable()) {
            ((UIButton) hovered).setHovered(true);
        }
    }

    /**
     * 清除所有按钮的悬停状态
     */
    private void clearHoverState() {
        clearHoverStateRecursive(this);
    }

    private void clearHoverStateRecursive(UIComponent component) {
        if (component instanceof UIButton) {
            ((UIButton) component).setHovered(false);
        }
        for (UIComponent child : component.getChildren()) {
            clearHoverStateRecursive(child);
        }
    }

    /**
     * 提交输入文本
     *
     * @param text 输入的文本
     * @return 是否处理了输入
     */
    public boolean handleTextInput(String text) {
        if (!enabled || focusedComponent == null) {
            return false;
        }

        if (focusedComponent instanceof UIInputField) {
            ((UIInputField) focusedComponent).handleInput(text);
            return true;
        }

        return false;
    }

    /**
     * 处理键盘事件
     *
     * @param keyCode 按键代码
     * @return 是否处理了按键
     */
    public boolean handleKeyPress(int keyCode) {
        if (!enabled || focusedComponent == null) {
            return false;
        }

        // 处理回退键
        if (keyCode == 67 || keyCode == 127) { // BACKSPACE
            if (focusedComponent instanceof UIInputField) {
                ((UIInputField) focusedComponent).handleBackspace();
                return true;
            }
        }

        return false;
    }
}
