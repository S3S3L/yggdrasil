package io.github.s3s3l.yggdrasil.game.core.grid;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 六边形坐标
 * 支持轴向坐标 (q, r) 和立方体坐标 (x, y, z) 转换
 *
 * 轴向坐标系统：
 * <pre>
 *       r
 *      / \
 *     | q |
 *      \ /
 * </pre>
 *
 * 立方体坐标系统（用于计算）：
 * <pre>
 *       y
 *      / \
 *     | x |
 *      \ /
 *       z = -x - y
 * </pre>
 */
@Getter
public class HexCoordinate implements Coordinate {

    /**
     * q 值（轴向坐标）/ x 值（立方体坐标）
     */
    private final int q;

    /**
     * r 值（轴向坐标）/ z 值（立方体坐标）
     */
    private final int r;

    /**
     * 私有构造，使用工厂方法创建
     *
     * @param q q 值
     * @param r r 值
     */
    private HexCoordinate(int q, int r) {
        this.q = q;
        this.r = r;
    }

    /**
     * 从轴向坐标创建
     *
     * @param q q 值
     * @param r r 值
     * @return 六边形坐标
     */
    public static HexCoordinate fromAxial(int q, int r) {
        return new HexCoordinate(q, r);
    }

    /**
     * 从立方体坐标创建
     *
     * @param x x 值
     * @param y y 值
     * @param z z 值
     * @return 六边形坐标
     */
    public static HexCoordinate fromCube(int x, int y, int z) {
        // 验证立方体坐标约束：x + y + z = 0
        if (x + y + z != 0) {
            throw new IllegalArgumentException(
                    "Cube coordinates must satisfy x + y + z = 0, got " + (x + y + z));
        }
        return new HexCoordinate(x, z);
    }

    /**
     * 从轴向坐标创建（别名）
     *
     * @param x x 值（q）
     * @param y y 值（r）
     * @return 六边形坐标
     */
    public static HexCoordinate fromAxialXy(int x, int y) {
        return fromAxial(x, y);
    }

    @Override
    public int getX() {
        return q;
    }

    @Override
    public int getY() {
        return r;
    }

    @Override
    public int getZ() {
        // 立方体坐标的 y = -q - r
        return -q - r;
    }

    /**
     * 获取立方体 x 坐标
     *
     * @return x 值
     */
    public int getCubeX() {
        return q;
    }

    /**
     * 获取立方体 y 坐标
     *
     * @return y 值
     */
    public int getCubeY() {
        return -q - r;
    }

    /**
     * 获取立方体 z 坐标
     *
     * @return z 值
     */
    public int getCubeZ() {
        return r;
    }

    @Override
    public HexCoordinate add(Coordinate other) {
        if (other instanceof HexCoordinate hex) {
            return fromAxial(this.q + hex.q, this.r + hex.r);
        }
        return fromAxial(this.q + other.getX(), this.r + other.getY());
    }

    @Override
    public HexCoordinate subtract(Coordinate other) {
        if (other instanceof HexCoordinate hex) {
            return fromAxial(this.q - hex.q, this.r - hex.r);
        }
        return fromAxial(this.q - other.getX(), this.r - other.getY());
    }

    @Override
    public HexCoordinate multiply(int scalar) {
        return fromAxial(this.q * scalar, this.r * scalar);
    }

    /**
     * 获取六边形的六个邻居方向
     *
     * @return 邻居方向数组
     */
    public static HexCoordinate[] getDirections() {
        return new HexCoordinate[]{
                fromAxial(1, 0),   // 东
                fromAxial(1, -1),  // 东南
                fromAxial(0, -1),  // 西南
                fromAxial(-1, 0),  // 西
                fromAxial(-1, 1),  // 西北
                fromAxial(0, 1)    // 东北
        };
    }

    /**
     * 获取六边形的六个角方向（用于更精确的距离计算）
     *
     * @return 角方向数组
     */
    public static HexCoordinate[] getCornerDirections() {
        return new HexCoordinate[]{
                fromAxial(2, -1),  // 东东北
                fromAxial(1, -2),  // 东南东
                fromAxial(-1, -1), // 西南西
                fromAxial(-2, 1),  // 西西南
                fromAxial(-1, 2),  // 西北西
                fromAxial(1, 1)    // 东北东
        };
    }

    @Override
    public String toString() {
        return String.format("HexCoord(%d,%d,%d)", q, getCubeY(), r);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        HexCoordinate that = (HexCoordinate) obj;
        return this.q == that.q && this.r == that.r;
    }

    @Override
    public int hashCode() {
        // 使用轴向坐标计算哈希
        return 31 * q + r;
    }
}
