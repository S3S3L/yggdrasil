package io.github.s3s3l.yggdrasil.game.core.flow;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GameManager 单元测试
 */
class GameManagerTest {

    @BeforeEach
    void setUp() {
        GameManager.resetInstance();
    }

    @AfterEach
    void tearDown() {
        GameManager.resetInstance();
    }

    @Test
    void testGetInstance() {
        GameManager manager1 = GameManager.getInstance();
        GameManager manager2 = GameManager.getInstance();
        assertSame(manager1, manager2);
    }

    @Test
    void testInitialState() {
        GameManager manager = GameManager.getInstance();
        assertEquals(GameState.INITIALIZING, manager.getCurrentState());
    }

    @Test
    void testSetState() {
        GameManager manager = GameManager.getInstance();
        manager.setState(GameState.MAIN_MENU);
        assertEquals(GameState.MAIN_MENU, manager.getCurrentState());
    }

    @Test
    void testSetStateWithSameState() {
        GameManager manager = GameManager.getInstance();
        manager.setState(GameState.PLAYING);
        manager.setState(GameState.PLAYING);

        assertEquals(GameState.PLAYING, manager.getCurrentState());
    }

    @Test
    void testSetStateWithNull() {
        GameManager manager = GameManager.getInstance();
        manager.setState(null);

        assertEquals(GameState.INITIALIZING, manager.getCurrentState());
    }

    @Test
    void testSetStateWithCallback() {
        GameManager manager = GameManager.getInstance();
        boolean[] called = new boolean[]{false};

        manager.setState(GameState.PLAYING, () -> called[0] = true);

        assertEquals(GameState.PLAYING, manager.getCurrentState());
        assertTrue(called[0]);
    }

    @Test
    void testIsState() {
        GameManager manager = GameManager.getInstance();
        manager.setState(GameState.PLAYING);

        assertTrue(manager.isState(GameState.PLAYING));
        assertFalse(manager.isState(GameState.PAUSED));
    }

    @Test
    void testIsPlaying() {
        GameManager manager = GameManager.getInstance();
        assertFalse(manager.isPlaying());

        manager.setState(GameState.PLAYING);
        assertTrue(manager.isPlaying());
    }

    @Test
    void testIsPaused() {
        GameManager manager = GameManager.getInstance();
        assertFalse(manager.isPaused());

        manager.setState(GameState.PAUSED);
        assertTrue(manager.isPaused());
    }

    @Test
    void testIsInMenu() {
        GameManager manager = GameManager.getInstance();

        manager.setState(GameState.MAIN_MENU);
        assertTrue(manager.isInMenu());

        manager.setState(GameState.SETTINGS);
        assertTrue(manager.isInMenu());

        manager.setState(GameState.SAVE_LOAD);
        assertTrue(manager.isInMenu());

        manager.setState(GameState.PLAYING);
        assertFalse(manager.isInMenu());
    }

    @Test
    void testPreviousState() {
        GameManager manager = GameManager.getInstance();
        manager.setState(GameState.MAIN_MENU);
        manager.setState(GameState.PLAYING);

        assertEquals(GameState.MAIN_MENU, manager.getPreviousState());
    }

    @Test
    void testBackToPreviousState() {
        GameManager manager = GameManager.getInstance();
        manager.setState(GameState.MAIN_MENU);
        manager.setState(GameState.PLAYING);

        boolean result = manager.backToPreviousState();

        assertTrue(result);
        assertEquals(GameState.MAIN_MENU, manager.getCurrentState());
    }

    @Test
    void testBackToPreviousStateNoPrevious() {
        GameManager manager = GameManager.getInstance();
        boolean result = manager.backToPreviousState();

        assertFalse(result);
    }

    @Test
    void testStartGame() {
        GameManager manager = GameManager.getInstance();
        manager.startGame();

        assertEquals(GameState.PLAYING, manager.getCurrentState());
    }

    @Test
    void testPauseGame() {
        GameManager manager = GameManager.getInstance();
        manager.startGame();
        manager.pauseGame();

        assertEquals(GameState.PAUSED, manager.getCurrentState());
    }

    @Test
    void testPauseGameNotPlaying() {
        GameManager manager = GameManager.getInstance();
        manager.pauseGame();

        // 不在游戏状态时暂停无效
        assertEquals(GameState.INITIALIZING, manager.getCurrentState());
    }

    @Test
    void testResumeGame() {
        GameManager manager = GameManager.getInstance();
        manager.startGame();
        manager.pauseGame();
        manager.resumeGame();

        assertEquals(GameState.PLAYING, manager.getCurrentState());
    }

    @Test
    void testResumeGameNotPaused() {
        GameManager manager = GameManager.getInstance();
        manager.resumeGame();

        // 不在暂停状态时恢复无效
        assertEquals(GameState.INITIALIZING, manager.getCurrentState());
    }

    @Test
    void testEndGame() {
        GameManager manager = GameManager.getInstance();
        manager.startGame();
        manager.endGame();

        assertEquals(GameState.MAIN_MENU, manager.getCurrentState());
    }

    @Test
    void testVictory() {
        GameManager manager = GameManager.getInstance();
        manager.victory();

        assertEquals(GameState.VICTORY, manager.getCurrentState());
    }

    @Test
    void testDefeat() {
        GameManager manager = GameManager.getInstance();
        manager.defeat();

        assertEquals(GameState.DEFEAT, manager.getCurrentState());
    }

    @Test
    void testEnterMainMenu() {
        GameManager manager = GameManager.getInstance();
        manager.enterMainMenu();

        assertEquals(GameState.MAIN_MENU, manager.getCurrentState());
    }

    @Test
    void testEnterSettings() {
        GameManager manager = GameManager.getInstance();
        manager.enterSettings();

        assertEquals(GameState.SETTINGS, manager.getCurrentState());
    }

    @Test
    void testEnterSaveLoad() {
        GameManager manager = GameManager.getInstance();
        manager.enterSaveLoad();

        assertEquals(GameState.SAVE_LOAD, manager.getCurrentState());
    }

    @Test
    void testEnterLoading() {
        GameManager manager = GameManager.getInstance();
        manager.enterLoading();

        assertEquals(GameState.LOADING, manager.getCurrentState());
    }

    @Test
    void testStateChangeListener() {
        GameManager manager = GameManager.getInstance();
        TestListener listener = new TestListener();
        manager.addStateChangeListener(listener);

        manager.setState(GameState.PLAYING);

        assertTrue(listener.changed);
        assertEquals(GameState.INITIALIZING, listener.oldState);
        assertEquals(GameState.PLAYING, listener.newState);
    }

    @Test
    void testRemoveStateChangeListener() {
        GameManager manager = GameManager.getInstance();
        TestListener listener = new TestListener();
        manager.addStateChangeListener(listener);
        manager.removeStateChangeListener(listener);

        manager.setState(GameState.PLAYING);

        assertFalse(listener.changed);
    }

    @Test
    void testClearListeners() {
        GameManager manager = GameManager.getInstance();
        TestListener listener1 = new TestListener();
        TestListener listener2 = new TestListener();
        manager.addStateChangeListener(listener1);
        manager.addStateChangeListener(listener2);

        manager.clearListeners();
        manager.setState(GameState.PLAYING);

        assertFalse(listener1.changed);
        assertFalse(listener2.changed);
    }

    private static class TestListener implements GameManager.GameStateChangeListener {
        boolean changed = false;
        GameState oldState = null;
        GameState newState = null;

        @Override
        public void onStateChanged(GameState oldState, GameState newState) {
            this.changed = true;
            this.oldState = oldState;
            this.newState = newState;
        }
    }
}
