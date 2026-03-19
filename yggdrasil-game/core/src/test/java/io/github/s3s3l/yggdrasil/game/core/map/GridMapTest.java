package io.github.s3s3l.yggdrasil.game.core.map;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;
import io.github.s3s3l.yggdrasil.game.core.grid.GridMetrics;
import io.github.s3s3l.yggdrasil.game.core.grid.HexCoordinate;
import io.github.s3s3l.yggdrasil.game.core.grid.HexGrid;
import io.github.s3s3l.yggdrasil.game.core.grid.SquareCoordinate;
import io.github.s3s3l.yggdrasil.game.core.grid.SquareGrid;

/**
 * GridMap 单元测试
 */
class GridMapTest {

    @Test
    void testCreateSquareMap() {
        GridMetrics metrics = GridMetrics.ofRect(10, 10);
        SquareGrid grid = new SquareGrid(metrics);
        GridMap map = new GridMap("Test Map", grid, TerrainType.GRASSLAND);

        assertEquals("Test Map", map.getName());
        assertEquals(grid, map.getGrid());
        assertEquals(TerrainType.GRASSLAND, map.getTerrain(SquareCoordinate.of(5, 5)));
    }

    @Test
    void testCreateHexMap() {
        GridMetrics metrics = GridMetrics.ofRect(10, 10);
        HexGrid grid = new HexGrid(metrics);
        GridMap map = new GridMap("Hex Map", grid, TerrainType.FOREST);

        assertEquals("Hex Map", map.getName());
        assertEquals(TerrainType.FOREST, map.getTerrain(HexCoordinate.fromAxial(5, 5)));
    }

    @Test
    void testSetTerrain() {
        GridMetrics metrics = GridMetrics.ofRect(10, 10);
        SquareGrid grid = new SquareGrid(metrics);
        GridMap map = new GridMap("Test Map", grid, TerrainType.GRASSLAND);

        Coordinate coord = SquareCoordinate.of(5, 5);
        map.setTerrain(coord, TerrainType.FOREST);

        assertEquals(TerrainType.FOREST, map.getTerrain(coord));
    }

    @Test
    void testSetTerrainOutOfBounds() {
        GridMetrics metrics = GridMetrics.ofRect(10, 10);
        SquareGrid grid = new SquareGrid(metrics);
        GridMap map = new GridMap("Test Map", grid, TerrainType.GRASSLAND);

        Coordinate invalidCoord = SquareCoordinate.of(15, 15);

        assertThrows(IllegalArgumentException.class, () -> {
            map.setTerrain(invalidCoord, TerrainType.FOREST);
        });
    }

    @Test
    void testIsValid() {
        GridMetrics metrics = GridMetrics.ofRect(10, 10);
        SquareGrid grid = new SquareGrid(metrics);
        GridMap map = new GridMap("Test Map", grid, TerrainType.GRASSLAND);

        assertTrue(map.isValid(SquareCoordinate.of(0, 0)));
        assertTrue(map.isValid(SquareCoordinate.of(9, 9)));
        assertFalse(map.isValid(SquareCoordinate.of(-1, 0)));
        assertFalse(map.isValid(SquareCoordinate.of(10, 5)));
    }

    @Test
    void testGetBounds() {
        GridMetrics metrics = GridMetrics.ofRect(10, 10);
        SquareGrid grid = new SquareGrid(metrics);
        GridMap map = new GridMap("Test Map", grid, TerrainType.GRASSLAND);

        MapBounds bounds = map.getBounds();

        assertEquals(0, bounds.getMinX());
        assertEquals(0, bounds.getMinY());
        assertEquals(10, bounds.getMaxX());
        assertEquals(10, bounds.getMaxY());
        assertEquals(10, bounds.getWidth());
        assertEquals(10, bounds.getHeight());
    }

    @Test
    void testGetTerrainCount() {
        GridMetrics metrics = GridMetrics.ofRect(10, 10);
        SquareGrid grid = new SquareGrid(metrics);
        GridMap map = new GridMap("Test Map", grid, TerrainType.GRASSLAND);

        assertEquals(0, map.getTerrainCount());

        map.setTerrain(SquareCoordinate.of(1, 1), TerrainType.FOREST);
        map.setTerrain(SquareCoordinate.of(2, 2), TerrainType.MOUNTAIN);

        assertEquals(2, map.getTerrainCount());
    }

    @Test
    void testClearTerrains() {
        GridMetrics metrics = GridMetrics.ofRect(10, 10);
        SquareGrid grid = new SquareGrid(metrics);
        GridMap map = new GridMap("Test Map", grid, TerrainType.GRASSLAND);

        map.setTerrain(SquareCoordinate.of(1, 1), TerrainType.FOREST);
        map.setTerrain(SquareCoordinate.of(2, 2), TerrainType.MOUNTAIN);

        map.clearTerrains();

        assertEquals(0, map.getTerrainCount());
        assertEquals(TerrainType.GRASSLAND, map.getTerrain(SquareCoordinate.of(1, 1)));
    }

    @Test
    void testGetTerrains() {
        GridMetrics metrics = GridMetrics.ofRect(10, 10);
        SquareGrid grid = new SquareGrid(metrics);
        GridMap map = new GridMap("Test Map", grid, TerrainType.GRASSLAND);

        Coordinate coord1 = SquareCoordinate.of(1, 1);
        Coordinate coord2 = SquareCoordinate.of(2, 2);
        map.setTerrain(coord1, TerrainType.FOREST);
        map.setTerrain(coord2, TerrainType.MOUNTAIN);

        var terrains = map.getTerrains();

        assertEquals(2, terrains.size());
        assertEquals(TerrainType.FOREST, terrains.get(coord1));
        assertEquals(TerrainType.MOUNTAIN, terrains.get(coord2));
    }

    @Test
    void testMapBoundsContains() {
        MapBounds bounds = MapBounds.ofSize(10, 10);

        assertTrue(bounds.contains(0, 0));
        assertTrue(bounds.contains(9, 9));
        assertFalse(bounds.contains(-1, 0));
        assertFalse(bounds.contains(10, 5));
    }

    @Test
    void testMapBoundsUnion() {
        MapBounds bounds1 = MapBounds.ofSize(10, 10);
        MapBounds bounds2 = MapBounds.ofRect(5, 5, 10, 10);

        MapBounds union = bounds1.union(bounds2);

        assertEquals(0, union.getMinX());
        assertEquals(0, union.getMinY());
        assertEquals(15, union.getMaxX());
        assertEquals(15, union.getMaxY());
    }

    @Test
    void testMapBoundsCenter() {
        MapBounds bounds = MapBounds.ofSize(10, 10);

        Coordinate center = bounds.getCenter();

        assertEquals(5, center.getX());
        assertEquals(5, center.getY());
    }
}
