package io.github.s3s3l.yggdrasil.game.core.demo.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PlayerEntity 单元测试
 */
class PlayerEntityTest {

    private PlayerEntity player;

    @BeforeEach
    void setUp() {
        player = new PlayerEntity("TestPlayer");
    }

    @Test
    void testConstructor() {
        assertEquals("TestPlayer", player.getName());
        assertEquals(EntityType.PLAYER, player.getType());
        assertEquals(0, player.getScore());
        assertEquals(1, player.getLevel());
    }

    @Test
    void testDefaultConstructor() {
        PlayerEntity p = new PlayerEntity();
        assertEquals("Player", p.getName());
    }

    @Test
    void testAddScore() {
        player.addScore(100);
        assertEquals(100, player.getScore());

        player.addScore(50);
        assertEquals(150, player.getScore());
    }

    @Test
    void testSetLevel() {
        player.setLevel(5);
        assertEquals(5, player.getLevel());
    }

    @Test
    void testLevelUp() {
        int initialLevel = player.getLevel();
        float initialMaxHealth = player.getHealthComponent().getMaxHealth();
        float initialAttackDamage = player.getAttackComponent().getAttackDamage();

        player.levelUp();

        assertEquals(initialLevel + 1, player.getLevel());
        assertEquals(initialMaxHealth + 20, player.getHealthComponent().getMaxHealth(), 0.01f);
        assertEquals(initialMaxHealth + 20, player.getHealthComponent().getCurrentHealth(), 0.01f);
        assertEquals(initialAttackDamage + 2, player.getAttackComponent().getAttackDamage(), 0.01f);
    }

    @Test
    void testInitialize() {
        PlayerEntity p = new PlayerEntity();
        assertFalse(p.isActive());

        p.initialize();
        assertTrue(p.isActive());
    }

    @Test
    void testDestroy() {
        player.initialize();
        player.addScore(100);
        player.destroy();

        assertFalse(player.isActive());
    }

    @Test
    void testTakeDamage() {
        player.initialize();
        float initialHealth = player.getHealthComponent().getCurrentHealth();

        player.takeDamage(30);

        assertEquals(initialHealth - 30, player.getHealthComponent().getCurrentHealth(), 0.01f);
    }

    @Test
    void testHeal() {
        player.initialize();
        player.takeDamage(50);

        player.heal(30);

        assertEquals(80, player.getHealthComponent().getCurrentHealth(), 0.01f);
    }

    @Test
    void testTryAttack() {
        player.initialize();
        assertTrue(player.tryAttack());
    }

    @Test
    void testMoveTo() {
        player.initialize();
        player.moveTo(100, 50);

        assertTrue(player.getMovementComponent().isMoving());
    }

    @Test
    void testStopMoving() {
        player.initialize();
        player.moveTo(100, 50);
        player.stopMoving();

        assertFalse(player.getMovementComponent().isMoving());
    }

    @Test
    void testSetPosition() {
        player.initialize();
        player.setPosition(50, 60);

        assertEquals(50, player.getX(), 0.01f);
        assertEquals(60, player.getY(), 0.01f);
    }

    @Test
    void testIsDead() {
        player.initialize();
        assertFalse(player.isDead());

        player.takeDamage(100);
        assertTrue(player.isDead());
    }
}
