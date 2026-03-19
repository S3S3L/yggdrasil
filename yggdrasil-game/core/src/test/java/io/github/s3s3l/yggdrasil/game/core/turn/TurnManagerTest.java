package io.github.s3s3l.yggdrasil.game.core.turn;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.ability.AbilitySystem;
import io.github.s3s3l.yggdrasil.game.core.unit.*;

import java.util.List;

/**
 * 回合管理器单元测试
 */
class TurnManagerTest {

    private TurnManager turnManager;
    private AbilitySystem abilitySystem;

    @BeforeEach
    void setUp() {
        abilitySystem = new AbilitySystem();
        turnManager = new TurnManager(abilitySystem);
    }

    @Test
    void testInitialTurnState() {
        assertEquals(1, turnManager.getCurrentTurn());
        assertEquals(-1, turnManager.getCurrentTurnOwnerId());
        assertEquals(TurnPhase.PREPARATION, turnManager.getCurrentPhase());
        assertFalse(turnManager.isGameOver());
    }

    @Test
    void testAddPlayer() {
        turnManager.addPlayer(1L);
        turnManager.addPlayer(2L);

        List<Long> players = turnManager.getPlayerOrder();
        assertEquals(2, players.size());
        assertEquals(1L, players.get(0));
        assertEquals(2L, players.get(1));
    }

    @Test
    void testRemovePlayer() {
        turnManager.addPlayer(1L);
        turnManager.addPlayer(2L);
        turnManager.addPlayer(3L);

        turnManager.removePlayer(2L);

        List<Long> players = turnManager.getPlayerOrder();
        assertEquals(2, players.size());
        assertFalse(players.contains(2L));
    }

    @Test
    void testStartNewTurn() {
        turnManager.addPlayer(1L);
        turnManager.startNewTurn();

        assertEquals(1, turnManager.getCurrentTurn());
        assertEquals(1L, turnManager.getCurrentTurnOwnerId());
        assertEquals(TurnPhase.ACTION, turnManager.getCurrentPhase());
    }

    @Test
    void testNextPhase() {
        turnManager.addPlayer(1L);
        turnManager.startNewTurn();

        // 当前是 ACTION 阶段
        assertEquals(TurnPhase.ACTION, turnManager.getCurrentPhase());

        // 进入 END 阶段
        turnManager.nextPhase();
        assertEquals(TurnPhase.END, turnManager.getCurrentPhase());

        // 进入下一回合
        turnManager.nextPhase();
        assertEquals(2, turnManager.getCurrentTurn());
        assertEquals(1L, turnManager.getCurrentTurnOwnerId());
    }

    @Test
    void testNextPhaseWithMultiplePlayers() {
        turnManager.addPlayer(1L);
        turnManager.addPlayer(2L);
        turnManager.startNewTurn();

        assertEquals(1L, turnManager.getCurrentTurnOwnerId());

        // 进入 END 阶段后进入下一回合
        turnManager.nextPhase(); // ACTION -> END
        turnManager.nextPhase(); // END -> 下一回合

        assertEquals(2, turnManager.getCurrentTurn());
        assertEquals(2L, turnManager.getCurrentTurnOwnerId());

        // 再下一回合
        turnManager.nextPhase();
        turnManager.nextPhase();

        assertEquals(3, turnManager.getCurrentTurn());
        assertEquals(1L, turnManager.getCurrentTurnOwnerId());
    }

    @Test
    void testEndCurrentTurn() {
        turnManager.addPlayer(1L);
        turnManager.startNewTurn();

        assertEquals(TurnPhase.ACTION, turnManager.getCurrentPhase());

        turnManager.endCurrentTurn();

        assertEquals(TurnPhase.END, turnManager.getCurrentPhase());
    }

    @Test
    void testIsPlayerTurn() {
        turnManager.addPlayer(1L);
        turnManager.addPlayer(2L);
        turnManager.startNewTurn();

        assertTrue(turnManager.isPlayerTurn(1L));
        assertFalse(turnManager.isPlayerTurn(2L));
    }

    @Test
    void testCanAct() {
        turnManager.addPlayer(1L);
        turnManager.startNewTurn();

        assertTrue(turnManager.canAct());

        turnManager.nextPhase(); // 进入 END 阶段
        assertFalse(turnManager.canAct());
    }

    @Test
    void testRestoreUnit() {
        turnManager.addPlayer(1L);
        Unit unit = new BasicUnit(1L, "Soldier", UnitType.INFANTRY, UnitStats.createInfantry(), Faction.PLAYER);

        // 消耗行动力
        unit.spendAp(5);
        assertEquals(5, unit.getCurrentAp());

        // 恢复行动力
        turnManager.restoreUnit(unit);
        assertEquals(10, unit.getCurrentAp());
    }

    @Test
    void testReduceCooldowns() {
        turnManager.addPlayer(1L);

        // 学习技能并施放
        io.github.s3s3l.yggdrasil.game.core.ability.Ability ability =
                io.github.s3s3l.yggdrasil.game.core.ability.Ability.createBasicHeal();
        abilitySystem.learnAbility(1L, ability);

        Unit caster = new BasicUnit(1L, "Healer", UnitType.HEALER, UnitStats.createInfantry(), Faction.PLAYER);
        Unit target = new BasicUnit(2L, "Soldier", UnitType.INFANTRY, UnitStats.createInfantry(), Faction.PLAYER);

        abilitySystem.castAbility(caster, ability, target);

        assertTrue(abilitySystem.isOnCooldown(1L, ability.getId()));

        // 减少冷却
        turnManager.reduceCooldowns(1L);

        assertEquals(1, abilitySystem.getRemainingCooldown(1L, ability.getId()));
    }

    @Test
    void testSetGameOver() {
        turnManager.addPlayer(1L);
        turnManager.startNewTurn();

        assertFalse(turnManager.isGameOver());

        turnManager.setGameOver(true);

        assertTrue(turnManager.isGameOver());
        assertFalse(turnManager.canAct());
    }

    @Test
    void testTurnListener() {
        turnManager.addPlayer(1L);

        TestTurnListener listener = new TestTurnListener();
        turnManager.addListener(listener);

        turnManager.startNewTurn();

        assertTrue(listener.turnStartCalled);
        assertEquals(1, listener.turn);
        assertEquals(1L, listener.ownerId);
        assertTrue(listener.phaseStartCalled);
    }

    @Test
    void testRemoveTurnListener() {
        turnManager.addPlayer(1L);

        TestTurnListener listener = new TestTurnListener();
        turnManager.addListener(listener);
        turnManager.removeListener(listener);

        turnManager.startNewTurn();

        assertFalse(listener.turnStartCalled);
    }

    @Test
    void testStartNewTurnWhenGameOver() {
        turnManager.addPlayer(1L);
        turnManager.setGameOver(true);

        turnManager.startNewTurn();

        assertEquals(-1, turnManager.getCurrentTurnOwnerId());
    }

    @Test
    void testNextPhaseWhenGameOver() {
        turnManager.addPlayer(1L);
        turnManager.startNewTurn();
        turnManager.setGameOver(true);

        int currentTurn = turnManager.getCurrentTurn();
        turnManager.nextPhase();

        assertEquals(currentTurn, turnManager.getCurrentTurn());
    }

    /**
     * 测试用回合监听器
     */
    private static class TestTurnListener implements TurnManager.TurnListener {
        boolean turnStartCalled = false;
        boolean phaseStartCalled = false;
        boolean gameOverCalled = false;
        int turn = 0;
        long ownerId = -1;
        TurnPhase phase = null;
        long winnerId = -1;

        @Override
        public void onTurnStart(int turn, long ownerId) {
            turnStartCalled = true;
            this.turn = turn;
            this.ownerId = ownerId;
        }

        @Override
        public void onPhaseStart(TurnPhase phase) {
            phaseStartCalled = true;
            this.phase = phase;
        }

        @Override
        public void onGameOver(long winnerId) {
            gameOverCalled = true;
            this.winnerId = winnerId;
        }
    }
}
