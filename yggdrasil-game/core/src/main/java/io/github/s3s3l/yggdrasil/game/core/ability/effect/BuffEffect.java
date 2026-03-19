package io.github.s3s3l.yggdrasil.game.core.ability.effect;

import io.github.s3s3l.yggdrasil.game.core.ability.Effect;
import io.github.s3s3l.yggdrasil.game.core.ability.EffectResult;
import io.github.s3s3l.yggdrasil.game.core.unit.Unit;
import lombok.Getter;

/**
 * 增益效果
 */
@Getter
public class BuffEffect implements Effect {

    private final String name;
    private final String description;
    private final int statBonus;
    private final StatType statType;
    private final int duration;
    private final int priority;

    public BuffEffect(String name, String description, StatType statType, int statBonus, int duration) {
        this.name = name;
        this.description = description;
        this.statType = statType;
        this.statBonus = statBonus;
        this.duration = duration;
        this.priority = 50;
    }

    public BuffEffect(String name, String description, StatType statType, int statBonus) {
        this(name, description, statType, statBonus, 1);
    }

    @Override
    public EffectResult apply(Unit target, Unit caster) {
        // 简化实现：增益效果应用于单位
        // 实际实现需要状态管理系统来追踪临时属性变化
        return EffectResult.success(name + " 已应用于 " + target.getName(), statBonus);
    }

    @Override
    public boolean canStack(Effect other) {
        if (!(other instanceof BuffEffect)) {
            return false;
        }
        BuffEffect otherBuff = (BuffEffect) other;
        return this.statType == otherBuff.statType && this.priority == otherBuff.priority;
    }

    /**
     * 属性类型
     */
    public enum StatType {
        ATTACK("攻击力"),
        DEFENSE("防御力"),
        CRITICAL("暴击率"),
        EVASION("闪避率"),
        VISION("视野"),
        MOVEMENT("移动力");

        private final String displayName;

        StatType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
