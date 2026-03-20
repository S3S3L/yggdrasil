package io.github.s3s3l.yggdrasil.game.core.ai;

/**
 * 动作评分器
 * 对特定动作进行效用评分
 */
public interface ActionScorer {

    /**
     * 评分动作
     *
     * @param context 效用上下文
     * @return 效用值 (0-100)
     */
    float score(UtilityContext context);

    /**
     * 获取动作名称
     *
     * @return 动作名称
     */
    String getActionName();
}
