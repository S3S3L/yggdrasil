package io.github.s3s3l.yggdrasil.game.core.flow.scene;

import io.github.s3s3l.yggdrasil.game.core.flow.GameManager;
import io.github.s3s3l.yggdrasil.game.core.flow.GameState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PauseMenuScene 单元测试
 */
class PauseMenuSceneTest {

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
        PauseMenuScene scene = new PauseMenuScene();
        assertEquals("PauseMenu", scene.getName());
    }

    @Test
    void testOnEnterWhenPlaying() {
        PauseMenuScene scene = new PauseMenuScene();
        GameManager.getInstance().setState(GameState.PLAYING);

        scene.onEnter();

        assertTrue(scene.isActive());
        assertEquals(GameState.PAUSED, GameManager.getInstance().getCurrentState());
    }

    @Test
    void testOnEnterWhenNotPlaying() {
        PauseMenuScene scene = new PauseMenuScene();
        GameManager.getInstance().setState(GameState.MAIN_MENU);

        scene.onEnter();

        assertTrue(scene.isActive());
        assertEquals(GameState.MAIN_MENU, GameManager.getInstance().getCurrentState());
    }

    @Test
    void testContinueGameWithCallback() {
        PauseMenuScene scene = new PauseMenuScene();
        boolean[] called = new boolean[]{false};
        scene.setOnContinueCallback(() -> called[0] = true);

        scene.continueGame();
        assertTrue(called[0]);
    }

    @Test
    void testContinueGameDefault() {
        PauseMenuScene scene = new PauseMenuScene();
        GameManager.getInstance().setState(GameState.PAUSED);

        scene.continueGame();

        assertEquals(GameState.PLAYING, GameManager.getInstance().getCurrentState());
    }

    @Test
    void testSaveGameWithCallback() {
        PauseMenuScene scene = new PauseMenuScene();
        boolean[] called = new boolean[]{false};
        scene.setOnSaveGameCallback(() -> called[0] = true);

        scene.saveGame();
        assertTrue(called[0]);
    }

    @Test
    void testSaveGameDefault() {
        PauseMenuScene scene = new PauseMenuScene();
        scene.saveGame();

        assertEquals(GameState.SAVE_LOAD, GameManager.getInstance().getCurrentState());
    }

    @Test
    void testLoadGameWithCallback() {
        PauseMenuScene scene = new PauseMenuScene();
        boolean[] called = new boolean[]{false};
        scene.setOnLoadGameCallback(() -> called[0] = true);

        scene.loadGame();
        assertTrue(called[0]);
    }

    @Test
    void testLoadGameDefault() {
        PauseMenuScene scene = new PauseMenuScene();
        scene.loadGame();

        assertEquals(GameState.SAVE_LOAD, GameManager.getInstance().getCurrentState());
    }

    @Test
    void testReturnToMainMenuWithCallback() {
        PauseMenuScene scene = new PauseMenuScene();
        boolean[] called = new boolean[]{false};
        scene.setOnReturnToMainMenuCallback(() -> called[0] = true);

        scene.returnToMainMenu();
        assertTrue(called[0]);
    }

    @Test
    void testReturnToMainMenuDefault() {
        PauseMenuScene scene = new PauseMenuScene();
        scene.returnToMainMenu();

        assertEquals(GameState.MAIN_MENU, GameManager.getInstance().getCurrentState());
    }

    @Test
    void testExitGameWithCallback() {
        PauseMenuScene scene = new PauseMenuScene();
        boolean[] called = new boolean[]{false};
        scene.setOnExitCallback(() -> called[0] = true);

        scene.exitGame();
        assertTrue(called[0]);
    }

    @Test
    void testSaveAndExit() {
        PauseMenuScene scene = new PauseMenuScene();
        boolean[] saveCalled = new boolean[]{false};
        boolean[] exitCalled = new boolean[]{false};

        scene.setOnSaveGameCallback(() -> saveCalled[0] = true);
        scene.setOnReturnToMainMenuCallback(() -> exitCalled[0] = true);

        scene.saveAndExit();

        assertTrue(saveCalled[0]);
        assertTrue(exitCalled[0]);
    }

    @Test
    void testCallbacksChaining() {
        PauseMenuScene scene = new PauseMenuScene();
        Runnable callback = () -> {};

        PauseMenuScene result = scene.setOnContinueCallback(callback);
        assertSame(scene, result);

        result = scene.setOnSaveGameCallback(callback);
        assertSame(scene, result);

        result = scene.setOnLoadGameCallback(callback);
        assertSame(scene, result);

        result = scene.setOnReturnToMainMenuCallback(callback);
        assertSame(scene, result);

        result = scene.setOnExitCallback(callback);
        assertSame(scene, result);
    }
}
