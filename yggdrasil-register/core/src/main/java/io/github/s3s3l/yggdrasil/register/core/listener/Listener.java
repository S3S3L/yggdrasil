package io.github.s3s3l.yggdrasil.register.core.listener;

import io.github.s3s3l.yggdrasil.register.core.event.Event;

public interface Listener<D, ET extends Enum<?>, E extends Event<ET, D>> {
    void onEvent(E event);
}
