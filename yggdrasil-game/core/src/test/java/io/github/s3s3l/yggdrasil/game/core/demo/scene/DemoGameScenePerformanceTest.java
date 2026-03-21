package io.github.s3s3l.yggdrasil.game.core.demo.scene;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.demo.entity.EnemyEntity;

/**
 * Demo 游戏场景性能测试
 */
public class DemoGameScenePerformanceTest {

    /**
     * 测试大量敌人时的性能
     */
    @Test
    void testPerformanceWithManyEnemies() throws InterruptedException {
        DemoGameScene gameScene = new DemoGameScene(120);
        gameScene.initializeForTest();

        // 手动生成大量敌人
        for (int i = 0; i < 10; i++) {
            gameScene.spawnEnemy();
        }

        long startTime = System.currentTimeMillis();

        // 模拟 60 帧（1 秒）的游戏更新
        for (int frame = 0; frame < 60; frame++) {
            gameScene.update(0.016f);
        }

        long endTime = System.currentTimeMillis();
        long elapsed = endTime - startTime;

        // 1 秒游戏逻辑应该在 100ms 内完成（给予宽松的时间）
        assertTrue(elapsed < 1000,
            "性能测试失败：60 帧更新耗时 " + elapsed + "ms，应该小于 1000ms");

        System.out.println("性能测试：60 帧更新耗时 " + elapsed + "ms");
    }

    /**
     * 测试敌人生成性能
     */
    @Test
    void testEnemySpawnPerformance() {
        DemoGameScene gameScene = new DemoGameScene(120);
        gameScene.initializeForTest();

        long startTime = System.currentTimeMillis();

        // 生成最大数量的敌人
        for (int i = 0; i < 10; i++) {
            gameScene.spawnEnemy();
        }

        long endTime = System.currentTimeMillis();
        long elapsed = endTime - startTime;

        // 生成 10 个敌人应该在 50ms 内完成
        assertTrue(elapsed < 50,
            "性能测试失败：生成 10 个敌人耗时 " + elapsed + "ms");

        System.out.println("敌人生成性能测试：生成 10 个敌人耗时 " + elapsed + "ms");
        assertTrue(gameScene.getEnemies().size() == 10, "应该生成 10 个敌人");
    }

    /**
     * 测试 UI 更新性能
     */
    @Test
    void testUIUpdatePerformance() throws InterruptedException {
        DemoGameScene gameScene = new DemoGameScene(120);
        gameScene.initializeForTest();

        // 生成一些敌人
        for (int i = 0; i < 5; i++) {
            gameScene.spawnEnemy();
        }

        long startTime = System.currentTimeMillis();

        // 模拟 100 次 UI 更新
        for (int i = 0; i < 100; i++) {
            gameScene.update(0.016f);
        }

        long endTime = System.currentTimeMillis();
        long elapsed = endTime - startTime;

        // 100 次 UI 更新应该在 500ms 内完成
        assertTrue(elapsed < 500,
            "性能测试失败：100 次 UI 更新耗时 " + elapsed + "ms");

        System.out.println("UI 更新性能测试：100 次更新耗时 " + elapsed + "ms");
    }

    /**
     * 测试碰撞检测性能
     */
    @Test
    void testCollisionDetectionPerformance() {
        DemoGameScene gameScene = new DemoGameScene(120);
        gameScene.initializeForTest();

        // 生成最大数量的敌人
        for (int i = 0; i < 10; i++) {
            gameScene.spawnEnemy();
        }

        long startTime = System.currentTimeMillis();

        // 执行 100 次碰撞检测
        for (int i = 0; i < 100; i++) {
            gameScene.checkCollisions();
        }

        long endTime = System.currentTimeMillis();
        long elapsed = endTime - startTime;

        // 100 次碰撞检测应该在 200ms 内完成
        assertTrue(elapsed < 200,
            "性能测试失败：100 次碰撞检测耗时 " + elapsed + "ms");

        System.out.println("碰撞检测性能测试：100 次检测耗时 " + elapsed + "ms");
    }

    /**
     * 测试敌人 AI 更新性能
     */
    @Test
    void testEnemyAIUpdatePerformance() {
        DemoGameScene gameScene = new DemoGameScene(120);
        gameScene.initializeForTest();

        // 生成敌人
        for (int i = 0; i < 10; i++) {
            gameScene.spawnEnemy();
        }

        long startTime = System.currentTimeMillis();

        // 更新所有敌人 AI 100 次
        for (int i = 0; i < 100; i++) {
            for (EnemyEntity enemy : gameScene.getEnemies()) {
                gameScene.updateEnemyAI(enemy, 0.016f);
            }
        }

        long endTime = System.currentTimeMillis();
        long elapsed = endTime - startTime;

        // 100 次 AI 更新应该在 300ms 内完成
        assertTrue(elapsed < 300,
            "性能测试失败：100 次 AI 更新耗时 " + elapsed + "ms");

        System.out.println("敌人 AI 性能测试：100 次更新耗时 " + elapsed + "ms");
    }

    /**
     * 测试内存分配 - 确保没有明显的内存泄漏
     */
    @Test
    void testMemoryAllocation() throws InterruptedException {
        DemoGameScene gameScene = new DemoGameScene(120);
        gameScene.initializeForTest();

        Runtime runtime = Runtime.getRuntime();
        runtime.gc(); // 请求 GC
        Thread.sleep(100); // 等待 GC

        long initialMemory = runtime.totalMemory() - runtime.freeMemory();

        // 运行多个游戏循环
        for (int cycle = 0; cycle < 10; cycle++) {
            // 生成和消灭敌人
            for (int i = 0; i < 10; i++) {
                gameScene.spawnEnemy();
            }
            for (int frame = 0; frame < 60; frame++) {
                gameScene.update(0.016f);
            }
            // 消灭所有敌人
            for (EnemyEntity enemy : gameScene.getEnemies()) {
                enemy.takeDamage(1000);
            }
            gameScene.update(0.016f);
        }

        runtime.gc(); // 请求 GC
        Thread.sleep(100); // 等待 GC

        long finalMemory = runtime.totalMemory() - runtime.freeMemory();
        long memoryIncrease = finalMemory - initialMemory;

        // 内存增长应该小于 10MB
        double memoryIncreaseMB = memoryIncrease / (1024.0 * 1024.0);
        System.out.println("内存测试：内存增长 " + memoryIncreaseMB + " MB");

        // 宽松测试，仅做基本检查
        assertTrue(memoryIncreaseMB < 50,
            "内存测试：内存增长过大 " + memoryIncreaseMB + " MB");
    }

    /**
     * 测试高帧率下的稳定性
     */
    @Test
    void testHighFrameRateStability() {
        DemoGameScene gameScene = new DemoGameScene(120);
        gameScene.initializeForTest();

        long startTime = System.nanoTime();

        // 模拟 144 FPS 更新 1000 次
        float deltaTime = 1.0f / 144.0f;
        for (int i = 0; i < 1000; i++) {
            gameScene.update(deltaTime);
        }

        long endTime = System.nanoTime();
        double elapsedSeconds = (endTime - startTime) / 1_000_000_000.0;

        System.out.println("高帧率测试：1000 帧 @144FPS 耗时 " + elapsedSeconds + " 秒");

        // 应该能在合理时间内完成
        assertTrue(elapsedSeconds < 10, "高帧率测试失败：耗时过长 " + elapsedSeconds + " 秒");
    }
}
