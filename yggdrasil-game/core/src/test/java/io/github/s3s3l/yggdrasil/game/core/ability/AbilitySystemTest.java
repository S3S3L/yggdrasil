package io.github.s3s3l.yggdrasil.game.core.ability;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.ability.effect.BuffEffect;
import io.github.s3s3l.yggdrasil.game.core.ability.effect.DamageEffect;
import io.github.s3s3l.yggdrasil.game.core.ability.effect.HealEffect;
import io.github.s3s3l.yggdrasil.game.core.action.Action;
import io.github.s3s3l.yggdrasil.game.core.grid.Grid;
import io.github.s3s3l.yggdrasil.game.core.grid.GridMetrics;
import io.github.s3s3l.yggdrasil.game.core.grid.SquareCoordinate;
import io.github.s3s3l.yggdrasil.game.core.grid.SquareGrid;
import io.github.s3s3l.yggdrasil.game.core.unit.*;

import java.util.List;

/**
 * 技能系统单元测试
 */
class AbilitySystemTest {

    private Grid grid;
    private Unit healer;
    private Unit soldier;
    private Unit enemy;
    private AbilitySystem abilitySystem;

    @BeforeEach
    void setUp() {
        grid = new SquareGrid(GridMetrics.ofRect(10, 10));

        healer = new BasicUnit(
                1L,
                "Healer",
                UnitType.HEALER,
                UnitStats.builder()
                        .maxHp(80)
                        .maxAp(12)
                        .attack(8)
                        .defense(5)
                        .vision(5)
                        .range(5)
                        .build(),
                Faction.PLAYER
        );

        soldier = new BasicUnit(
                2L,
                "Soldier",
                UnitType.INFANTRY,
                UnitStats.createInfantry(),
                Faction.PLAYER
        );

        enemy = new BasicUnit(
                3L,
                "Enemy",
                UnitType.INFANTRY,
                UnitStats.createInfantry(),
                Faction.ENEMY
        );

        abilitySystem = new AbilitySystem();
    }

    @Test
    void testLearnAbility() {
        Ability ability = Ability.createBasicHeal();

        // 学习技能
        abilitySystem.learnAbility(healer.getId(), ability);

        // 验证已学习
        assertTrue(abilitySystem.hasAbility(healer.getId(), ability.getId()));

        // 验证技能列表
        List<Ability> abilities = abilitySystem.getUnitAbilities(healer.getId());
        assertEquals(1, abilities.size());
        assertEquals(ability.getId(), abilities.get(0).getId());
    }

    @Test
    void testForgetAbility() {
        Ability ability = Ability.createBasicHeal();

        // 学习并忘记技能
        abilitySystem.learnAbility(healer.getId(), ability);
        assertTrue(abilitySystem.hasAbility(healer.getId(), ability.getId()));

        abilitySystem.forgetAbility(healer.getId(), ability.getId());
        assertFalse(abilitySystem.hasAbility(healer.getId(), ability.getId()));
    }

    @Test
    void testCastAbility() {
        Ability healAbility = Ability.createBasicHeal();

        // 学习技能
        abilitySystem.learnAbility(healer.getId(), healAbility);

        // 让士兵受伤
        soldier.takeDamage(30);
        int injuredHp = soldier.getCurrentHp();

        // 施放治疗技能
        List<EffectResult> results = abilitySystem.castAbility(healer, healAbility, soldier);

        // 验证结果
        assertFalse(results.isEmpty());
        assertTrue(results.stream().anyMatch(EffectResult::isSuccess));
        assertTrue(soldier.getCurrentHp() > injuredHp);
    }

    @Test
    void testCastAbilityOnCooldown() {
        Ability ability = Ability.createBasicHeal();

        abilitySystem.learnAbility(healer.getId(), ability);

        // 第一次施放
        abilitySystem.castAbility(healer, ability, soldier);

        // 验证在冷却中
        assertTrue(abilitySystem.isOnCooldown(healer.getId(), ability.getId()));

        // 第二次施放应该失败
        List<EffectResult> results = abilitySystem.castAbility(healer, ability, soldier);
        assertTrue(results.isEmpty());
    }

    @Test
    void testReduceCooldowns() {
        Ability ability = Ability.createBasicHeal();

        abilitySystem.learnAbility(healer.getId(), ability);

        // 施放技能
        abilitySystem.castAbility(healer, ability, soldier);

        // 验证初始冷却时间
        int initialCooldown = abilitySystem.getRemainingCooldown(healer.getId(), ability.getId());
        assertTrue(initialCooldown > 0);

        // 减少冷却
        abilitySystem.reduceCooldowns(healer.getId());

        // 验证冷却时间减少
        int reducedCooldown = abilitySystem.getRemainingCooldown(healer.getId(), ability.getId());
        assertEquals(initialCooldown - 1, reducedCooldown);
    }

    @Test
    void testCastAbilityInsufficientAp() {
        Ability ability = Ability.createBasicHeal();

        abilitySystem.learnAbility(healer.getId(), ability);

        // 消耗所有行动力
        healer.spendAp(12);

        // 验证无法施放
        assertFalse(abilitySystem.canCastAbility(healer, ability, soldier));

        List<EffectResult> results = abilitySystem.castAbility(healer, ability, soldier);
        assertTrue(results.isEmpty());
    }

    @Test
    void testCastAbilityWrongTargetType() {
        Ability healAbility = Ability.createBasicHeal();

        abilitySystem.learnAbility(healer.getId(), healAbility);

        // 治疗技能不能对敌方单位使用
        assertFalse(abilitySystem.canCastAbility(healer, healAbility, enemy));
    }

    @Test
    void testHealEffect() {
        Unit damagedUnit = new BasicUnit(
                4L,
                "Damaged",
                UnitType.INFANTRY,
                UnitStats.createInfantry(),
                Faction.PLAYER
        );
        damagedUnit.takeDamage(50);

        int hpBefore = damagedUnit.getCurrentHp();

        HealEffect heal = new HealEffect("强力治疗", 30);
        EffectResult result = heal.apply(damagedUnit, healer);

        assertTrue(result.isSuccess());
        assertTrue(damagedUnit.getCurrentHp() > hpBefore);
        assertEquals(30, result.getValue());
    }

    @Test
    void testHealEffectOverflow() {
        Unit fullHpUnit = new BasicUnit(
                5L,
                "FullHp",
                UnitType.INFANTRY,
                UnitStats.createInfantry(),
                Faction.PLAYER
        );

        HealEffect heal = new HealEffect("治疗", 30);
        EffectResult result = heal.apply(fullHpUnit, healer);

        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("已满"));
    }

    @Test
    void testDamageEffect() {
        DamageEffect damage = new DamageEffect("强力攻击", 25);
        EffectResult result = damage.apply(enemy, healer);

        assertTrue(result.isSuccess());
        assertTrue(result.getValue() > 0);
    }

    @Test
    void testBuffEffect() {
        BuffEffect buff = new BuffEffect(
                "力量祝福",
                "攻击力 +10",
                BuffEffect.StatType.ATTACK,
                10,
                3
        );

        EffectResult result = buff.apply(soldier, healer);

        assertTrue(result.isSuccess());
        assertEquals(10, result.getValue());
    }

    @Test
    void testBuffEffectStacking() {
        BuffEffect buff1 = new BuffEffect("力量祝福", "攻击力 +5", BuffEffect.StatType.ATTACK, 5);
        BuffEffect buff2 = new BuffEffect("力量祝福", "攻击力 +10", BuffEffect.StatType.ATTACK, 10);
        HealEffect heal = new HealEffect("治疗", 10);

        // 同类型增益可以叠加
        assertTrue(buff1.canStack(buff2));

        // 不同类型效果不能叠加
        assertFalse(buff1.canStack(heal));
    }

    @Test
    void testDamageEffectNotStacking() {
        DamageEffect damage1 = new DamageEffect("攻击", 20);
        DamageEffect damage2 = new DamageEffect("强力攻击", 30);

        // 伤害效果不叠加
        assertFalse(damage1.canStack(damage2));
    }

    @Test
    void testAbilityBuilder() {
        Ability customAbility = Ability.builder()
                .id(999L)
                .name("自定义技能")
                .description("这是一个自定义技能")
                .icon("custom_icon")
                .apCost(5)
                .cooldown(3)
                .range(4)
                .targetType(Action.TargetType.UNIT_FRIENDLY)
                .effect(new HealEffect("治疗", 50))
                .effect(new BuffEffect("增益", "防御 +5", BuffEffect.StatType.DEFENSE, 5))
                .build();

        assertEquals(999L, customAbility.getId());
        assertEquals("自定义技能", customAbility.getName());
        assertEquals(5, customAbility.getApCost());
        assertEquals(3, customAbility.getCooldown());
        assertEquals(4, customAbility.getRange());
        assertEquals(Action.TargetType.UNIT_FRIENDLY, customAbility.getTargetType());
        assertEquals(2, customAbility.getEffects().size());
    }

    @Test
    void testAbilityApplyMultipleEffects() {
        Ability multiEffectAbility = Ability.builder()
                .id(100L)
                .name("多重效果技能")
                .description("同时造成伤害和治疗")
                .icon("multi")
                .apCost(4)
                .range(3)
                .targetType(Action.TargetType.UNIT_HOSTILE)
                .effect(new DamageEffect("伤害", 15))
                .build();

        List<EffectResult> results = multiEffectAbility.apply(healer, enemy);

        assertFalse(results.isEmpty());
    }

    @Test
    void testRegisteredAbilities() {
        // 验证基础技能已注册
        assertNotNull(AbilitySystem.getRegisteredAbility(1L));
        assertNotNull(AbilitySystem.getRegisteredAbility(2L));
        assertNotNull(AbilitySystem.getRegisteredAbility(3L));

        // 验证获取所有技能
        var allAbilities = AbilitySystem.getAllRegisteredAbilities();
        assertTrue(allAbilities.size() >= 3);
    }

    @Test
    void testGetUnitAbilitiesEmpty() {
        List<Ability> abilities = abilitySystem.getUnitAbilities(999L);
        assertTrue(abilities.isEmpty());
    }
}
