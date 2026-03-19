package io.github.s3s3l.yggdrasil.game.core.grid;

/**
 * 网格坐标接口
 * 定义网格坐标的基本行为
 */
public interface Coordinate {

    /**
     * 获取 X 轴分量（或六边形的 q 值）
     *
     * @return X 分量
     */
    int getX();

    /**
     * 获取 Y 轴分量（或六边形的 r 值）
     *
     * @return Y 分量
     */
    int getY();

    /**
     * 获取 Z 轴分量（六边形立方体坐标使用）
     *
     * @return Z 分量，平面网格返回 0
     */
    default int getZ() {
        return 0;
    }

    /**
     * 坐标加法
     *
     * @param other 另一个坐标
     * @return 相加后的坐标
     */
    Coordinate add(Coordinate other);

    /**
     * 坐标减法
     *
     * @param other 另一个坐标
     * @return 相减后的坐标
     */
    Coordinate subtract(Coordinate other);

    /**
     * 坐标乘以标量
     *
     * @param scalar 标量
     * @return 缩放后的坐标
     */
    Coordinate multiply(int scalar);

    /**
     * 获取坐标的哈希值（用于 Map 的 key）
     *
     * @return 哈希值
     */
    @Override
    int hashCode();

    /**
     * 坐标相等性判断
     *
     * @param obj 比较对象
     * @return true 如果坐标相等
     */
    @Override
    boolean equals(Object obj);

    /**
     * 坐标字符串表示
     *
     * @return 坐标字符串
     */
    @Override
    String toString();
}
