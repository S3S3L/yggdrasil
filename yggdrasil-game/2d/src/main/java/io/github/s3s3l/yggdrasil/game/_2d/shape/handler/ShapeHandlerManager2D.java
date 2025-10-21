package io.github.s3s3l.yggdrasil.game._2d.shape.handler;

import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.handler.ShapeHandlerManager;
import io.github.s3s3l.yggdrasil.utils.reflect.scan.ClassScanner;

public class ShapeHandlerManager2D extends ShapeHandlerManager<Vector2D> {

    private static final ShapeHandlerManager2D INSTANCE = new ShapeHandlerManager2D();

    private ShapeHandlerManager2D() {
        new ClassScanner().scan("io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl")
                .stream()
                .filter(ShapeHandler2D.class::isAssignableFrom)
                .forEach(handlerType -> {
                    try {
                        ShapeHandler2D handler = (ShapeHandler2D) handlerType.getDeclaredConstructor().newInstance();
                        register(handler);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to instantiate handler: " + handlerType.getName(), e);
                    }
                });
    }

    public static ShapeHandlerManager2D get() {
        return INSTANCE;
    }
}
