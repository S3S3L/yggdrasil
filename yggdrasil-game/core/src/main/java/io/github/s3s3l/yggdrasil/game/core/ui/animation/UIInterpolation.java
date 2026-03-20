package io.github.s3s3l.yggdrasil.game.core.ui.animation;

/**
 * 插值器接口
 * 定义两个值之间的插值计算
 */
public interface UIInterpolation {

    /**
     * 计算插值
     *
     * @param from  起始值
     * @param to    目标值
     * @param alpha 插值因子 (0.0 - 1.0)
     * @return 插值结果
     */
    float interpolate(float from, float to, float alpha);

    /**
     * 预定义的线性插值
     */
    UIInterpolation LINEAR = (from, to, alpha) -> from + (to - from) * alpha;

    /**
     * 预定义的平滑步进插值
     */
    UIInterpolation SMOOTH_STEP = (from, to, alpha) -> {
        float t = alpha * alpha * (3 - 2 * alpha);
        return from + (to - from) * t;
    };

    /**
     * 预定义的更平滑步进插值
     */
    UIInterpolation SMOOTHER_STEP = (from, to, alpha) -> {
        float t = alpha * alpha * alpha * (alpha * (alpha * 6 - 15) + 10);
        return from + (to - from) * t;
    };
}
