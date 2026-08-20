package io.github.s3s3l.yggdrasil.game.core.layout;

/**
 * 布局计算结果，记录最终位置与尺寸。
 */
public class LayoutResult {
    /** 左上角 X 坐标（包含 margin 后的最终位置） */
    private float x;
    /** 左上角 Y 坐标（包含 margin 后的最终位置） */
    private float y;
    /** 盒子总宽度（含 padding） */
    private float width;
    /** 盒子总高度（含 padding） */
    private float height;
    /** 内容区宽度（扣除 padding） */
    private float contentWidth;
    /** 内容区高度（扣除 padding） */
    private float contentHeight;

    public LayoutResult() {
    }

    public LayoutResult(float x, float y, float width, float height, float contentWidth, float contentHeight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.contentWidth = contentWidth;
        this.contentHeight = contentHeight;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getContentWidth() {
        return contentWidth;
    }

    public void setContentWidth(float contentWidth) {
        this.contentWidth = contentWidth;
    }

    public float getContentHeight() {
        return contentHeight;
    }

    public void setContentHeight(float contentHeight) {
        this.contentHeight = contentHeight;
    }
}
