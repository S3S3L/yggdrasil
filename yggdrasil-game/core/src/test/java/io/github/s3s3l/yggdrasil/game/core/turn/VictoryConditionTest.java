package io.github.s3s3l.yggdrasil.game.core.turn;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.ability.AbilitySystem;
import io.github.s3s3l.yggdrasil.game.core.turn.condition.AnnihilationVictoryCondition;
import io.github.s3s3l.yggdrasil.game.core.turn.condition.TurnLimitVictoryCondition;
import io.github.s3s3l.yggdrasil.game.core.unit.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 胜利条件单元测试
 */
class VictoryConditionTest {

    private AbilitySystem abilitySystem;
    private TurnManager turnManager;

    @BeforeEach
    void setUp() {
        abilitySystem = new AbilitySystem();
        turnManager = new TurnManager(abilitySystem);
    }

    @Test
    void testAnnihilationVictoryConditionEnemyAlive() {
        AnnihilationVictoryCondition condition = new AnnihilationVictoryCondition(Faction.ENEMY);

        List<Unit> units = new ArrayList<>();
        units.add(createUnit(1L, "Player Soldier", Faction.PLAYER));
        units.add(createUnit(2L, "Enemy Soldier", Faction.ENEMY));

        VictoryCondition.VictoryResult result = condition.check(units, turnManager);

        assertFalse(result.isMet());
        assertEquals(-1, result.getWinnerId());
    }

    @Test
    void testAnnihilationVictoryConditionEnemyDead() {
        AnnihilationVictoryCondition condition = new AnnihilationVictoryCondition(Faction.ENEMY);

        List<Unit> units = new ArrayList<>();
        Unit playerUnit = createUnit(1L, "Player Soldier", Faction.PLAYER);
        Unit enemyUnit = createUnit(2L, "Enemy Soldier", Faction.ENEMY);

        // 杀死敌方单位
        enemyUnit.takeDamage(1000);

        units.add(playerUnit);
        units.add(enemyUnit);

        VictoryCondition.VictoryResult result = condition.check(units, turnManager);

        assertTrue(result.isMet());
        assertNotNull(result.getReason());
        assertTrue(result.getReason().contains("全灭"));
    }

    @Test
    void testAnnihilationVictoryConditionMultipleEnemies() {
        AnnihilationVictoryCondition condition = new AnnihilationVictoryCondition(Faction.ENEMY);

        List<Unit> units = new ArrayList<>();
        units.add(createUnit(1L, "Player Soldier", Faction.PLAYER));
        units.add(createUnit(2L, "Enemy Soldier 1", Faction.ENEMY));
        units.add(createUnit(3L, "Enemy Soldier 2", Faction.ENEMY));

        // 只杀死一个敌方单位
        units.get(1).takeDamage(1000);

        VictoryCondition.VictoryResult result = condition.check(units, turnManager);

        assertFalse(result.isMet());
    }

    @Test
    void testAnnihilationVictoryConditionAllEnemiesDead() {
        AnnihilationVictoryCondition condition = new AnnihilationVictoryCondition(Faction.ENEMY);

        List<Unit> units = new ArrayList<>();
        units.add(createUnit(1L, "Player Soldier", Faction.PLAYER));
        Unit enemy1 = createUnit(2L, "Enemy Soldier 1", Faction.ENEMY);
        Unit enemy2 = createUnit(3L, "Enemy Soldier 2", Faction.ENEMY);

        // 杀死所有敌方单位
        enemy1.takeDamage(1000);
        enemy2.takeDamage(1000);

        units.add(enemy1);
        units.add(enemy2);

        VictoryCondition.VictoryResult result = condition.check(units, turnManager);

        assertTrue(result.isMet());
    }

    @Test
    void testTurnLimitVictoryConditionNotExceeded() {
        TurnLimitVictoryCondition condition = new TurnLimitVictoryCondition(10, 1L);

        turnManager.addPlayer(1L);
        turnManager.startNewTurn();

        VictoryCondition.VictoryResult result = condition.check(new ArrayList<>(), turnManager);

        assertFalse(result.isMet());
        assertEquals(-1, result.getWinnerId());
    }

    @Test
    void testTurnLimitVictoryConditionExceeded() {
        TurnLimitVictoryCondition condition = new TurnLimitVictoryCondition(5, 1L);

        turnManager.addPlayer(1L);

        // 模拟超过回合数
        // 由于无法直接设置回合数，我们通过多次调用 nextPhase 来模拟
        for (int i = 0; i < 12; i++) {
            turnManager.nextPhase();
        }

        VictoryCondition.VictoryResult result = condition.check(new ArrayList<>(), turnManager);

        assertTrue(result.isMet());
        assertTrue(result.getReason().contains("超过回合数"));
    }

    @Test
    void testTurnLimitVictoryConditionGetRemainingTurns() {
        TurnLimitVictoryCondition condition = new TurnLimitVictoryCondition(10, 1L);

        turnManager.addPlayer(1L);
        turnManager.startNewTurn();

        assertEquals(9, condition.getRemainingTurns(turnManager));

        // 进入下一回合
        turnManager.nextPhase();
        turnManager.nextPhase();

        assertEquals(8, condition.getRemainingTurns(turnManager));
    }

    @Test
    void testTurnLimitVictoryConditionZeroRemaining() {
        TurnLimitVictoryCondition condition = new TurnLimitVictoryCondition(1, 1L);

        turnManager.addPlayer(1L);
        turnManager.startNewTurn();

        // 第 1 回合，剩余 0 回合
        assertEquals(0, condition.getRemainingTurns(turnManager));
    }

    @Test
    void testVictoryResultFactoryMethods() {
        // 测试 none()
        VictoryCondition.VictoryResult none = VictoryCondition.VictoryResult.none();
        assertFalse(none.isMet());
        assertEquals(-1, none.getWinnerId());
        assertNull(none.getReason());

        // 测试 victory()
        VictoryCondition.VictoryResult victory = VictoryCondition.VictoryResult.victory(1L, "测试胜利");
        assertTrue(victory.isMet());
        assertEquals(1L, victory.getWinnerId());
        assertEquals("测试胜利", victory.getReason());

        // 测试 draw()
        VictoryCondition.VictoryResult draw = VictoryCondition.VictoryResult.draw("平局");
        assertTrue(draw.isMet());
        assertEquals(-1, draw.getWinnerId());
        assertEquals("平局", draw.getReason());
    }

    @Test
    void testVictoryConditionNames() {
        AnnihilationVictoryCondition annihilation = new AnnihilationVictoryCondition(Faction.ENEMY);
        assertEquals("歼灭战", annihilation.getName());
        assertTrue(annihilation.getDescription().contains("消灭"));

        TurnLimitVictoryCondition turnLimit = new TurnLimitVictoryCondition(10, 1L);
        assertEquals("时间限制", turnLimit.getName());
        assertTrue(turnLimit.getDescription().contains("回合"));
    }

    private Unit createUnit(long id, String name, Faction faction) {
        return new BasicUnit(id, name, UnitType.INFANTRY, UnitStats.createInfantry(), faction);
    }
}
