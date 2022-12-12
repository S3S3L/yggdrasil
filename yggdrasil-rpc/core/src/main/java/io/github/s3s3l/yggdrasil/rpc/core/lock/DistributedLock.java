package io.github.s3s3l.yggdrasil.rpc.core.lock;

public interface DistributedLock {
    boolean tryLock(String key);

    void lock(String key);

    void unlock(String key);

    DistributedLockType type();
}
