package io.github.s3s3l.yggdrasil.rpc.core.config;

import io.github.s3s3l.yggdrasil.rpc.core.Listenable;
import io.github.s3s3l.yggdrasil.rpc.core.event.Event;

public interface ConfigManager<C, EN extends Enum<?>, E extends Event<EN, C>> extends Listenable<C, EN, E> {
    boolean update(String key, C config);

    boolean delete(String key);

    C get(String key);
}
