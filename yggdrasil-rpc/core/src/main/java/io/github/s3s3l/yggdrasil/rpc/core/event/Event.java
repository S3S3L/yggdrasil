package io.github.s3s3l.yggdrasil.rpc.core.event;

public interface Event<E extends Enum<?>, D> {
    E eventType();

    D data();

    D oldData();

    String key();
}
