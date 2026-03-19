package io.github.s3s3l.yggdrasil.game.core.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

/**
 * 科技单元测试
 */
class TechnologyTest {

    private Technology technology;

    @BeforeEach
    void setUp() {
        technology = Technology.builder()
                .id(1L)
                .name("Iron Working")
                .description("Unlocks iron weapons and armor")
                .type(TechnologyType.MILITARY)
                .tier(TechnologyTier.TIER_2)
                .researchTime(5)
                .build();
    }

    @Test
    void testTechnologyCreation() {
        assertEquals(1L, technology.getId());
        assertEquals("Iron Working", technology.getName());
        assertEquals(TechnologyType.MILITARY, technology.getType());
        assertEquals(TechnologyTier.TIER_2, technology.getTier());
        assertEquals(5, technology.getResearchTime());
    }

    @Test
    void testTechnologyPrerequisites() {
        Technology advancedTech = Technology.builder()
                .id(2L)
                .name("Steel Working")
                .prerequisites(java.util.List.of("tech_iron", "tech_mining"))
                .build();

        assertEquals(2, advancedTech.getPrerequisites().size());
        assertTrue(advancedTech.getPrerequisites().contains("tech_iron"));
    }

    @Test
    void testTechnologyCost() {
        Technology expensiveTech = Technology.builder()
                .id(3L)
                .name("Advanced AI")
                .cost(Map.of("science", 5000, "gold", 2000))
                .build();

        assertEquals(5000, expensiveTech.getCost("science"));
        assertEquals(2000, expensiveTech.getCost("gold"));
    }
}
