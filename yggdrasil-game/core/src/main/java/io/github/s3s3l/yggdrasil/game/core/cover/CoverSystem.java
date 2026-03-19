package io.github.s3s3l.yggdrasil.game.core.cover;

import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;
import io.github.s3s3l.yggdrasil.game.core.grid.Grid;
import io.github.s3s3l.yggdrasil.game.core.grid.SquareCoordinate;
import io.github.s3s3l.yggdrasil.game.core.map.GameMap;
import io.github.s3s3l.yggdrasil.game.core.map.Terrain;

import java.util.Set;

/**
 * 掩体系统
 * 计算单位在特定位置的掩体效果
 */
public class CoverSystem {

    /**
     * 游戏地图
     */
    private final GameMap map;

    /**
     * 网格
     */
    private final Grid grid;

    /**
     * 创建掩体系统
     *
     * @param map 游戏地图
     */
    public CoverSystem(GameMap map) {
        this.map = map;
        this.grid = map.getGrid();
    }

    /**
     * 获取单位在指定位置的掩体值
     *
     * @param position 单位位置
     * @param attacker 攻击者位置
     * @return 掩体值
     */
    public CoverValue getCoverAt(Coordinate position, Coordinate attacker) {
        if (position == null || attacker == null) {
            return CoverValue.none();
        }

        // 获取地形掩体
        CoverValue terrainCover = getTerrainCover(position);

        // 如果有地形掩体，检查方向
        if (terrainCover.hasCover()) {
            int direction = calculateDirection(position, attacker);
            return new CoverValue(
                    terrainCover.getCoverType(),
                    direction,
                    position
            );
        }

        return CoverValue.none();
    }

    /**
     * 获取地形提供的掩体
     *
     * @param position 位置
     * @return 地形掩体值
     */
    public CoverValue getTerrainCover(Coordinate position) {
        if (position == null || map == null) {
            return CoverValue.none();
        }

        Terrain terrain = map.getTerrain(position);
        if (terrain == null) {
            return CoverValue.none();
        }

        // 根据地形类型判断掩体
        String terrainName = terrain.getName().toLowerCase();

        if (terrainName.contains("建筑") || terrainName.contains("building")) {
            return CoverValue.full();
        } else if (terrainName.contains("墙") || terrainName.contains("wall")) {
            return CoverValue.half();
        } else if (terrainName.contains("森林") || terrainName.contains("forest")) {
            return CoverValue.light();
        } else if (terrainName.contains("废墟") || terrainName.contains("ruin")) {
            return CoverValue.half();
        } else if (terrainName.contains("岩石") || terrainName.contains("rock")) {
            return CoverValue.light();
        }

        return CoverValue.none();
    }

    /**
     * 计算从目标到攻击者的方向
     *
     * @param from     起点
     * @param to       终点
     * @return 方向角度（0-360）
     */
    public int calculateDirection(Coordinate from, Coordinate to) {
        if (from == null || to == null) {
            return -1;
        }

        int dx = to.getX() - from.getX();
        int dy = to.getY() - from.getY();

        // 计算角度（弧度转角度）
        double radians = Math.atan2(dy, dx);
        double degrees = Math.toDegrees(radians);

        // 转换为 0-360 范围
        if (degrees < 0) {
            degrees += 360;
        }

        return (int) Math.round(degrees);
    }

    /**
     * 检查两点之间是否有掩体遮挡
     *
     * @param from     起点
     * @param to       终点
     * @return true 如果有掩体遮挡
     */
    public boolean hasCoverObstruction(Coordinate from, Coordinate to) {
        if (from == null || to == null) {
            return false;
        }

        // 获取直线上的所有坐标
        java.util.List<Coordinate> line = getLineCoordinates(from, to);

        // 检查是否有阻挡视线的地形
        for (Coordinate coord : line) {
            if (!coord.equals(from) && !coord.equals(to)) {
                CoverValue cover = getTerrainCover(coord);
                if (cover.getCoverType() == CoverType.FULL ||
                    cover.getCoverType() == CoverType.HARD) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 获取直线上的所有坐标
     *
     * @param from 起点
     * @param to   终点
     * @return 直线上的坐标列表
     */
    private java.util.List<Coordinate> getLineCoordinates(Coordinate from, Coordinate to) {
        java.util.List<Coordinate> line = new java.util.ArrayList<>();

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
            Coordinate coord = SquareCoordinate.of(x0, y0);
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

    /**
     * 计算攻击的实际伤害（考虑掩体）
     *
     * @param damage     原始伤害
     * @param target     目标位置
     * @param attacker   攻击者位置
     * @return 实际伤害
     */
    public int calculateActualDamage(int damage, Coordinate target, Coordinate attacker) {
        CoverValue cover = getCoverAt(target, attacker);
        return cover.applyDamageReduction(damage);
    }

    /**
     * 获取游戏地图
     *
     * @return 游戏地图
     */
    public GameMap getMap() {
        return map;
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
