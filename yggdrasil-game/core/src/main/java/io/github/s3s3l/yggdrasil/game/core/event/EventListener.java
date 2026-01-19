package io.github.s3s3l.yggdrasil.game.core.event;

@FunctionalInterface
public interface EventListener<E extends Event> {
    EventResult handle(EventContext<E> context);
}
