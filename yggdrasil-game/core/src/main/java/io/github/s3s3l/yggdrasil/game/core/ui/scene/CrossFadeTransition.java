package io.github.s3s3l.yggdrasil.game.core.ui.scene;

/**
 * 交叉淡出过渡
 * 源场景淡出的同时目标场景淡入
 */
public class CrossFadeTransition extends SceneTransition {

    private float fromAlpha;
    private float toAlpha;

    /**
     * 创建交叉淡出过渡
     *
     * @param duration 过渡时长
     */
    public CrossFadeTransition(float duration) {
        super(duration);
        this.fromAlpha = 1;
        this.toAlpha = 0;
    }

    /**
     * 获取源场景透明度
     *
     * @return 透明度（0-1）
     */
    public float getFromAlpha() {
        return fromAlpha;
    }

    /**
     * 获取目标场景透明度
     *
     * @return 透明度（0-1）
     */
    public float getToAlpha() {
        return toAlpha;
    }

    @Override
    public void render() {
        // 实际渲染由 UIRenderer 处理
    }

    @Override
    protected void onBegin(UIScene fromScene, UIScene toScene) {
        fromAlpha = 1;
        toAlpha = 0;
    }

    @Override
    protected void onUpdate(float progress) {
        fromAlpha = 1 - progress;
        toAlpha = progress;
    }

    @Override
    protected void onFinish() {
        fromAlpha = 0;
        toAlpha = 1;
    }
}
