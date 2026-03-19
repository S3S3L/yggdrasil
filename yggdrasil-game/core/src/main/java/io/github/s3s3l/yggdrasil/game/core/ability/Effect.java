package io.github.s3s3l.yggdrasil.game.core.ability;

import io.github.s3s3l.yggdrasil.game.core.unit.Unit;

/**
 * 技能效果接口
 * 定义技能对单位产生的效果
 */
public interface Effect {

    /**
     * 获取效果名称
     *
     * @return 效果名称
     */
    String getName();

    /**
     * 获取效果描述
     *
     * @return 效果描述
     */
    String getDescription();

    /**
     * 应用效果到目标单位
     *
     * @param target 目标单位
     * @param caster 施法者单位
     * @return 效果应用结果
     */
    EffectResult apply(Unit target, Unit caster);

    /**
     * 效果是否可以叠加
     *
     * @param other 另一个效果
     * @return true 如果可以叠加
     */
    boolean canStack(Effect other);

    /**
     * 效果持续时间（回合数）
     *
     * @return 持续时间，-1 表示永久
     */
    default int getDuration() {
        return 0;
    }

    /**
     * 效果优先级（用于解决冲突）
     *
     * @return 优先级
     */
    default int getPriority() {
        return 0;
    }
}
