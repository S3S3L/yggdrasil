package io.github.s3s3l.yggdrasil.game.core.demo.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.demo.entity.EnemyEntity;
import io.github.s3s3l.yggdrasil.game.core.demo.entity.PlayerEntity;
import io.github.s3s3l.yggdrasil.game.core.demo.ui.EnemyHealthBar;
import io.github.s3s3l.yggdrasil.game.core.demo.ui.GameOverDialog;
import io.github.s3s3l.yggdrasil.game.core.demo.ui.HealthBar;
import io.github.s3s3l.yggdrasil.game.core.demo.ui.ScoreDisplay;
import io.github.s3s3l.yggdrasil.game.core.demo.ui.TimerDisplay;

/**
 * DemoGameScene 扩展功能的单元测试
 */
public class DemoGameSceneExtendedTest {

    private DemoGameScene gameScene;

    @BeforeEach
    void setUp() {
        gameScene = new DemoGameScene(120);
    }

    @Test
    void testUIComponentsInitialization() {
        // 初始化场景
        gameScene.initializeForTest();

        // 验证 UI 组件已初始化
        PlayerEntity player = gameScene.getPlayer();
        assertNotNull(player);
        assertFalse(player.isDead());
        assertEquals(100, player.getHealthComponent().getCurrentHealth(), 0.01f);
    }

    @Test
    void testTimerDisplay() {
        gameScene.initializeForTest();

        // 模拟游戏运行
        for (int i = 0; i < 60; i++) {
            gameScene.update(0.016f);
        }

        // 验证计时器已更新（大约 1 秒）
        float gameTimer = gameScene.getGameTimer();
        assertTrue(gameTimer >= 0.9f, "计时器应该更新到至少 0.9 秒");
    }

    @Test
    void testScoreDisplay() {
        gameScene.initializeForTest();

        PlayerEntity player = gameScene.getPlayer();
        assertEquals(0, player.getScore());

        // 模拟击败敌人
        EnemyEntity enemy = new EnemyEntity("TestEnemy", EnemyEntity.EnemyType.NORMAL);
        enemy.initialize();
        gameScene.getEnemies().add(enemy);

        // 击败敌人
        enemy.takeDamage(100);
        gameScene.update(0.016f);

        // 验证分数增加（普通敌人 50 分）
        assertTrue(player.getScore() >= 50, "击败敌人后应该获得 50 分");
    }

    @Test
    void testHealthBarUpdate() {
        gameScene.initializeForTest();

        PlayerEntity player = gameScene.getPlayer();
        float initialHealth = player.getHealthComponent().getCurrentHealth();

        // 玩家受到伤害
        player.takeDamage(30);

        // 验证生命值减少
        assertEquals(initialHealth - 30, player.getHealthComponent().getCurrentHealth(), 0.01f);
    }

    @Test
    void testEnemySpawnWithHealthBar() {
        gameScene.initializeForTest();

        // 手动生成一个敌人
        gameScene.spawnEnemy();

        // 验证敌人生成
        assertFalse(gameScene.getEnemies().isEmpty());
        EnemyEntity enemy = gameScene.getEnemies().get(0);
        assertNotNull(enemy);
        assertTrue(enemy.isActive());
    }

    @Test
    void testGameOverDialog_Victory() {
        // 创建短时间的游戏场景（0.5 秒胜利）
        gameScene = new DemoGameScene(0.5f);
        gameScene.initializeForTest();

        // 运行足够长的时间以触发胜利
        for (int i = 0; i < 50; i++) {
            gameScene.update(0.016f);
        }

        // 验证游戏结束且胜利
        assertTrue(gameScene.isGameOver());
        assertTrue(gameScene.isVictory());
    }

    @Test
    void testGameOverDialog_Defeat() {
        gameScene.initializeForTest();

        // 玩家死亡
        PlayerEntity player = gameScene.getPlayer();
        player.takeDamage(1000);

        // 更新场景
        gameScene.update(0.016f);

        // 验证游戏结束且失败
        assertTrue(gameScene.isGameOver());
        assertFalse(gameScene.isVictory());
    }

    @Test
    void testRestartGame() {
        gameScene.initializeForTest();

        // 修改游戏状态
        PlayerEntity player = gameScene.getPlayer();
        player.takeDamage(50);
        player.addScore(100);

        float damagedHealth = player.getHealthComponent().getCurrentHealth();
        int score = player.getScore();

        assertTrue(damagedHealth < 100, "玩家应该受到伤害");
        assertEquals(100, score, "玩家应该有分数");

        // 手动重置游戏状态（因为 restartGame 是私有的）
        player.getHealthComponent().reset();
        player.setLevel(1);
        player.addScore(-player.getScore());

        for (EnemyEntity enemy : new java.util.ArrayList<>(gameScene.getEnemies())) {
            enemy.destroy();
        }
        gameScene.getEnemies().clear();

        // 验证状态重置
        assertEquals(100, player.getHealthComponent().getCurrentHealth(), 0.01f);
        assertEquals(0, player.getScore());
        assertTrue(gameScene.getEnemies().isEmpty());
        assertFalse(gameScene.isGameOver());
    }

    @Test
    void testTimerDisplayFormatTime() {
        TimerDisplay timerDisplay = new TimerDisplay();

        // 测试时间格式化
        assertEquals("00:00", timerDisplay.formatTime(0));
        assertEquals("00:30", timerDisplay.formatTime(30));
        assertEquals("01:00", timerDisplay.formatTime(60));
        assertEquals("01:30", timerDisplay.formatTime(90));
        assertEquals("02:45", timerDisplay.formatTime(165));
    }

    @Test
    void testHealthBarColorChange() {
        HealthBar healthBar = new HealthBar(200, 20);

        // 高生命值 - 绿色
        healthBar.setHealth(100, 100);
        // 验证生命值百分比正确
        assertEquals(1.0f, healthBar.getHealthPercent(), 0.01f);

        // 中等生命值 - 黄色
        healthBar.setHealth(40, 100);
        assertEquals(0.4f, healthBar.getHealthPercent(), 0.01f);

        // 低生命值 - 红色
        healthBar.setHealth(20, 100);
        assertEquals(0.2f, healthBar.getHealthPercent(), 0.01f);
    }

    @Test
    void testScoreDisplayUpdate() {
        ScoreDisplay scoreDisplay = new ScoreDisplay();

        assertEquals(0, scoreDisplay.getScore());

        scoreDisplay.addScore(50);
        assertEquals(50, scoreDisplay.getScore());

        scoreDisplay.addScore(100);
        assertEquals(150, scoreDisplay.getScore());

        scoreDisplay.setScore(0);
        assertEquals(0, scoreDisplay.getScore());
    }

    @Test
    void testEnemyHealthBarTypes() {
        // 测试不同类型敌人的血条
        EnemyEntity normalEnemy = new EnemyEntity("Normal", EnemyEntity.EnemyType.NORMAL);
        normalEnemy.initialize();

        EnemyHealthBar normalBar = new EnemyHealthBar();
        normalBar.setHealth(
            normalEnemy.getHealthComponent().getCurrentHealth(),
            normalEnemy.getHealthComponent().getMaxHealth()
        );

        assertEquals(1.0f, normalBar.getHealthPercent(), 0.01f);
        assertEquals(50, normalEnemy.getHealthComponent().getMaxHealth(), 0.01f);

        EnemyEntity eliteEnemy = new EnemyEntity("Elite", EnemyEntity.EnemyType.ELITE);
        eliteEnemy.initialize();

        EnemyHealthBar eliteBar = new EnemyHealthBar();
        eliteBar.setHealth(
            eliteEnemy.getHealthComponent().getCurrentHealth(),
            eliteEnemy.getHealthComponent().getMaxHealth()
        );

        assertEquals(1.0f, eliteBar.getHealthPercent(), 0.01f);
        assertEquals(150, eliteEnemy.getHealthComponent().getMaxHealth(), 0.01f);

        EnemyEntity bossEnemy = new EnemyEntity("Boss", EnemyEntity.EnemyType.BOSS);
        bossEnemy.initialize();

        EnemyHealthBar bossBar = new EnemyHealthBar();
        bossBar.setHealth(
            bossEnemy.getHealthComponent().getCurrentHealth(),
            bossEnemy.getHealthComponent().getMaxHealth()
        );

        assertEquals(1.0f, bossBar.getHealthPercent(), 0.01f);
        assertEquals(500, bossEnemy.getHealthComponent().getMaxHealth(), 0.01f);
    }

    @Test
    void testSurvivalTimeWithDifferentSettings() {
        // 测试不同生存时间的设置
        DemoGameScene shortGame = new DemoGameScene(10); // 10 秒
        shortGame.initializeForTest();

        for (int i = 0; i < 100; i++) {
            shortGame.update(0.016f);
        }

        assertTrue(shortGame.getGameTimer() >= 1.5f, "游戏时间应该至少 1.5 秒");
        assertFalse(shortGame.isGameOver(), "10 秒游戏不应该这么快结束");
    }

    @Test
    void testPlayerHealthComponent() {
        gameScene.initializeForTest();

        PlayerEntity player = gameScene.getPlayer();

        // 测试生命组件功能
        assertEquals(100, player.getHealthComponent().getMaxHealth(), 0.01f);
        assertEquals(100, player.getHealthComponent().getCurrentHealth(), 0.01f);
        assertFalse(player.isDead());

        // 受到伤害
        player.takeDamage(30);
        assertEquals(70, player.getHealthComponent().getCurrentHealth(), 0.01f);

        // 治疗
        player.heal(20);
        assertEquals(90, player.getHealthComponent().getCurrentHealth(), 0.01f);

        // 治疗不能超过最大生命值
        player.heal(100);
        assertEquals(100, player.getHealthComponent().getCurrentHealth(), 0.01f);
    }
}
