package io.github.s3s3l.yggdrasil.game.core.unit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.grid.SquareCoordinate;

/**
 * Unit 单元测试
 */
class UnitTest {

    @Test
    void testCreateUnit() {
        UnitStats stats = UnitStats.createInfantry();
        Unit unit = new BasicUnit(1L, "Test Soldier", UnitType.INFANTRY, stats, Faction.PLAYER);

        assertEquals(1L, unit.getId());
        assertEquals("Test Soldier", unit.getName());
        assertEquals(UnitType.INFANTRY, unit.getUnitType());
        assertEquals(Faction.PLAYER, unit.getFaction());
        assertEquals(stats.getMaxHp(), unit.getCurrentHp());
        assertEquals(stats.getMaxAp(), unit.getCurrentAp());
    }

    @Test
    void testTakeDamage() {
        Unit unit = new BasicUnit(1L, "Soldier", UnitType.INFANTRY, UnitStats.createInfantry(), Faction.PLAYER);

        int damage = unit.takeDamage(25);

        assertTrue(damage > 0);
        assertEquals(100 - damage, unit.getCurrentHp());
    }

    @Test
    void testTakeDamageWithDefense() {
        UnitStats stats = UnitStats.builder()
                .maxHp(100)
                .defense(15)
                .build();
        Unit unit = new BasicUnit(1L, "Tank", UnitType.TANK, stats, Faction.PLAYER);

        int damage = unit.takeDamage(20);

        // 20 - 15 = 5 点伤害（至少造成 1 点伤害）
        assertEquals(5, damage);
        assertEquals(95, unit.getCurrentHp());
    }

    @Test
    void testTakeDamageMinimumOne() {
        UnitStats stats = UnitStats.builder()
                .maxHp(100)
                .defense(100)
                .build();
        Unit unit = new BasicUnit(1L, "Armored", UnitType.TANK, stats, Faction.PLAYER);

        int damage = unit.takeDamage(5);

        // 至少造成 1 点伤害
        assertEquals(1, damage);
    }

    @Test
    void testHeal() {
        Unit unit = new BasicUnit(1L, "Soldier", UnitType.INFANTRY, UnitStats.createInfantry(), Faction.PLAYER);
        unit.takeDamage(50); // 实际伤害 35 (50-15 防御), 剩余 65 HP

        unit.heal(30);

        assertEquals(95, unit.getCurrentHp());
    }

    @Test
    void testHealOverflow() {
        Unit unit = new BasicUnit(1L, "Soldier", UnitType.INFANTRY, UnitStats.createInfantry(), Faction.PLAYER);
        unit.takeDamage(20);

        unit.heal(50);

        assertEquals(100, unit.getCurrentHp());
    }

    @Test
    void testSpendAp() {
        Unit unit = new BasicUnit(1L, "Soldier", UnitType.INFANTRY, UnitStats.createInfantry(), Faction.PLAYER);

        boolean result = unit.spendAp(5);

        assertTrue(result);
        assertEquals(5, unit.getCurrentAp());
    }

    @Test
    void testSpendApInsufficient() {
        Unit unit = new BasicUnit(1L, "Soldier", UnitType.INFANTRY, UnitStats.createInfantry(), Faction.PLAYER);

        boolean result = unit.spendAp(15);

        assertFalse(result);
        assertEquals(10, unit.getCurrentAp());
    }

    @Test
    void testResetAp() {
        Unit unit = new BasicUnit(1L, "Soldier", UnitType.INFANTRY, UnitStats.createInfantry(), Faction.PLAYER);
        unit.spendAp(7);

        unit.resetAp();

        assertEquals(10, unit.getCurrentAp());
    }

    @Test
    void testIsAlive() {
        UnitStats stats = UnitStats.builder().maxHp(50).build();
        Unit unit = new BasicUnit(1L, "Soldier", UnitType.INFANTRY, stats, Faction.PLAYER);

        assertTrue(unit.isAlive());

        unit.takeDamage(50);

        assertFalse(unit.isAlive());
    }

    @Test
    void testIsOfFaction() {
        Unit unit = new BasicUnit(1L, "Soldier", UnitType.INFANTRY, UnitStats.createInfantry(), Faction.PLAYER);

        assertTrue(unit.isOfFaction(Faction.PLAYER));
        assertFalse(unit.isOfFaction(Faction.ENEMY));
    }

    @Test
    void testUnitTypeAdvantage() {
        // 步兵克制弓箭手
        assertTrue(UnitType.INFANTRY.hasAdvantageOver(UnitType.ARCHER));
        // 弓箭手克制骑兵
        assertTrue(UnitType.ARCHER.hasAdvantageOver(UnitType.CAVALRY));
        // 骑兵克制步兵
        assertTrue(UnitType.CAVALRY.hasAdvantageOver(UnitType.INFANTRY));
        // 枪兵克制骑兵
        assertTrue(UnitType.LANCER.hasAdvantageOver(UnitType.CAVALRY));
    }

    @Test
    void testTypeModifier() {
        // 克制时 1.5 倍伤害
        assertEquals(1.5f, UnitType.getTypeModifier(UnitType.INFANTRY, UnitType.ARCHER));
        // 被克时 0.8 倍伤害
        assertEquals(0.8f, UnitType.getTypeModifier(UnitType.ARCHER, UnitType.INFANTRY));
        // 正常伤害
        assertEquals(1.0f, UnitType.getTypeModifier(UnitType.INFANTRY, UnitType.INFANTRY));
    }

    @Test
    void testFactionRelationships() {
        // 玩家和盟友友好
        assertTrue(Faction.PLAYER.isFriendlyWith(Faction.ALLY));
        assertTrue(Faction.ALLY.isFriendlyWith(Faction.PLAYER));

        // 玩家和敌人敌对
        assertTrue(Faction.PLAYER.isHostileTo(Faction.ENEMY));
        assertTrue(Faction.ENEMY.isHostileTo(Faction.PLAYER));

        // 中立与所有阵营非敌对（除非特别定义）
        assertFalse(Faction.NEUTRAL.isHostileTo(Faction.PLAYER));
    }

    @Test
    void testUnitPosition() {
        Unit unit = new BasicUnit(1L, "Soldier", UnitType.INFANTRY, UnitStats.createInfantry(),
                Faction.PLAYER, SquareCoordinate.of(5, 5));

        assertEquals(5, unit.getPosition().getX());
        assertEquals(5, unit.getPosition().getY());

        unit.setPosition(SquareCoordinate.of(10, 10));

        assertEquals(10, unit.getPosition().getX());
        assertEquals(10, unit.getPosition().getY());
    }

    @Test
    void testUnitStatsPresets() {
        UnitStats infantry = UnitStats.createInfantry();
        UnitStats cavalry = UnitStats.createCavalry();
        UnitStats archer = UnitStats.createArcher();

        // 验证属性差异
        assertTrue(cavalry.getMovement() > infantry.getMovement());
        assertTrue(archer.getRange() > infantry.getRange());
        assertTrue(archer.getVision() > infantry.getVision());
    }
}
