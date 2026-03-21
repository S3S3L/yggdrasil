package io.github.s3s3l.yggdrasil.game.core.demo.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EnemyEntity 单元测试
 */
class EnemyEntityTest {

    @Test
    void testConstructorNormal() {
        EnemyEntity enemy = new EnemyEntity(EnemyEntity.EnemyType.NORMAL);

        assertEquals(EntityType.ENEMY, enemy.getType());
        assertEquals(EnemyEntity.EnemyType.NORMAL, enemy.getEnemyType());
        assertEquals(50, enemy.getHealthComponent().getMaxHealth(), 0.01f);
        assertEquals(50, enemy.getBounty());
    }

    @Test
    void testConstructorElite() {
        EnemyEntity enemy = new EnemyEntity(EnemyEntity.EnemyType.ELITE);

        assertEquals(150, enemy.getHealthComponent().getMaxHealth(), 0.01f);
        assertEquals(200, enemy.getBounty());
    }

    @Test
    void testConstructorBoss() {
        EnemyEntity enemy = new EnemyEntity(EnemyEntity.EnemyType.BOSS);

        assertEquals(500, enemy.getHealthComponent().getMaxHealth(), 0.01f);
        assertEquals(1000, enemy.getBounty());
    }

    @Test
    void testConstructorWithName() {
        EnemyEntity enemy = new EnemyEntity("Boss1", EnemyEntity.EnemyType.BOSS);

        assertEquals("Boss1", enemy.getName());
        assertEquals(EnemyEntity.EnemyType.BOSS, enemy.getEnemyType());
    }

    @Test
    void testInitialize() {
        EnemyEntity enemy = new EnemyEntity(EnemyEntity.EnemyType.NORMAL);
        assertFalse(enemy.isActive());

        enemy.initialize();
        assertTrue(enemy.isActive());
    }

    @Test
    void testDestroy() {
        EnemyEntity enemy = new EnemyEntity(EnemyEntity.EnemyType.NORMAL);
        enemy.initialize();
        enemy.destroy();

        assertFalse(enemy.isActive());
    }

    @Test
    void testTakeDamage() {
        EnemyEntity enemy = new EnemyEntity(EnemyEntity.EnemyType.NORMAL);
        enemy.initialize();

        enemy.takeDamage(30);

        assertEquals(20, enemy.getHealthComponent().getCurrentHealth(), 0.01f);
    }

    @Test
    void testIsDead() {
        EnemyEntity enemy = new EnemyEntity(EnemyEntity.EnemyType.NORMAL);
        enemy.initialize();

        assertFalse(enemy.isDead());

        enemy.takeDamage(50);
        assertTrue(enemy.isDead());
    }
}
