package io.github.s3s3l.yggdrasil.game.core.ai;

/**
 * 效用评估器
 * 评估某个动作或状态的效用值
 */
public interface UtilityEvaluator {

    /**
     * 计算效用值
     *
     * @param context 效用上下文
     * @return 效用值 (0-100)
     */
    float calculate(UtilityContext context);

    /**
     * 获取评估器描述
     *
     * @return 描述
     */
    String getDescription();
}
