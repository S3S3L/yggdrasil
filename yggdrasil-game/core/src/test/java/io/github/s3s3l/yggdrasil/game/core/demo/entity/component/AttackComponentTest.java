package io.github.s3s3l.yggdrasil.game.core.demo.entity.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AttackComponent 单元测试
 */
class AttackComponentTest {

    private AttackComponent attackComponent;

    @BeforeEach
    void setUp() {
        attackComponent = new AttackComponent(10, 50, 1.0f);
    }

    @Test
    void testConstructor() {
        assertEquals(10, attackComponent.getAttackDamage(), 0.01f);
        assertEquals(50, attackComponent.getAttackRange(), 0.01f);
        assertEquals(1.0f, attackComponent.getBaseAttackCooldown(), 0.01f);
        assertTrue(attackComponent.canAttack());
    }

    @Test
    void testSetAttackDamage() {
        attackComponent.setAttackDamage(20);
        assertEquals(20, attackComponent.getAttackDamage(), 0.01f);
    }

    @Test
    void testSetAttackRange() {
        attackComponent.setAttackRange(100);
        assertEquals(100, attackComponent.getAttackRange(), 0.01f);
    }

    @Test
    void testSetAttackCooldown() {
        attackComponent.setAttackCooldown(2.0f);
        assertEquals(2.0f, attackComponent.getBaseAttackCooldown(), 0.01f);
    }

    @Test
    void testTryAttack() {
        assertTrue(attackComponent.tryAttack());
        assertFalse(attackComponent.canAttack());
    }

    @Test
    void testTryAttackOnCooldown() {
        attackComponent.tryAttack();
        assertFalse(attackComponent.tryAttack());
    }

    @Test
    void testUpdate() {
        attackComponent.tryAttack();
        attackComponent.update(0.5f);

        assertEquals(0.5f, attackComponent.getCurrentCooldown(), 0.01f);
        assertFalse(attackComponent.canAttack());
    }

    @Test
    void testUpdateCooldownComplete() {
        attackComponent.tryAttack();
        attackComponent.update(1.0f);

        assertEquals(0, attackComponent.getCurrentCooldown(), 0.01f);
        assertTrue(attackComponent.canAttack());
    }

    @Test
    void testCooldownProgress() {
        assertEquals(1, attackComponent.getCooldownProgress(), 0.01f);

        attackComponent.tryAttack();
        assertEquals(0, attackComponent.getCooldownProgress(), 0.01f);

        attackComponent.update(0.5f);
        assertEquals(0.5f, attackComponent.getCooldownProgress(), 0.01f);
    }

    @Test
    void testResetCooldown() {
        attackComponent.tryAttack();
        attackComponent.resetCooldown();

        assertEquals(0, attackComponent.getCurrentCooldown(), 0.01f);
        assertTrue(attackComponent.canAttack());
    }

    @Test
    void testAttackSpeedBonus() {
        attackComponent.setAttackSpeedBonus(1.0f); // +100% 攻速
        assertEquals(0.5f, attackComponent.getEffectiveCooldown(), 0.01f);
    }

    @Test
    void testAttackSpeedBonusZero() {
        attackComponent.setAttackSpeedBonus(0);
        assertEquals(1.0f, attackComponent.getEffectiveCooldown(), 0.01f);
    }

    @Test
    void testNegativeAttackSpeedBonus() {
        attackComponent.setAttackSpeedBonus(-0.5f);
        assertEquals(0, attackComponent.getAttackSpeedBonus(), 0.01f);
    }

    @Test
    void testEffectiveCooldownWithBonus() {
        attackComponent.setAttackSpeedBonus(0.5f); // +50% 攻速
        float expectedCooldown = 1.0f / 1.5f;
        assertEquals(expectedCooldown, attackComponent.getEffectiveCooldown(), 0.01f);
    }
}
