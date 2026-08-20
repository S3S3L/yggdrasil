package io.github.s3s3l.yggdrasil.game._2d.shape;

/**
 * 2D形状类型
 */
public enum ShapeType2D {

    /**
     * 线段
     */
    SEGMENT(1001),
    /**
     * 射线
     */
    RAY(1002),
    /**
     * 直线
     */
    LINE(1003),
    /**
     * 圆
     */
    CIRCLE(2001),
    /**
     * 矩形
     */
    RECTANGLE(2002),
    /**
     * 多边形
     */
    POLYGON(2003),
    /**
     * 无限平面
     */
    INFINITE_PLANE(2004);

    private final int code;

    private ShapeType2D(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}
