package io.github.s3s3l.yggdrasil.game._2d.event.mouse;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 鼠标按钮枚举
 */
@Getter
@AllArgsConstructor
public enum MouseButton {
    /**
     * 左键
     */
    LEFT(0, "Left"),

    /**
     * 右键
     */
    RIGHT(1, "Right"),

    /**
     * 中键（滚轮键）
     */
    MIDDLE(2, "Middle"),

    /**
     * 侧键 1（后退）
     */
    BACK(3, "Back"),

    /**
     * 侧键 2（前进）
     */
    FORWARD(4, "Forward");

    private final int code;
    private final String name;

    /**
     * 根据按钮代码获取鼠标按钮
     *
     * @param code 按钮代码
     * @return 鼠标按钮枚举，如果不存在则返回 null
     */
    public static MouseButton fromCode(int code) {
        for (MouseButton button : values()) {
            if (button.getCode() == code) {
                return button;
            }
        }
        return null;
    }

    /**
     * 根据按钮名称获取鼠标按钮
     *
     * @param name 按钮名称
     * @return 鼠标按钮枚举，如果不存在则返回 null
     */
    public static MouseButton fromName(String name) {
        for (MouseButton button : values()) {
            if (button.getName().equalsIgnoreCase(name)) {
                return button;
            }
        }
        return null;
    }
}
