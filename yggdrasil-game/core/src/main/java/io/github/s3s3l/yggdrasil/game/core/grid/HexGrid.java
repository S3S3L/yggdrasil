package io.github.s3s3l.yggdrasil.game.core.grid;

import java.util.ArrayList;
import java.util.List;

/**
 * 六边形网格实现
 * 支持"点朝上"（pointy-top）和"边朝上"（flat-top）两种方向
 */
public class HexGrid extends AbstractGrid {

    /**
     * 六边形方向：点朝上或边朝上
     */
    public enum Orientation {
        /**
         * 点朝上（垂直方向六边形）
         */
        POINTY_TOP,
        /**
         * 边朝上（水平方向六边形）
         */
        FLAT_TOP
    }

    private final Orientation orientation;
    private final List<HexCoordinate> directionOffsets;

    /**
     * 创建六边形网格（默认点朝上）
     *
     * @param metrics 网格度量
     */
    public HexGrid(GridMetrics metrics) {
        this(metrics, Orientation.POINTY_TOP);
    }

    /**
     * 创建六边形网格
     *
     * @param metrics     网格度量
     * @param orientation 六边形方向
     */
    public HexGrid(GridMetrics metrics, Orientation orientation) {
        super(metrics);
        this.orientation = orientation;
        this.directionOffsets = initDirectionOffsets();
    }

    private List<HexCoordinate> initDirectionOffsets() {
        List<HexCoordinate> offsets = new ArrayList<>(6);
        if (orientation == Orientation.POINTY_TOP) {
            // 点朝上：6 个邻居方向
            offsets.add(HexCoordinate.fromAxial(1, 0));   // 东
            offsets.add(HexCoordinate.fromAxial(0, 1));   // 东南
            offsets.add(HexCoordinate.fromAxial(-1, 1));  // 西南
            offsets.add(HexCoordinate.fromAxial(-1, 0));  // 西
            offsets.add(HexCoordinate.fromAxial(0, -1));  // 西北
            offsets.add(HexCoordinate.fromAxial(1, -1));  // 东北
        } else {
            // 边朝上：6 个邻居方向
            offsets.add(HexCoordinate.fromAxial(1, 0));   // 东南
            offsets.add(HexCoordinate.fromAxial(1, -1));  // 西南
            offsets.add(HexCoordinate.fromAxial(0, -1));  // 西
            offsets.add(HexCoordinate.fromAxial(-1, 0));  // 西北
            offsets.add(HexCoordinate.fromAxial(-1, 1));  // 东北
            offsets.add(HexCoordinate.fromAxial(0, 1));   // 东
        }
        return offsets;
    }

    @Override
    protected int getDefaultNeighborCount() {
        return 6;
    }

    @Override
    protected HexCoordinate getDirectionOffset(int index) {
        return directionOffsets.get(index % directionOffsets.size());
    }

    @Override
    public HexCoordinate toCoordinate(int index) {
        int q = index % metrics.getWidth();
        int r = index / metrics.getWidth();
        return HexCoordinate.fromAxial(q, r);
    }

    @Override
    public int toIndex(Coordinate coord) {
        if (!(coord instanceof HexCoordinate hex)) {
            throw new IllegalArgumentException("Coordinate must be HexCoordinate");
        }
        return hex.getQ() + hex.getR() * metrics.getWidth();
    }

    @Override
    public double getDistance(Coordinate a, Coordinate b) {
        if (!(a instanceof HexCoordinate hexA) || !(b instanceof HexCoordinate hexB)) {
            throw new IllegalArgumentException("Coordinates must be HexCoordinate");
        }

        // 六边形距离使用立方体坐标
        // distance = (|x1-x2| + |y1-y2| + |z1-z2|) / 2
        int dx = hexA.getCubeX() - hexB.getCubeX();
        int dy = hexA.getCubeY() - hexB.getCubeY();
        int dz = hexA.getCubeZ() - hexB.getCubeZ();

        return (Math.abs(dx) + Math.abs(dy) + Math.abs(dz)) / 2.0;
    }

    public List<Coordinate> getNeighbors(HexCoordinate coord, int count) {
        List<Coordinate> neighbors = new ArrayList<>(Math.min(count, 6));
        for (int i = 0; i < Math.min(count, 6); i++) {
            HexCoordinate offset = directionOffsets.get(i);
            HexCoordinate neighbor = coord.add(offset);
            if (isValid(neighbor)) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    public List<Coordinate> getNeighbors(HexCoordinate coord) {
        return getNeighbors(coord, 6);
    }

    @Override
    public boolean isValid(Coordinate coord) {
        if (!(coord instanceof HexCoordinate hex)) {
            return false;
        }

        // 六边形坐标有效性检查（考虑偏移）
        int q = hex.getQ();
        int r = hex.getR();

        // 检查基本范围
        if (!metrics.contains(q, r)) {
            return false;
        }

        // 对于六边形网格，某些 (q, r) 组合可能无效
        // 这里使用简单的矩形边界，可以根据需要扩展为六边形边界
        return true;
    }

    @Override
    protected HexCoordinate createCoordinate(int x, int y) {
        return HexCoordinate.fromAxial(x, y);
    }

    /**
     * 获取六边形方向
     *
     * @return 六边形方向
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * 获取六边形的所有邻居（6 个方向）
     *
     * @param coord 中心坐标
     * @return 邻居坐标列表
     */
    public List<HexCoordinate> getHexNeighbors(HexCoordinate coord) {
        List<Coordinate> neighbors = getNeighbors(coord);
        List<HexCoordinate> hexNeighbors = new ArrayList<>(neighbors.size());
        for (Coordinate n : neighbors) {
            hexNeighbors.add((HexCoordinate) n);
        }
        return hexNeighbors;
    }

    /**
     * 获取六边形环（指定半径的所有坐标）
     *
     * @param center 中心坐标
     * @param radius 环半径
     * @return 环上的坐标列表
     */
    public List<HexCoordinate> getHexRing(HexCoordinate center, int radius) {
        List<HexCoordinate> ring = new ArrayList<>();

        if (radius == 0) {
            ring.add(center);
            return ring;
        }

        // 六边形环的 6 个方向
        HexCoordinate[] cubeDirections = {
                HexCoordinate.fromAxial(1, 0),    // +x, -y, 0z
                HexCoordinate.fromAxial(0, 1),    // 0x, -y, +z
                HexCoordinate.fromAxial(-1, 1),   // -x, 0y, +z
                HexCoordinate.fromAxial(-1, 0),   // -x, +y, 0z
                HexCoordinate.fromAxial(0, -1),   // 0x, +y, -z
                HexCoordinate.fromAxial(1, -1)    // +x, 0y, -z
        };

        // 从中心向方向 4 移动 radius 步作为起点
        HexCoordinate current = center.add(cubeDirections[4].multiply(radius));

        for (int i = 0; i < 6; i++) {
            HexCoordinate direction = cubeDirections[(i + 1) % 6];
            for (int j = 0; j < radius; j++) {
                if (isValid(current)) {
                    ring.add(current);
                }
                current = current.add(direction);
            }
        }

        return ring;
    }

    /**
     * 获取六边形范围内的所有坐标
     *
     * @param center 中心坐标
     * @param radius 半径
     * @return 范围内的坐标列表
     */
    public List<HexCoordinate> getHexRange(HexCoordinate center, int radius) {
        List<HexCoordinate> range = new ArrayList<>();

        for (int r = 0; r <= radius; r++) {
            range.addAll(getHexRing(center, r));
        }

        return range;
    }
}
