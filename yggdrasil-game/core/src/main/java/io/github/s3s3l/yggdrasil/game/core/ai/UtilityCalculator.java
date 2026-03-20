package io.github.s3s3l.yggdrasil.game.core.ai;

import java.util.ArrayList;
import java.util.List;

/**
 * 效用计算工具类
 * 提供常用的效用计算方法
 */
public class UtilityCalculator {

    /**
     * 线性插值
     *
     * @param value 当前值
     * @param min   最小值
     * @param max   最大值
     * @return 归一化后的值 (0-1)
     */
    public static float normalize(float value, float min, float max) {
        if (max == min) {
            return 0.5f;
        }
        return (value - min) / (max - min);
    }

    /**
     * 线性评分
     *
     * @param value 当前值
     * @param min   最小值
     * @param max   最大值
     * @return 评分 (0-100)
     */
    public static float linearScore(float value, float min, float max) {
        return normalize(value, min, max) * 100f;
    }

    /**
     * 二次曲线评分
     *
     * @param value 当前值
     * @param min   最小值
     * @param max   最大值
     * @return 评分 (0-100)
     */
    public static float quadraticScore(float value, float min, float max) {
        float normalized = normalize(value, min, max);
        return normalized * normalized * 100f;
    }

    /**
     * 反二次曲线评分
     *
     * @param value 当前值
     * @param min   最小值
     * @param max   最大值
     * @return 评分 (0-100)
     */
    public static float inverseQuadraticScore(float value, float min, float max) {
        float normalized = normalize(value, min, max);
        return (1f - normalized * normalized) * 100f;
    }

    /**
     * 加权求和
     *
     * @param scores 得分数组
     * @param weights 权重数组
     * @return 加权总分 (0-100)
     */
    public static float weightedSum(float[] scores, float[] weights) {
        if (scores.length != weights.length) {
            throw new IllegalArgumentException("Scores and weights must have the same length");
        }

        float totalWeight = 0f;
        float weightedSum = 0f;

        for (int i = 0; i < scores.length; i++) {
            weightedSum += scores[i] * weights[i];
            totalWeight += weights[i];
        }

        if (totalWeight == 0) {
            return 0f;
        }

        return (weightedSum / totalWeight);
    }

    /**
     * 乘法组合
     *
     * @param scores 得分数组
     * @return 组合得分 (0-100)
     */
    public static float multiply(float[] scores) {
        if (scores.length == 0) {
            return 0f;
        }

        float product = 1f;
        for (float score : scores) {
            product *= (score / 100f);
        }

        return product * 100f;
    }
}
