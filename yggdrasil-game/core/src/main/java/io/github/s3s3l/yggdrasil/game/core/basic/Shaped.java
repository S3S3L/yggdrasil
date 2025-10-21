package io.github.s3s3l.yggdrasil.game.core.basic;

import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public interface Shaped<T, S extends Shape<T>> {

    S getShape();

    void setShape(S shape);
}
