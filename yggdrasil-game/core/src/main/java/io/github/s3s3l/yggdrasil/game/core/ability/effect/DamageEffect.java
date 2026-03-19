package io.github.s3s3l.yggdrasil.game.core.ability.effect;

import io.github.s3s3l.yggdrasil.game.core.ability.Effect;
import io.github.s3s3l.yggdrasil.game.core.ability.EffectResult;
import io.github.s3s3l.yggdrasil.game.core.unit.Unit;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 伤害效果
 */
@Getter
public class DamageEffect implements Effect {

    private final String name = "伤害";
    private final String description;
    private final int damageAmount;
    private final int duration;
    private final int priority;

    public DamageEffect(int damageAmount) {
        this("造成伤害", damageAmount, 0, 100);
    }

    public DamageEffect(String description, int damageAmount) {
        this(description, damageAmount, 0, 100);
    }

    public DamageEffect(String description, int damageAmount, int duration, int priority) {
        this.description = description;
        this.damageAmount = damageAmount;
        this.duration = duration;
        this.priority = priority;
    }

    @Override
    public EffectResult apply(Unit target, Unit caster) {
        int actualDamage = target.takeDamage(damageAmount);
        return EffectResult.success("造成了 " + actualDamage + " 点伤害", actualDamage);
    }

    @Override
    public boolean canStack(Effect other) {
        // 伤害效果不叠加，取最大值
        return false;
    }
}
