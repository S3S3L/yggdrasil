package io.github.s3s3l.yggdrasil.game.core.ai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 效用 AI 单元测试
 */
class UtilityAITest {

    private UtilityAIController controller;
    private UtilityContext context;

    @BeforeEach
    void setUp() {
        controller = new UtilityAIController(1L);
        context = controller.getContext();
    }

    @Test
    void testControllerCreation() {
        assertEquals(1L, controller.getId());
        assertNull(controller.getLastSelectedAction());
    }

    @Test
    void testUtilityCalculatorNormalize() {
        assertEquals(0.5f, UtilityCalculator.normalize(50f, 0f, 100f));
        assertEquals(0f, UtilityCalculator.normalize(0f, 0f, 100f));
        assertEquals(1f, UtilityCalculator.normalize(100f, 0f, 100f));
    }

    @Test
    void testUtilityCalculatorLinearScore() {
        assertEquals(50f, UtilityCalculator.linearScore(50f, 0f, 100f));
        assertEquals(0f, UtilityCalculator.linearScore(0f, 0f, 100f));
        assertEquals(100f, UtilityCalculator.linearScore(100f, 0f, 100f));
    }

    @Test
    void testUtilityCalculatorQuadraticScore() {
        assertEquals(25f, UtilityCalculator.quadraticScore(50f, 0f, 100f));
        assertEquals(0f, UtilityCalculator.quadraticScore(0f, 0f, 100f));
        assertEquals(100f, UtilityCalculator.quadraticScore(100f, 0f, 100f));
    }

    @Test
    void testUtilityCalculatorInverseQuadraticScore() {
        assertEquals(75f, UtilityCalculator.inverseQuadraticScore(50f, 0f, 100f));
        assertEquals(100f, UtilityCalculator.inverseQuadraticScore(0f, 0f, 100f));
        assertEquals(0f, UtilityCalculator.inverseQuadraticScore(100f, 0f, 100f));
    }

    @Test
    void testUtilityCalculatorWeightedSum() {
        float[] scores = {80f, 60f, 40f};
        float[] weights = {0.5f, 0.3f, 0.2f};
        assertEquals(66f, UtilityCalculator.weightedSum(scores, weights));
    }

    @Test
    void testUtilityCalculatorMultiply() {
        float[] scores = {50f, 50f};
        assertEquals(25f, UtilityCalculator.multiply(scores));
    }

    @Test
    void testActionSelector() {
        controller.addScorer(new TestActionScorer("attack", 80f));
        controller.addScorer(new TestActionScorer("defend", 60f));
        controller.addScorer(new TestActionScorer("retreat", 40f));

        String action = controller.tick();
        assertEquals("attack", action);
        assertEquals("attack", controller.getLastSelectedAction());
    }

    @Test
    void testActionSelectorWithThreshold() {
        controller.addScorer(new TestActionScorer("attack", 80f));
        controller.addScorer(new TestActionScorer("defend", 60f));
        controller.addScorer(new TestActionScorer("retreat", 40f));
        controller.setSelectionThreshold(70f);

        String action = controller.tick();
        assertEquals("attack", action);
    }

    @Test
    void testActionSelectorNoActionAboveThreshold() {
        controller.addScorer(new TestActionScorer("attack", 30f));
        controller.addScorer(new TestActionScorer("defend", 20f));
        controller.setSelectionThreshold(50f);

        String action = controller.tick();
        assertNull(action);
    }

    @Test
    void testGetAllActions() {
        controller.addScorer(new TestActionScorer("attack", 80f));
        controller.addScorer(new TestActionScorer("defend", 60f));
        controller.addScorer(new TestActionScorer("retreat", 40f));

        var actions = controller.getAllActions();
        assertEquals(3, actions.size());
        assertEquals("attack", actions.get(0));
        assertEquals("defend", actions.get(1));
        assertEquals("retreat", actions.get(2));
    }

    @Test
    void testRemoveScorer() {
        TestActionScorer scorer1 = new TestActionScorer("attack", 80f);
        TestActionScorer scorer2 = new TestActionScorer("defend", 60f);
        controller.addScorer(scorer1);
        controller.addScorer(scorer2);

        controller.removeScorer("attack");

        String action = controller.tick();
        assertEquals("defend", action);
    }

    @Test
    void testReset() {
        controller.addScorer(new TestActionScorer("attack", 80f));
        controller.tick();
        controller.getContext().setData("test", "value");
        controller.reset();

        assertNull(controller.getLastSelectedAction());
        assertFalse(controller.getContext().hasData("test"));
    }

    // 测试用的评分器
    private static class TestActionScorer implements ActionScorer {
        private final String actionName;
        private final float score;

        TestActionScorer(String actionName, float score) {
            this.actionName = actionName;
            this.score = score;
        }

        @Override
        public float score(UtilityContext context) {
            return score;
        }

        @Override
        public String getActionName() {
            return actionName;
        }
    }
}
