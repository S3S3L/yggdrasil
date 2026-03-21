package io.github.s3s3l.yggdrasil.game.core.render.culling;

/**
 * 可剔除对象接口
 * 用于视锥剔除的对象应实现此接口
 */
public interface Cullable {

    /**
     * 获取对象的中心 X 坐标
     *
     * @return 中心 X 坐标
     */
    float getCenterX();

    /**
     * 获取对象的中心 Y 坐标
     *
     * @return 中心 Y 坐标
     */
    float getCenterY();

    /**
     * 获取对象的包围盒宽度
     *
     * @return 宽度
     */
    float getBoundingWidth();

    /**
     * 获取对象的包围盒高度
     *
     * @return 高度
     */
    float getBoundingHeight();

    /**
     * 检查对象是否可见
     * 可以在对象被隐藏时返回 false
     *
     * @return true 如果对象可见
     */
    default boolean isVisible() {
        return true;
    }
}
