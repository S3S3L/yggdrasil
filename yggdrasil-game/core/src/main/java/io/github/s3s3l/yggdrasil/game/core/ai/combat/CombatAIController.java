package io.github.s3s3l.yggdrasil.game.core.ai.combat;

import io.github.s3s3l.yggdrasil.game.core.ai.UtilityAIController;
import io.github.s3s3l.yggdrasil.game.core.ai.UtilityContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 战斗 AI 控制器
 * 管理战斗单位的 AI 行为
 */
public class CombatAIController {

    private final UtilityAIController utilityAI;
    private final TargetSelector targetSelector;
    private final TacticalEvaluator tacticalEvaluator;
    private final CombatContext combatContext;
    private CombatDecision lastDecision;

    public CombatAIController(long id) {
        this.utilityAI = new UtilityAIController(id);
        this.targetSelector = new TargetSelector();
        this.tacticalEvaluator = new TacticalEvaluator();
        this.combatContext = new CombatContext();
    }

    /**
     * 获取效用 AI 控制器
     *
     * @return 效用 AI 控制器
     */
    public UtilityAIController getUtilityAI() {
        return utilityAI;
    }

    /**
     * 获取目标选择器
     *
     * @return 目标选择器
     */
    public TargetSelector getTargetSelector() {
        return targetSelector;
    }

    /**
     * 获取战术评估器
     *
     * @return 战术评估器
     */
    public TacticalEvaluator getTacticalEvaluator() {
        return tacticalEvaluator;
    }

    /**
     * 获取战斗上下文
     *
     * @return 战斗上下文
     */
    public CombatContext getCombatContext() {
        return combatContext;
    }

    /**
     * 执行战斗 AI 决策
     *
     * @return 战斗决策
     */
    public CombatDecision tick() {
        // 评估战术形势
        float tacticalScore = evaluateTacticalSituation();
        combatContext.setTacticalScore(tacticalScore);

        // 选择目标
        CombatTarget target = selectTarget();
        combatContext.setCurrentTarget(target);

        // 使用效用 AI 选择动作
        String action = utilityAI.tick();

        // 创建决策
        lastDecision = createDecision(action, target, tacticalScore);
        return lastDecision;
    }

    /**
     * 评估战术形势
     *
     * @return 战术评分
     */
    private float evaluateTacticalSituation() {
        var factors = tacticalEvaluator.createTacticalFactors();
        factors.put("forceAdvantage", combatContext.getForceAdvantage());
        factors.put("positionAdvantage", combatContext.getPositionAdvantage());
        factors.put("healthAdvantage", combatContext.getHealthAdvantage());
        factors.put("numberAdvantage", combatContext.getNumberAdvantage());
        return tacticalEvaluator.evaluateTacticalSituation(factors);
    }

    /**
     * 选择目标
     *
     * @return 选中的目标
     */
    private CombatTarget selectTarget() {
        List<CombatTarget> targets = combatContext.getVisibleEnemies();
        if (targets.isEmpty()) {
            return null;
        }
        return targetSelector.selectTarget(targets, combatContext.getTargetSelectionStrategy());
    }

    /**
     * 创建战斗决策
     *
     * @param action       选中的动作
     * @param target       目标
     * @param tacticalScore 战术评分
     * @return 战斗决策
     */
    private CombatDecision createDecision(String action, CombatTarget target, float tacticalScore) {
        CombatDecision.Builder builder = CombatDecision.builder();

        // 根据战术评分决定基本策略
        if (tacticalScore < 30f) {
            builder.type(TacticalType.RETREAT);
        } else if (tacticalScore < 50f) {
            builder.type(TacticalType.DEFEND);
        } else if (tacticalScore >= 70f) {
            builder.type(TacticalType.ATTACK);
        } else {
            builder.type(TacticalType.MOVE);
        }

        // 设置目标
        if (target != null) {
            builder.target(target);
        }

        // 设置技能
        if (action != null && !action.isEmpty()) {
            builder.skill(action);
        }

        // 设置原因
        builder.reason(String.format("Tactical score: %.1f, Action: %s", tacticalScore, action));

        return builder.build();
    }

    /**
     * 获取上次决策
     *
     * @return 上次决策
     */
    public CombatDecision getLastDecision() {
        return lastDecision;
    }

    /**
     * 重置战斗 AI
     */
    public void reset() {
        utilityAI.reset();
        combatContext.clear();
        lastDecision = null;
    }
}
