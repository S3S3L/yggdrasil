package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * UI 模态对话框组件
 * 用于创建模态窗口，支持按钮操作
 */
public class UIModalDialog extends UIComponent {

    private String title;                         // 标题
    private UIComponent content;                  // 内容组件
    private String message;                       // 消息文本
    private List<ButtonInfo> buttons;             // 按钮列表
    private DialogResult result;                  // 对话框结果
    private boolean resizable;                    // 是否可调整大小
    private boolean draggable;                    // 是否可拖动
    private float titleHeight;                    // 标题栏高度
    private UIColor titleColor;                   // 标题栏背景颜色
    private UIColor titleTextColor;               // 标题文本颜色
    private UIColor backgroundColor;              // 背景颜色
    private UIColor overlayColor;                 // 遮罩颜色
    private float overlayAlpha;                   // 遮罩透明度
    private DialogListener listener;              // 监听器
    private boolean isDragging;                   // 是否正在拖动
    private float dragOffsetX;                    // 拖动偏移 X
    private float dragOffsetY;                    // 拖动偏移 Y
    private boolean showCloseButton;              // 是否显示关闭按钮

    public boolean isDragging() {
        return isDragging;
    }

    public void setDragging(boolean dragging) {
        isDragging = dragging;
    }

    public float getDragOffsetX() {
        return dragOffsetX;
    }

    public void setDragOffsetX(float dragOffsetX) {
        this.dragOffsetX = dragOffsetX;
    }

    public float getDragOffsetY() {
        return dragOffsetY;
    }

    public void setDragOffsetY(float dragOffsetY) {
        this.dragOffsetY = dragOffsetY;
    }

    /**
     * 对话框结果枚举
     */
    public enum DialogResult {
        NONE,       // 无结果
        OK,         // 确定
        CANCEL,     // 取消
        YES,        // 是
        NO,         // 否
        APPLY,      // 应用
        CLOSE       // 关闭
    }

    /**
     * 按钮信息类
     */
    public static class ButtonInfo {
        public String text;
        public DialogResult result;
        public boolean isDefault;
        public boolean isCancel;

        public ButtonInfo(String text, DialogResult result) {
            this(text, result, false, false);
        }

        public ButtonInfo(String text, DialogResult result, boolean isDefault, boolean isCancel) {
            this.text = text;
            this.result = result;
            this.isDefault = isDefault;
            this.isCancel = isCancel;
        }
    }

    /**
     * 对话框监听器接口
     */
    public interface DialogListener {
        /**
         * 当对话框结果确定时调用
         *
         * @param dialog 对话框
         * @param result 结果
         */
        void onResult(UIModalDialog dialog, DialogResult result);

        /**
         * 当对话框关闭时调用
         *
         * @param dialog 对话框
         */
        void onClosed(UIModalDialog dialog);
    }

    public UIModalDialog() {
        this("");
    }

    public UIModalDialog(String title) {
        this.title = title;
        this.buttons = new ArrayList<>();
        this.result = DialogResult.NONE;
        this.resizable = true;
        this.draggable = true;
        this.titleHeight = 40;
        this.titleColor = new UIColor(0.2f, 0.2f, 0.2f, 1f);
        this.titleTextColor = UIColor.WHITE;
        this.backgroundColor = new UIColor(0.15f, 0.15f, 0.15f, 0.95f);
        this.overlayColor = new UIColor(0, 0, 0, 0.5f);
        this.overlayAlpha = 0.5f;
        this.showCloseButton = true;
        setBounds(100, 100, 400, 300);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UIComponent getContent() {
        return content;
    }

    public void setContent(UIComponent content) {
        this.content = content;
        if (content != null) {
            content.setParent(this);
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ButtonInfo> getButtons() {
        return buttons;
    }

    public void setButtons(List<ButtonInfo> buttons) {
        this.buttons = buttons != null ? new ArrayList<>(buttons) : new ArrayList<>();
    }

    public DialogResult getResult() {
        return result;
    }

    public void setResult(DialogResult result) {
        this.result = result;
    }

    public boolean isResizable() {
        return resizable;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public float getTitleHeight() {
        return titleHeight;
    }

    public void setTitleHeight(float titleHeight) {
        this.titleHeight = titleHeight;
    }

    public UIColor getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(UIColor titleColor) {
        this.titleColor = titleColor;
    }

    public UIColor getTitleTextColor() {
        return titleTextColor;
    }

    public void setTitleTextColor(UIColor titleTextColor) {
        this.titleTextColor = titleTextColor;
    }

    public UIColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(UIColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public UIColor getOverlayColor() {
        return overlayColor;
    }

    public void setOverlayColor(UIColor overlayColor) {
        this.overlayColor = overlayColor;
    }

    public float getOverlayAlpha() {
        return overlayAlpha;
    }

    public void setOverlayAlpha(float overlayAlpha) {
        this.overlayAlpha = overlayAlpha;
    }

    public DialogListener getListener() {
        return listener;
    }

    public void setListener(DialogListener listener) {
        this.listener = listener;
    }

    public boolean isShowCloseButton() {
        return showCloseButton;
    }

    public void setShowCloseButton(boolean showCloseButton) {
        this.showCloseButton = showCloseButton;
    }

    /**
     * 添加按钮
     *
     * @param text 按钮文本
     * @param result 按钮结果
     */
    public void addButton(String text, DialogResult result) {
        buttons.add(new ButtonInfo(text, result));
    }

    /**
     * 添加按钮
     *
     * @param text 按钮文本
     * @param result 按钮结果
     * @param isDefault 是否默认按钮
     * @param isCancel 是否取消按钮
     */
    public void addButton(String text, DialogResult result, boolean isDefault, boolean isCancel) {
        buttons.add(new ButtonInfo(text, result, isDefault, isCancel));
    }

    /**
     * 清除所有按钮
     */
    public void clearButtons() {
        buttons.clear();
    }

    /**
     * 获取内容区域的边界
     *
     * @return 边界数组 [x, y, width, height]
     */
    public float[] getContentBounds() {
        float[] bounds = new float[4];
        bounds[0] = getX() + 10;
        bounds[1] = getY() + titleHeight + 10;
        bounds[2] = getWidth() - 20;
        bounds[3] = getHeight() - titleHeight - 60; // 减去按钮区域
        return bounds;
    }

    /**
     * 获取按钮区域的边界
     *
     * @return 边界数组 [x, y, width, height]
     */
    public float[] getButtonAreaBounds() {
        float[] bounds = new float[4];
        bounds[0] = getX() + 10;
        bounds[1] = getY() + getHeight() - 50;
        bounds[2] = getWidth() - 20;
        bounds[3] = 40;
        return bounds;
    }

    /**
     * 检查点击是否在标题栏上
     *
     * @param x X 坐标
     * @param y Y 坐标
     * @return 是否点击在标题栏上
     */
    public boolean isClickOnTitleBar(float x, float y) {
        return y >= getY() + getHeight() - titleHeight && y <= getY() + getHeight();
    }

    /**
     * 检查点击是否在关闭按钮上
     *
     * @param x X 坐标
     * @param y Y 坐标
     * @return 是否点击在关闭按钮上
     */
    public boolean isClickOnCloseButton(float x, float y) {
        if (!showCloseButton) {
            return false;
        }
        float closeSize = titleHeight - 8;
        float closeX = getX() + getWidth() - closeSize - 4;
        float closeY = getY() + getHeight() - (titleHeight - closeSize) / 2 - closeSize / 2;
        return x >= closeX && x <= closeX + closeSize && y >= closeY && y <= closeY + closeSize;
    }

    /**
     * 获取按钮的边界
     *
     * @param index 按钮索引
     * @return 边界数组 [x, y, width, height]
     */
    public float[] getButtonBounds(int index) {
        float[] bounds = new float[4];
        float[] buttonArea = getButtonAreaBounds();

        int buttonCount = buttons.size();
        if (buttonCount == 0) {
            return bounds;
        }

        float buttonWidth = 80;
        float buttonSpacing = 10;
        float totalWidth = buttonCount * buttonWidth + (buttonCount - 1) * buttonSpacing;
        float startX = buttonArea[0] + (buttonArea[2] - totalWidth) / 2;

        bounds[0] = startX + index * (buttonWidth + buttonSpacing);
        bounds[1] = buttonArea[1];
        bounds[2] = buttonWidth;
        bounds[3] = buttonArea[3];

        return bounds;
    }

    /**
     * 获取点击位置的按钮索引
     *
     * @param x X 坐标
     * @param y Y 坐标
     * @return 按钮索引，未点击按钮返回 -1
     */
    public int getButtonAtPosition(float x, float y) {
        for (int i = 0; i < buttons.size(); i++) {
            float[] bounds = getButtonBounds(i);
            if (x >= bounds[0] && x <= bounds[0] + bounds[2] &&
                y >= bounds[1] && y <= bounds[1] + bounds[3]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 处理鼠标按下事件
     *
     * @param x X 坐标
     * @param y Y 坐标
     */
    public void handleMouseDown(float x, float y) {
        if (isClickOnCloseButton(x, y)) {
            close(DialogResult.CLOSE);
            return;
        }

        if (draggable && isClickOnTitleBar(x, y)) {
            isDragging = true;
            dragOffsetX = x - getX();
            dragOffsetY = y - getY();
        }
    }

    /**
     * 处理鼠标释放事件
     *
     * @param x X 坐标
     * @param y Y 坐标
     */
    public void handleMouseUp(float x, float y) {
        if (isDragging) {
            isDragging = false;
        }

        // 检查是否点击在按钮上
        int buttonIndex = getButtonAtPosition(x, y);
        if (buttonIndex >= 0 && buttonIndex < buttons.size()) {
            ButtonInfo button = buttons.get(buttonIndex);
            close(button.result);
        }
    }

    /**
     * 处理鼠标移动事件
     *
     * @param x X 坐标
     * @param y Y 坐标
     */
    public void handleMouseMove(float x, float y) {
        if (isDragging) {
            float newX = x - dragOffsetX;
            float newY = y - dragOffsetY;
            setBounds(newX, newY, getWidth(), getHeight());
        }
    }

    /**
     * 处理点击事件
     *
     * @param x X 坐标
     * @param y Y 坐标
     */
    public void handleClick(float x, float y) {
        // 点击对话框外部时关闭
        if (x < getX() || x > getX() + getWidth() ||
            y < getY() || y > getY() + getHeight()) {
            close(DialogResult.CANCEL);
        }
    }

    /**
     * 关闭对话框
     *
     * @param result 结果
     */
    public void close(DialogResult result) {
        this.result = result;
        setVisibility(UIVisibility.HIDDEN);
        if (listener != null) {
            listener.onResult(this, result);
            listener.onClosed(this);
        }
    }

    /**
     * 显示对话框
     */
    public void show() {
        setVisibility(UIVisibility.VISIBLE);
        this.result = DialogResult.NONE;
    }

    /**
     * 显示对话框并设置模态
     *
     * @param parent 父组件
     */
    public void showModal(UIComponent parent) {
        if (parent != null) {
            // 设置位置到父组件中心
            float parentX = parent.getX();
            float parentY = parent.getY();
            float parentWidth = parent.getWidth();
            float parentHeight = parent.getHeight();

            float dialogWidth = getPreferredWidth();
            float dialogHeight = getPreferredHeight();

            setBounds(
                parentX + (parentWidth - dialogWidth) / 2,
                parentY + (parentHeight - dialogHeight) / 2,
                dialogWidth,
                dialogHeight
            );
        }
        show();
    }

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        // 渲染遮罩
        renderOverlay();

        // 渲染对话框背景
        renderBackground();

        // 渲染标题栏
        renderTitleBar();

        // 渲染内容
        renderContent();

        // 渲染按钮
        renderButtons();
    }

    /**
     * 渲染遮罩
     */
    protected void renderOverlay() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染背景
     */
    protected void renderBackground() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染标题栏
     */
    protected void renderTitleBar() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染内容
     */
    protected void renderContent() {
        if (content != null) {
            float[] contentBounds = getContentBounds();
            content.setBounds(contentBounds[0], contentBounds[1], contentBounds[2], contentBounds[3]);
            content.render();
        }
    }

    /**
     * 渲染按钮
     */
    protected void renderButtons() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 获取推荐的宽度
     *
     * @return 推荐宽度
     */
    public float getPreferredWidth() {
        return 400; // 默认宽度
    }

    /**
     * 获取推荐的高度
     *
     * @return 推荐高度
     */
    public float getPreferredHeight() {
        return 300; // 默认高度
    }

    /**
     * 自动调整大小以适应内容
     */
    public void pack() {
        float contentWidth = content != null && content instanceof UIPanel ? content.getWidth() : 200;
        float contentHeight = content != null && content instanceof UIPanel ? content.getHeight() : 100;

        float minWidth = Math.max(200, contentWidth + 40);
        float minHeight = contentHeight + titleHeight + 80;

        setBounds(getX(), getY(), minWidth, minHeight);
    }

    /**
     * 创建标准确认对话框
     *
     * @param title 标题
     * @param message 消息
     * @return 对话框
     */
    public static UIModalDialog createConfirmDialog(String title, String message) {
        UIModalDialog dialog = new UIModalDialog(title);
        dialog.setMessage(message);
        dialog.addButton("确定", DialogResult.OK, true, false);
        dialog.addButton("取消", DialogResult.CANCEL, false, true);
        return dialog;
    }

    /**
     * 创建标准信息对话框
     *
     * @param title 标题
     * @param message 消息
     * @return 对话框
     */
    public static UIModalDialog createInfoDialog(String title, String message) {
        UIModalDialog dialog = new UIModalDialog(title);
        dialog.setMessage(message);
        dialog.addButton("确定", DialogResult.OK, true, false);
        return dialog;
    }

    /**
     * 创建标准错误对话框
     *
     * @param title 标题
     * @param message 消息
     * @return 对话框
     */
    public static UIModalDialog createErrorDialog(String title, String message) {
        UIModalDialog dialog = new UIModalDialog(title);
        dialog.setMessage(message);
        dialog.addButton("确定", DialogResult.OK, true, false);
        return dialog;
    }
}
