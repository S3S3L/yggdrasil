package io.github.s3s3l.yggdrasil.rpc.core.listener;

import io.github.s3s3l.yggdrasil.rpc.core.event.Event;

public interface Listener<D, EN extends Enum<?>, E extends Event<EN, D>> {
    void onEvent(E event);
}
