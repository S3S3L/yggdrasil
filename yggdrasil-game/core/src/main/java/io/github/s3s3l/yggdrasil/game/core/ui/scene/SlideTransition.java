package io.github.s3s3l.yggdrasil.game.core.ui.scene;

/**
 * 滑动过渡
 */
public class SlideTransition extends SceneTransition {

    public enum SlideDirection {
        LEFT, RIGHT, UP, DOWN
    }

    private SlideDirection direction;
    private float offsetX;
    private float offsetY;

    /**
     * 创建滑动过渡
     *
     * @param duration  过渡时长
     * @param direction 滑动方向
     */
    public SlideTransition(float duration, SlideDirection direction) {
        super(duration);
        this.direction = direction;
        this.offsetX = 0;
        this.offsetY = 0;
    }

    /**
     * 获取滑动方向
     *
     * @return 方向
     */
    public SlideDirection getDirection() {
        return direction;
    }

    /**
     * 设置滑动方向
     *
     * @param direction 方向
     */
    public void setDirection(SlideDirection direction) {
        this.direction = direction;
    }

    /**
     * 获取 X 轴偏移
     *
     * @return X 偏移
     */
    public float getOffsetX() {
        return offsetX;
    }

    /**
     * 获取 Y 轴偏移
     *
     * @return Y 偏移
     */
    public float getOffsetY() {
        return offsetY;
    }

    @Override
    public void render() {
        // 实际渲染由 UIRenderer 处理
    }

    @Override
    protected void onUpdate(float progress) {
        // 计算偏移量
        switch (direction) {
            case LEFT:
                offsetX = -1 + progress * 2; // -1 -> 1
                offsetY = 0;
                break;
            case RIGHT:
                offsetX = 1 - progress * 2; // 1 -> -1
                offsetY = 0;
                break;
            case UP:
                offsetX = 0;
                offsetY = -1 + progress * 2; // -1 -> 1
                break;
            case DOWN:
                offsetX = 0;
                offsetY = 1 - progress * 2; // 1 -> -1
                break;
            default:
                offsetX = 0;
                offsetY = 0;
                break;
        }
    }

    @Override
    protected void onFinish() {
        offsetX = 0;
        offsetY = 0;
    }
}
