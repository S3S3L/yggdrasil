package io.github.s3s3l.yggdrasil.game.core.ui.scene;

/**
 * 缩放过渡
 */
public class ZoomTransition extends SceneTransition {

    private float startScale;
    private float endScale;
    private float currentScale;

    /**
     * 创建缩放过渡
     *
     * @param duration 过渡时长
     */
    public ZoomTransition(float duration) {
        this(duration, 0.5f, 1.0f);
    }

    /**
     * 创建缩放过渡
     *
     * @param duration   过渡时长
     * @param startScale 起始缩放
     * @param endScale   结束缩放
     */
    public ZoomTransition(float duration, float startScale, float endScale) {
        super(duration);
        this.startScale = startScale;
        this.endScale = endScale;
        this.currentScale = startScale;
    }

    /**
     * 获取起始缩放
     *
     * @return 起始缩放
     */
    public float getStartScale() {
        return startScale;
    }

    /**
     * 设置起始缩放
     *
     * @param startScale 起始缩放
     */
    public void setStartScale(float startScale) {
        this.startScale = startScale;
    }

    /**
     * 获取结束缩放
     *
     * @return 结束缩放
     */
    public float getEndScale() {
        return endScale;
    }

    /**
     * 设置结束缩放
     *
     * @param endScale 结束缩放
     */
    public void setEndScale(float endScale) {
        this.endScale = endScale;
    }

    /**
     * 获取当前缩放
     *
     * @return 当前缩放
     */
    public float getCurrentScale() {
        return currentScale;
    }

    @Override
    public void render() {
        // 实际渲染由 UIRenderer 处理
    }

    @Override
    protected void onUpdate(float progress) {
        // 线性插值
        currentScale = startScale + (endScale - startScale) * progress;
    }

    @Override
    protected void onFinish() {
        currentScale = endScale;
    }
}
