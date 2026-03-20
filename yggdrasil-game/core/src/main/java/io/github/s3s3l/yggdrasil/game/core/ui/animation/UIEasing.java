package io.github.s3s3l.yggdrasil.game.core.ui.animation;

/**
 * 缓动函数接口
 * 定义动画的速度曲线
 */
public interface UIEasing {

    /**
     * 计算缓动值
     *
     * @param t 时间进度 (0.0 - 1.0)
     * @return 缓动后的值 (0.0 - 1.0)
     */
    float apply(float t);

    /**
     * 预定义的线性缓动
     */
    UIEasing LINEAR = t -> t;

    /**
     * 预定义的二次方缓入
     */
    UIEasing IN_QUAD = t -> t * t;

    /**
     * 预定义的二次方缓出
     */
    UIEasing OUT_QUAD = t -> t * (2 - t);

    /**
     * 预定义的二次方缓入缓出
     */
    UIEasing IN_OUT_QUAD = t -> t < 0.5f ? 2 * t * t : -1 + (4 - 2 * t) * t;

    /**
     * 预定义的三次方缓入
     */
    UIEasing IN_CUBIC = t -> t * t * t;

    /**
     * 预定义的三次方缓出
     */
    UIEasing OUT_CUBIC = t -> {
        float t1 = t - 1;
        return t1 * t1 * t1 + 1;
    };

    /**
     * 预定义的三次方缓入缓出
     */
    UIEasing IN_OUT_CUBIC = t -> {
        if (t < 0.5f) {
            return 4 * t * t * t;
        } else {
            float t1 = t - 1;
            return 4 * t1 * t1 * t1 + 1;
        }
    };

    /**
     * 预定义的五次方缓入
     */
    UIEasing IN_QUINT = t -> {
        float t2 = t * t;
        return t * t2 * t2;
    };

    /**
     * 预定义的五次方缓出
     */
    UIEasing OUT_QUINT = t -> {
        float t1 = t - 1;
        float t2 = t1 * t1;
        return t1 * t2 * t2 + 1;
    };

    /**
     * 预定义的五次方缓入缓出
     */
    UIEasing IN_OUT_QUINT = t -> {
        if (t < 0.5f) {
            float t2 = t * t;
            return 16 * t * t2 * t2;
        } else {
            float t1 = t - 1;
            float t2 = t1 * t1;
            return 16 * t1 * t2 * t2 + 1;
        }
    };

    /**
     * 预定义的正弦缓入
     */
    UIEasing IN_SINE = t -> 1 - (float) Math.cos(t * Math.PI / 2);

    /**
     * 预定义的正弦缓出
     */
    UIEasing OUT_SINE = t -> (float) Math.sin(t * Math.PI / 2);

    /**
     * 预定义的正弦缓入缓出
     */
    UIEasing IN_OUT_SINE = t -> -(float) Math.cos(Math.PI * t) / 2 + 0.5f;

    /**
     * 预定义的指数缓入
     */
    UIEasing IN_EXPO = t -> t == 0 ? 0 : (float) Math.pow(2, 10 * (t - 1));

    /**
     * 预定义的指数缓出
     */
    UIEasing OUT_EXPO = t -> t == 1 ? 1 : 1 - (float) Math.pow(2, -10 * t);

    /**
     * 预定义的指数缓入缓出
     */
    UIEasing IN_OUT_EXPO = t -> {
        if (t == 0) return 0;
        if (t == 1) return 1;
        if (t < 0.5f) {
            return (float) Math.pow(2, 20 * t - 10) / 2;
        } else {
            return (2 - (float) Math.pow(2, -20 * t + 10)) / 2;
        }
    };

    /**
     * 预定义的圆形缓入
     */
    UIEasing IN_CIRC = t -> 1 - (float) Math.sqrt(1 - t * t);

    /**
     * 预定义的圆形缓出
     */
    UIEasing OUT_CIRC = t -> (float) Math.sqrt(1 - (t - 1) * (t - 1));

    /**
     * 预定义的圆形缓入缓出
     */
    UIEasing IN_OUT_CIRC = t -> {
        if (t < 0.5f) {
            return (1 - (float) Math.sqrt(1 - 4 * t * t)) / 2;
        } else {
            return (1 + (float) Math.sqrt(1 - (2 * t - 1) * (2 * t - 1))) / 2;
        }
    };

    /**
     * 预定义的弹跳缓出
     */
    UIEasing OUT_BOUNCE = t -> {
        final float n1 = 7.5625f;
        final double d1 = 2.75d;

        if (t < 1 / d1) {
            return n1 * t * t;
        } else if (t < 2 / d1) {
            return n1 * (t -= 1.5f / d1) * t + 0.75f;
        } else if (t < 2.5 / d1) {
            return n1 * (t -= 2.25f / d1) * t + 0.9375f;
        } else {
            return n1 * (t -= 2.625f / d1) * t + 0.984375f;
        }
    };

    /**
     * 预定义的弹性缓出
     */
    UIEasing OUT_ELASTIC = t -> {
        if (t == 0) return 0;
        if (t == 1) return 1;
        return (float) (Math.pow(2, -10 * t) * Math.sin((t * 10 - 0.75f) * (2 * Math.PI) / 3)) + 1;
    };

    /**
     * 预定义的背冲缓出
     */
    UIEasing OUT_BACK = t -> {
        final float c1 = 1.70158f;
        final float c3 = c1 + 1;
        return 1 + c3 * (float) Math.pow(t - 1, 3) + c1 * (float) Math.pow(t - 1, 2);
    };
}
