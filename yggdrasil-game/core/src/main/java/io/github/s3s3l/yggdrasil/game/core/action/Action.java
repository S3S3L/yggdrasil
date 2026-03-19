package io.github.s3s3l.yggdrasil.game.core.action;

import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;
import io.github.s3s3l.yggdrasil.game.core.unit.Unit;

import java.util.List;

/**
 * 动作接口
 * 定义单位可以执行的基本动作
 */
public interface Action {

    /**
     * 获取动作名称
     *
     * @return 动作名称
     */
    String getName();

    /**
     * 获取动作描述
     *
     * @return 动作描述
     */
    String getDescription();

    /**
     * 获取动作消耗的行动力
     *
     * @return 行动力消耗
     */
    int getApCost();

    /**
     * 获取动作消耗的生命值
     *
     * @return 生命值消耗
     */
    default int getHpCost() {
        return 0;
    }

    /**
     * 获取动作冷却时间（回合数）
     *
     * @return 冷却时间
     */
    default int getCooldown() {
        return 0;
    }

    /**
     * 获取动作作用范围
     *
     * @return 作用范围
     */
    int getRange();

    /**
     * 获取动作目标类型
     *
     * @return 目标类型
     */
    TargetType getTargetType();

    /**
     * 检查动作是否可以执行
     *
     * @param unit   执行动作的单位
     * @param target 目标位置或单位
     * @return true 如果可以执行
     */
    boolean canExecute(Unit unit, Object target);

    /**
     * 执行动作
     *
     * @param unit   执行动作的单位
     * @param target 目标位置或单位
     * @return 动作执行结果
     */
    ActionResult execute(Unit unit, Object target);

    /**
     * 获取动作可以执行的所有目标位置
     *
     * @param unit 执行动作的单位
     * @return 可执行目标列表
     */
    List<Coordinate> getValidTargets(Unit unit);

    /**
     * 动作目标类型
     */
    enum TargetType {
        /**
         * 无目标（自身动作）
         */
        NONE,
        /**
         * 地面位置
         */
        POSITION,
        /**
         * 单位（友方）
         */
        UNIT_FRIENDLY,
        /**
         * 单位（敌方）
         */
        UNIT_HOSTILE,
        /**
         * 单位（任意）
         */
        UNIT_ANY,
        /**
         * 位置或单位
         */
        POSITION_OR_UNIT
    }
}
