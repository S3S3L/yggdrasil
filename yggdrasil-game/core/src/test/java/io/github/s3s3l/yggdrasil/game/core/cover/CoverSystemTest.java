package io.github.s3s3l.yggdrasil.game.core.cover;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;
import io.github.s3s3l.yggdrasil.game.core.grid.Grid;
import io.github.s3s3l.yggdrasil.game.core.grid.GridMetrics;
import io.github.s3s3l.yggdrasil.game.core.grid.SquareCoordinate;
import io.github.s3s3l.yggdrasil.game.core.grid.SquareGrid;
import io.github.s3s3l.yggdrasil.game.core.map.GameMap;
import io.github.s3s3l.yggdrasil.game.core.map.GridMap;
import io.github.s3s3l.yggdrasil.game.core.map.TerrainType;

/**
 * 掩体系统单元测试
 */
class CoverSystemTest {

    private GameMap map;
    private CoverSystem coverSystem;
    private Grid grid;

    @BeforeEach
    void setUp() {
        grid = new SquareGrid(GridMetrics.ofRect(20, 20));
        map = new GridMap("Test Map", grid, TerrainType.GRASSLAND);
        coverSystem = new CoverSystem(map);
    }

    @Test
    void testGetCoverAtNoCover() {
        Coordinate position = SquareCoordinate.of(5, 5);
        Coordinate attacker = SquareCoordinate.of(5, 10);

        CoverValue cover = coverSystem.getCoverAt(position, attacker);

        assertEquals(CoverType.NONE, cover.getCoverType());
        assertFalse(cover.hasCover());
    }

    @Test
    void testGetCoverAtNullPositions() {
        CoverValue cover1 = coverSystem.getCoverAt(null, SquareCoordinate.of(5, 10));
        CoverValue cover2 = coverSystem.getCoverAt(SquareCoordinate.of(5, 5), null);
        CoverValue cover3 = coverSystem.getCoverAt(null, null);

        assertEquals(CoverType.NONE, cover1.getCoverType());
        assertEquals(CoverType.NONE, cover2.getCoverType());
        assertEquals(CoverType.NONE, cover3.getCoverType());
    }

    @Test
    void testGetTerrainCover() {
        // 默认地形应该是草地，无掩体
        Coordinate position = SquareCoordinate.of(5, 5);
        CoverValue cover = coverSystem.getTerrainCover(position);

        assertEquals(CoverType.NONE, cover.getCoverType());
    }

    @Test
    void testCalculateDirection() {
        Coordinate from = SquareCoordinate.of(5, 5);

        // 正东方向
        int east = coverSystem.calculateDirection(from, SquareCoordinate.of(10, 5));
        assertEquals(0, east);

        // 正北方向
        int north = coverSystem.calculateDirection(from, SquareCoordinate.of(5, 10));
        assertEquals(90, north);

        // 正西方向
        int west = coverSystem.calculateDirection(from, SquareCoordinate.of(0, 5));
        assertEquals(180, west);

        // 正南方向
        int south = coverSystem.calculateDirection(from, SquareCoordinate.of(5, 0));
        assertEquals(270, south);
    }

    @Test
    void testCalculateDirectionNullPositions() {
        int direction1 = coverSystem.calculateDirection(null, SquareCoordinate.of(5, 10));
        int direction2 = coverSystem.calculateDirection(SquareCoordinate.of(5, 5), null);

        assertEquals(-1, direction1);
        assertEquals(-1, direction2);
    }

    @Test
    void testHasCoverObstruction() {
        Coordinate from = SquareCoordinate.of(5, 5);
        Coordinate to = SquareCoordinate.of(5, 10);

        // 无障碍物
        assertFalse(coverSystem.hasCoverObstruction(from, to));
    }

    @Test
    void testCalculateActualDamage() {
        Coordinate target = SquareCoordinate.of(5, 5);
        Coordinate attacker = SquareCoordinate.of(5, 10);
        int damage = 50;

        // 无掩体时，伤害不变
        int actualDamage = coverSystem.calculateActualDamage(damage, target, attacker);
        assertEquals(50, actualDamage);
    }

    @Test
    void testCoverValue() {
        CoverValue cover = new CoverValue(CoverType.HALF);

        assertEquals(CoverType.HALF, cover.getCoverType());
        assertEquals(50, cover.getDamageReduction());
        assertEquals(10, cover.getDefenseBonus());
        assertTrue(cover.hasCover());
    }

    @Test
    void testCoverValueWithDirection() {
        Coordinate source = SquareCoordinate.of(5, 5);
        CoverValue cover = new CoverValue(CoverType.FULL, 90, source);

        assertEquals(CoverType.FULL, cover.getCoverType());
        assertEquals(90, cover.getDirection());
        assertEquals(source, cover.getCoverSource());
        assertTrue(cover.hasCover());
    }

    @Test
    void testCoverValueIsCoverFromDirection() {
        // 无方向掩体对所有方向有效
        CoverValue cover1 = new CoverValue(CoverType.HALF);
        assertTrue(cover1.isCoverFromDirection(0));
        assertTrue(cover1.isCoverFromDirection(90));
        assertTrue(cover1.isCoverFromDirection(180));

        // 有方向掩体只对该方向有效
        CoverValue cover2 = new CoverValue(CoverType.FULL, 90, null);
        assertTrue(cover2.isCoverFromDirection(90));
        assertTrue(cover2.isCoverFromDirection(45)); // 45 度误差范围内
        assertTrue(cover2.isCoverFromDirection(135)); // 45 度误差范围内
        assertFalse(cover2.isCoverFromDirection(270)); // 相反方向
    }

    @Test
    void testCoverValueApplyDamageReduction() {
        CoverValue none = new CoverValue(CoverType.NONE);
        CoverValue light = new CoverValue(CoverType.LIGHT);
        CoverValue half = new CoverValue(CoverType.HALF);
        CoverValue full = new CoverValue(CoverType.FULL);

        int damage = 100;

        assertEquals(100, none.applyDamageReduction(damage));
        assertEquals(75, light.applyDamageReduction(damage));
        assertEquals(50, half.applyDamageReduction(damage));
        assertEquals(20, full.applyDamageReduction(damage));
    }

    @Test
    void testCoverValueApplyDamageReductionMinimum() {
        CoverValue full = new CoverValue(CoverType.FULL);

        // 至少造成 1 点伤害
        int damage = full.applyDamageReduction(5);
        assertTrue(damage >= 1);
    }

    @Test
    void testCoverValueZeroDamage() {
        CoverValue cover = new CoverValue(CoverType.HALF);

        assertEquals(0, cover.applyDamageReduction(0));
        assertEquals(0, cover.applyDamageReduction(-10));
    }

    @Test
    void testCoverTypeDamageReduction() {
        assertEquals(0, CoverType.NONE.getDamageReduction());
        assertEquals(25, CoverType.LIGHT.getDamageReduction());
        assertEquals(50, CoverType.HALF.getDamageReduction());
        assertEquals(80, CoverType.FULL.getDamageReduction());
        assertEquals(90, CoverType.HARD.getDamageReduction());
    }

    @Test
    void testCoverTypeDefenseBonus() {
        assertEquals(0, CoverType.NONE.getDefenseBonus());
        assertEquals(5, CoverType.LIGHT.getDefenseBonus());
        assertEquals(10, CoverType.HALF.getDefenseBonus());
        assertEquals(20, CoverType.FULL.getDefenseBonus());
        assertEquals(25, CoverType.HARD.getDefenseBonus());
    }

    @Test
    void testCoverTypeIsValid() {
        assertFalse(CoverType.NONE.isValid());
        assertTrue(CoverType.LIGHT.isValid());
        assertTrue(CoverType.HALF.isValid());
        assertTrue(CoverType.FULL.isValid());
        assertTrue(CoverType.HARD.isValid());
    }

    @Test
    void testCoverTypeGetTotalDefense() {
        CoverType half = CoverType.HALF;

        assertEquals(10, half.getTotalDefense(0));
        assertEquals(30, half.getTotalDefense(20));
        assertEquals(50, half.getTotalDefense(40));
    }

    @Test
    void testCoverValueStaticMethods() {
        CoverValue none = CoverValue.none();
        CoverValue light = CoverValue.light();
        CoverValue half = CoverValue.half();
        CoverValue full = CoverValue.full();

        assertEquals(CoverType.NONE, none.getCoverType());
        assertEquals(CoverType.LIGHT, light.getCoverType());
        assertEquals(CoverType.HALF, half.getCoverType());
        assertEquals(CoverType.FULL, full.getCoverType());
    }
}
