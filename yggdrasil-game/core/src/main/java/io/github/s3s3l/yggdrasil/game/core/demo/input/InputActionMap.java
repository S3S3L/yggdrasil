package io.github.s3s3l.yggdrasil.game.core.demo.input;

import io.github.s3s3l.yggdrasil.game.core.demo.entity.GameAction;

import java.util.HashMap;
import java.util.Map;

/**
 * 输入动作映射
 * 将按键映射到游戏动作
 */
public class InputActionMap {

    /**
     * 按键到动作的映射
     */
    private final Map<Integer, GameAction> keyToAction;

    /**
     * 创建输入动作映射
     */
    public InputActionMap() {
        this.keyToAction = new HashMap<>();
    }

    /**
     * 绑定按键到动作
     *
     * @param keyCode 按键代码
     * @param action  游戏动作
     */
    public void bind(int keyCode, GameAction action) {
        keyToAction.put(keyCode, action);
    }

    /**
     * 解绑按键
     *
     * @param keyCode 按键代码
     */
    public void unbind(int keyCode) {
        keyToAction.remove(keyCode);
    }

    /**
     * 获取按键对应的动作
     *
     * @param keyCode 按键代码
     * @return 游戏动作，如果没有绑定则返回 null
     */
    public GameAction getAction(int keyCode) {
        return keyToAction.get(keyCode);
    }

    /**
     * 检查按键是否已绑定
     *
     * @param keyCode 按键代码
     * @return true 如果已绑定
     */
    public boolean isBound(int keyCode) {
        return keyToAction.containsKey(keyCode);
    }

    /**
     * 清除所有绑定
     */
    public void clear() {
        keyToAction.clear();
    }

    /**
     * 获取所有绑定
     *
     * @return 按键到动作的映射副本
     */
    public Map<Integer, GameAction> getAllBindings() {
        return new HashMap<>(keyToAction);
    }

    /**
     * 获取绑定数量
     *
     * @return 绑定数量
     */
    public int getBindingCount() {
        return keyToAction.size();
    }
}
