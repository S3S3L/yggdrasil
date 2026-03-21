package io.github.s3s3l.yggdrasil.game.core.flow;

import java.util.ArrayList;
import java.util.List;

/**
 * 游戏管理器
 * 管理游戏状态和状态转换
 */
public class GameManager {

    private static GameManager instance;

    private GameState currentState;
    private GameState previousState;
    private final List<GameStateChangeListener> listeners;

    /**
     * 私有构造函数
     */
    private GameManager() {
        this.currentState = GameState.INITIALIZING;
        this.previousState = null;
        this.listeners = new ArrayList<>();
    }

    /**
     * 获取单例实例
     *
     * @return GameManager 实例
     */
    public static synchronized GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    /**
     * 重置单例（用于测试）
     */
    public static synchronized void resetInstance() {
        if (instance != null) {
            instance.currentState = GameState.INITIALIZING;
            instance.previousState = null;
            instance.listeners.clear();
        }
        instance = null;
    }

    /**
     * 获取当前游戏状态
     *
     * @return 当前游戏状态
     */
    public synchronized GameState getCurrentState() {
        return currentState;
    }

    /**
     * 获取上一个游戏状态
     *
     * @return 上一个游戏状态
     */
    public synchronized GameState getPreviousState() {
        return previousState;
    }

    /**
     * 检查是否处于指定状态
     *
     * @param state 要检查的状态
     * @return true 如果处于该状态
     */
    public synchronized boolean isState(GameState state) {
        return currentState == state;
    }

    /**
     * 检查是否处于游戏中状态
     *
     * @return true 如果正在游戏中
     */
    public synchronized boolean isPlaying() {
        return currentState == GameState.PLAYING;
    }

    /**
     * 检查是否处于暂停状态
     *
     * @return true 如果已暂停
     */
    public synchronized boolean isPaused() {
        return currentState == GameState.PAUSED;
    }

    /**
     * 检查是否处于菜单状态
     *
     * @return true 如果在菜单中
     */
    public synchronized boolean isInMenu() {
        return currentState == GameState.MAIN_MENU ||
                currentState == GameState.SETTINGS ||
                currentState == GameState.SAVE_LOAD;
    }

    /**
     * 设置游戏状态
     *
     * @param newState 新状态
     */
    public synchronized void setState(GameState newState) {
        if (newState == null || newState == currentState) {
            return;
        }

        previousState = currentState;
        currentState = newState;

        notifyStateChanged(previousState, currentState);
    }

    /**
     * 设置游戏状态（带回调）
     *
     * @param newState 新状态
     * @param runnable 状态改变后执行的回调
     */
    public synchronized void setState(GameState newState, Runnable runnable) {
        setState(newState);
        if (runnable != null) {
            runnable.run();
        }
    }

    /**
     * 返回上一个状态
     *
     * @return true 如果成功返回
     */
    public synchronized boolean backToPreviousState() {
        if (previousState == null) {
            return false;
        }

        GameState temp = currentState;
        currentState = previousState;
        previousState = temp;

        notifyStateChanged(temp, currentState);
        return true;
    }

    /**
     * 添加状态变化监听器
     *
     * @param listener 监听器
     */
    public synchronized void addStateChangeListener(GameStateChangeListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * 移除状态变化监听器
     *
     * @param listener 监听器
     */
    public synchronized void removeStateChangeListener(GameStateChangeListener listener) {
        listeners.remove(listener);
    }

    /**
     * 清除所有监听器
     */
    public synchronized void clearListeners() {
        listeners.clear();
    }

    /**
     * 通知状态变化
     *
     * @param oldState 旧状态
     * @param newState 新状态
     */
    private void notifyStateChanged(GameState oldState, GameState newState) {
        for (GameStateChangeListener listener : listeners) {
            try {
                listener.onStateChanged(oldState, newState);
            } catch (Exception e) {
                // 忽略监听器异常
            }
        }
    }

    /**
     * 开始游戏（从主菜单进入游戏）
     */
    public synchronized void startGame() {
        previousState = currentState;
        currentState = GameState.PLAYING;
        notifyStateChanged(previousState, currentState);
    }

    /**
     * 暂停游戏
     */
    public synchronized void pauseGame() {
        if (currentState == GameState.PLAYING) {
            previousState = currentState;
            currentState = GameState.PAUSED;
            notifyStateChanged(previousState, currentState);
        }
    }

    /**
     * 恢复游戏
     */
    public synchronized void resumeGame() {
        if (currentState == GameState.PAUSED) {
            previousState = currentState;
            currentState = GameState.PLAYING;
            notifyStateChanged(previousState, currentState);
        }
    }

    /**
     * 结束游戏（返回主菜单）
     */
    public synchronized void endGame() {
        previousState = currentState;
        currentState = GameState.MAIN_MENU;
        notifyStateChanged(previousState, currentState);
    }

    /**
     * 游戏胜利
     */
    public synchronized void victory() {
        previousState = currentState;
        currentState = GameState.VICTORY;
        notifyStateChanged(previousState, currentState);
    }

    /**
     * 游戏失败
     */
    public synchronized void defeat() {
        previousState = currentState;
        currentState = GameState.DEFEAT;
        notifyStateChanged(previousState, currentState);
    }

    /**
     * 进入主菜单
     */
    public synchronized void enterMainMenu() {
        previousState = currentState;
        currentState = GameState.MAIN_MENU;
        notifyStateChanged(previousState, currentState);
    }

    /**
     * 进入设置菜单
     */
    public synchronized void enterSettings() {
        previousState = currentState;
        currentState = GameState.SETTINGS;
        notifyStateChanged(previousState, currentState);
    }

    /**
     * 进入存档/读档菜单
     */
    public synchronized void enterSaveLoad() {
        previousState = currentState;
        currentState = GameState.SAVE_LOAD;
        notifyStateChanged(previousState, currentState);
    }

    /**
     * 进入加载画面
     */
    public synchronized void enterLoading() {
        previousState = currentState;
        currentState = GameState.LOADING;
        notifyStateChanged(previousState, currentState);
    }

    /**
     * 状态变化监听器接口
     */
    public interface GameStateChangeListener {
        /**
         * 状态变化时调用
         *
         * @param oldState 旧状态
         * @param newState 新状态
         */
        void onStateChanged(GameState oldState, GameState newState);
    }
}
