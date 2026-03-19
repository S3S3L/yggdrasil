package io.github.s3s3l.yggdrasil.game.core.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

/**
 * 建筑单元测试
 */
class BuildingTest {

    private Building building;

    @BeforeEach
    void setUp() {
        building = Building.builder()
                .id(1L)
                .name("Barracks")
                .description("Military building")
                .type(BuildingType.MILITARY)
                .health(500)
                .defense(50)
                .build();
    }

    @Test
    void testBuildingCreation() {
        assertEquals(1L, building.getId());
        assertEquals("Barracks", building.getName());
        assertEquals(BuildingType.MILITARY, building.getType());
        assertEquals(500, building.getHealth());
        assertEquals(50, building.getDefense());
        assertEquals(BuildingState.CONSTRUCTED, building.getState());
    }

    @Test
    void testBuildingCost() {
        Building expensiveBuilding = Building.builder()
                .id(2L)
                .name("Castle")
                .cost(Map.of("gold", 1000, "wood", 500))
                .build();

        assertEquals(1000, expensiveBuilding.getCost("gold"));
        assertEquals(500, expensiveBuilding.getCost("wood"));
        assertEquals(0, expensiveBuilding.getCost("stone"));
    }

    @Test
    void testBuildingRequirements() {
        Building advancedBuilding = Building.builder()
                .id(3L)
                .name("Advanced Factory")
                .requirements(java.util.List.of("tech_industrial", "tech_engineering"))
                .build();

        assertEquals(2, advancedBuilding.getRequirements().size());
        assertTrue(advancedBuilding.getRequirements().contains("tech_industrial"));
    }
}
