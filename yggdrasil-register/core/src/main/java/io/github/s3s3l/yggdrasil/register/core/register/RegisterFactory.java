package io.github.s3s3l.yggdrasil.register.core.register;

import java.net.URI;

import io.github.s3s3l.yggdrasil.register.core.event.Event;
import io.github.s3s3l.yggdrasil.register.core.node.Node;

public abstract class RegisterFactory<N extends Node, D, ET extends Enum<?>, E extends Event<ET, D>> {
    public RegisterFactory() {
    }

    abstract Register<N, D, ET, E> getRegister(URI uri);

    abstract String getScheme();
}
