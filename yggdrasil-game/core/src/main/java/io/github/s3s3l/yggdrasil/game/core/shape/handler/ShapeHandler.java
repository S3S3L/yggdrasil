package io.github.s3s3l.yggdrasil.game.core.shape.handler;

import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public interface ShapeHandler<T> {

    /**
     * 判断src是否完全包含target
     * 
     * @param src
     * @param target
     * @return
     */
    boolean contains(Shape<T> src, Shape<T> target);

    /**
     * 判断src和target是否相交
     * 
     * @param src
     * @param target
     * @return
     */
    boolean intersects(Shape<T> src, Shape<T> target);

    /**
     * 计算src和target的交点
     * 
     * @param src
     * @param target
     * @return
     */
    // T[] intersectPoint(Shape<T> src, Shape<T> target);

    int srcType();

    int targetType();
}
