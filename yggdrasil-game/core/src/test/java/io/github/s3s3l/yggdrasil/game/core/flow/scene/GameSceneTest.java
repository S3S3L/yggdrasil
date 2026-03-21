package io.github.s3s3l.yggdrasil.game.core.flow.scene;

import io.github.s3s3l.yggdrasil.game.core.flow.GameManager;
import io.github.s3s3l.yggdrasil.game.core.flow.GameState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GameScene 单元测试
 */
class GameSceneTest {

    @BeforeEach
    void setUp() {
        GameManager.resetInstance();
    }

    @AfterEach
    void tearDown() {
        GameManager.resetInstance();
    }

    @Test
    void testConstructor() {
        GameScene scene = new GameScene();
        assertEquals("Game", scene.getName());
        assertEquals(0, scene.getGameTime(), 0.01f);
        assertEquals(1.0f, scene.getTimeScale(), 0.01f);
    }

    @Test
    void testConstructorWithName() {
        GameScene scene = new GameScene("MyGame");
        assertEquals("MyGame", scene.getName());
    }

    @Test
    void testOnEnter() {
        GameScene scene = new GameScene();
        scene.onEnter();

        assertTrue(scene.isActive());
        assertEquals(GameState.PLAYING, GameManager.getInstance().getCurrentState());
    }

    @Test
    void testUpdate() {
        GameScene scene = new GameScene();
        scene.onEnter();

        scene.update(0.5f);
        assertEquals(0.5f, scene.getGameTime(), 0.01f);

        scene.update(0.5f);
        assertEquals(1.0f, scene.getGameTime(), 0.01f);
    }

    @Test
    void testUpdateWithTimeScale() {
        GameScene scene = new GameScene();
        scene.setTimeScale(2.0f);
        scene.onEnter();

        scene.update(0.5f);
        assertEquals(1.0f, scene.getGameTime(), 0.01f);
    }

    @Test
    void testUpdateWhenPaused() {
        GameScene scene = new GameScene();
        scene.onEnter();
        scene.pause();  // 使用公共方法 pause() 来暂停

        float initialTime = scene.getGameTime();
        scene.update(0.5f);

        // 暂停时游戏时间不应增加
        assertEquals(initialTime, scene.getGameTime(), 0.01f);
    }

    @Test
    void testSetGameTime() {
        GameScene scene = new GameScene();
        scene.setGameTime(100.5f);
        assertEquals(100.5f, scene.getGameTime(), 0.01f);
    }

    @Test
    void testSetTimeScale() {
        GameScene scene = new GameScene();
        scene.setTimeScale(0.5f);
        assertEquals(0.5f, scene.getTimeScale(), 0.01f);
    }

    @Test
    void testSetTimeScaleZero() {
        GameScene scene = new GameScene();
        scene.setTimeScale(0);
        assertEquals(0, scene.getTimeScale(), 0.01f);
    }

    @Test
    void testSetNegativeTimeScale() {
        GameScene scene = new GameScene();
        scene.setTimeScale(-1.0f);
        assertEquals(0, scene.getTimeScale(), 0.01f);
    }

    @Test
    void testPauseWithCallback() {
        GameScene scene = new GameScene();
        boolean[] called = new boolean[]{false};
        scene.setOnPauseCallback(() -> called[0] = true);

        scene.pause();
        assertTrue(called[0]);
    }

    @Test
    void testPauseDefault() {
        GameScene scene = new GameScene();
        scene.onEnter();
        scene.pause();

        assertEquals(GameState.PAUSED, GameManager.getInstance().getCurrentState());
    }

    @Test
    void testResume() {
        GameScene scene = new GameScene();
        GameManager.getInstance().setState(GameState.PAUSED);

        scene.resume();

        assertEquals(GameState.PLAYING, GameManager.getInstance().getCurrentState());
    }

    @Test
    void testVictoryWithCallback() {
        GameScene scene = new GameScene();
        boolean[] called = new boolean[]{false};
        scene.setOnVictoryCallback(() -> called[0] = true);

        scene.victory();
        assertTrue(called[0]);
    }

    @Test
    void testVictoryDefault() {
        GameScene scene = new GameScene();
        scene.victory();

        assertEquals(GameState.VICTORY, GameManager.getInstance().getCurrentState());
    }

    @Test
    void testDefeatWithCallback() {
        GameScene scene = new GameScene();
        boolean[] called = new boolean[]{false};
        scene.setOnDefeatCallback(() -> called[0] = true);

        scene.defeat();
        assertTrue(called[0]);
    }

    @Test
    void testDefeatDefault() {
        GameScene scene = new GameScene();
        scene.defeat();

        assertEquals(GameState.DEFEAT, GameManager.getInstance().getCurrentState());
    }

    @Test
    void testGameOverWithCallback() {
        GameScene scene = new GameScene();
        boolean[] called = new boolean[]{false};
        scene.setOnGameOverCallback(() -> called[0] = true);

        scene.gameOver();
        assertTrue(called[0]);
    }

    @Test
    void testGameOverDefault() {
        GameScene scene = new GameScene();
        scene.gameOver();

        assertEquals(GameState.MAIN_MENU, GameManager.getInstance().getCurrentState());
    }

    @Test
    void testCallbacksChaining() {
        GameScene scene = new GameScene();
        Runnable callback = () -> {};

        GameScene result = scene.setOnPauseCallback(callback);
        assertSame(scene, result);

        result = scene.setOnGameOverCallback(callback);
        assertSame(scene, result);

        result = scene.setOnVictoryCallback(callback);
        assertSame(scene, result);

        result = scene.setOnDefeatCallback(callback);
        assertSame(scene, result);
    }
}
