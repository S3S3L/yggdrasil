package io.github.s3s3l.yggdrasil.game.core.demo.scene;

import io.github.s3s3l.yggdrasil.game.core.demo.entity.EnemyEntity;
import io.github.s3s3l.yggdrasil.game.core.demo.entity.PlayerEntity;
import io.github.s3s3l.yggdrasil.game.core.demo.entity.GameAction;
import io.github.s3s3l.yggdrasil.game.core.demo.input.GameInputProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DemoGameScene 单元测试
 */
class DemoGameSceneTest {

    private DemoGameScene gameScene;

    @BeforeEach
    void setUp() {
        gameScene = new DemoGameScene(60); // 60 秒胜利
    }

    @Test
    void testConstructor() {
        assertNotNull(gameScene);
        assertEquals("DemoGame", gameScene.getName());
    }

    @Test
    void testConstructorWithSurvivalTime() {
        DemoGameScene scene = new DemoGameScene(120);
        assertNotNull(scene);
    }

    @Test
    void testOnEnter() {
        gameScene.initializeForTest();

        assertNotNull(gameScene.getPlayer());
        assertNotNull(gameScene.getInputProcessor());
        assertTrue(gameScene.getPlayer().isActive());
    }

    @Test
    void testPlayerAttack() {
        gameScene.initializeForTest();

        PlayerEntity player = gameScene.getPlayer();
        assertNotNull(player);

        // 生成一个在攻击范围内的敌人
        EnemyEntity enemy = new EnemyEntity("TestEnemy", EnemyEntity.EnemyType.NORMAL);
        enemy.initialize();
        enemy.setPosition(10, 0); // 在攻击范围内（攻击范围 50）
        gameScene.getEnemies().add(enemy);

        // 执行攻击
        gameScene.playerAttack();

        // 敌人应该受到伤害
        assertTrue(enemy.getHealthComponent().getCurrentHealth() < enemy.getHealthComponent().getMaxHealth());
    }

    @Test
    void testPlayerAttackCooldown() {
        gameScene.initializeForTest();

        PlayerEntity player = gameScene.getPlayer();

        // 第一次攻击应该成功
        boolean firstAttack = player.tryAttack();
        assertTrue(firstAttack);

        // 立即第二次攻击应该失败（冷却中）
        boolean secondAttack = player.tryAttack();
        assertFalse(secondAttack);
    }

    @Test
    void testPlayerUseSkill1_Heal() {
        gameScene.initializeForTest();

        PlayerEntity player = gameScene.getPlayer();
        float initialHealth = player.getHealthComponent().getCurrentHealth();

        // 先受到伤害
        player.takeDamage(30);
        float damagedHealth = player.getHealthComponent().getCurrentHealth();
        assertEquals(initialHealth - 30, damagedHealth, 0.01f);

        // 使用技能 1 治疗
        gameScene.playerUseSkill(1);

        // 生命值应该恢复
        assertTrue(player.getHealthComponent().getCurrentHealth() > damagedHealth);
    }

    @Test
    void testPlayerUseSkill2_AOE() {
        gameScene.initializeForTest();

        // 生成多个敌人
        EnemyEntity enemy1 = new EnemyEntity("Enemy1", EnemyEntity.EnemyType.NORMAL);
        EnemyEntity enemy2 = new EnemyEntity("Enemy2", EnemyEntity.EnemyType.NORMAL);
        enemy1.initialize();
        enemy2.initialize();
        gameScene.getEnemies().add(enemy1);
        gameScene.getEnemies().add(enemy2);

        float initialHealth1 = enemy1.getHealthComponent().getCurrentHealth();
        float initialHealth2 = enemy2.getHealthComponent().getCurrentHealth();

        // 使用技能 2（范围攻击）
        gameScene.playerUseSkill(2);

        // 两个敌人都应该受到伤害
        assertTrue(enemy1.getHealthComponent().getCurrentHealth() < initialHealth1);
        assertTrue(enemy2.getHealthComponent().getCurrentHealth() < initialHealth2);
    }

    @Test
    void testPlayerUseSkill3_Buff() {
        gameScene.initializeForTest();

        PlayerEntity player = gameScene.getPlayer();

        // 先攻击进入冷却
        player.tryAttack();
        float cooldown = player.getAttackComponent().getCurrentCooldown();
        assertTrue(cooldown > 0);

        // 使用技能 3 重置冷却
        gameScene.playerUseSkill(3);

        // 冷却应该重置
        assertEquals(0, player.getAttackComponent().getCurrentCooldown(), 0.01f);
    }

    @Test
    void testEnemyAI_TrackPlayer() {
        gameScene.initializeForTest();

        PlayerEntity player = gameScene.getPlayer();
        EnemyEntity enemy = new EnemyEntity("TestEnemy", EnemyEntity.EnemyType.NORMAL);
        enemy.initialize();
        enemy.setPosition(100, 100); // 远离玩家
        gameScene.getEnemies().add(enemy);

        // 更新敌人 AI 和敌人（模拟多帧）
        for (int i = 0; i < 100; i++) {
            gameScene.updateEnemyAI(enemy, 0.016f);
            enemy.update(0.016f);
        }

        // 敌人应该向玩家移动（位置应该更接近玩家）
        assertTrue(enemy.getX() < 100, "Enemy X should be less than 100");
        assertTrue(enemy.getY() < 100, "Enemy Y should be less than 100");
    }

    @Test
    void testEnemyAI_AttackPlayer() {
        gameScene.initializeForTest();

        PlayerEntity player = gameScene.getPlayer();
        EnemyEntity enemy = new EnemyEntity("TestEnemy", EnemyEntity.EnemyType.NORMAL);
        enemy.initialize();
        enemy.setPosition(5, 0); // 在攻击范围内
        gameScene.getEnemies().add(enemy);

        float initialHealth = player.getHealthComponent().getCurrentHealth();

        // 敌人 AI 攻击
        gameScene.enemyAIAttack(enemy);

        // 玩家应该受到伤害
        assertTrue(player.getHealthComponent().getCurrentHealth() < initialHealth);
    }

    @Test
    void testCheckGameOver_PlayerDead() {
        gameScene.initializeForTest();

        PlayerEntity player = gameScene.getPlayer();

        // 玩家死亡
        player.takeDamage(100);
        assertTrue(player.isDead());

        // 检查游戏结束
        gameScene.checkGameOver();

        assertTrue(gameScene.isGameOver());
        assertFalse(gameScene.isVictory());
    }

    @Test
    void testCheckGameOver_Victory() {
        gameScene = new DemoGameScene(0.5f); // 0.5 秒胜利（快速测试）
        gameScene.initializeForTest();

        // 模拟时间流逝（通过 update 累加时间）
        // 0.5 秒 / 0.016s 每帧 = 约 32 帧
        for (int i = 0; i < 50; i++) {
            gameScene.update(0.016f);
        }

        // 检查游戏结束
        gameScene.checkGameOver();

        assertTrue(gameScene.isGameOver(), "Game should be over");
        assertTrue(gameScene.isVictory(), "Should be victory");
    }

    @Test
    void testCheckGameOver_NotOver() {
        gameScene.initializeForTest();

        // 游戏刚开始，不应结束
        gameScene.checkGameOver();

        assertFalse(gameScene.isGameOver());
    }

    @Test
    void testSpawnEnemy() {
        gameScene.initializeForTest();

        int initialEnemyCount = gameScene.getEnemies().size();

        // 生成敌人
        gameScene.spawnEnemy();

        // 敌人数量应该增加
        assertEquals(initialEnemyCount + 1, gameScene.getEnemies().size());
    }

    @Test
    void testSpawnEnemyMaxLimit() {
        gameScene.initializeForTest();

        // 生成到最大数量
        for (int i = 0; i < 10; i++) {
            gameScene.spawnEnemy();
        }

        int maxCount = gameScene.getEnemies().size();

        // 尝试再生成
        gameScene.spawnEnemy();

        // 数量不应超过最大值
        assertEquals(maxCount, gameScene.getEnemies().size());
    }

    @Test
    void testOnEnemyDefeated_AddScore() {
        gameScene.initializeForTest();

        PlayerEntity player = gameScene.getPlayer();
        int initialScore = player.getScore();

        EnemyEntity enemy = new EnemyEntity(EnemyEntity.EnemyType.NORMAL);
        int bounty = enemy.getBounty();

        // 敌人被击败
        gameScene.onEnemyDefeated(enemy);

        // 玩家分数应该增加
        assertEquals(initialScore + bounty, player.getScore());
    }

    @Test
    void testGetters() {
        gameScene.initializeForTest();

        assertNotNull(gameScene.getPlayer());
        assertNotNull(gameScene.getEnemies());
        assertNotNull(gameScene.getInputProcessor());
        assertFalse(gameScene.isGameOver());
        assertFalse(gameScene.isVictory());
    }
}
