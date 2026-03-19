package io.github.s3s3l.yggdrasil.game._2d.event.keyboard;

import io.github.s3s3l.yggdrasil.game._2d.event.InputEvent;
import io.github.s3s3l.yggdrasil.game.core.event.EventPriority;
import lombok.Getter;

/**
 * 键盘事件
 * 表示键盘按键的按下、释放和键入事件
 */
@Getter
public class KeyboardEvent extends InputEvent {

    /**
     * 键盘事件类型
     */
    private final KeyboardEventType type;

    /**
     * 按键代码（keyCode）
     */
    private final int keyCode;

    /**
     * 字符（仅 KEY_TYPED 事件有效）
     */
    private final char character;

    /**
     * 是否按下 Ctrl 键
     */
    private final boolean controlDown;

    /**
     * 是否按下 Shift 键
     */
    private final boolean shiftDown;

    /**
     * 是否按下 Alt 键
     */
    private final boolean altDown;

    private KeyboardEvent(Object source, KeyboardEventType type, int keyCode, char character,
                          boolean controlDown, boolean shiftDown, boolean altDown, EventPriority priority) {
        super(source, priority);
        this.type = type;
        this.keyCode = keyCode;
        this.character = character;
        this.controlDown = controlDown;
        this.shiftDown = shiftDown;
        this.altDown = altDown;
    }

    @Override
    public InputDeviceType getDeviceType() {
        return InputDeviceType.KEYBOARD;
    }

    /**
     * 判断是否是组合键 Ctrl+X
     *
     * @return true 如果是 Ctrl+X
     */
    public boolean isCtrlX() {
        return controlDown && character == 'X';
    }

    /**
     * 判断是否是组合键 Ctrl+C
     *
     * @return true 如果是 Ctrl+C
     */
    public boolean isCtrlC() {
        return controlDown && character == 'C';
    }

    /**
     * 判断是否是组合键 Ctrl+V
     *
     * @return true 如果是 Ctrl+V
     */
    public boolean isCtrlV() {
        return controlDown && character == 'V';
    }

    /**
     * 判断是否是组合键 Ctrl+A
     *
     * @return true 如果是 Ctrl+A
     */
    public boolean isCtrlA() {
        return controlDown && character == 'A';
    }

    /**
     * 判断是否是组合键 Ctrl+Z
     *
     * @return true 如果是 Ctrl+Z
     */
    public boolean isCtrlZ() {
        return controlDown && character == 'Z';
    }

    /**
     * 创建按键按下事件
     *
     * @param source      事件源
     * @param keyCode     按键代码
     * @param controlDown Ctrl 键状态
     * @param shiftDown   Shift 键状态
     * @param altDown     Alt 键状态
     * @return 键盘事件
     */
    public static KeyboardEvent keyDown(Object source, int keyCode, boolean controlDown, boolean shiftDown,
            boolean altDown) {
        return new KeyboardEvent(source, KeyboardEventType.KEY_DOWN, keyCode, '\0', controlDown, shiftDown, altDown,
                EventPriority.NORMAL);
    }

    /**
     * 创建按键释放事件
     *
     * @param source      事件源
     * @param keyCode     按键代码
     * @param controlDown Ctrl 键状态
     * @param shiftDown   Shift 键状态
     * @param altDown     Alt 键状态
     * @return 键盘事件
     */
    public static KeyboardEvent keyUp(Object source, int keyCode, boolean controlDown, boolean shiftDown,
            boolean altDown) {
        return new KeyboardEvent(source, KeyboardEventType.KEY_UP, keyCode, '\0', controlDown, shiftDown, altDown,
                EventPriority.NORMAL);
    }

    /**
     * 创建字符输入事件
     *
     * @param source      事件源
     * @param character   输入字符
     * @param controlDown Ctrl 键状态
     * @param shiftDown   Shift 键状态
     * @param altDown     Alt 键状态
     * @return 键盘事件
     */
    public static KeyboardEvent keyTyped(Object source, char character, boolean controlDown, boolean shiftDown,
            boolean altDown) {
        return new KeyboardEvent(source, KeyboardEventType.KEY_TYPED, 0, character, controlDown, shiftDown, altDown,
                EventPriority.NORMAL);
    }

    /**
     * 键盘事件类型枚举
     */
    public enum KeyboardEventType {
        /**
         * 按键按下
         */
        KEY_DOWN,

        /**
         * 按键释放
         */
        KEY_UP,

        /**
         * 字符输入
         */
        KEY_TYPED
    }
}
