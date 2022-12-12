package io.github.s3s3l.yggdrasil.rpc.core.register;

import io.github.s3s3l.yggdrasil.rpc.core.Listenable;
import io.github.s3s3l.yggdrasil.rpc.core.event.Event;

public interface Register<N, EN extends Enum<?>, E extends Event<EN, N>> extends Listenable<N, EN, E> {
    boolean register(String key, N nodeInfo);

    boolean update(String key, N nodeInfo);

    RegisterType type();
}
