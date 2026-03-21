package io.github.s3s3l.yggdrasil.game.core.demo.entity.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * HealthComponent 单元测试
 */
class HealthComponentTest {

    private HealthComponent healthComponent;

    @BeforeEach
    void setUp() {
        healthComponent = new HealthComponent(100);
    }

    @Test
    void testConstructor() {
        assertEquals(100, healthComponent.getCurrentHealth(), 0.01f);
        assertEquals(100, healthComponent.getMaxHealth(), 0.01f);
        assertEquals(1.0f, healthComponent.getHealthPercent(), 0.01f);
        assertFalse(healthComponent.isDead());
    }

    @Test
    void testDamage() {
        healthComponent.damage(30);
        assertEquals(70, healthComponent.getCurrentHealth(), 0.01f);
        assertFalse(healthComponent.isDead());
    }

    @Test
    void testDamageGreaterThanCurrent() {
        healthComponent.damage(150);
        assertEquals(0, healthComponent.getCurrentHealth(), 0.01f);
        assertTrue(healthComponent.isDead());
    }

    @Test
    void testHeal() {
        healthComponent.damage(50);
        healthComponent.heal(30);
        assertEquals(80, healthComponent.getCurrentHealth(), 0.01f);
    }

    @Test
    void testHealAboveMax() {
        healthComponent.heal(50);
        assertEquals(100, healthComponent.getCurrentHealth(), 0.01f);
    }

    @Test
    void testSetMaxHealth() {
        healthComponent.setMaxHealth(150);
        assertEquals(150, healthComponent.getMaxHealth(), 0.01f);
        assertEquals(100, healthComponent.getCurrentHealth(), 0.01f);
    }

    @Test
    void testSetMaxHealthBelowCurrent() {
        healthComponent.damage(30);
        healthComponent.setMaxHealth(50);
        assertEquals(50, healthComponent.getMaxHealth(), 0.01f);
        assertEquals(50, healthComponent.getCurrentHealth(), 0.01f);
    }

    @Test
    void testHealthPercent() {
        healthComponent.damage(50);
        assertEquals(0.5f, healthComponent.getHealthPercent(), 0.01f);
    }

    @Test
    void testHealthPercentWithZeroMax() {
        healthComponent.setMaxHealth(0);
        assertEquals(0, healthComponent.getHealthPercent(), 0.01f);
    }

    @Test
    void testReset() {
        healthComponent.damage(50);
        healthComponent.reset();
        assertEquals(100, healthComponent.getCurrentHealth(), 0.01f);
    }

    @Test
    void testDeathListener() {
        boolean[] deathCalled = new boolean[]{false};
        healthComponent.setOnDeath(() -> deathCalled[0] = true);

        healthComponent.damage(100);
        assertTrue(deathCalled[0]);
    }

    @Test
    void testHealthChangedListener() {
        int[] changeCount = new int[]{0};
        healthComponent.setOnHealthChanged(() -> changeCount[0]++);

        healthComponent.damage(10);
        assertEquals(1, changeCount[0]);

        healthComponent.heal(5);
        assertEquals(2, changeCount[0]);
    }

    @Test
    void testNegativeDamage() {
        healthComponent.damage(-10);
        assertEquals(100, healthComponent.getCurrentHealth(), 0.01f);
    }

    @Test
    void testNegativeHeal() {
        healthComponent.heal(-10);
        assertEquals(100, healthComponent.getCurrentHealth(), 0.01f);
    }
}
