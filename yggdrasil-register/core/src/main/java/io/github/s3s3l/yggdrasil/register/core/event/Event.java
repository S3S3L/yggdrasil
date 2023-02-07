package io.github.s3s3l.yggdrasil.register.core.event;

public interface Event<ET extends Enum<?>, D> {
    ET eventType();

    D data();

    D oldData();
}
