package io.github.s3s3l.yggdrasil.game.core.ai.campaign;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 战役 AI 单元测试
 */
class CampaignAITest {

    private CampaignAIController controller;
    private CampaignContext context;

    @BeforeEach
    void setUp() {
        controller = new CampaignAIController(1L);
        context = controller.getContext();
    }

    @Test
    void testControllerCreation() {
        assertEquals(1L, controller.getUtilityAI().getId());
        assertNotNull(controller.getContext());
    }

    @Test
    void testStrategicTypeSelection() {
        // 设置有利局势
        context.setResource("gold", 1000);
        context.setResource("science", 500);
        context.setControlledRegions(List.of(1L, 2L, 3L, 4L, 5L));
        context.setEnemyRegions(List.of(1L, 2L));
        context.setDiplomaticRelations(Map.of(1L, 50f, 2L, 30f));

        CampaignDecision decision = controller.tick();
        assertNotNull(decision);
    }

    @Test
    void testStrategicTypeDefense() {
        // 设置危急局势
        context.setResource("gold", 50);
        context.setControlledRegions(List.of(1L));
        context.setEnemyRegions(List.of(1L, 2L, 3L, 4L, 5L));
        context.setAdjacentEnemies(List.of(1L, 2L, 3L));

        CampaignDecision decision = controller.tick();
        // 危急局势应该选择防御或巩固
        assertTrue(decision.getType() == StrategicType.DEFENSE ||
                decision.getType() == StrategicType.CONSOLIDATION);
    }

    @Test
    void testStrategicTypeExpansion() {
        // 设置有利经济局势
        context.setResource("gold", 600);
        context.setControlledRegions(List.of(1L, 2L, 3L));
        context.setEnemyRegions(List.of(1L));
        context.setNeutralRegions(List.of(4L, 5L));

        CampaignDecision decision = controller.tick();
        assertNotNull(decision);
    }

    @Test
    void testCampaignDecision() {
        CampaignDecision decision = CampaignDecision.builder()
                .type(StrategicType.EXPANSION)
                .targetRegionId(5L)
                .action("expand")
                .reason("Expanding to neutral territory")
                .build();

        assertEquals(StrategicType.EXPANSION, decision.getType());
        assertEquals(5L, decision.getTargetRegionId());
        assertTrue(decision.hasTarget());
        assertTrue(decision.hasAction());
        assertEquals("expand", decision.getAction());
    }

    @Test
    void testCampaignContext() {
        context.setResource("gold", 500);
        context.setResource("science", 300);
        context.setControlledRegions(List.of(1L, 2L, 3L));
        context.setStrategicScore(70f);

        assertEquals(500, context.getResource("gold"));
        assertEquals(300, context.getResource("science"));
        assertEquals(3, context.getControlledRegions().size());
        assertEquals(70f, context.getStrategicScore());
    }

    @Test
    void testDiplomaticRelations() {
        context.setDiplomaticRelation(1L, 80f);
        context.setDiplomaticRelation(2L, -30f);

        assertEquals(80f, context.getDiplomaticRelation(1L));
        assertEquals(-30f, context.getDiplomaticRelation(2L));
    }

    @Test
    void testCampaignAIReset() {
        context.setResource("gold", 500);
        context.setStrategicScore(70f);
        controller.tick();
        controller.reset();

        assertEquals(50f, context.getStrategicScore());
    }

    @Test
    void testGetLastDecision() {
        assertNull(controller.getLastDecision());

        controller.tick();

        assertNotNull(controller.getLastDecision());
    }
}
