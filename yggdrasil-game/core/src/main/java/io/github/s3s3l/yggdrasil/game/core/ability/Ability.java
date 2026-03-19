package io.github.s3s3l.yggdrasil.game.core.ability;

import io.github.s3s3l.yggdrasil.game.core.ability.effect.BuffEffect;
import io.github.s3s3l.yggdrasil.game.core.ability.effect.DamageEffect;
import io.github.s3s3l.yggdrasil.game.core.ability.effect.HealEffect;
import io.github.s3s3l.yggdrasil.game.core.action.Action;
import io.github.s3s3l.yggdrasil.game.core.unit.Unit;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 技能类
 * 技能是效果的容器，可以包含多个效果
 */
@Getter
public class Ability {

    /**
     * 技能 ID
     */
    private final long id;

    /**
     * 技能名称
     */
    private final String name;

    /**
     * 技能描述
     */
    private final String description;

    /**
     * 技能图标
     */
    private final String icon;

    /**
     * 行动力消耗
     */
    private final int apCost;

    /**
     * 冷却时间
     */
    private final int cooldown;

    /**
     * 作用范围
     */
    private final int range;

    /**
     * 目标类型
     */
    private final Action.TargetType targetType;

    /**
     * 技能效果列表
     */
    private final List<Effect> effects;

    /**
     * 创建技能
     */
    public Ability(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.icon = builder.icon;
        this.apCost = builder.apCost;
        this.cooldown = builder.cooldown;
        this.range = builder.range;
        this.targetType = builder.targetType;
        this.effects = new ArrayList<>(builder.effects);
    }

    /**
     * 应用技能到目标
     *
     * @param caster 施法者
     * @param target 目标
     * @return 技能应用结果
     */
    public List<EffectResult> apply(Unit caster, Unit target) {
        List<EffectResult> results = new ArrayList<>();

        for (Effect effect : effects) {
            EffectResult result = effect.apply(target, caster);
            results.add(result);
        }

        return results;
    }

    /**
     * 获取所有效果
     *
     * @return 效果列表（不可变）
     */
    public List<Effect> getEffects() {
        return Collections.unmodifiableList(effects);
    }

    /**
     * 技能构建器
     */
    public static class Builder {
        private long id;
        private String name;
        private String description;
        private String icon;
        private int apCost;
        private int cooldown;
        private int range;
        private Action.TargetType targetType = Action.TargetType.UNIT_ANY;
        private List<Effect> effects = new ArrayList<>();

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder icon(String icon) {
            this.icon = icon;
            return this;
        }

        public Builder apCost(int apCost) {
            this.apCost = apCost;
            return this;
        }

        public Builder cooldown(int cooldown) {
            this.cooldown = cooldown;
            return this;
        }

        public Builder range(int range) {
            this.range = range;
            return this;
        }

        public Builder targetType(Action.TargetType targetType) {
            this.targetType = targetType;
            return this;
        }

        public Builder effect(Effect effect) {
            this.effects.add(effect);
            return this;
        }

        public Builder effects(List<Effect> effects) {
            this.effects.addAll(effects);
            return this;
        }

        public Ability build() {
            return new Ability(this);
        }
    }

    /**
     * 创建技能构建器
     *
     * @return 构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 创建基础治疗技能
     *
     * @return 治疗技能
     */
    public static Ability createBasicHeal() {
        return builder()
                .id(1L)
                .name("基础治疗")
                .description("恢复目标 30 点生命值")
                .icon("heal_basic")
                .apCost(3)
                .cooldown(2)
                .range(3)
                .targetType(Action.TargetType.UNIT_FRIENDLY)
                .effect(new HealEffect("基础治疗", 30))
                .build();
    }

    /**
     * 创建基础攻击技能
     *
     * @return 攻击技能
     */
    public static Ability createBasicAttack() {
        return builder()
                .id(2L)
                .name("基础攻击")
                .description("对目标造成 20 点伤害")
                .icon("attack_basic")
                .apCost(2)
                .cooldown(0)
                .range(1)
                .targetType(Action.TargetType.UNIT_HOSTILE)
                .effect(new DamageEffect("基础攻击", 20))
                .build();
    }

    /**
     * 创建基础增益技能
     *
     * @return 增益技能
     */
    public static Ability createBasicBuff() {
        return builder()
                .id(3L)
                .name("勇气祝福")
                .description("提升目标 5 点攻击力，持续 2 回合")
                .icon("buff_attack")
                .apCost(2)
                .cooldown(3)
                .range(2)
                .targetType(Action.TargetType.UNIT_FRIENDLY)
                .effect(new BuffEffect("勇气祝福", "攻击力 +5", BuffEffect.StatType.ATTACK, 5, 2))
                .build();
    }
}
