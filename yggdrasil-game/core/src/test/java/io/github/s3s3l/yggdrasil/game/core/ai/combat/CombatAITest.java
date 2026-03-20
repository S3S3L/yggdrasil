package io.github.s3s3l.yggdrasil.game.core.ai.combat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 战斗 AI 单元测试
 */
class CombatAITest {

    private CombatAIController controller;
    private CombatContext context;

    @BeforeEach
    void setUp() {
        controller = new CombatAIController(1L);
        context = controller.getCombatContext();
    }

    @Test
    void testControllerCreation() {
        assertNotNull(controller.getUtilityAI());
        assertNotNull(controller.getTargetSelector());
        assertNotNull(controller.getTacticalEvaluator());
    }

    @Test
    void testTargetSelectorNearest() {
        TargetSelector selector = controller.getTargetSelector();

        CombatTarget target1 = CombatTarget.builder()
                .id(1L)
                .name("Enemy 1")
                .distance(10f)
                .build();
        CombatTarget target2 = CombatTarget.builder()
                .id(2L)
                .name("Enemy 2")
                .distance(5f)
                .build();

        CombatTarget selected = selector.selectNearest(List.of(target1, target2));
        assertEquals(target2, selected);
    }

    @Test
    void testTargetSelectorLowestHealth() {
        TargetSelector selector = controller.getTargetSelector();

        CombatTarget target1 = CombatTarget.builder()
                .id(1L)
                .name("Enemy 1")
                .health(50f)
                .maxHealth(100f)
                .build();
        CombatTarget target2 = CombatTarget.builder()
                .id(2L)
                .name("Enemy 2")
                .health(30f)
                .maxHealth(100f)
                .build();

        CombatTarget selected = selector.selectLowestHealth(List.of(target1, target2));
        assertEquals(target2, selected);
    }

    @Test
    void testTargetSelectorHighestThreat() {
        TargetSelector selector = controller.getTargetSelector();

        CombatTarget target1 = CombatTarget.builder()
                .id(1L)
                .name("Enemy 1")
                .threatLevel(50f)
                .build();
        CombatTarget target2 = CombatTarget.builder()
                .id(2L)
                .name("Enemy 2")
                .threatLevel(80f)
                .build();

        CombatTarget selected = selector.selectHighestThreat(List.of(target1, target2));
        assertEquals(target2, selected);
    }

    @Test
    void testTargetSelectorStrategy() {
        TargetSelector selector = controller.getTargetSelector();

        CombatTarget target1 = CombatTarget.builder()
                .id(1L)
                .name("Enemy 1")
                .distance(10f)
                .threatLevel(50f)
                .build();
        CombatTarget target2 = CombatTarget.builder()
                .id(2L)
                .name("Enemy 2")
                .distance(5f)
                .threatLevel(80f)
                .build();

        // 使用最近策略
        CombatTarget nearest = selector.selectTarget(
                List.of(target1, target2),
                TargetSelectionStrategy.NEAREST
        );
        assertEquals(target2, nearest);

        // 使用威胁最高策略
        CombatTarget mostDangerous = selector.selectTarget(
                List.of(target1, target2),
                TargetSelectionStrategy.MOST_DANGEROUS
        );
        assertEquals(target2, mostDangerous);
    }

    @Test
    void testTacticalEvaluatorForceAdvantage() {
        TacticalEvaluator evaluator = controller.getTacticalEvaluator();

        assertEquals(75f, evaluator.evaluateForceAdvantage(150f, 100f));
        assertEquals(25f, evaluator.evaluateForceAdvantage(50f, 100f));
        assertEquals(100f, evaluator.evaluateForceAdvantage(200f, 0f));
    }

    @Test
    void testTacticalEvaluatorPositionAdvantage() {
        TacticalEvaluator evaluator = controller.getTacticalEvaluator();

        assertEquals(100f, evaluator.evaluatePositionAdvantage(true, true, true));
        assertEquals(40f, evaluator.evaluatePositionAdvantage(true, false, false));
        assertEquals(0f, evaluator.evaluatePositionAdvantage(false, false, false));
    }

    @Test
    void testTacticalEvaluatorHealthAdvantage() {
        TacticalEvaluator evaluator = controller.getTacticalEvaluator();

        assertEquals(62.5f, evaluator.evaluateHealthAdvantage(0.75f, 0.5f));
        assertEquals(37.5f, evaluator.evaluateHealthAdvantage(0.25f, 0.5f));
        assertEquals(50f, evaluator.evaluateHealthAdvantage(0.5f, 0.5f));
    }

    @Test
    void testCombatTargetIsLowHealth() {
        CombatTarget target = CombatTarget.builder()
                .id(1L)
                .name("Enemy")
                .health(20f)
                .maxHealth(100f)
                .build();

        assertTrue(target.isLowHealth(0.3f));
        assertFalse(target.isLowHealth(0.1f));
    }

    @Test
    void testCombatTargetIsInRange() {
        CombatTarget target = CombatTarget.builder()
                .id(1L)
                .name("Enemy")
                .range(5f)
                .distance(10f)
                .build();

        assertTrue(target.isInRange(6f));
        assertFalse(target.isInRange(4f));
    }

    @Test
    void testCombatDecision() {
        CombatTarget target = CombatTarget.builder()
                .id(1L)
                .name("Enemy")
                .build();

        CombatDecision decision = CombatDecision.builder()
                .type(TacticalType.ATTACK)
                .target(target)
                .skill("fireball")
                .reason("High tactical advantage")
                .build();

        assertEquals(TacticalType.ATTACK, decision.getType());
        assertTrue(decision.hasTarget());
        assertTrue(decision.hasSkill());
        assertEquals("fireball", decision.getSkillId());
    }

    @Test
    void testCombatContext() {
        context.setForceAdvantage(70f);
        context.setHealthAdvantage(60f);
        context.setTargetSelectionStrategy(TargetSelectionStrategy.WEAKEST);

        assertEquals(70f, context.getForceAdvantage());
        assertEquals(60f, context.getHealthAdvantage());
        assertEquals(TargetSelectionStrategy.WEAKEST, context.getTargetSelectionStrategy());
    }

    @Test
    void testCombatAIReset() {
        context.setForceAdvantage(70f);
        controller.tick();
        controller.reset();

        assertEquals(50f, context.getForceAdvantage());
    }
}
