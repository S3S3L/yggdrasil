package io.github.s3s3l.yggdrasil.game.core.ui;

/**
 * UI 列表项组件
 * 用于 UIList 中的单项显示
 */
public class UIListItem extends UIComponent {

    private String text;
    private UIColor textColor;
    private float fontSize;
    private boolean selected;
    private Object data;                        // 附加数据
    private ItemClickListener clickListener;    // 点击监听器

    public UIListItem() {
        this(null);
    }

    public UIListItem(String text) {
        this.text = text;
        this.textColor = UIColor.WHITE;
        this.fontSize = 16;
        this.selected = false;
    }

    public UIListItem(String text, Object data) {
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

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ItemClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onClick() {
        if (clickListener != null) {
            clickListener.onItemClick(this);
        }
    }

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        // 渲染背景（选中状态高亮）
        renderBackground();

        // 渲染文本
        renderText();
    }

    /**
     * 渲染背景
     */
    protected void renderBackground() {
        // 实际渲染由游戏引擎实现
        // 根据 selected 状态决定是否高亮
    }

    /**
     * 渲染文本
     */
    protected void renderText() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 列表项点击监听器接口
     */
    public interface ItemClickListener {
        /**
         * 当列表项被点击时调用
         *
         * @param item 被点击的列表项
         */
        void onItemClick(UIListItem item);
    }
}
