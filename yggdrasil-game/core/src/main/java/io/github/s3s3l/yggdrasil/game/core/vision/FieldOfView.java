package io.github.s3s3l.yggdrasil.game.core.vision;

import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;
import io.github.s3s3l.yggdrasil.game.core.grid.Grid;
import io.github.s3s3l.yggdrasil.game.core.grid.SquareCoordinate;

import java.util.*;

/**
 * 视野系统实现
 * 计算单位的可见范围和视线
 */
public class FieldOfView implements VisionSystem {

    /**
     * 网格
     */
    private final Grid grid;

    /**
     * 视线检测器
     */
    private final LineOfSight lineOfSight;

    /**
     * 阻挡物集合
     */
    private Set<Coordinate> blockers;

    /**
     * 创建视野系统
     *
     * @param grid 网格
     */
    public FieldOfView(Grid grid) {
        this.grid = grid;
        this.lineOfSight = new LineOfSight(grid);
        this.blockers = new HashSet<>();
    }

    @Override
    public List<Coordinate> getFieldOfView(Coordinate position, int visionRange) {
        List<Coordinate> visible = new ArrayList<>();

        if (position == null || visionRange <= 0) {
            return visible;
        }

        // 遍历范围内的所有坐标
        for (int dx = -visionRange; dx <= visionRange; dx++) {
            for (int dy = -visionRange; dy <= visionRange; dy++) {
                int x = position.getX() + dx;
                int y = position.getY() + dy;

                // 检查距离
                Coordinate target = SquareCoordinate.of(x, y);
                if (target == null || !grid.isValid(target)) {
                    continue;
                }

                double distance = grid.getDistance(position, target);
                if (distance > visionRange) {
                    continue;
                }

                // 检查视线
                if (hasLineOfSight(position, target)) {
                    visible.add(target);
                }
            }
        }

        return visible;
    }

    @Override
    public boolean hasLineOfSight(Coordinate from, Coordinate to) {
        return lineOfSight.hasLineOfSight(from, to);
    }

    @Override
    public boolean isVisible(Coordinate observer, Coordinate target, int visionRange) {
        if (observer == null || target == null) {
            return false;
        }

        // 检查距离
        double distance = grid.getDistance(observer, target);
        if (distance > visionRange) {
            return false;
        }

        // 检查视线
        return hasLineOfSight(observer, target);
    }

    @Override
    public void updateBlockers(Set<Coordinate> blockers) {
        this.blockers = blockers != null ? new HashSet<>(blockers) : new HashSet<>();
        this.lineOfSight.setBlockers(this.blockers);
    }

    @Override
    public VisibilityMap getVisibilityMap(Coordinate observer, int visionRange) {
        VisibilityMap map = new VisibilityMap();

        if (observer == null) {
            return map;
        }

        List<Coordinate> visible = getFieldOfView(observer, visionRange);
        for (Coordinate coord : visible) {
            map.setVisible(coord, true);
        }

        return map;
    }

    /**
     * 添加阻挡物
     *
     * @param blocker 阻挡物坐标
     */
    public void addBlocker(Coordinate blocker) {
        if (blocker != null) {
            blockers.add(blocker);
            lineOfSight.setBlockers(blockers);
        }
    }

    /**
     * 移除阻挡物
     *
     * @param blocker 阻挡物坐标
     */
    public void removeBlocker(Coordinate blocker) {
        if (blocker != null) {
            blockers.remove(blocker);
            lineOfSight.setBlockers(blockers);
        }
    }

    /**
     * 清除所有阻挡物
     */
    public void clearBlockers() {
        blockers.clear();
        lineOfSight.setBlockers(blockers);
    }

    /**
     * 获取网格
     *
     * @return 网格
     */
    public Grid getGrid() {
        return grid;
    }
}
