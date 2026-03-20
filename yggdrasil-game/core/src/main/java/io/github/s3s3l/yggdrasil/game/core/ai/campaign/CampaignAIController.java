package io.github.s3s3l.yggdrasil.game.core.ai.campaign;

import io.github.s3s3l.yggdrasil.game.core.ai.UtilityAIController;

import java.util.Comparator;
import java.util.List;

/**
 * 战役 AI 控制器
 * 管理战役层面的 AI 行为
 */
public class CampaignAIController {

    private final UtilityAIController utilityAI;
    private final CampaignContext context;
    private CampaignDecision lastDecision;

    public CampaignAIController(long id) {
        this.utilityAI = new UtilityAIController(id);
        this.context = new CampaignContext();
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
     * 获取战役上下文
     *
     * @return 战役上下文
     */
    public CampaignContext getContext() {
        return context;
    }

    /**
     * 执行战役 AI 决策
     *
     * @return 战役决策
     */
    public CampaignDecision tick() {
        // 评估战略形势
        float strategicScore = evaluateStrategicSituation();
        context.setStrategicScore(strategicScore);

        // 选择战略类型
        StrategicType strategy = selectStrategy(strategicScore);
        context.setCurrentStrategy(strategy);

        // 根据战略类型执行具体决策
        CampaignDecision decision = createDecision(strategy);
        lastDecision = decision;
        return lastDecision;
    }

    /**
     * 评估战略形势
     *
     * @return 战略评分
     */
    private float evaluateStrategicSituation() {
        float score = 50f;

        // 资源评估
        int totalResources = context.getResources().values().stream()
                .mapToInt(Integer::intValue)
                .sum();
        if (totalResources > 1000) {
            score += 20f;
        } else if (totalResources > 500) {
            score += 10f;
        } else if (totalResources < 100) {
            score -= 20f;
        }

        // 领土评估
        int regionCount = context.getControlledRegions().size();
        int enemyCount = context.getEnemyRegions().size();
        if (regionCount > enemyCount * 2) {
            score += 15f;
        } else if (enemyCount > regionCount * 2) {
            score -= 15f;
        }

        // 外交评估
        double avgDiplomacy = context.getDiplomaticRelations().values().stream()
                .mapToDouble(Double::valueOf)
                .average()
                .orElse(0.0);
        score += (float) (avgDiplomacy / 5.0);

        // 相邻敌人威胁
        int adjacentEnemyCount = context.getAdjacentEnemies().size();
        if (adjacentEnemyCount > 3) {
            score -= 20f;
        } else if (adjacentEnemyCount > 1) {
            score -= 10f;
        }

        return Math.max(0f, Math.min(100f, score));
    }

    /**
     * 选择战略类型
     *
     * @param strategicScore 战略评分
     * @return 战略类型
     */
    private StrategicType selectStrategy(float strategicScore) {
        if (strategicScore < 30f) {
            // 局势危急，采取防御策略
            return StrategicType.DEFENSE;
        } else if (strategicScore < 50f) {
            // 局势不利，巩固发展
            return StrategicType.CONSOLIDATION;
        } else if (strategicScore >= 70f) {
            // 局势有利，可以选择扩张或进攻
            // 检查资源是否充足
            int gold = context.getResource("gold");
            if (gold > 500) {
                return StrategicType.MILITARY_OFFENSIVE;
            } else {
                return StrategicType.EXPANSION;
            }
        } else {
            // 局势均衡，发展经济或科技
            int science = context.getResource("science");
            if (science > 300) {
                return StrategicType.TECHNOLOGY_DEVELOPMENT;
            } else {
                return StrategicType.ECONOMIC_DEVELOPMENT;
            }
        }
    }

    /**
     * 创建战役决策
     *
     * @param strategy 战略类型
     * @return 战役决策
     */
    private CampaignDecision createDecision(StrategicType strategy) {
        CampaignDecision.Builder builder = CampaignDecision.builder();
        builder.type(strategy);

        switch (strategy) {
            case EXPANSION:
                // 选择最近的neutral region
                Long target = selectExpansionTarget();
                builder.targetRegionId(target != null ? target : 0);
                builder.action("expand");
                builder.reason("Expanding territory");
                break;

            case DEFENSE:
                // 选择受威胁最大的 region
                Long defendTarget = selectDefenseTarget();
                builder.targetRegionId(defendTarget != null ? defendTarget : 0);
                builder.action("defend");
                builder.reason("Defending threatened territory");
                break;

            case ECONOMIC_DEVELOPMENT:
                builder.action("build_economy");
                builder.reason("Developing economy");
                break;

            case TECHNOLOGY_DEVELOPMENT:
                builder.action("research");
                builder.reason("Researching technology");
                break;

            case DIPLOMACY:
                builder.action("diplomacy");
                builder.reason("Improving diplomatic relations");
                break;

            case MILITARY_OFFENSIVE:
                Long attackTarget = selectAttackTarget();
                builder.targetRegionId(attackTarget != null ? attackTarget : 0);
                builder.action("attack");
                builder.reason("Launching military offensive");
                break;

            case CONSOLIDATION:
                builder.action("consolidate");
                builder.reason("Consolidating control");
                break;
        }

        return builder.build();
    }

    /**
     * 选择扩张目标
     *
     * @return 目标区域 ID
     */
    private Long selectExpansionTarget() {
        List<Long> neutralRegions = context.getNeutralRegions();
        if (neutralRegions.isEmpty()) {
            return null;
        }
        // 简单实现：返回第一个 neutral region
        // 实际实现中应该根据距离、资源价值等因素选择
        return neutralRegions.get(0);
    }

    /**
     * 选择防御目标
     *
     * @return 目标区域 ID
     */
    private Long selectDefenseTarget() {
        List<Long> adjacentEnemies = context.getAdjacentEnemies();
        if (adjacentEnemies.isEmpty()) {
            return null;
        }
        // 简单实现：返回第一个相邻敌人
        // 实际实现中应该根据威胁程度选择
        return adjacentEnemies.get(0);
    }

    /**
     * 选择攻击目标
     *
     * @return 目标区域 ID
     */
    private Long selectAttackTarget() {
        List<Long> enemyRegions = context.getEnemyRegions();
        if (enemyRegions.isEmpty()) {
            return null;
        }
        // 简单实现：返回第一个敌人区域
        // 实际实现中应该根据战略价值选择
        return enemyRegions.get(0);
    }

    /**
     * 获取上次决策
     *
     * @return 上次决策
     */
    public CampaignDecision getLastDecision() {
        return lastDecision;
    }

    /**
     * 重置战役 AI
     */
    public void reset() {
        utilityAI.reset();
        context.clear();
        lastDecision = null;
    }
}
