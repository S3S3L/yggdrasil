package io.github.s3s3l.yggdrasil.game.core.ai;

import io.github.s3s3l.yggdrasil.game.core.basic.ID;

import java.util.*;

/**
 * 效用 AI 控制器
 * 管理和执行效用 AI 系统
 */
public class UtilityAIController implements ID {

    private long numericId;
    private final Map<String, ActionScorer> scorers;
    private final ActionSelector selector;
    private final UtilityContext context;
    private String lastSelectedAction;

    public UtilityAIController(long id) {
        this.numericId = id;
        this.scorers = new HashMap<>();
        this.selector = new ActionSelector();
        this.context = new UtilityContext();
    }

    @Override
    public long getId() {
        return numericId;
    }

    @Override
    public void setId(long id) {
        // ID is immutable
    }

    /**
     * 添加评分器
     *
     * @param scorer 评分器
     */
    public void addScorer(ActionScorer scorer) {
        scorers.put(scorer.getActionName(), scorer);
        selector.addScorer(scorer);
    }

    /**
     * 移除评分器
     *
     * @param actionName 动作名称
     */
    public void removeScorer(String actionName) {
        ActionScorer scorer = scorers.remove(actionName);
        if (scorer != null) {
            selector.removeScorer(scorer);
        }
    }

    /**
     * 设置选择阈值
     *
     * @param threshold 阈值
     */
    public void setSelectionThreshold(float threshold) {
        selector.setSelectionThreshold(threshold);
    }

    /**
     * 获取上下文
     *
     * @return 效用上下文
     */
    public UtilityContext getContext() {
        return context;
    }

    /**
     * 执行效用 AI 选择
     *
     * @return 选中的动作名称
     */
    public String tick() {
        lastSelectedAction = selector.selectBestAction(context);
        return lastSelectedAction;
    }

    /**
     * 获取上次选择的动作
     *
     * @return 动作名称
     */
    public String getLastSelectedAction() {
        return lastSelectedAction;
    }

    /**
     * 获取所有满足阈值的动作（按优先级排序）
     *
     * @return 动作列表
     */
    public List<String> getAllActions() {
        return selector.selectAllActions(context);
    }

    /**
     * 重置效用 AI
     */
    public void reset() {
        context.clear();
        lastSelectedAction = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UtilityAIController that = (UtilityAIController) o;
        return Objects.equals(numericId, that.numericId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numericId);
    }
}
