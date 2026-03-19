package io.github.s3s3l.yggdrasil.game.core.grid;

import java.util.ArrayList;
import java.util.List;

/**
 * 网格抽象基类
 * 提供网格的通用实现
 */
public abstract class AbstractGrid implements Grid {

    /**
     * 网格度量信息
     */
    protected final GridMetrics metrics;

    public AbstractGrid(GridMetrics metrics) {
        this.metrics = metrics;
    }

    @Override
    public GridMetrics getMetrics() {
        return metrics;
    }

    @Override
    public boolean isValid(Coordinate coord) {
        if (coord == null) {
            return false;
        }
        return metrics.contains(coord);
    }

    @Override
    public List<Coordinate> getNeighbors(Coordinate coord) {
        return getNeighbors(coord, getDefaultNeighborCount());
    }

    /**
     * 获取默认的邻居数量
     *
     * @return 邻居数量（六边形为 6，正方形为 4 或 8）
     */
    protected abstract int getDefaultNeighborCount();

    /**
     * 获取邻居方向偏移
     *
     * @param index 方向索引
     * @return 方向偏移坐标
     */
    protected abstract Coordinate getDirectionOffset(int index);

    @Override
    public List<Coordinate> getNeighbors(Coordinate coord, int count) {
        if (!isValid(coord)) {
            return new ArrayList<>();
        }

        List<Coordinate> neighbors = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            Coordinate offset = getDirectionOffset(i);
            Coordinate neighbor = coord.add(offset);
            if (isValid(neighbor)) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    /**
     * 线性插值路径
     *
     * @param from 起始坐标
     * @param to   目标坐标
     * @return 路径坐标列表
     */
    @Override
    public List<Coordinate> getPath(Coordinate from, Coordinate to) {
        List<Coordinate> path = new ArrayList<>();

        if (!isValid(from) || !isValid(to)) {
            return path;
        }

        // 使用线性插值生成路径
        int dx = to.getX() - from.getX();
        int dy = to.getY() - from.getY();
        int steps = Math.max(Math.abs(dx), Math.abs(dy));

        if (steps == 0) {
            path.add(from);
            return path;
        }

        for (int i = 0; i <= steps; i++) {
            float t = (float) i / steps;
            int x = Math.round(from.getX() + dx * t);
            int y = Math.round(from.getY() + dy * t);
            Coordinate coord = createCoordinate(x, y);
            if (isValid(coord)) {
                path.add(coord);
            }
        }

        return path;
    }

    /**
     * 创建坐标对象
     *
     * @param x X 坐标
     * @param y Y 坐标
     * @return 坐标对象
     */
    protected abstract Coordinate createCoordinate(int x, int y);

    /**
     * 创建坐标对象（六边形支持 z 轴）
     *
     * @param x X 坐标
     * @param y Y 坐标
     * @param z Z 坐标
     * @return 坐标对象
     */
    protected Coordinate createCoordinate(int x, int y, int z) {
        return createCoordinate(x, y);
    }
}
