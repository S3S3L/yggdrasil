package io.github.s3s3l.yggdrasil.game.core.event;

import java.util.concurrent.atomic.AtomicLong;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
class ListenerEntry {
    private final EventListener<Event> listener;
    private final EventOptions options;
    private final AtomicLong lastFireNanos = new AtomicLong(0L);
    private final AtomicLong scheduledNanos = new AtomicLong(0L);
}
