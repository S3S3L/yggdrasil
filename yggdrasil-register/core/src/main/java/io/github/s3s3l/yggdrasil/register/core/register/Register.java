package io.github.s3s3l.yggdrasil.register.core.register;

import io.github.s3s3l.yggdrasil.register.core.Listenable;
import io.github.s3s3l.yggdrasil.register.core.event.Event;
import io.github.s3s3l.yggdrasil.register.core.node.Node;

public interface Register<N extends Node, D, ET extends Enum<?>, E extends Event<ET, D>> extends Listenable<N, D, ET, E> {
    boolean register(N node, D data);

    boolean update(N node, D data);

    boolean remove(N node);

    RegisterType type();
}
