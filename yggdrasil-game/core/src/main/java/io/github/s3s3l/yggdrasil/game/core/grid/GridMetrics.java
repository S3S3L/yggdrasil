package io.github.s3s3l.yggdrasil.game.core.grid;

import lombok.Builder;
import lombok.Getter;

/**
 * 网格度量信息
 * 包含网格的尺寸和边界信息
 */
@Getter
public class GridMetrics {

    /**
     * 网格宽度（X 轴方向的数量）
     */
    private final int width;

    /**
     * 网格高度（Y 轴方向的数量）
     */
    private final int height;

    /**
     * 网格单元大小（世界单位）
     */
    private final float cellSize;

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

    @Builder
    public GridMetrics(int width, int height, float cellSize, int minX, int minY, int maxX, int maxY) {
        this.width = width;
        this.height = height;
        this.cellSize = cellSize;
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    /**
     * 创建矩形范围的度量（从 0 开始）
     *
     * @param width  宽度
     * @param height 高度
     * @return 网格度量
     */
    public static GridMetrics ofRect(int width, int height) {
        return GridMetrics.builder()
                .width(width)
                .height(height)
                .cellSize(1.0f)
                .minX(0)
                .minY(0)
                .maxX(width)
                .maxY(height)
                .build();
    }

    /**
     * 创建矩形范围的度量（自定义起点）
     *
     * @param minX   最小 X
     * @param minY   最小 Y
     * @param width  宽度
     * @param height 高度
     * @return 网格度量
     */
    public static GridMetrics ofRect(int minX, int minY, int width, int height) {
        return GridMetrics.builder()
                .width(width)
                .height(height)
                .cellSize(1.0f)
                .minX(minX)
                .minY(minY)
                .maxX(minX + width)
                .maxY(minY + height)
                .build();
    }

    /**
     * 检查坐标是否在度量范围内
     *
     * @param x X 坐标
     * @param y Y 坐标
     * @return true 如果在范围内
     */
    public boolean contains(int x, int y) {
        return x >= minX && x < maxX && y >= minY && y < maxY;
    }

    /**
     * 检查坐标是否在度量范围内
     *
     * @param coord 坐标
     * @return true 如果在范围内
     */
    public boolean contains(Coordinate coord) {
        return contains(coord.getX(), coord.getY());
    }
}
