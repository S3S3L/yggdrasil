package io.github.s3s3l.yggdrasil.game.core.scene;

import lombok.extern.slf4j.Slf4j;

/**
 * 淡入淡出过渡效果
 * 简单的场景过渡效果实现
 */
@Slf4j
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
        log.debug("FadeTransition: Starting fade effect (duration: {}ms)", duration);
    }

    @Override
    public void onTransitionEnd(Scene fromScene, Scene toScene) {
        log.debug("FadeTransition: Ending fade effect");
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
