package io.github.s3s3l.yggdrasil.game.core.ui;

@FunctionalInterface
public interface UiNodeDrawHandler {

    void draw(UiNode node, UiDrawContext context);
}
