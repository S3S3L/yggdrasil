package io.github.s3s3l.yggdrasil.utils.promise;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class PromiseSupport<T> implements Future<T> {
    private static final int RUNNING = 1;
    private static final int FAILED = -1;
    private static final int COMPLETED = 0;

    private final Object lock;

    private volatile int state = RUNNING;
    private T value;
    @Getter(AccessLevel.PACKAGE)
    private Exception exception;

    PromiseSupport() {
        lock = new Object();
    }

    void fullfill() {
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    void fulfill(T value) {
        this.value = value;
        state = COMPLETED;
        fullfill();
    }

    void fulfillWithException(Exception exception) {
        this.exception = exception;
        state = FAILED;
        log.debug("Promise fulfillWithException", exception);
        fullfill();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return state < RUNNING;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        if (state == COMPLETED) {
            return value;
        }
        synchronized (lock) {
            while (state == RUNNING) {
                lock.wait();
            }
        }
        if (state == COMPLETED) {
            return value;
        }
        throw new ExecutionException(exception);
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (state == COMPLETED) {
            return value;
        }
        synchronized (lock) {
            while (state == RUNNING) {
                try {
                    lock.wait(unit.toMillis(timeout));
                } catch (InterruptedException e) {
                    log.warn("Interrupted", e);
                    Thread.currentThread()
                            .interrupt();
                }
            }
        }
        if (state == COMPLETED) {
            return value;
        }
        throw new ExecutionException(exception);
    }

}
