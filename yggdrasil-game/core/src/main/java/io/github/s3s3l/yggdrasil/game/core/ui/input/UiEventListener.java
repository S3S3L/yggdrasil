package io.github.s3s3l.yggdrasil.game.core.ui.input;

@FunctionalInterface
public interface UiEventListener {

    void handle(UiInputEvent event);
}
