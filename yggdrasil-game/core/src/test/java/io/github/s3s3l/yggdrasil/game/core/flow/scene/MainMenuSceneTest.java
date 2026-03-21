package io.github.s3s3l.yggdrasil.game.core.flow.scene;

import io.github.s3s3l.yggdrasil.game.core.flow.GameManager;
import io.github.s3s3l.yggdrasil.game.core.flow.GameState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MainMenuScene 单元测试
 */
class MainMenuSceneTest {

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
        MainMenuScene scene = new MainMenuScene();
        assertEquals("MainMenu", scene.getName());
    }

    @Test
    void testOnEnter() {
        MainMenuScene scene = new MainMenuScene();
        scene.onEnter();

        assertTrue(scene.isActive());
        assertEquals(GameState.MAIN_MENU, GameManager.getInstance().getCurrentState());
    }

    @Test
    void testStartGameWithCallback() {
        MainMenuScene scene = new MainMenuScene();
        boolean[] called = new boolean[]{false};
        scene.setOnStartGameCallback(() -> called[0] = true);

        scene.startGame();
        assertTrue(called[0]);
    }

    @Test
    void testStartGameDefault() {
        MainMenuScene scene = new MainMenuScene();
        scene.startGame();

        assertEquals(GameState.PLAYING, GameManager.getInstance().getCurrentState());
    }

    @Test
    void testLoadGameWithCallback() {
        MainMenuScene scene = new MainMenuScene();
        boolean[] called = new boolean[]{false};
        scene.setOnLoadGameCallback(() -> called[0] = true);

        scene.loadGame();
        assertTrue(called[0]);
    }

    @Test
    void testLoadGameDefault() {
        MainMenuScene scene = new MainMenuScene();
        scene.loadGame();

        assertEquals(GameState.SAVE_LOAD, GameManager.getInstance().getCurrentState());
    }

    @Test
    void testOpenSettingsWithCallback() {
        MainMenuScene scene = new MainMenuScene();
        boolean[] called = new boolean[]{false};
        scene.setOnSettingsCallback(() -> called[0] = true);

        scene.openSettings();
        assertTrue(called[0]);
    }

    @Test
    void testOpenSettingsDefault() {
        MainMenuScene scene = new MainMenuScene();
        scene.openSettings();

        assertEquals(GameState.SETTINGS, GameManager.getInstance().getCurrentState());
    }

    @Test
    void testExitGameWithCallback() {
        MainMenuScene scene = new MainMenuScene();
        boolean[] called = new boolean[]{false};
        scene.setOnExitCallback(() -> called[0] = true);

        scene.exitGame();
        assertTrue(called[0]);
    }

    @Test
    void testContinueGame() {
        MainMenuScene scene = new MainMenuScene();
        GameManager.getInstance().setState(GameState.PAUSED);

        scene.continueGame();

        assertEquals(GameState.PLAYING, GameManager.getInstance().getCurrentState());
    }

    @Test
    void testSetCallbacksChaining() {
        MainMenuScene scene = new MainMenuScene();
        Runnable callback = () -> {};

        MainMenuScene result = scene.setOnStartGameCallback(callback);
        assertSame(scene, result);

        result = scene.setOnLoadGameCallback(callback);
        assertSame(scene, result);

        result = scene.setOnSettingsCallback(callback);
        assertSame(scene, result);

        result = scene.setOnExitCallback(callback);
        assertSame(scene, result);
    }
}
