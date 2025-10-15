package io.github.s3s3l.yggdrasil.game.core.event.handler;

import io.github.s3s3l.yggdrasil.game.core.event.Event;

public interface EventHandler<E extends Event> {
    void handle(E event);
}
