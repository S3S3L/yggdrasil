package io.github.s3s3l.yggdrasil.game.core.demo.input;

import io.github.s3s3l.yggdrasil.game.core.demo.entity.GameAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * InputActionMap 单元测试
 */
class InputActionMapTest {

    private InputActionMap inputActionMap;

    @BeforeEach
    void setUp() {
        inputActionMap = new InputActionMap();
    }

    @Test
    void testBind() {
        inputActionMap.bind(1, GameAction.MOVE_UP);
        assertEquals(GameAction.MOVE_UP, inputActionMap.getAction(1));
    }

    @Test
    void testUnbind() {
        inputActionMap.bind(1, GameAction.MOVE_UP);
        inputActionMap.unbind(1);
        assertNull(inputActionMap.getAction(1));
    }

    @Test
    void testGetActionNotBound() {
        assertNull(inputActionMap.getAction(999));
    }

    @Test
    void testIsBound() {
        inputActionMap.bind(1, GameAction.MOVE_UP);
        assertTrue(inputActionMap.isBound(1));
        assertFalse(inputActionMap.isBound(2));
    }

    @Test
    void testClear() {
        inputActionMap.bind(1, GameAction.MOVE_UP);
        inputActionMap.bind(2, GameAction.MOVE_DOWN);
        inputActionMap.clear();

        assertEquals(0, inputActionMap.getBindingCount());
    }

    @Test
    void testGetAllBindings() {
        inputActionMap.bind(1, GameAction.MOVE_UP);
        inputActionMap.bind(2, GameAction.MOVE_DOWN);

        var allBindings = inputActionMap.getAllBindings();
        assertEquals(2, allBindings.size());
        assertEquals(GameAction.MOVE_UP, allBindings.get(1));
        assertEquals(GameAction.MOVE_DOWN, allBindings.get(2));
    }

    @Test
    void testGetBindingCount() {
        assertEquals(0, inputActionMap.getBindingCount());

        inputActionMap.bind(1, GameAction.MOVE_UP);
        assertEquals(1, inputActionMap.getBindingCount());
    }

    @Test
    void testRebind() {
        inputActionMap.bind(1, GameAction.MOVE_UP);
        inputActionMap.bind(1, GameAction.MOVE_DOWN);

        assertEquals(GameAction.MOVE_DOWN, inputActionMap.getAction(1));
    }
}
