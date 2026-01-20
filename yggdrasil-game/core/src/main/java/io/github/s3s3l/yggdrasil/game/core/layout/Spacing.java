package io.github.s3s3l.yggdrasil.game.core.layout;

/**
 * 间距类
 * 表示四个方向的间距（可用于margin或padding）
 */
public class Spacing {
    /** 上间距 */
    private LengthUnit top;
    /** 右间距 */
    private LengthUnit right;
    /** 下间距 */
    private LengthUnit bottom;
    /** 左间距 */
    private LengthUnit left;

    /**
     * 默认构造函数，所有间距初始化为0
     */
    public Spacing() {
        this(LengthUnit.px(0));
    }

    /**
     * 构造函数，四个方向使用相同间距
     * @param all 所有方向的间距
     */
    public Spacing(LengthUnit all) {
        this(all, all, all, all);
    }

    /**
     * 构造函数，垂直和水平方向使用不同间距
     * @param vertical 垂直方向（上下）的间距
     * @param horizontal 水平方向（左右）的间距
     */
    public Spacing(LengthUnit vertical, LengthUnit horizontal) {
        this(vertical, horizontal, vertical, horizontal);
    }

    /**
     * 构造函数，分别指定四个方向的间距
     * @param top 上间距
     * @param right 右间距
     * @param bottom 下间距
     * @param left 左间距
     */
    public Spacing(LengthUnit top, LengthUnit right, LengthUnit bottom, LengthUnit left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    /**
     * 获取上间距
     * @return 上间距
     */
    public LengthUnit getTop() {
        return top;
    }

    /**
     * 设置上间距
     * @param top 上间距
     */
    public void setTop(LengthUnit top) {
        this.top = top;
    }

    /**
     * 获取右间距
     * @return 右间距
     */
    public LengthUnit getRight() {
        return right;
    }

    /**
     * 设置右间距
     * @param right 右间距
     */
    public void setRight(LengthUnit right) {
        this.right = right;
    }

    /**
     * 获取下间距
     * @return 下间距
     */
    public LengthUnit getBottom() {
        return bottom;
    }

    /**
     * 设置下间距
     * @param bottom 下间距
     */
    public void setBottom(LengthUnit bottom) {
        this.bottom = bottom;
    }

    /**
     * 获取左间距
     * @return 左间距
     */
    public LengthUnit getLeft() {
        return left;
    }

    /**
     * 设置左间距
     * @param left 左间距
     */
    public void setLeft(LengthUnit left) {
        this.left = left;
    }

    /**
     * 设置所有方向的间距为相同值
     * @param value 间距值
     */
    public void setAll(LengthUnit value) {
        this.top = value;
        this.right = value;
        this.bottom = value;
        this.left = value;
    }
}
