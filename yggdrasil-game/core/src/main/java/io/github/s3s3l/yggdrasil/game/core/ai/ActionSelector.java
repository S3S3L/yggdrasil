package io.github.s3s3l.yggdrasil.game.core.ai;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 动作选择器
 * 根据效用值选择最佳动作
 */
public class ActionSelector {

    private final List<ActionScorer> scorers;
    private float selectionThreshold;

    public ActionSelector() {
        this.scorers = new ArrayList<>();
        this.selectionThreshold = 0f;
    }

    /**
     * 添加评分器
     *
     * @param scorer 评分器
     */
    public void addScorer(ActionScorer scorer) {
        scorers.add(scorer);
    }

    /**
     * 移除评分器
     *
     * @param scorer 评分器
     */
    public void removeScorer(ActionScorer scorer) {
        scorers.remove(scorer);
    }

    /**
     * 设置选择阈值
     *
     * @param threshold 阈值
     */
    public void setSelectionThreshold(float threshold) {
        this.selectionThreshold = threshold;
    }

    /**
     * 选择最佳动作
     *
     * @param context 效用上下文
     * @return 最佳动作名称，如果没有满足阈值的动作则返回 null
     */
    public String selectBestAction(UtilityContext context) {
        if (scorers.isEmpty()) {
            return null;
        }

        List<ActionScore> scores = new ArrayList<>();
        for (ActionScorer scorer : scorers) {
            float score = scorer.score(context);
            if (score >= selectionThreshold) {
                scores.add(new ActionScore(scorer.getActionName(), score));
            }
        }

        if (scores.isEmpty()) {
            return null;
        }

        // 按分数降序排序
        scores.sort(Comparator.comparingDouble((ActionScore s) -> s.score).reversed());

        return scores.get(0).actionName;
    }

    /**
     * 选择所有满足阈值的动作（按分数排序）
     *
     * @param context 效用上下文
     * @return 动作列表
     */
    public List<String> selectAllActions(UtilityContext context) {
        if (scorers.isEmpty()) {
            return List.of();
        }

        List<ActionScore> scores = new ArrayList<>();
        for (ActionScorer scorer : scorers) {
            float score = scorer.score(context);
            if (score >= selectionThreshold) {
                scores.add(new ActionScore(scorer.getActionName(), score));
            }
        }

        // 按分数降序排序
        scores.sort(Comparator.comparingDouble((ActionScore s) -> s.score).reversed());

        return scores.stream()
                .map(s -> s.actionName)
                .collect(Collectors.toList());
    }

    /**
     * 内部类：动作分数
     */
    private static class ActionScore {
        final String actionName;
        final float score;

        ActionScore(String actionName, float score) {
            this.actionName = actionName;
            this.score = score;
        }
    }
}
