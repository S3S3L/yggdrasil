package io.github.s3s3l.yggdrasil.game.core.ability;

import io.github.s3s3l.yggdrasil.game.core.unit.Unit;

import java.util.*;

/**
 * 技能系统
 * 管理单位的技能学习和使用
 */
public class AbilitySystem {

    /**
     * 单位已学习的技能
     */
    private final Map<Long, Set<Ability>> unitAbilities;

    /**
     * 技能冷却记录
     */
    private final Map<Long, Map<Long, Integer>> abilityCooldowns;

    /**
     * 所有已注册的技能
     */
    private static final Map<Long, Ability> REGISTERED_ABILITIES = new HashMap<>();

    static {
        // 注册基础技能
        registerAbility(Ability.createBasicHeal());
        registerAbility(Ability.createBasicAttack());
        registerAbility(Ability.createBasicBuff());
    }

    public AbilitySystem() {
        this.unitAbilities = new HashMap<>();
        this.abilityCooldowns = new HashMap<>();
    }

    /**
     * 注册技能
     *
     * @param ability 技能
     */
    public static void registerAbility(Ability ability) {
        REGISTERED_ABILITIES.put(ability.getId(), ability);
    }

    /**
     * 获取已注册的技能
     *
     * @param id 技能 ID
     * @return 技能，不存在则返回 null
     */
    public static Ability getRegisteredAbility(long id) {
        return REGISTERED_ABILITIES.get(id);
    }

    /**
     * 获取所有已注册的技能
     *
     * @return 技能列表
     */
    public static Collection<Ability> getAllRegisteredAbilities() {
        return Collections.unmodifiableCollection(REGISTERED_ABILITIES.values());
    }

    /**
     * 单位学习技能
     *
     * @param unitId  单位 ID
     * @param ability 技能
     */
    public void learnAbility(long unitId, Ability ability) {
        unitAbilities.computeIfAbsent(unitId, k -> new HashSet<>()).add(ability);
    }

    /**
     * 单位忘记技能
     *
     * @param unitId    单位 ID
     * @param abilityId 技能 ID
     */
    public void forgetAbility(long unitId, long abilityId) {
        Set<Ability> abilities = unitAbilities.get(unitId);
        if (abilities != null) {
            abilities.removeIf(ability -> ability.getId() == abilityId);
        }
    }

    /**
     * 获取单位已学习的技能
     *
     * @param unitId 单位 ID
     * @return 技能列表
     */
    public List<Ability> getUnitAbilities(long unitId) {
        Set<Ability> abilities = unitAbilities.get(unitId);
        if (abilities == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(new ArrayList<>(abilities));
    }

    /**
     * 检查单位是否已学习某技能
     *
     * @param unitId    单位 ID
     * @param abilityId 技能 ID
     * @return true 如果已学习
     */
    public boolean hasAbility(long unitId, long abilityId) {
        Set<Ability> abilities = unitAbilities.get(unitId);
        if (abilities == null) {
            return false;
        }
        return abilities.stream().anyMatch(a -> a.getId() == abilityId);
    }

    /**
     * 减少单位所有技能的冷却时间
     *
     * @param unitId 单位 ID
     */
    public void reduceCooldowns(long unitId) {
        Map<Long, Integer> cooldowns = abilityCooldowns.get(unitId);
        if (cooldowns != null) {
            cooldowns.replaceAll((id, remaining) -> Math.max(0, remaining - 1));
            cooldowns.entrySet().removeIf(e -> e.getValue() == 0);
        }
    }

    /**
     * 检查技能是否在冷却中
     *
     * @param unitId    单位 ID
     * @param abilityId 技能 ID
     * @return true 如果在冷却中
     */
    public boolean isOnCooldown(long unitId, long abilityId) {
        Map<Long, Integer> cooldowns = abilityCooldowns.get(unitId);
        if (cooldowns == null) {
            return false;
        }
        return cooldowns.getOrDefault(abilityId, 0) > 0;
    }

    /**
     * 获取技能剩余冷却时间
     *
     * @param unitId    单位 ID
     * @param abilityId 技能 ID
     * @return 剩余冷却回合数
     */
    public int getRemainingCooldown(long unitId, long abilityId) {
        Map<Long, Integer> cooldowns = abilityCooldowns.get(unitId);
        if (cooldowns == null) {
            return 0;
        }
        return cooldowns.getOrDefault(abilityId, 0);
    }

    /**
     * 设置技能冷却
     *
     * @param unitId    单位 ID
     * @param abilityId 技能 ID
     * @param cooldown  冷却时间
     */
    public void setCooldown(long unitId, long abilityId, int cooldown) {
        if (cooldown > 0) {
            abilityCooldowns.computeIfAbsent(unitId, k -> new HashMap<>()).put(abilityId, cooldown);
        }
    }

    /**
     * 单位施放技能
     *
     * @param unit   施法者
     * @param ability 技能
     * @param target 目标
     * @return 技能应用结果
     */
    public List<EffectResult> castAbility(Unit unit, Ability ability, Unit target) {
        if (!canCastAbility(unit, ability, target)) {
            return Collections.emptyList();
        }

        // 设置冷却
        if (ability.getCooldown() > 0) {
            setCooldown(unit.getId(), ability.getId(), ability.getCooldown());
        }

        // 应用技能效果
        return ability.apply(unit, target);
    }

    /**
     * 检查单位是否可以施放技能
     *
     * @param unit    施法者
     * @param ability 技能
     * @param target  目标
     * @return true 如果可以施放
     */
    public boolean canCastAbility(Unit unit, Ability ability, Unit target) {
        // 检查是否已学习
        if (!hasAbility(unit.getId(), ability.getId())) {
            return false;
        }

        // 检查冷却
        if (isOnCooldown(unit.getId(), ability.getId())) {
            return false;
        }

        // 检查行动力
        if (unit.getCurrentAp() < ability.getApCost()) {
            return false;
        }

        // 检查目标类型
        if (target != null) {
            switch (ability.getTargetType()) {
                case UNIT_FRIENDLY:
                    if (!unit.getFaction().isFriendlyWith(target.getFaction())) {
                        return false;
                    }
                    break;
                case UNIT_HOSTILE:
                    if (!unit.getFaction().isHostileTo(target.getFaction())) {
                        return false;
                    }
                    break;
            }
        }

        return true;
    }
}
