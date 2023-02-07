package io.github.s3s3l.yggdrasil.register.core;

import io.github.s3s3l.yggdrasil.register.core.event.Event;
import io.github.s3s3l.yggdrasil.register.core.listener.ListenType;
import io.github.s3s3l.yggdrasil.register.core.listener.Listener;
import io.github.s3s3l.yggdrasil.register.core.node.Node;

public interface Listenable<N extends Node, D, ET extends Enum<?>, E extends Event<ET, D>> {
    void addListener(Node node, Listener<D, ET, E> listener, ListenType type);

    void removeListener(Listener<D, ET, E> listener);
}
