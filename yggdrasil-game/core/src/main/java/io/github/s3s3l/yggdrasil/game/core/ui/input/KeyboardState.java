package io.github.s3s3l.yggdrasil.game.core.ui.input;

import java.util.*;

/**
 * 键盘状态追踪器
 * 追踪每个按键的状态（按下、 held、释放）
 */
public class KeyboardState {

    /**
     * 按键状态枚举
     */
    public enum KeyStatus {
        UP,         // 未按下
        PRESSED,    // 刚按下（当前帧）
        HELD,       // 持续按下
        RELEASED    // 刚释放（当前帧）
    }

    private final Map<Integer, KeyStatus> keyStates;
    private final Map<Integer, Integer> heldTimers;  // 按键按下持续时间（毫秒）
    private final Map<Integer, Integer> repeatTimers; // 连发计时器
    private int repeatDelay;     // 连发延迟（毫秒）
    private int repeatInterval;  // 连发间隔（毫秒）

    /**
     * 创建键盘状态追踪器
     */
    public KeyboardState() {
        this.keyStates = new HashMap<>();
        this.heldTimers = new HashMap<>();
        this.repeatTimers = new HashMap<>();
        this.repeatDelay = 500;      // 默认 500ms 后开始连发
        this.repeatInterval = 100;   // 默认每 100ms 触发一次
    }

    /**
     * 处理按键按下事件
     *
     * @param keyCode 按键代码
     * @param timestamp 时间戳（毫秒）
     */
    public void keyDown(int keyCode, long timestamp) {
        keyStates.put(keyCode, KeyStatus.PRESSED);
        heldTimers.put(keyCode, (int) timestamp);
        repeatTimers.put(keyCode, (int) timestamp + repeatDelay);
    }

    /**
     * 处理按键释放事件
     *
     * @param keyCode 按键代码
     */
    public void keyUp(int keyCode) {
        keyStates.put(keyCode, KeyStatus.RELEASED);
        heldTimers.remove(keyCode);
        repeatTimers.remove(keyCode);
    }

    /**
     * 更新键盘状态（每帧调用）
     *
     * @param timestamp 当前时间戳（毫秒）
     */
    public void update(long timestamp) {
        for (Map.Entry<Integer, KeyStatus> entry : keyStates.entrySet()) {
            int keyCode = entry.getKey();
            KeyStatus status = entry.getValue();

            if (status == KeyStatus.PRESSED) {
                // 从 PRESSED 变为 HELD
                keyStates.put(keyCode, KeyStatus.HELD);
            } else if (status == KeyStatus.RELEASED) {
                // 从 RELEASED 变为 UP
                keyStates.put(keyCode, KeyStatus.UP);
            } else if (status == KeyStatus.HELD) {
                // 更新连发计时器
                Integer heldTime = heldTimers.get(keyCode);
                Integer repeatTime = repeatTimers.get(keyCode);

                if (heldTime != null && repeatTime != null) {
                    int elapsed = (int) timestamp - heldTime;

                    // 检查是否需要触发连发
                    if (elapsed >= repeatDelay && (int) timestamp >= repeatTime) {
                        repeatTimers.put(keyCode, (int) timestamp + repeatInterval);
                        // 连发触发，标记为 PRESSED 以便触发一帧
                        keyStates.put(keyCode, KeyStatus.PRESSED);
                    }
                }
            }
        }
    }

    /**
     * 检查按键是否刚被按下
     *
     * @param keyCode 按键代码
     * @return true 如果按键刚被按下
     */
    public boolean isKeyPressed(int keyCode) {
        KeyStatus status = keyStates.get(keyCode);
        return status == KeyStatus.PRESSED;
    }

    /**
     * 检查按键是否持续按下
     *
     * @param keyCode 按键代码
     * @return true 如果按键持续按下
     */
    public boolean isKeyHeld(int keyCode) {
        KeyStatus status = keyStates.get(keyCode);
        return status == KeyStatus.HELD || status == KeyStatus.PRESSED;
    }

    /**
     * 检查按键是否刚被释放
     *
     * @param keyCode 按键代码
     * @return true 如果按键刚被释放
     */
    public boolean isKeyReleased(int keyCode) {
        KeyStatus status = keyStates.get(keyCode);
        return status == KeyStatus.RELEASED;
    }

    /**
     * 检查按键是否未按下
     *
     * @param keyCode 按键代码
     * @return true 如果按键未按下
     */
    public boolean isKeyUp(int keyCode) {
        KeyStatus status = keyStates.get(keyCode);
        return status == KeyStatus.UP || status == null;
    }

    /**
     * 获取按键按下的持续时间（毫秒）
     *
     * @param keyCode 按键代码
     * @param currentTimestamp 当前时间戳
     * @return 持续时间，如果未按下返回 0
     */
    public int getHoldDuration(int keyCode, long currentTimestamp) {
        Integer heldTime = heldTimers.get(keyCode);
        if (heldTime == null || !isKeyHeld(keyCode)) {
            return 0;
        }
        return (int) currentTimestamp - heldTime;
    }

    /**
     * 设置连发延迟
     *
     * @param delayMs 延迟（毫秒）
     */
    public void setRepeatDelay(int delayMs) {
        this.repeatDelay = delayMs;
    }

    /**
     * 设置连发间隔
     *
     * @param intervalMs 间隔（毫秒）
     */
    public void setRepeatInterval(int intervalMs) {
        this.repeatInterval = intervalMs;
    }

    /**
     * 获取所有当前按下的按键
     *
     * @return 按键代码列表
     */
    public List<Integer> getPressedKeys() {
        List<Integer> pressed = new ArrayList<>();
        for (Map.Entry<Integer, KeyStatus> entry : keyStates.entrySet()) {
            if (entry.getValue() == KeyStatus.PRESSED || entry.getValue() == KeyStatus.HELD) {
                pressed.add(entry.getKey());
            }
        }
        return pressed;
    }

    /**
     * 重置所有按键状态
     */
    public void reset() {
        keyStates.clear();
        heldTimers.clear();
        repeatTimers.clear();
    }
}
