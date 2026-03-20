package io.github.s3s3l.yggdrasil.game.core.ui.input;

/**
 * 触摸事件
 * 封装触摸输入的详细信息
 */
public class TouchEvent {

    /**
     * 触摸动作类型
     */
    public enum Action {
        /**
         * 触摸按下
         */
        DOWN,

        /**
         * 触摸释放
         */
        UP,

        /**
         * 触摸移动
         */
        MOVE,

        /**
         * 取消
         */
        CANCEL
    }

    private float x;
    private float y;
    private Action action;
    private int pointerId;
    private long timestamp;
    private float pressure;

    /**
     * 创建触摸事件
     */
    public TouchEvent() {
        this.action = Action.DOWN;
        this.pointerId = 0;
        this.timestamp = System.currentTimeMillis();
        this.pressure = 1.0f;
    }

    /**
     * 创建触摸事件
     *
     * @param x         X 坐标
     * @param y         Y 坐标
     * @param action    动作类型
     * @param pointerId 指针 ID
     */
    public TouchEvent(float x, float y, Action action, int pointerId) {
        this.x = x;
        this.y = y;
        this.action = action;
        this.pointerId = pointerId;
        this.timestamp = System.currentTimeMillis();
        this.pressure = 1.0f;
    }

    /**
     * 创建触摸事件
     *
     * @param x         X 坐标
     * @param y         Y 坐标
     * @param action    动作类型
     * @param pointerId 指针 ID
     * @param pressure  压力值
     */
    public TouchEvent(float x, float y, Action action, int pointerId, float pressure) {
        this.x = x;
        this.y = y;
        this.action = action;
        this.pointerId = pointerId;
        this.timestamp = System.currentTimeMillis();
        this.pressure = pressure;
    }

    /**
     * 设置事件属性
     *
     * @param x         X 坐标
     * @param y         Y 坐标
     * @param action    动作类型
     * @param pointerId 指针 ID
     */
    public void set(float x, float y, Action action, int pointerId) {
        this.x = x;
        this.y = y;
        this.action = action;
        this.pointerId = pointerId;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 设置事件属性
     *
     * @param x         X 坐标
     * @param y         Y 坐标
     * @param action    动作类型
     * @param pointerId 指针 ID
     * @param pressure  压力值
     */
    public void set(float x, float y, Action action, int pointerId, float pressure) {
        this.x = x;
        this.y = y;
        this.action = action;
        this.pointerId = pointerId;
        this.timestamp = System.currentTimeMillis();
        this.pressure = pressure;
    }

    /**
     * 复制另一个事件
     *
     * @param other 要复制的事件
     */
    public void copyFrom(TouchEvent other) {
        this.x = other.x;
        this.y = other.y;
        this.action = other.action;
        this.pointerId = other.pointerId;
        this.timestamp = other.timestamp;
        this.pressure = other.pressure;
    }

    /**
     * 复制当前事件
     *
     * @return 新的事件副本
     */
    public TouchEvent copy() {
        TouchEvent copy = new TouchEvent();
        copy.copyFrom(this);
        return copy;
    }

    // ==================== Getter/Setter ====================

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public int getPointerId() {
        return pointerId;
    }

    public void setPointerId(int pointerId) {
        this.pointerId = pointerId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    @Override
    public String toString() {
        return "TouchEvent{" +
                "x=" + x +
                ", y=" + y +
                ", action=" + action +
                ", pointerId=" + pointerId +
                '}';
    }
}
