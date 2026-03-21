package io.github.s3s3l.yggdrasil.game.core.demo.input;

import io.github.s3s3l.yggdrasil.game.core.demo.entity.GameAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GameInputProcessor 单元测试
 */
class GameInputProcessorTest {

    private GameInputProcessor inputProcessor;

    @BeforeEach
    void setUp() {
        inputProcessor = new GameInputProcessor();
    }

    @Test
    void testConstructor() {
        assertNotNull(inputProcessor.getInputActionMap());
    }

    @Test
    void testKeydown() {
        inputProcessor.getInputActionMap().bind(1, GameAction.ATTACK);
        inputProcessor.keyDown(1);

        assertTrue(inputProcessor.isActionActive(GameAction.ATTACK));
    }

    @Test
    void testKeyup() {
        inputProcessor.getInputActionMap().bind(1, GameAction.ATTACK);
        inputProcessor.keyDown(1);
        inputProcessor.keyUp(1);

        assertFalse(inputProcessor.isActionActive(GameAction.ATTACK));
    }

    @Test
    void testKeydownNotBound() {
        inputProcessor.keyDown(999);
        // 不应抛出异常
    }

    @Test
    void testSetActionListener() {
        boolean[] called = new boolean[]{false};
        inputProcessor.setActionListener(GameAction.ATTACK, () -> called[0] = true);

        inputProcessor.getInputActionMap().bind(1, GameAction.ATTACK);
        inputProcessor.keyDown(1);

        assertTrue(called[0]);
    }

    @Test
    void testRemoveActionListener() {
        boolean[] called = new boolean[]{false};
        inputProcessor.setActionListener(GameAction.ATTACK, () -> called[0] = true);
        inputProcessor.removeActionListener(GameAction.ATTACK);

        inputProcessor.getInputActionMap().bind(1, GameAction.ATTACK);
        inputProcessor.keyDown(1);

        assertFalse(called[0]);
    }

    @Test
    void testGetMoveDirection() {
        inputProcessor.getInputActionMap().bind(1, GameAction.MOVE_UP);
        inputProcessor.getInputActionMap().bind(2, GameAction.MOVE_RIGHT);

        inputProcessor.keyDown(1);
        inputProcessor.keyDown(2);

        float[] direction = inputProcessor.getMoveDirection();
        // 对角线方向归一化后约为 0.707
        assertEquals(0.707f, direction[1], 0.01f); // Y 轴向上
        assertEquals(0.707f, direction[0], 0.01f); // X 轴向右
    }

    @Test
    void testGetMoveDirectionZero() {
        float[] direction = inputProcessor.getMoveDirection();
        assertEquals(0, direction[0], 0.01f);
        assertEquals(0, direction[1], 0.01f);
    }

    @Test
    void testGetMoveDirectionNormalized() {
        inputProcessor.getInputActionMap().bind(1, GameAction.MOVE_UP);
        inputProcessor.getInputActionMap().bind(2, GameAction.MOVE_RIGHT);
        inputProcessor.getInputActionMap().bind(3, GameAction.MOVE_DOWN);
        inputProcessor.getInputActionMap().bind(4, GameAction.MOVE_LEFT);

        // 同时按四个方向
        inputProcessor.keyDown(1);
        inputProcessor.keyDown(2);
        inputProcessor.keyDown(3);
        inputProcessor.keyDown(4);

        float[] direction = inputProcessor.getMoveDirection();
        // 相反方向抵消
        assertEquals(0, direction[0], 0.01f);
        assertEquals(0, direction[1], 0.01f);
    }

    @Test
    void testGetActiveActions() {
        inputProcessor.getInputActionMap().bind(1, GameAction.ATTACK);
        inputProcessor.getInputActionMap().bind(2, GameAction.JUMP);

        inputProcessor.keyDown(1);

        var activeActions = inputProcessor.getActiveActions();
        assertEquals(1, activeActions.size());
        assertTrue(activeActions.contains(GameAction.ATTACK));
    }

    @Test
    void testClearActiveActions() {
        inputProcessor.getInputActionMap().bind(1, GameAction.ATTACK);
        inputProcessor.keyDown(1);

        inputProcessor.clearActiveActions();

        assertFalse(inputProcessor.isActionActive(GameAction.ATTACK));
    }

    @Test
    void testMultipleActionsSameKey() {
        inputProcessor.getInputActionMap().bind(1, GameAction.ATTACK);
        inputProcessor.keyDown(1);
        inputProcessor.keyUp(1);
        inputProcessor.keyDown(1);

        assertTrue(inputProcessor.isActionActive(GameAction.ATTACK));
    }
}
