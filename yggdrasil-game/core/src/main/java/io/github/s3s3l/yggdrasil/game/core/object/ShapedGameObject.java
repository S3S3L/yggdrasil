package io.github.s3s3l.yggdrasil.game.core.object;

import io.github.s3s3l.yggdrasil.game.core.basic.Shaped;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;
import io.github.s3s3l.yggdrasil.game.core.state.State;

public abstract class ShapedGameObject<T, S extends Shape<T>> extends AbstractGameObject implements Shaped<T, S> {

    protected State<S> shape;

    public ShapedGameObject(GameObjectConfig config) {
        super(config);
    }

    @Override
    public S getShape() {
        return shape.getClone();
    }

    @Override
    public void setShape(S shape) {
        this.shape.update(s -> shape);
    }
}
