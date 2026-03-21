package io.github.s3s3l.yggdrasil.game.core.demo.entity.component;

/**
 * 移动组件
 * 管理实体的移动速度、位置等
 */
public class MovementComponent {

    /**
     * X 轴位置
     */
    private float x;

    /**
     * Y 轴位置
     */
    private float y;

    /**
     * X 轴速度
     */
    private float velocityX;

    /**
     * Y 轴速度
     */
    private float velocityY;

    /**
     * 移动速度
     */
    private float moveSpeed;

    /**
     * 是否正在移动
     */
    private boolean isMoving;

    /**
     * 创建移动组件
     */
    public MovementComponent() {
        this(0, 0, 100);
    }

    /**
     * 创建移动组件
     *
     * @param x        初始 X 位置
     * @param y        初始 Y 位置
     * @param moveSpeed 移动速度
     */
    public MovementComponent(float x, float y, float moveSpeed) {
        this.x = x;
        this.y = y;
        this.moveSpeed = moveSpeed;
        this.velocityX = 0;
        this.velocityY = 0;
        this.isMoving = false;
    }

    /**
     * 获取 X 位置
     *
     * @return X 位置
     */
    public float getX() {
        return x;
    }

    /**
     * 获取 Y 位置
     *
     * @return Y 位置
     */
    public float getY() {
        return y;
    }

    /**
     * 设置位置
     *
     * @param x X 位置
     * @param y Y 位置
     */
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 获取 X 速度
     *
     * @return X 速度
     */
    public float getVelocityX() {
        return velocityX;
    }

    /**
     * 获取 Y 速度
     *
     * @return Y 速度
     */
    public float getVelocityY() {
        return velocityY;
    }

    /**
     * 设置速度
     *
     * @param velocityX X 速度
     * @param velocityY Y 速度
     */
    public void setVelocity(float velocityX, float velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.isMoving = velocityX != 0 || velocityY != 0;
    }

    /**
     * 设置移动速度
     *
     * @param speed 移动速度
     */
    public void setMoveSpeed(float speed) {
        this.moveSpeed = speed;
    }

    /**
     * 获取移动速度
     *
     * @return 移动速度
     */
    public float getMoveSpeed() {
        return moveSpeed;
    }

    /**
     * 检查是否正在移动
     *
     * @return true 如果正在移动
     */
    public boolean isMoving() {
        return isMoving;
    }

    /**
     * 更新位置（每帧调用）
     *
     * @param delta 时间增量（秒）
     */
    public void update(float delta) {
        if (isMoving) {
            x += velocityX * delta;
            y += velocityY * delta;
        }
    }

    /**
     * 向目标方向移动
     *
     * @param directionX 方向 X（-1 到 1）
     * @param directionY 方向 Y（-1 到 1）
     */
    public void move(float directionX, float directionY) {
        // 归一化方向向量
        float length = (float) Math.sqrt(directionX * directionX + directionY * directionY);
        if (length > 0) {
            directionX /= length;
            directionY /= length;
        }

        velocityX = directionX * moveSpeed;
        velocityY = directionY * moveSpeed;
        isMoving = directionX != 0 || directionY != 0;
    }

    /**
     * 停止移动
     */
    public void stop() {
        velocityX = 0;
        velocityY = 0;
        isMoving = false;
    }

    /**
     * 计算到目标位置的距离
     *
     * @param targetX 目标 X
     * @param targetY 目标 Y
     * @return 距离
     */
    public float distanceTo(float targetX, float targetY) {
        float dx = targetX - x;
        float dy = targetY - y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}
