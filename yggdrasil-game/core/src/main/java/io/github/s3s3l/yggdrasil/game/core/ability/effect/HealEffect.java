package io.github.s3s3l.yggdrasil.game.core.ability.effect;

import io.github.s3s3l.yggdrasil.game.core.ability.Effect;
import io.github.s3s3l.yggdrasil.game.core.ability.EffectResult;
import io.github.s3s3l.yggdrasil.game.core.unit.Unit;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 治疗效果
 */
@Getter
public class HealEffect implements Effect {

    private final String name = "治疗";
    private final String description;
    private final int healAmount;
    private final int duration;
    private final int priority;

    public HealEffect(int healAmount) {
        this("治疗", healAmount, 0, 100);
    }

    public HealEffect(String description, int healAmount) {
        this(description, healAmount, 0, 100);
    }

    public HealEffect(String description, int healAmount, int duration, int priority) {
        this.description = description;
        this.healAmount = healAmount;
        this.duration = duration;
        this.priority = priority;
    }

    @Override
    public EffectResult apply(Unit target, Unit caster) {
        int actualHeal = Math.min(healAmount, target.getStats().getMaxHp() - target.getCurrentHp());
        if (actualHeal <= 0) {
            return EffectResult.failure("目标生命值已满");
        }
        target.heal(actualHeal);
        return EffectResult.success("恢复了 " + actualHeal + " 点生命值", actualHeal);
    }

    @Override
    public boolean canStack(Effect other) {
        return other instanceof HealEffect && priority == other.getPriority();
    }
}
