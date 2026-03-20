package io.github.s3s3l.yggdrasil.game.core.ui;

/**
 * UI 表格列
 * 用于定义表格列的属性
 */
public class UITableColumn {

    private String name;                  // 列名称
    private String headerText;            // 表头文本
    private float width;                  // 列宽（0 表示自动）
    private boolean resizable;            // 是否可调整大小
    private boolean sortable;             // 是否可排序
    private boolean visible;              // 是否可见
    private int textAlign;                // 文本对齐方式

    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_CENTER = 1;
    public static final int ALIGN_RIGHT = 2;

    public UITableColumn() {
        this("");
    }

    public UITableColumn(String name) {
        this(name, 0);
    }

    public UITableColumn(String name, float width) {
        this.name = name;
        this.headerText = name;
        this.width = width;
        this.resizable = true;
        this.sortable = false;
        this.visible = true;
        this.textAlign = ALIGN_LEFT;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public boolean isResizable() {
        return resizable;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    public boolean isSortable() {
        return sortable;
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(int textAlign) {
        this.textAlign = textAlign;
    }
}
