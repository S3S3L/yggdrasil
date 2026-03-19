package io.github.s3s3l.yggdrasil.game._2d.event.mouse;

import io.github.s3s3l.yggdrasil.game._2d.event.InputEvent;
import io.github.s3s3l.yggdrasil.game.core.event.EventPriority;
import lombok.Getter;

/**
 * 鼠标点击事件
 */
@Getter
public class MouseClickEvent extends InputEvent {

    /**
     * 鼠标按钮
     */
    private final int button;

    /**
     * 点击位置的 X 坐标
     */
    private final float x;

    /**
     * 点击位置的 Y 坐标
     */
    private final float y;

    /**
     * 点击次数（1=单击，2=双击）
     */
    private final int clickCount;

    private MouseClickEvent(Object source, int button, float x, float y, int clickCount, EventPriority priority) {
        super(source, priority);
        this.button = button;
        this.x = x;
        this.y = y;
        this.clickCount = clickCount;
    }

    @Override
    public InputDeviceType getDeviceType() {
        return InputDeviceType.MOUSE;
    }

    /**
     * 创建鼠标点击事件
     *
     * @param source    事件源
     * @param button    鼠标按钮
     * @param x         X 坐标
     * @param y         Y 坐标
     * @param clickCount 点击次数
     * @return 鼠标点击事件
     */
    public static MouseClickEvent create(Object source, int button, float x, float y, int clickCount) {
        return new MouseClickEvent(source, button, x, y, clickCount, EventPriority.NORMAL);
    }

    /**
     * 创建鼠标点击事件（指定优先级）
     *
     * @param source    事件源
     * @param button    鼠标按钮
     * @param x         X 坐标
     * @param y         Y 坐标
     * @param clickCount 点击次数
     * @param priority  优先级
     * @return 鼠标点击事件
     */
    public static MouseClickEvent create(Object source, int button, float x, float y, int clickCount,
            EventPriority priority) {
        return new MouseClickEvent(source, button, x, y, clickCount, priority);
    }

    /**
     * 判断是否为左键点击
     *
     * @return true 如果是左键
     */
    public boolean isLeftButton() {
        return button == io.github.s3s3l.yggdrasil.game.core.basic.mouse.MouseButton.LEFT;
    }

    /**
     * 判断是否为右键点击
     *
     * @return true 如果是右键
     */
    public boolean isRightButton() {
        return button == io.github.s3s3l.yggdrasil.game.core.basic.mouse.MouseButton.RIGHT;
    }

    /**
     * 判断是否为中键点击
     *
     * @return true 如果是中键
     */
    public boolean isMiddleButton() {
        return button == io.github.s3s3l.yggdrasil.game.core.basic.mouse.MouseButton.MIDDLE;
    }

    /**
     * 判断是否为双击
     *
     * @return true 如果是双击
     */
    public boolean isDoubleClick() {
        return clickCount >= 2;
    }
}
