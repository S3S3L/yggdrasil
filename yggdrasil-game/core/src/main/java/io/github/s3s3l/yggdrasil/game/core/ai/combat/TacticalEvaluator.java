package io.github.s3s3l.yggdrasil.game.core.ai.combat;

import java.util.HashMap;
import java.util.Map;

/**
 * 战术评估器
 * 评估当前战场的战术形势
 */
public class TacticalEvaluator {

    /**
     * 评估我方优势
     *
     * @param myForce    我方战力
     * @param enemyForce 敌方战力
     * @return 优势值 (0-100)，50 表示均势
     */
    public float evaluateForceAdvantage(float myForce, float enemyForce) {
        if (enemyForce == 0) {
            return 100f;
        }
        float ratio = myForce / enemyForce;
        return Math.min(100f, ratio * 50f);
    }

    /**
     * 评估位置优势
     *
     * @param hasCover     是否有掩体
     * @param hasHighGround 是否有高地优势
     * @param isFlanking   是否正在侧翼
     * @return 位置优势值 (0-100)
     */
    public float evaluatePositionAdvantage(boolean hasCover, boolean hasHighGround, boolean isFlanking) {
        float advantage = 0f;
        if (hasCover) advantage += 40f;
        if (hasHighGround) advantage += 30f;
        if (isFlanking) advantage += 30f;
        return Math.min(100f, advantage);
    }

    /**
     * 评估生命优势
     *
     * @param myHealthPercent    我方生命百分比
     * @param enemyHealthPercent 敌方生命百分比
     * @return 生命优势值 (0-100)
     */
    public float evaluateHealthAdvantage(float myHealthPercent, float enemyHealthPercent) {
        float diff = myHealthPercent - enemyHealthPercent;
        return 50f + (diff * 50f);
    }

    /**
     * 评估数量优势
     *
     * @param myCount    我方单位数量
     * @param enemyCount 敌方单位数量
     * @return 数量优势值 (0-100)
     */
    public float evaluateNumberAdvantage(int myCount, int enemyCount) {
        if (enemyCount == 0) {
            return 100f;
        }
        float ratio = (float) myCount / enemyCount;
        return Math.min(100f, ratio * 50f);
    }

    /**
     * 综合战术评估
     *
     * @param factors 战术因素
     * @return 总体战术评分 (0-100)
     */
    public float evaluateTacticalSituation(Map<String, Object> factors) {
        float score = 50f; // 基准分

        // 战力评估
        Object forceAdvantage = factors.get("forceAdvantage");
        if (forceAdvantage instanceof Float) {
            score += ((Float) forceAdvantage - 50f) * 0.3f;
        }

        // 位置评估
        Object positionAdvantage = factors.get("positionAdvantage");
        if (positionAdvantage instanceof Float) {
            score += ((Float) positionAdvantage - 50f) * 0.2f;
        }

        // 生命评估
        Object healthAdvantage = factors.get("healthAdvantage");
        if (healthAdvantage instanceof Float) {
            score += ((Float) healthAdvantage - 50f) * 0.2f;
        }

        // 数量评估
        Object numberAdvantage = factors.get("numberAdvantage");
        if (numberAdvantage instanceof Float) {
            score += ((Float) numberAdvantage - 50f) * 0.3f;
        }

        return Math.max(0f, Math.min(100f, score));
    }

    /**
     * 创建战术因素映射
     *
     * @return 战术因素映射
     */
    public Map<String, Object> createTacticalFactors() {
        return new HashMap<>();
    }
}
