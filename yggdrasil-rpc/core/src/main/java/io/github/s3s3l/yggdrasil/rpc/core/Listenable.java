package io.github.s3s3l.yggdrasil.rpc.core;

import io.github.s3s3l.yggdrasil.rpc.core.event.Event;
import io.github.s3s3l.yggdrasil.rpc.core.listener.ListenType;
import io.github.s3s3l.yggdrasil.rpc.core.listener.Listener;

public interface Listenable<N, EN extends Enum<?>, E extends Event<EN, N>> {
    void addListener(String key, Listener<N, EN, E> listener, ListenType type);

    void removeListener(Listener<N, EN, E> listener);
}
