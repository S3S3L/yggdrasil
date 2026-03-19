package io.github.s3s3l.yggdrasil.game.core.scene;

/**
 * 淡入淡出过渡效果
 * 简单的场景过渡效果实现
 */
public class FadeTransition implements SceneTransition {

    /**
     * 过渡时长（毫秒）
     */
    private final long duration;

    public FadeTransition() {
        this(500); // 默认 500ms
    }

    public FadeTransition(long duration) {
        this.duration = duration;
    }

    @Override
    public void onTransitionStart(Scene fromScene, Scene toScene) {
        // 这里可以添加淡出动画逻辑
        // 由于当前框架没有动画系统，暂时留空
        System.out.println("FadeTransition: Starting fade effect (duration: " + duration + "ms)");
    }

    @Override
    public void onTransitionEnd(Scene fromScene, Scene toScene) {
        // 这里可以添加淡入动画逻辑
        System.out.println("FadeTransition: Ending fade effect");
    }

    /**
     * 获取过渡时长
     *
     * @return 过渡时长（毫秒）
     */
    public long getDuration() {
        return duration;
    }
}
