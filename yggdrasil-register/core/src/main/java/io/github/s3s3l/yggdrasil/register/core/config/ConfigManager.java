package io.github.s3s3l.yggdrasil.register.core.config;

import io.github.s3s3l.yggdrasil.register.core.Listenable;
import io.github.s3s3l.yggdrasil.register.core.event.Event;
import io.github.s3s3l.yggdrasil.register.core.node.Node;

public interface ConfigManager<N extends Node, D, ET extends Enum<?>, E extends Event<ET, D>> extends Listenable<N, D, ET, E> {
    boolean update(N Node, D data);

    boolean delete(N Node);

    D get(N Node);
}
