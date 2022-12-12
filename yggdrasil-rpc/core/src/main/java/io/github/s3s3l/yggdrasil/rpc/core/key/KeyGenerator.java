package io.github.s3s3l.yggdrasil.rpc.core.key;

public interface KeyGenerator<T> {
    String getKey(T config, String id, KeyType type);
}
