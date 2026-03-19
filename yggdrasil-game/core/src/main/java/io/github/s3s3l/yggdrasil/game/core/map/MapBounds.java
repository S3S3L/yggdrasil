package io.github.s3s3l.yggdrasil.game.core.map;

import lombok.Builder;
import lombok.Getter;

/**
 * 地图边界
 * 定义地图的范围和尺寸
 */
@Getter
public class MapBounds {

    /**
     * 最小 X 坐标
     */
    private final int minX;

    /**
     * 最小 Y 坐标
     */
    private final int minY;

    /**
     * 最大 X 坐标（不包含）
     */
    private final int maxX;

    /**
     * 最大 Y 坐标（不包含）
     */
    private final int maxY;

    /**
     * 地图宽度
     */
    private final int width;

    /**
     * 地图高度
     */
    private final int height;

    @Builder
    public MapBounds(int minX, int minY, int maxX, int maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.width = maxX - minX;
        this.height = maxY - minY;
    }

    /**
     * 创建从 (0,0) 开始的边界
     *
     * @param width  宽度
     * @param height 高度
     * @return 地图边界
     */
    public static MapBounds ofSize(int width, int height) {
        return new MapBounds(0, 0, width, height);
    }

    /**
     * 创建自定义起点和大小的边界
     *
     * @param minX   最小 X
     * @param minY   最小 Y
     * @param width  宽度
     * @param height 高度
     * @return 地图边界
     */
    public static MapBounds ofRect(int minX, int minY, int width, int height) {
        return new MapBounds(minX, minY, minX + width, minY + height);
    }

    /**
     * 检查坐标是否在边界内
     *
     * @param x X 坐标
     * @param y Y 坐标
     * @return true 如果在边界内
     */
    public boolean contains(int x, int y) {
        return x >= minX && x < maxX && y >= minY && y < maxY;
    }

    /**
     * 检查坐标是否在边界内
     *
     * @param x X 坐标
     * @param y Y 坐标
     * @param z Z 坐标（用于 3D，目前忽略）
     * @return true 如果在边界内
     */
    public boolean contains(int x, int y, int z) {
        return contains(x, y);
    }

    /**
     * 检查坐标点是否在边界内
     *
     * @param point 坐标点
     * @return true 如果在边界内
     */
    public boolean contains(io.github.s3s3l.yggdrasil.game.core.grid.Coordinate point) {
        return contains(point.getX(), point.getY());
    }

    /**
     * 合并两个边界
     *
     * @param other 另一个边界
     * @return 合并后的边界
     */
    public MapBounds union(MapBounds other) {
        return new MapBounds(
                Math.min(this.minX, other.minX),
                Math.min(this.minY, other.minY),
                Math.max(this.maxX, other.maxX),
                Math.max(this.maxY, other.maxY)
        );
    }

    /**
     * 获取边界中心点
     *
     * @return 中心点坐标
     */
    public io.github.s3s3l.yggdrasil.game.core.grid.Coordinate getCenter() {
        int centerX = (minX + maxX) / 2;
        int centerY = (minY + maxY) / 2;
        return io.github.s3s3l.yggdrasil.game.core.grid.SquareCoordinate.of(centerX, centerY);
    }
}
