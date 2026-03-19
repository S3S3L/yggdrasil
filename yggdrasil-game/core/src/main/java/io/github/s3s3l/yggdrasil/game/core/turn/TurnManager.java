package io.github.s3s3l.yggdrasil.game.core.turn;

import io.github.s3s3l.yggdrasil.game.core.ability.AbilitySystem;
import io.github.s3s3l.yggdrasil.game.core.unit.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 回合管理器
 * 管理游戏的回合流程
 */
public class TurnManager {

    /**
     * 当前回合数
     */
    private int currentTurn;

    /**
     * 当前回合所属的玩家 ID
     */
    private long currentTurnOwnerId;

    /**
     * 当前阶段
     */
    private TurnPhase currentPhase;

    /**
     * 玩家 ID 列表（按回合顺序）
     */
    private final List<Long> playerOrder;

    /**
     * 当前回合的玩家索引
     */
    private int currentPlayerIndex;

    /**
     * 技能系统（用于冷却管理）
     */
    private final AbilitySystem abilitySystem;

    /**
     * 回合监听器列表
     */
    private final List<TurnListener> listeners;

    /**
     * 游戏是否结束
     */
    private boolean gameOver;

    /**
     * 创建回合管理器
     *
     * @param abilitySystem 技能系统
     */
    public TurnManager(AbilitySystem abilitySystem) {
        this.abilitySystem = abilitySystem;
        this.currentTurn = 1;
        this.currentTurnOwnerId = -1;
        this.currentPhase = TurnPhase.PREPARATION;
        this.playerOrder = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.listeners = new ArrayList<>();
        this.gameOver = false;
    }

    /**
     * 添加玩家到回合顺序
     *
     * @param playerId 玩家 ID
     */
    public void addPlayer(long playerId) {
        if (!playerOrder.contains(playerId)) {
            playerOrder.add(playerId);
        }
    }

    /**
     * 移除玩家
     *
     * @param playerId 玩家 ID
     */
    public void removePlayer(long playerId) {
        playerOrder.removeIf(id -> id == playerId);
    }

    /**
     * 获取当前回合数
     *
     * @return 回合数
     */
    public int getCurrentTurn() {
        return currentTurn;
    }

    /**
     * 获取当前回合所有者 ID
     *
     * @return 玩家 ID
     */
    public long getCurrentTurnOwnerId() {
        return currentTurnOwnerId;
    }

    /**
     * 获取当前阶段
     *
     * @return 当前阶段
     */
    public TurnPhase getCurrentPhase() {
        return currentPhase;
    }

    /**
     * 获取玩家顺序
     *
     * @return 玩家 ID 列表
     */
    public List<Long> getPlayerOrder() {
        return new ArrayList<>(playerOrder);
    }

    /**
     * 添加回合监听器
     *
     * @param listener 监听器
     */
    public void addListener(TurnListener listener) {
        listeners.add(listener);
    }

    /**
     * 移除回合监听器
     *
     * @param listener 监听器
     */
    public void removeListener(TurnListener listener) {
        listeners.remove(listener);
    }

    /**
     * 开始新回合
     */
    public void startNewTurn() {
        if (gameOver) {
            return;
        }

        // 确定当前玩家
        if (playerOrder.isEmpty()) {
            currentTurnOwnerId = -1;
        } else {
            currentTurnOwnerId = playerOrder.get(currentPlayerIndex);
        }

        // 进入准备阶段
        currentPhase = TurnPhase.PREPARATION;
        notifyTurnStart(currentTurn, currentTurnOwnerId);

        // 执行准备阶段
        executePreparationPhase();

        // 自动进入行动阶段
        nextPhase();
    }

    /**
     * 执行准备阶段
     */
    private void executePreparationPhase() {
        // 恢复所有单位的行动力
        // 减少技能冷却
        // 处理持续效果
        notifyPhaseStart(TurnPhase.PREPARATION);
    }

    /**
     * 进入下一阶段
     */
    public void nextPhase() {
        if (gameOver) {
            return;
        }

        switch (currentPhase) {
            case PREPARATION:
                currentPhase = TurnPhase.ACTION;
                notifyPhaseStart(TurnPhase.ACTION);
                break;
            case ACTION:
                currentPhase = TurnPhase.END;
                notifyPhaseStart(TurnPhase.END);
                executeEndPhase();
                break;
            case END:
                // 进入下一回合
                currentTurn++;
                currentPlayerIndex = (currentPlayerIndex + 1) % playerOrder.size();
                startNewTurn();
                break;
        }
    }

    /**
     * 执行结束阶段
     */
    private void executeEndPhase() {
        // 清理临时效果
        // 检查胜利条件
        notifyPhaseStart(TurnPhase.END);
    }

    /**
     * 结束当前玩家的回合
     */
    public void endCurrentTurn() {
        if (currentPhase == TurnPhase.ACTION) {
            nextPhase();
        }
    }

    /**
     * 恢复单位的行动力
     *
     * @param unit 单位
     */
    public void restoreUnit(Unit unit) {
        if (unit == null) {
            return;
        }
        unit.resetAp();
    }

    /**
     * 减少技能的冷却时间
     *
     * @param playerId 玩家 ID
     */
    public void reduceCooldowns(long playerId) {
        if (abilitySystem != null) {
            abilitySystem.reduceCooldowns(playerId);
        }
    }

    /**
     * 检查是否是指定玩家的回合
     *
     * @param playerId 玩家 ID
     * @return true 如果是该玩家的回合
     */
    public boolean isPlayerTurn(long playerId) {
        return currentTurnOwnerId == playerId;
    }

    /**
     * 检查当前是否可以行动
     *
     * @return true 如果可以行动
     */
    public boolean canAct() {
        return currentPhase == TurnPhase.ACTION && !gameOver;
    }

    /**
     * 设置游戏结束
     *
     * @param gameOver 游戏是否结束
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
        if (gameOver) {
            notifyGameOver(currentTurnOwnerId);
        }
    }

    /**
     * 检查游戏是否结束
     *
     * @return true 如果游戏结束
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * 通知回合开始
     */
    private void notifyTurnStart(int turn, long ownerId) {
        for (TurnListener listener : listeners) {
            listener.onTurnStart(turn, ownerId);
        }
    }

    /**
     * 通知阶段开始
     */
    private void notifyPhaseStart(TurnPhase phase) {
        for (TurnListener listener : listeners) {
            listener.onPhaseStart(phase);
        }
    }

    /**
     * 通知游戏结束
     */
    private void notifyGameOver(long winnerId) {
        for (TurnListener listener : listeners) {
            listener.onGameOver(winnerId);
        }
    }

    /**
     * 回合监听器接口
     */
    public interface TurnListener {
        /**
         * 回合开始时调用
         *
         * @param turn    回合数
         * @param ownerId 回合所有者 ID
         */
        void onTurnStart(int turn, long ownerId);

        /**
         * 阶段开始时调用
         *
         * @param phase 当前阶段
         */
        void onPhaseStart(TurnPhase phase);

        /**
         * 游戏结束时调用
         *
         * @param winnerId 胜利者 ID
         */
        void onGameOver(long winnerId);
    }
}
