package io.github.s3s3l.yggdrasil.game.core.grid;

import java.util.ArrayList;
import java.util.List;

/**
 * 正方形网格实现
 * 支持 4 方向（曼哈顿）和 8 方向（切比雪夫）邻居
 */
public class SquareGrid extends AbstractGrid {

    /**
     * 邻居模式
     */
    public enum NeighborMode {
        /**
         * 4 方向邻居（曼哈顿距离，不包括对角线）
         */
        MANHATTAN(4),
        /**
         * 8 方向邻居（切比雪夫距离，包括对角线）
         */
        CHEBYSHEV(8);

        private final int neighborCount;

        NeighborMode(int neighborCount) {
            this.neighborCount = neighborCount;
        }

        public int getNeighborCount() {
            return neighborCount;
        }
    }

    private final NeighborMode neighborMode;
    private final List<SquareCoordinate> directionOffsets;

    /**
     * 创建正方形网格（默认 8 方向）
     *
     * @param metrics 网格度量
     */
    public SquareGrid(GridMetrics metrics) {
        this(metrics, NeighborMode.CHEBYSHEV);
    }

    /**
     * 创建正方形网格
     *
     * @param metrics     网格度量
     * @param neighborMode 邻居模式
     */
    public SquareGrid(GridMetrics metrics, NeighborMode neighborMode) {
        super(metrics);
        this.neighborMode = neighborMode;
        this.directionOffsets = initDirectionOffsets();
    }

    private List<SquareCoordinate> initDirectionOffsets() {
        if (neighborMode == NeighborMode.MANHATTAN) {
            // 4 方向：上、下、左、右
            return List.of(
                    SquareCoordinate.of(0, 1),   // 北
                    SquareCoordinate.of(1, 0),   // 东
                    SquareCoordinate.of(0, -1),  // 南
                    SquareCoordinate.of(-1, 0)   // 西
            );
        } else {
            // 8 方向：包括对角线
            return List.of(
                    SquareCoordinate.of(0, 1),   // 北
                    SquareCoordinate.of(1, 1),   // 东北
                    SquareCoordinate.of(1, 0),   // 东
                    SquareCoordinate.of(1, -1),  // 东南
                    SquareCoordinate.of(0, -1),  // 南
                    SquareCoordinate.of(-1, -1), // 西南
                    SquareCoordinate.of(-1, 0),  // 西
                    SquareCoordinate.of(-1, 1)   // 西北
            );
        }
    }

    @Override
    protected int getDefaultNeighborCount() {
        return neighborMode.getNeighborCount();
    }

    @Override
    protected SquareCoordinate getDirectionOffset(int index) {
        return directionOffsets.get(index % directionOffsets.size());
    }

    @Override
    public SquareCoordinate toCoordinate(int index) {
        int x = index % metrics.getWidth();
        int y = index / metrics.getWidth();
        return SquareCoordinate.of(x, y);
    }

    @Override
    public int toIndex(Coordinate coord) {
        if (!(coord instanceof SquareCoordinate square)) {
            throw new IllegalArgumentException("Coordinate must be SquareCoordinate");
        }
        return square.getX() + square.getY() * metrics.getWidth();
    }

    @Override
    public double getDistance(Coordinate a, Coordinate b) {
        if (!(a instanceof SquareCoordinate squareA) || !(b instanceof SquareCoordinate squareB)) {
            throw new IllegalArgumentException("Coordinates must be SquareCoordinate");
        }

        if (neighborMode == NeighborMode.MANHATTAN) {
            return squareA.manhattanDistance(squareB);
        } else {
            return squareA.chebyshevDistance(squareB);
        }
    }

    @Override
    public boolean isValid(Coordinate coord) {
        if (!(coord instanceof SquareCoordinate square)) {
            return false;
        }
        return metrics.contains(square.getX(), square.getY());
    }

    @Override
    protected SquareCoordinate createCoordinate(int x, int y) {
        return SquareCoordinate.of(x, y);
    }

    /**
     * 获取邻居模式
     *
     * @return 邻居模式
     */
    public NeighborMode getNeighborMode() {
        return neighborMode;
    }

    /**
     * 获取 4 方向邻居
     *
     * @param coord 中心坐标
     * @return 邻居坐标列表
     */
    public List<SquareCoordinate> getCardinalNeighbors(SquareCoordinate coord) {
        List<Coordinate> neighbors = getNeighbors(coord, 4);
        List<SquareCoordinate> cardinalNeighbors = new ArrayList<>(neighbors.size());
        for (Coordinate n : neighbors) {
            cardinalNeighbors.add((SquareCoordinate) n);
        }
        return cardinalNeighbors;
    }

    /**
     * 获取 8 方向邻居
     *
     * @param coord 中心坐标
     * @return 邻居坐标列表
     */
    public List<SquareCoordinate> getAllNeighbors(SquareCoordinate coord) {
        List<Coordinate> neighbors = getNeighbors(coord, 8);
        List<SquareCoordinate> allNeighbors = new ArrayList<>(neighbors.size());
        for (Coordinate n : neighbors) {
            allNeighbors.add((SquareCoordinate) n);
        }
        return allNeighbors;
    }

    /**
     * 获取指定半径范围内的所有坐标
     *
     * @param center 中心坐标
     * @param radius 半径（曼哈顿或切比雪夫）
     * @return 范围内的坐标列表
     */
    public List<SquareCoordinate> getRange(SquareCoordinate center, int radius) {
        List<SquareCoordinate> range = new ArrayList<>();

        if (neighborMode == NeighborMode.MANHATTAN) {
            // 曼哈顿距离范围（菱形）
            for (int dx = -radius; dx <= radius; dx++) {
                int dyRange = radius - Math.abs(dx);
                for (int dy = -dyRange; dy <= dyRange; dy++) {
                    SquareCoordinate coord = SquareCoordinate.of(center.getX() + dx, center.getY() + dy);
                    if (isValid(coord)) {
                        range.add(coord);
                    }
                }
            }
        } else {
            // 切比雪夫距离范围（正方形）
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    SquareCoordinate coord = SquareCoordinate.of(center.getX() + dx, center.getY() + dy);
                    if (isValid(coord)) {
                        range.add(coord);
                    }
                }
            }
        }

        return range;
    }
}
