package io.github.s3s3l.yggdrasil.game.core.ui;

/**
 * UI 表格单元格
 * 用于存储表格单元格的数据和属性
 */
public class UITableCell {

    private Object value;                 // 单元格值
    private Object data;                  // 附加数据
    private String text;                  // 显示文本
    private UIColor textColor;            // 文本颜色
    private UIColor backgroundColor;      // 背景颜色
    private boolean editable;             // 是否可编辑
    private int textAlign;                // 文本对齐方式

    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_CENTER = 1;
    public static final int ALIGN_RIGHT = 2;

    public UITableCell() {
        this(null);
    }

    public UITableCell(Object value) {
        this.value = value;
        this.text = value != null ? value.toString() : "";
        this.textColor = UIColor.WHITE;
        this.backgroundColor = null;
        this.editable = false;
        this.textAlign = ALIGN_LEFT;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
        this.text = value != null ? value.toString() : "";
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
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

    public UIColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(UIColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public int getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(int textAlign) {
        this.textAlign = textAlign;
    }

    @Override
    public String toString() {
        return text != null ? text : "";
    }
}
