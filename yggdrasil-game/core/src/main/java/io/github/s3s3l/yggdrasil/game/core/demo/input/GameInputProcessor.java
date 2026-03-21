package io.github.s3s3l.yggdrasil.game.core.demo.input;

import io.github.s3s3l.yggdrasil.game.core.demo.entity.GameAction;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 游戏输入处理器
 * 处理游戏输入并转换为游戏动作
 */
public class GameInputProcessor {

    /**
     * 输入动作映射
     */
    private final InputActionMap inputActionMap;

    /**
     * 当前按下的动作
     */
    private final Map<GameAction, Boolean> activeActions;

    /**
     * 动作监听器
     */
    private final Map<GameAction, Runnable> actionListeners;

    /**
     * 创建游戏输入处理器
     */
    public GameInputProcessor() {
        this.inputActionMap = new InputActionMap();
        this.activeActions = new HashMap<>();
        this.actionListeners = new HashMap<>();

        // 初始化所有动作为 false
        for (GameAction action : GameAction.values()) {
            activeActions.put(action, false);
        }
    }

    /**
     * 获取输入动作映射
     *
     * @return 输入动作映射
     */
    public InputActionMap getInputActionMap() {
        return inputActionMap;
    }

    /**
     * 设置动作监听器
     *
     * @param action   动作
     * @param listener 监听器
     */
    public void setActionListener(GameAction action, Runnable listener) {
        if (action != null) {
            actionListeners.put(action, listener);
        }
    }

    /**
     * 移除动作监听器
     *
     * @param action 动作
     */
    public void removeActionListener(GameAction action) {
        actionListeners.remove(action);
    }

    /**
     * 处理按键按下事件
     *
     * @param keyCode 按键代码
     */
    public void keyDown(int keyCode) {
        GameAction action = inputActionMap.getAction(keyCode);
        if (action != null) {
            activeActions.put(action, true);
            triggerAction(action);
        }
    }

    /**
     * 处理按键释放事件
     *
     * @param keyCode 按键代码
     */
    public void keyUp(int keyCode) {
        GameAction action = inputActionMap.getAction(keyCode);
        if (action != null) {
            activeActions.put(action, false);
        }
    }

    /**
     * 触发动作
     *
     * @param action 动作
     */
    private void triggerAction(GameAction action) {
        Runnable listener = actionListeners.get(action);
        if (listener != null) {
            listener.run();
        }
    }

    /**
     * 检查动作是否激活
     *
     * @param action 动作
     * @return true 如果激活
     */
    public boolean isActionActive(GameAction action) {
        Boolean active = activeActions.get(action);
        return active != null && active;
    }

    /**
     * 检查移动输入
     *
     * @return 移动方向（x, y），如果没有移动则为 (0, 0)
     */
    public float[] getMoveDirection() {
        float x = 0;
        float y = 0;

        if (isActionActive(GameAction.MOVE_LEFT)) {
            x -= 1;
        }
        if (isActionActive(GameAction.MOVE_RIGHT)) {
            x += 1;
        }
        if (isActionActive(GameAction.MOVE_UP)) {
            y += 1;
        }
        if (isActionActive(GameAction.MOVE_DOWN)) {
            y -= 1;
        }

        // 归一化
        float length = (float) Math.sqrt(x * x + y * y);
        if (length > 1) {
            x /= length;
            y /= length;
        }

        return new float[]{x, y};
    }

    /**
     * 获取所有激活的动作
     *
     * @return 激活的动作集合
     */
    public Set<GameAction> getActiveActions() {
        Set<GameAction> activeSet = new java.util.HashSet<>();
        for (Map.Entry<GameAction, Boolean> entry : activeActions.entrySet()) {
            if (entry.getValue()) {
                activeSet.add(entry.getKey());
            }
        }
        return activeSet;
    }

    /**
     * 清除所有激活状态
     */
    public void clearActiveActions() {
        for (GameAction action : GameAction.values()) {
            activeActions.put(action, false);
        }
    }
}
