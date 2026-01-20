package io.github.s3s3l.yggdrasil.game.core.layout;

/**
 * 定位偏移类
 * 表示元素的位置偏移（上、右、下、左）和层级索引
 */
public class Position {
    /** 上边距偏移 */
    private LengthUnit top;
    /** 右边距偏移 */
    private LengthUnit right;
    /** 下边距偏移 */
    private LengthUnit bottom;
    /** 左边距偏移 */
    private LengthUnit left;
    /** 层级索引，值越大越在上层 */
    private int zIndex;

    /**
     * 构造函数，初始化所有偏移为auto，z-index为0
     */
    public Position() {
        this.top = LengthUnit.auto();
        this.right = LengthUnit.auto();
        this.bottom = LengthUnit.auto();
        this.left = LengthUnit.auto();
        this.zIndex = 0;
    }

    /**
     * 获取上边距偏移
     * @return 上边距偏移
     */
    public LengthUnit getTop() {
        return top;
    }

    /**
     * 设置上边距偏移
     * @param top 上边距偏移
     */
    public void setTop(LengthUnit top) {
        this.top = top;
    }

    /**
     * 获取右边距偏移
     * @return 右边距偏移
     */
    public LengthUnit getRight() {
        return right;
    }

    /**
     * 设置右边距偏移
     * @param right 右边距偏移
     */
    public void setRight(LengthUnit right) {
        this.right = right;
    }

    /**
     * 获取下边距偏移
     * @return 下边距偏移
     */
    public LengthUnit getBottom() {
        return bottom;
    }

    /**
     * 设置下边距偏移
     * @param bottom 下边距偏移
     */
    public void setBottom(LengthUnit bottom) {
        this.bottom = bottom;
    }

    /**
     * 获取左边距偏移
     * @return 左边距偏移
     */
    public LengthUnit getLeft() {
        return left;
    }

    /**
     * 设置左边距偏移
     * @param left 左边距偏移
     */
    public void setLeft(LengthUnit left) {
        this.left = left;
    }

    /**
     * 获取层级索引
     * @return 层级索引
     */
    public int getZIndex() {
        return zIndex;
    }

    /**
     * 设置层级索引
     * @param zIndex 层级索引
     */
    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }
}
