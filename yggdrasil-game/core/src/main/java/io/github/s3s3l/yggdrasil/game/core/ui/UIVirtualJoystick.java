package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.Objects;

/**
 * UI 虚拟摇杆组件
 * 8 方向/360 度模式，支持摇杆大小调节，支持摇杆跟随模式，用于移动端触摸控制
 */
public class UIVirtualJoystick extends UIComponent {

    private float radius;                            // 摇杆半径
    private float knobRadius;                        // 摇杆头半径
    private float knobX, knobY;                      // 摇杆头位置
    private Vector2 direction;                       // 方向向量
    private float angle;                             // 角度（度）
    private JoystickMode mode;                       // 8 方向/360 度
    private boolean followTouch;                     // 跟随触摸
    private UIColor stickColor;                      // 摇杆座颜色
    private UIColor knobColor;                       // 摇杆头颜色
    private UIColor activeColor;                     // 激活状态颜色
    private float deadZone;                          // 死区范围
    private JoystickListener listener;               // 摇杆监听器
    private boolean touching;                        // 是否正在触摸
    private float touchX, touchY;                    // 触摸点位置
    private float centerX, centerY;                  // 中心点位置
    private boolean snapBack;                        // 自动回弹
    private float snapBackSpeed;                     // 回弹速度

    /**
     * 2D 向量类
     */
    public static class Vector2 {
        public float x;
        public float y;

        public Vector2() {
            this(0, 0);
        }

        public Vector2(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public Vector2 set(float x, float y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Vector2 set(Vector2 other) {
            this.x = other.x;
            this.y = other.y;
            return this;
        }

        public float length() {
            return (float) Math.sqrt(x * x + y * y);
        }

        public float lengthSquared() {
            return x * x + y * y;
        }

        public Vector2 normalize() {
            float len = length();
            if (len > 0) {
                x /= len;
                y /= len;
            }
            return this;
        }

        public Vector2 scale(float scale) {
            x *= scale;
            y *= scale;
            return this;
        }

        public Vector2 copy() {
            return new Vector2(x, y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vector2 vector2 = (Vector2) o;
            return Float.compare(vector2.x, x) == 0 && Float.compare(vector2.y, y) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    /**
     * 摇杆模式枚举
     */
    public enum JoystickMode {
        DIRECTION_8,      // 8 方向（每 45 度一个方向）
        DIRECTION_4,      // 4 方向（上下左右）
        DIRECTION_360     // 360 度自由方向
    }

    /**
     * 摇杆监听器接口
     */
    public interface JoystickListener {
        /**
         * 当方向改变时调用
         *
         * @param joystick 虚拟摇杆
         * @param direction 方向向量
         * @param angle 角度（度）
         * @param intensity 强度 (0-1)
         */
        void onDirectionChanged(UIVirtualJoystick joystick, Vector2 direction, float angle, float intensity);

        /**
         * 当触摸开始时调用
         *
         * @param joystick 虚拟摇杆
         * @param x 触摸 X 坐标
         * @param y 触摸 Y 坐标
         */
        void onTouchStart(UIVirtualJoystick joystick, float x, float y);

        /**
         * 当触摸结束时调用
         *
         * @param joystick 虚拟摇杆
         */
        void onTouchEnd(UIVirtualJoystick joystick);
    }

    public UIVirtualJoystick() {
        this(50, 20);
    }

    public UIVirtualJoystick(float radius, float knobRadius) {
        this.radius = Math.max(10, radius);
        this.knobRadius = Math.max(5, knobRadius);
        this.knobX = 0;
        this.knobY = 0;
        this.direction = new Vector2(0, 0);
        this.angle = 0;
        this.mode = JoystickMode.DIRECTION_360;
        this.followTouch = false;
        this.stickColor = new UIColor(0.3f, 0.3f, 0.3f, 0.8f);
        this.knobColor = UIColor.WHITE;
        this.activeColor = UIColor.BLUE;
        this.deadZone = 0.1f;
        this.touching = false;
        this.touchX = 0;
        this.touchY = 0;
        this.centerX = 0;
        this.centerY = 0;
        this.snapBack = true;
        this.snapBackSpeed = 10f;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = Math.max(10, radius);
    }

    public float getKnobRadius() {
        return knobRadius;
    }

    public void setKnobRadius(float knobRadius) {
        this.knobRadius = Math.max(5, knobRadius);
    }

    public float getKnobX() {
        return knobX;
    }

    public float getKnobY() {
        return knobY;
    }

    public Vector2 getDirection() {
        return direction.copy();
    }

    public float getAngle() {
        return angle;
    }

    public JoystickMode getMode() {
        return mode;
    }

    public void setMode(JoystickMode mode) {
        this.mode = mode;
    }

    public boolean isFollowTouch() {
        return followTouch;
    }

    public void setFollowTouch(boolean followTouch) {
        this.followTouch = followTouch;
    }

    public UIColor getStickColor() {
        return stickColor;
    }

    public void setStickColor(UIColor stickColor) {
        this.stickColor = stickColor;
    }

    public UIColor getKnobColor() {
        return knobColor;
    }

    public void setKnobColor(UIColor knobColor) {
        this.knobColor = knobColor;
    }

    public UIColor getActiveColor() {
        return activeColor;
    }

    public void setActiveColor(UIColor activeColor) {
        this.activeColor = activeColor;
    }

    public float getDeadZone() {
        return deadZone;
    }

    public void setDeadZone(float deadZone) {
        this.deadZone = Math.max(0, Math.min(1, deadZone));
    }

    public JoystickListener getListener() {
        return listener;
    }

    public void setListener(JoystickListener listener) {
        this.listener = listener;
    }

    public boolean isTouching() {
        return touching;
    }

    public boolean isSnapBack() {
        return snapBack;
    }

    public void setSnapBack(boolean snapBack) {
        this.snapBack = snapBack;
    }

    public float getSnapBackSpeed() {
        return snapBackSpeed;
    }

    public void setSnapBackSpeed(float snapBackSpeed) {
        this.snapBackSpeed = Math.max(1f, snapBackSpeed);
    }

    /**
     * 获取摇杆强度 (0-1)
     *
     * @return 强度
     */
    public float getIntensity() {
        return direction.length();
    }

    /**
     * 获取归一化的方向向量
     *
     * @return 归一化方向向量
     */
    public Vector2 getNormalizedDirection() {
        Vector2 normalized = direction.copy();
        float len = normalized.length();
        if (len > 0) {
            normalized.x /= len;
            normalized.y /= len;
        }
        return normalized;
    }

    /**
     * 获取 8 方向的角度（45 度倍数）
     *
     * @return 8 方向角度
     */
    private float snapTo8Direction(float angle) {
        return Math.round(angle / 45) * 45;
    }

    /**
     * 获取 4 方向的角度（90 度倍数）
     *
     * @return 4 方向角度
     */
    private float snapTo4Direction(float angle) {
        // 将角度映射到最近的 90 度倍数（0, 90, 180, 270）
        float snapped = Math.round(angle / 90) * 90;
        // 如果是 360，转为 0
        return snapped % 360;
    }

    /**
     * 处理触摸开始
     *
     * @param x 触摸 X 坐标
     * @param y 触摸 Y 坐标
     */
    public void handleTouchStart(float x, float y) {
        // 检查触摸点是否在摇杆范围内
        float dx = x - centerX;
        float dy = y - centerY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance <= radius) {
            touching = true;
            touchX = x;
            touchY = y;

            if (followTouch) {
                // 跟随模式：移动中心点到触摸位置
                centerX = x;
                centerY = y;
            }

            // 更新摇杆头位置
            updateKnobPosition(x, y);

            if (listener != null) {
                listener.onTouchStart(this, x, y);
            }
        }
    }

    /**
     * 处理触摸拖动
     *
     * @param x 触摸 X 坐标
     * @param y 触摸 Y 坐标
     */
    public void handleTouchDrag(float x, float y) {
        if (!touching) {
            return;
        }

        touchX = x;
        touchY = y;
        updateKnobPosition(x, y);
    }

    /**
     * 处理触摸结束
     *
     * @param x 触摸 X 坐标
     * @param y 触摸 Y 坐标
     */
    public void handleTouchEnd(float x, float y) {
        if (!touching) {
            return;
        }

        touching = false;

        if (snapBack) {
            // 回弹到中心
            knobX = centerX;
            knobY = centerY;
            direction.set(0, 0);
            angle = 0;
        }

        if (listener != null) {
            listener.onTouchEnd(this);
        }
    }

    /**
     * 更新摇杆头位置
     *
     * @param touchX 触摸 X 坐标
     * @param touchY 触摸 Y 坐标
     */
    private void updateKnobPosition(float touchX, float touchY) {
        float dx = touchX - centerX;
        float dy = touchY - centerY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        // 限制摇杆头在半径范围内
        float maxDistance = radius - knobRadius;
        float clampedDistance = Math.min(distance, maxDistance);

        // 计算角度
        float newAngle = (float) Math.toDegrees(Math.atan2(dy, dx));
        if (newAngle < 0) {
            newAngle += 360;
        }

        // 根据模式钳制角度
        if (mode == JoystickMode.DIRECTION_8) {
            newAngle = snapTo8Direction(newAngle);
        } else if (mode == JoystickMode.DIRECTION_4) {
            newAngle = snapTo4Direction(newAngle);
        }

        // 计算摇杆头位置
        if (distance > deadZone * maxDistance) {
            knobX = centerX + (float) Math.cos(Math.toRadians(newAngle)) * clampedDistance;
            knobY = centerY + (float) Math.sin(Math.toRadians(newAngle)) * clampedDistance;

            // 更新方向向量（归一化）
            float intensity = clampedDistance / maxDistance;
            direction.x = (float) Math.cos(Math.toRadians(newAngle)) * intensity;
            direction.y = (float) Math.sin(Math.toRadians(newAngle)) * intensity;
        } else {
            // 在死区内，回到中心
            knobX = centerX;
            knobY = centerY;
            direction.set(0, 0);
            newAngle = 0;
        }

        // 通知监听器
        if (listener != null && angle != newAngle) {
            angle = newAngle;
            listener.onDirectionChanged(this, direction.copy(), angle, getIntensity());
        }
    }

    /**
     * 检查点是否在摇杆内
     *
     * @param x X 坐标
     * @param y Y 坐标
     * @return 是否在摇杆内
     */
    public boolean containsPoint(float x, float y) {
        float dx = x - centerX;
        float dy = y - centerY;
        return dx * dx + dy * dy <= radius * radius;
    }

    /**
     * 设置摇杆中心位置
     *
     * @param x X 坐标
     * @param y Y 坐标
     */
    public void setCenterPosition(float x, float y) {
        this.centerX = x;
        this.centerY = y;
        if (!touching) {
            this.knobX = x;
            this.knobY = y;
        }
    }

    /**
     * 更新摇杆（每帧调用）
     *
     * @param deltaTime 时间增量
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // 如果没有触摸且需要回弹，逐渐回弹到中心
        if (!touching && snapBack) {
            float dx = centerX - knobX;
            float dy = centerY - knobY;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            if (distance > 0.1f) {
                float moveDistance = snapBackSpeed * deltaTime;
                if (moveDistance >= distance) {
                    knobX = centerX;
                    knobY = centerY;
                    direction.set(0, 0);
                    angle = 0;
                } else {
                    knobX += (dx / distance) * moveDistance;
                    knobY += (dy / distance) * moveDistance;
                }
            }
        }
    }

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        // 渲染摇杆座
        renderStick();

        // 渲染摇杆头
        renderKnob();
    }

    /**
     * 渲染摇杆座
     */
    protected void renderStick() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染摇杆头
     */
    protected void renderKnob() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 设置摇杆位置（用于布局）
     *
     * @param x X 坐标
     * @param y Y 坐标
     */
    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        // 如果没有设置过中心点，使用位置作为中心
        if (centerX == 0 && centerY == 0) {
            setCenterPosition(x + radius, y + radius);
        }
    }

    /**
     * 获取摇杆占用的总区域（包括最大偏移）
     *
     * @return 总区域大小
     */
    public float getTotalSize() {
        return radius * 2;
    }

    /**
     * 检查摇杆是否处于活动状态（有输入）
     *
     * @return true 如果正在使用
     */
    public boolean isActive() {
        return touching || direction.lengthSquared() > 0;
    }
}
