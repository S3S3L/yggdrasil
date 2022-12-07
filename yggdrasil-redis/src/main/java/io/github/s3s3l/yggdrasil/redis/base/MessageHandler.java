package io.github.s3s3l.yggdrasil.redis.base;

@FunctionalInterface
public interface MessageHandler<T> {
    void handle(T message, String pattern);
}
