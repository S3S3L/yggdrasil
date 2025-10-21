package io.github.s3s3l.yggdrasil.game.core.shape.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public abstract class ShapeHandlerManager<T> {

    protected final Map<Integer, Map<Integer, ShapeHandler<T>>> handlers = new ConcurrentHashMap<>();

    public void register(ShapeHandler<T> handler) {
        handlers.computeIfAbsent(handler.srcType(), k -> new ConcurrentHashMap<>()).put(handler.targetType(), handler);
    }

    public boolean contains(Shape<T> src, Shape<T> target) {
        return getHandler(src, target).contains(src, target);
    }

    public boolean intersects(Shape<T> src, Shape<T> target) {
        return getHandler(src, target).intersects(src, target);
    }

    protected ShapeHandler<T> getHandler(Shape<T> src, Shape<T> target) {
        if (!handlers.containsKey(src.type())) {
            throw new IllegalArgumentException("Unsupported source shape type: " + src.type());
        }

        Map<Integer, ShapeHandler<T>> shapeHandlerLookup = handlers.get(src.type());
        if (!shapeHandlerLookup.containsKey(target.type())) {
            throw new IllegalArgumentException(
                    "Unsupported. source shape type: " + src.type() + " target shape type: " + target.type());
        }

        ShapeHandler<T> handler = shapeHandlerLookup.get(target.type());
        return handler;
    }
}
