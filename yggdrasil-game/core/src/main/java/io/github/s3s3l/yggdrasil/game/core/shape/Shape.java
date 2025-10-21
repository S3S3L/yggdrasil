package io.github.s3s3l.yggdrasil.game.core.shape;

/**
 * @param <T> 点的类型，2D或3D
 */
public interface Shape<T> {
    /**
     * 判断一个点是否包含在形状内
     * 
     * @param point 目标点
     * @param <T>   点的类型，2D或3D
     * @return
     */
    boolean contains(T point);

    /**
     * 判断一个形状是否完全包含在当前形状内
     * 
     * @param other 目标形状
     * @return
     */
    boolean contains(Shape<T> other);

    /**
     * 判断两个形状是否相交
     * 
     * @param other 目标形状
     * @return
     */
    boolean intersects(Shape<T> other);

    /**
     * 获取形状类型
     * 
     * @return 形状类型
     */
    int type();
}
