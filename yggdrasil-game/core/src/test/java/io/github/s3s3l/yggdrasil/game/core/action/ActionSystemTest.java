package io.github.s3s3l.yggdrasil.game.core.action;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.grid.Grid;
import io.github.s3s3l.yggdrasil.game.core.grid.GridMetrics;
import io.github.s3s3l.yggdrasil.game.core.grid.SquareCoordinate;
import io.github.s3s3l.yggdrasil.game.core.grid.SquareGrid;
import io.github.s3s3l.yggdrasil.game.core.unit.*;

/**
 * 动作系统单元测试
 */
class ActionSystemTest {

    private Grid grid;
    private Unit playerUnit;
    private Unit enemyUnit;
    private Unit allyUnit;

    @BeforeEach
    void setUp() {
        grid = new SquareGrid(GridMetrics.ofRect(10, 10));

        playerUnit = new BasicUnit(
                1L,
                "Player Soldier",
                UnitType.INFANTRY,
                UnitStats.createInfantry(),
                Faction.PLAYER
        );

        enemyUnit = new BasicUnit(
                2L,
                "Enemy Soldier",
                UnitType.INFANTRY,
                UnitStats.createInfantry(),
                Faction.ENEMY
        );

        allyUnit = new BasicUnit(
                3L,
                "Ally Soldier",
                UnitType.INFANTRY,
                UnitStats.createInfantry(),
                Faction.PLAYER  // 和玩家同一阵营
        );
    }

    @Test
    void testBasicAttackAction() {
        BasicAttackAction attack = new BasicAttackAction(grid, 10);

        // 设置位置（相邻）
        playerUnit.setPosition(SquareCoordinate.of(0, 0));
        enemyUnit.setPosition(SquareCoordinate.of(1, 0));

        // 验证可以攻击
        assertTrue(attack.canExecute(playerUnit, enemyUnit));

        // 执行攻击
        ActionResult result = attack.execute(playerUnit, enemyUnit);

        assertTrue(result.isSuccess());
        assertEquals(attack.getApCost(), result.getApCost());
        assertTrue(result.getMessage().contains("造成了"));
        assertTrue(result.getMessage().contains("点伤害"));
    }

    @Test
    void testBasicAttackActionInsufficientAp() {
        BasicAttackAction attack = new BasicAttackAction(grid, 10);

        // 消耗所有行动力
        playerUnit.spendAp(10);

        // 验证无法攻击
        assertFalse(attack.canExecute(playerUnit, enemyUnit));

        ActionResult result = attack.execute(playerUnit, enemyUnit);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("无法"));
    }

    @Test
    void testBasicAttackActionOutOfRange() {
        BasicAttackAction attack = new BasicAttackAction(grid, 10);

        // 设置位置（距离为 5）
        playerUnit.setPosition(SquareCoordinate.of(0, 0));
        enemyUnit.setPosition(SquareCoordinate.of(5, 0));

        // 验证无法攻击（超出范围）
        assertFalse(attack.canExecute(playerUnit, enemyUnit));
    }

    @Test
    void testBasicAttackActionFriendlyFire() {
        BasicAttackAction attack = new BasicAttackAction(grid, 10);

        // 设置位置（相邻）
        playerUnit.setPosition(SquareCoordinate.of(0, 0));
        // 使用同一阵营的单位测试友军伤害
        Unit friendlyUnit = new BasicUnit(
                4L,
                "Friendly",
                UnitType.INFANTRY,
                UnitStats.createInfantry(),
                Faction.PLAYER
        );
        friendlyUnit.setPosition(SquareCoordinate.of(1, 0));

        // 验证无法攻击友方
        assertFalse(attack.canExecute(playerUnit, friendlyUnit));
    }

    @Test
    void testMoveAction() {
        MoveAction move = new MoveAction(grid);

        playerUnit.setPosition(SquareCoordinate.of(5, 5));
        SquareCoordinate target = SquareCoordinate.of(5, 6);

        // 验证可以移动
        assertTrue(move.canExecute(playerUnit, target));

        // 执行移动
        ActionResult result = move.execute(playerUnit, target);

        assertTrue(result.isSuccess());
        assertEquals(target, playerUnit.getPosition());
    }

    @Test
    void testMoveActionMultipleSteps() {
        MoveAction move = new MoveAction(grid);

        playerUnit.setPosition(SquareCoordinate.of(5, 5));
        SquareCoordinate target = SquareCoordinate.of(6, 5);

        // 验证可以移动
        assertTrue(move.canExecute(playerUnit, target));

        // 执行移动
        ActionResult result = move.execute(playerUnit, target);
        assertTrue(result.isSuccess());

        // 验证新位置的邻居也可以移动
        SquareCoordinate nextTarget = SquareCoordinate.of(6, 6);
        assertTrue(move.canExecute(playerUnit, nextTarget));
    }

    @Test
    void testMoveActionDeadUnit() {
        MoveAction move = new MoveAction(grid);

        playerUnit.setPosition(SquareCoordinate.of(5, 5));
        playerUnit.takeDamage(1000); // 杀死单位

        SquareCoordinate target = SquareCoordinate.of(5, 6);

        // 验证死亡单位无法移动
        assertFalse(move.canExecute(playerUnit, target));
    }

    @Test
    void testUseAbilityAction() {
        io.github.s3s3l.yggdrasil.game.core.ability.Ability ability =
                io.github.s3s3l.yggdrasil.game.core.ability.Ability.createBasicHeal();
        UseAbilityAction action = new UseAbilityAction(ability);

        playerUnit.setPosition(SquareCoordinate.of(5, 5));
        allyUnit.setPosition(SquareCoordinate.of(5, 6));

        // 让盟友先受伤
        allyUnit.takeDamage(30);

        // 验证可以使用技能
        assertTrue(action.canExecute(playerUnit, allyUnit));

        // 记录初始生命值
        int initialHp = allyUnit.getCurrentHp();

        // 执行技能
        ActionResult result = action.execute(playerUnit, allyUnit);

        assertTrue(result.isSuccess());
        assertTrue(allyUnit.getCurrentHp() > initialHp);
    }

    @Test
    void testUseAbilityActionInsufficientAp() {
        io.github.s3s3l.yggdrasil.game.core.ability.Ability ability =
                io.github.s3s3l.yggdrasil.game.core.ability.Ability.createBasicHeal();
        UseAbilityAction action = new UseAbilityAction(ability);

        // 消耗所有行动力
        playerUnit.spendAp(10);

        // 验证无法使用技能
        assertFalse(action.canExecute(playerUnit, allyUnit));

        ActionResult result = action.execute(playerUnit, allyUnit);
        assertFalse(result.isSuccess());
    }

    @Test
    void testDefaultActionValidator() {
        DefaultActionValidator validator = new DefaultActionValidator();
        BasicAttackAction attack = new BasicAttackAction(grid, 10);

        playerUnit.setPosition(SquareCoordinate.of(0, 0));
        enemyUnit.setPosition(SquareCoordinate.of(1, 0));

        // 验证有效动作
        ActionValidator.ValidationResult result = validator.validate(attack, playerUnit, enemyUnit);
        assertTrue(result.isValid());
    }

    @Test
    void testDefaultActionValidatorDeadUnit() {
        DefaultActionValidator validator = new DefaultActionValidator();
        BasicAttackAction attack = new BasicAttackAction(grid, 10);

        playerUnit.takeDamage(1000); // 杀死单位

        // 验证死亡单位无法执行动作
        ActionValidator.ValidationResult result = validator.validate(attack, playerUnit, enemyUnit);
        assertFalse(result.isValid());
        assertTrue(result.getReason().contains("未存活"));
    }

    @Test
    void testDefaultActionValidatorInsufficientAp() {
        DefaultActionValidator validator = new DefaultActionValidator();
        BasicAttackAction attack = new BasicAttackAction(grid, 10);

        playerUnit.spendAp(10); // 消耗所有行动力

        // 验证行动力不足
        ActionValidator.ValidationResult result = validator.validate(attack, playerUnit, enemyUnit);
        assertFalse(result.isValid());
        assertTrue(result.getReason().contains("行动力不足"));
    }

    @Test
    void testActionResultFactoryMethods() {
        // 测试成功结果
        ActionResult success1 = ActionResult.success("测试成功");
        assertTrue(success1.isSuccess());
        assertEquals("测试成功", success1.getMessage());
        assertEquals(0, success1.getApCost());

        // 测试成功结果（带消耗）
        ActionResult success2 = ActionResult.success("测试成功", 3, 10);
        assertTrue(success2.isSuccess());
        assertEquals(3, success2.getApCost());
        assertEquals(10, success2.getHpCost());

        // 测试失败结果
        ActionResult failure1 = ActionResult.failure("测试失败");
        assertFalse(failure1.isSuccess());
        assertEquals("测试失败", failure1.getMessage());

        // 测试失败结果（带原因）
        ActionResult failure2 = ActionResult.failure("测试失败", "原因");
        assertFalse(failure2.isSuccess());
        assertTrue(failure2.getMessage().contains("原因"));
    }

    @Test
    void testValidationResultFactoryMethods() {
        // 测试有效结果
        ActionValidator.ValidationResult valid = ActionValidator.ValidationResult.valid();
        assertTrue(valid.isValid());
        assertNull(valid.getReason());

        // 测试无效结果
        ActionValidator.ValidationResult invalid = ActionValidator.ValidationResult.invalid("测试原因");
        assertFalse(invalid.isValid());
        assertEquals("测试原因", invalid.getReason());
    }
}
