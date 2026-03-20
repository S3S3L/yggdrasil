package io.github.s3s3l.yggdrasil.game.core.ui;

/**
 * UI 工具提示组件
 * 悬浮提示信息，支持自动定位和淡入淡出动画
 */
public class UITooltip extends UIComponent {

    private String text;
    private UIColor textColor;
    private UIColor backgroundColor;
    private float fontSize;
    private TooltipPosition position;       // 提示框位置
    private float offsetX;                  // X 偏移量
    private float offsetY;                  // Y 偏移量
    private float showDelay;                // 显示延迟（秒）
    private float hideDelay;                // 隐藏延迟（秒）
    private float fadeDuration;             // 淡入淡出时长（秒）
    private float delayTimer;               // 延迟计时器
    private float fadeTimer;                // 淡入淡出计时器
    private boolean fadingIn;               // 是否正在淡入
    private boolean fadingOut;              // 是否正在淡出
    private UIComponent target;             // 目标组件
    private TooltipListener listener;       // 监听器

    /**
     * 提示框位置枚举
     */
    public enum TooltipPosition {
        /** 上方 */
        TOP,
        /** 下方 */
        BOTTOM,
        /** 左侧 */
        LEFT,
        /** 右侧 */
        RIGHT,
        /** 跟随鼠标 */
        FOLLOW_MOUSE
    }

    /**
     * 工具提示监听器接口
     */
    public interface TooltipListener {
        /**
         * 当提示显示时调用
         *
         * @param tooltip 工具提示组件
         */
        void onShow(UITooltip tooltip);

        /**
         * 当提示隐藏时调用
         *
         * @param tooltip 工具提示组件
         */
        void onHide(UITooltip tooltip);
    }

    public UITooltip() {
        this("");
    }

    public UITooltip(String text) {
        this.text = text;
        this.textColor = UIColor.WHITE;
        this.backgroundColor = new UIColor(0.1f, 0.1f, 0.1f, 0.95f);
        this.fontSize = 14;
        this.position = TooltipPosition.FOLLOW_MOUSE;
        this.offsetX = 10;
        this.offsetY = 10;
        this.showDelay = 0.3f;
        this.hideDelay = 0.1f;
        this.fadeDuration = 0.2f;
        this.delayTimer = 0;
        this.fadeTimer = 0;
        this.fadingIn = false;
        this.fadingOut = false;
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

    public UIColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(UIColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public TooltipPosition getPosition() {
        return position;
    }

    public void setPosition(TooltipPosition position) {
        this.position = position;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public float getShowDelay() {
        return showDelay;
    }

    public void setShowDelay(float showDelay) {
        this.showDelay = showDelay;
    }

    public float getHideDelay() {
        return hideDelay;
    }

    public void setHideDelay(float hideDelay) {
        this.hideDelay = hideDelay;
    }

    public float getFadeDuration() {
        return fadeDuration;
    }

    public void setFadeDuration(float fadeDuration) {
        this.fadeDuration = fadeDuration;
    }

    public UIComponent getTarget() {
        return target;
    }

    public void setTarget(UIComponent target) {
        this.target = target;
    }

    public TooltipListener getListener() {
        return listener;
    }

    public void setListener(TooltipListener listener) {
        this.listener = listener;
    }

    public boolean isFadingIn() {
        return fadingIn;
    }

    public boolean isFadingOut() {
        return fadingOut;
    }

    /**
     * 显示工具提示
     */
    public void show() {
        if (!isVisible() && !fadingIn) {
            fadingIn = true;
            fadingOut = false;
            delayTimer = 0;
            setVisibility(UIVisibility.VISIBLE);
            if (listener != null) {
                listener.onShow(this);
            }
        }
    }

    /**
     * 隐藏工具提示
     */
    public void hide() {
        if (isVisible() && !fadingOut) {
            fadingOut = true;
            fadingIn = false;
            delayTimer = 0;
            if (listener != null) {
                listener.onHide(this);
            }
        }
    }

    /**
     * 设置目标组件并添加悬停监听
     *
     * @param target 目标组件
     */
    public void attachTo(UIComponent target) {
        this.target = target;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // 处理淡入动画
        if (fadingIn) {
            fadeTimer += deltaTime;
            if (fadeTimer >= fadeDuration) {
                fadeTimer = fadeDuration;
                fadingIn = false;
            }
        }

        // 处理淡出动画
        if (fadingOut) {
            fadeTimer += deltaTime;
            if (fadeTimer >= fadeDuration) {
                fadeTimer = 0;
                fadingOut = false;
                setVisibility(UIVisibility.HIDDEN);
            }
        }

        // 更新透明度
        float targetOpacity;
        if (fadingIn) {
            targetOpacity = fadeTimer / fadeDuration;
        } else if (fadingOut) {
            targetOpacity = 1 - (fadeTimer / fadeDuration);
        } else {
            targetOpacity = isVisible() ? 1 : 0;
        }
        setOpacity(targetOpacity);

        // 更新位置
        if (target != null && isVisible()) {
            updatePosition();
        }
    }

    /**
     * 更新工具提示位置
     */
    private void updatePosition() {
        if (target == null) {
            return;
        }

        float x, y;

        switch (position) {
            case TOP:
                x = target.getX() + (target.getWidth() - getWidth()) / 2;
                y = target.getY() - getHeight() - offsetY;
                break;
            case BOTTOM:
                x = target.getX() + (target.getWidth() - getWidth()) / 2;
                y = target.getY() + target.getHeight() + offsetY;
                break;
            case LEFT:
                x = target.getX() - getWidth() - offsetX;
                y = target.getY() + (target.getHeight() - getHeight()) / 2;
                break;
            case RIGHT:
                x = target.getX() + target.getWidth() + offsetX;
                y = target.getY() + (target.getHeight() - getHeight()) / 2;
                break;
            case FOLLOW_MOUSE:
            default:
                // 跟随鼠标位置由 UIManager 处理
                x = target.getX() + offsetX;
                y = target.getY() + offsetY;
                break;
        }

        setPosition(x, y);
    }

    @Override
    public void render() {
        if (!isVisible() && !fadingIn) {
            return;
        }

        // 渲染背景
        renderBackground();

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
     * 渲染文本
     */
    protected void renderText() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 获取当前淡入淡出进度 (0-1)
     *
     * @return 淡入淡出进度
     */
    public float getFadeProgress() {
        if (fadeDuration <= 0) {
            return 1;
        }
        return fadeTimer / fadeDuration;
    }
}
