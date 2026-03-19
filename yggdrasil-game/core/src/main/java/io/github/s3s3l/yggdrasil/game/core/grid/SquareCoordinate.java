package io.github.s3s3l.yggdrasil.game.core.grid;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 正方形坐标
 * 使用标准的笛卡尔坐标系统
 */
@Getter
@EqualsAndHashCode
public class SquareCoordinate implements Coordinate {

    /**
     * X 坐标
     */
    private final int x;

    /**
     * Y 坐标
     */
    private final int y;

    /**
     * 私有构造，使用工厂方法创建
     *
     * @param x X 坐标
     * @param y Y 坐标
     */
    private SquareCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 从笛卡尔坐标创建
     *
     * @param x X 坐标
     * @param y Y 坐标
     * @return 正方形坐标
     */
    public static SquareCoordinate of(int x, int y) {
        return new SquareCoordinate(x, y);
    }

    @Override
    public int getZ() {
        return 0;
    }

    @Override
    public SquareCoordinate add(Coordinate other) {
        return new SquareCoordinate(this.x + other.getX(), this.y + other.getY());
    }

    @Override
    public SquareCoordinate subtract(Coordinate other) {
        return new SquareCoordinate(this.x - other.getX(), this.y - other.getY());
    }

    @Override
    public SquareCoordinate multiply(int scalar) {
        return new SquareCoordinate(this.x * scalar, this.y * scalar);
    }

    /**
     * 获取曼哈顿距离（不考虑对角线）
     *
     * @param other 另一个坐标
     * @return 曼哈顿距离
     */
    public int manhattanDistance(SquareCoordinate other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }

    /**
     * 获取切比雪夫距离（考虑对角线）
     *
     * @param other 另一个坐标
     * @return 切比雪夫距离
     */
    public int chebyshevDistance(SquareCoordinate other) {
        return Math.max(Math.abs(this.x - other.x), Math.abs(this.y - other.y));
    }

    /**
     * 获取 4 个邻居方向
     *
     * @return 邻居方向数组
     */
    public static SquareCoordinate[] getCardinalDirections() {
        return new SquareCoordinate[]{
                of(0, 1),    // 北
                of(1, 0),    // 东
                of(0, -1),   // 南
                of(-1, 0)    // 西
        };
    }

    /**
     * 获取 8 个邻居方向（包括对角线）
     *
     * @return 邻居方向数组
     */
    public static SquareCoordinate[] getAllDirections() {
        return new SquareCoordinate[]{
                of(0, 1),    // 北
                of(1, 1),    // 东北
                of(1, 0),    // 东
                of(1, -1),   // 东南
                of(0, -1),   // 南
                of(-1, -1),  // 西南
                of(-1, 0),   // 西
                of(-1, 1)    // 西北
        };
    }

    @Override
    public String toString() {
        return String.format("SquareCoord(%d,%d)", x, y);
    }
}
