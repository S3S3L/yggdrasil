package io.github.s3s3l.yggdrasil.game.core.grid;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 网格方向枚举
 * 定义网格的可能方向
 */
@Getter
@RequiredArgsConstructor
public enum GridDirection {
    /**
     * 北（上）
     */
    NORTH(0, 1, "北"),

    /**
     * 东北（右上）
     */
    NORTHEAST(1, 1, "东北"),

    /**
     * 东（右）
     */
    EAST(1, 0, "东"),

    /**
     * 东南（右下）
     */
    SOUTHEAST(1, -1, "东南"),

    /**
     * 南（下）
     */
    SOUTH(0, -1, "南"),

    /**
     * 西南（左下）
     */
    SOUTHWEST(-1, -1, "西南"),

    /**
     * 西（左）
     */
    WEST(-1, 0, "西"),

    /**
     * 西北（左上）
     */
    NORTHWEST(-1, 1, "西北");

    /**
     * X 方向偏移
     */
    private final int offsetX;

    /**
     * Y 方向偏移
     */
    private final int offsetY;

    /**
     * 中文名称
     */
    private final String name;

    /**
     * 获取相反方向
     *
     * @return 相反方向
     */
    public GridDirection opposite() {
        return values()[(ordinal() + 4) % 8];
    }

    /**
     * 顺时针旋转
     *
     * @return 旋转后的方向
     */
    public GridDirection clockwise() {
        return values()[(ordinal() + 1) % 8];
    }

    /**
     * 逆时针旋转
     *
     * @return 旋转后的方向
     */
    public GridDirection counterClockwise() {
        return values()[(ordinal() + 7) % 8];
    }

    /**
     * 从方向偏移获取最接近的方向
     *
     * @param dx X 偏移
     * @param dy Y 偏移
     * @return 最接近的方向
     */
    public static GridDirection fromOffset(int dx, int dy) {
        if (dx == 0 && dy > 0) return NORTH;
        if (dx == 0 && dy < 0) return SOUTH;
        if (dx > 0 && dy == 0) return EAST;
        if (dx < 0 && dy == 0) return WEST;

        if (dx > 0 && dy > 0) return NORTHEAST;
        if (dx > 0 && dy < 0) return SOUTHEAST;
        if (dx < 0 && dy < 0) return SOUTHWEST;
        if (dx < 0 && dy > 0) return NORTHWEST;

        return NORTH; // 默认
    }
}
