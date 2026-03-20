package io.github.s3s3l.yggdrasil.game.core.ui;

/**
 * UI 网格单元格
 * 用于存储网格单元格的数据和属性
 */
public class UIGridCell {

    private int row;                              // 行索引
    private int column;                           // 列索引
    private Object content;                       // 内容
    private Object data;                          // 附加数据
    private UIColor backgroundColor;              // 背景颜色
    private UIColor textColor;                    // 文本颜色
    private boolean selected;                     // 是否被选中
    private boolean hover;                        // 是否悬停
    private int textAlign;                        // 文本对齐方式
    private CellListener listener;                // 单元格监听器

    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_CENTER = 1;
    public static final int ALIGN_RIGHT = 2;

    /**
     * 单元格监听器接口
     */
    public interface CellListener {
        /**
         * 当单元格被点击时调用
         *
         * @param cell 单元格
         */
        void onClick(UIGridCell cell);

        /**
         * 当单元格被双击时调用
         *
         * @param cell 单元格
         */
        void onDoubleClick(UIGridCell cell);

        /**
         * 当单元格状态改变时调用
         *
         * @param cell 单元格
         */
        void onStateChanged(UIGridCell cell);
    }

    public UIGridCell() {
        this(0, 0);
    }

    public UIGridCell(int row, int column) {
        this.row = row;
        this.column = column;
        this.content = null;
        this.data = null;
        this.backgroundColor = null;
        this.textColor = UIColor.WHITE;
        this.selected = false;
        this.hover = false;
        this.textAlign = ALIGN_CENTER;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public UIColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(UIColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public UIColor getTextColor() {
        return textColor;
    }

    public void setTextColor(UIColor textColor) {
        this.textColor = textColor;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        boolean changed = this.selected != selected;
        this.selected = selected;
        if (changed && listener != null) {
            listener.onStateChanged(this);
        }
    }

    public boolean isHover() {
        return hover;
    }

    public void setHover(boolean hover) {
        this.hover = hover;
    }

    public int getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(int textAlign) {
        this.textAlign = textAlign;
    }

    public CellListener getListener() {
        return listener;
    }

    public void setListener(CellListener listener) {
        this.listener = listener;
    }

    /**
     * 处理点击事件
     */
    public void onClick() {
        if (listener != null) {
            listener.onClick(this);
        }
    }

    /**
     * 处理双击事件
     */
    public void onDoubleClick() {
        if (listener != null) {
            listener.onDoubleClick(this);
        }
    }

    @Override
    public String toString() {
        return "UIGridCell[" + row + "," + column + "]";
    }
}
