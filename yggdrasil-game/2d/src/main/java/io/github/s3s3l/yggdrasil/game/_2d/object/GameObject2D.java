package io.github.s3s3l.yggdrasil.game._2d.object;

import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.object.GameObjectConfig;
import io.github.s3s3l.yggdrasil.game.core.object.ShapedGameObject;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public abstract class GameObject2D<S extends Shape<Vector2D>> extends ShapedGameObject<Vector2D, S> {

    public GameObject2D(GameObjectConfig config) {
        super(config);
    }
    
}
