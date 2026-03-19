package io.github.s3s3l.yggdrasil.game.core.vision;

import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;
import io.github.s3s3l.yggdrasil.game.core.grid.Grid;
import io.github.s3s3l.yggdrasil.game.core.grid.SquareCoordinate;

import java.util.Set;

/**
 * 视线检测器
 * 使用射线投射算法检测两点之间的视线
 */
public class LineOfSight {

    /**
     * 网格
     */
    private final Grid grid;

    /**
     * 阻挡物集合
     */
    private Set<Coordinate> blockers;

    /**
     * 创建视线检测器
     *
     * @param grid 网格
     */
    public LineOfSight(Grid grid) {
        this.grid = grid;
    }

    /**
     * 设置阻挡物
     *
     * @param blockers 阻挡物坐标集合
     */
    public void setBlockers(Set<Coordinate> blockers) {
        this.blockers = blockers;
    }

    /**
     * 检查两点之间是否有视线
     * 使用 Bresenham 直线算法
     *
     * @param from 起点
     * @param to   终点
     * @return true 如果视线畅通
     */
    public boolean hasLineOfSight(Coordinate from, Coordinate to) {
        if (from == null || to == null) {
            return false;
        }

        if (from.equals(to)) {
            return true;
        }

        int x0 = from.getX();
        int y0 = from.getY();
        int x1 = to.getX();
        int y1 = to.getY();

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            Coordinate current = createCoordinate(x0, y0);

            // 检查是否是阻挡物（排除起点和终点）
            if (current != null && !current.equals(from) && !current.equals(to)) {
                if (isBlocker(current)) {
                    return false;
                }
            }

            if (x0 == x1 && y0 == y1) {
                break;
            }

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }

        return true;
    }

    /**
     * 根据网格类型创建坐标
     * @param x X 坐标
     * @param y Y 坐标
     * @return 坐标对象
     */
    private Coordinate createCoordinate(int x, int y) {
        // 默认使用 SquareCoordinate
        return SquareCoordinate.of(x, y);
    }

    /**
     * 检查坐标是否是阻挡物
     *
     * @param coord 坐标
     * @return true 如果是阻挡物
     */
    private boolean isBlocker(Coordinate coord) {
        if (blockers == null) {
            return false;
        }
        return blockers.contains(coord);
    }

    /**
     * 获取直线上的所有坐标
     *
     * @param from 起点
     * @param to   终点
     * @return 直线上的坐标列表
     */
    public java.util.List<Coordinate> getLineCoordinates(Coordinate from, Coordinate to) {
        java.util.List<Coordinate> line = new java.util.ArrayList<>();

        if (from == null || to == null) {
            return line;
        }

        int x0 = from.getX();
        int y0 = from.getY();
        int x1 = to.getX();
        int y1 = to.getY();

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            Coordinate coord = createCoordinate(x0, y0);
            if (coord != null && grid.isValid(coord)) {
                line.add(coord);
            }

            if (x0 == x1 && y0 == y1) {
                break;
            }

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }

        return line;
    }
}
