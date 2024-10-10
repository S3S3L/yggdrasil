package io.github.s3s3l.yggdrasil.promise;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class NativeLockPromiseSupport<T> extends PromiseSupport<T> {
    private static final int RUNNING = 1;
    private static final int FAILED = -1;
    private static final int COMPLETED = 0;

    private final Object lock;

    private volatile int state = RUNNING;
    private T value;

    NativeLockPromiseSupport(ExecutorService executorService) {
        super(executorService);
        lock = new Object();
    }

    @Override
    public void fullfill() {
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    @Override
    public void fulfill(Operator<T> operator) {
        executorService.submit(() -> {
            try {
                this.value = operator.operate();
                state = COMPLETED;
                fullfill();
            } catch (Exception e) {
                fulfillWithException(e);
            }
        });
    }

    @Override
    public void fulfillWithException(Exception exception) {
        this.exception = exception;
        state = FAILED;
        handleException(exception);
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

        switch (state) {
            case COMPLETED:
                return value;
            case FAILED:
            default:
                return null;
        }
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (state == COMPLETED) {
            return value;
        }
        synchronized (lock) {
            try {
                lock.wait(unit.toMillis(timeout));
            } catch (InterruptedException e) {
                System.out.println("excetpion");
                Thread.currentThread()
                        .interrupt();
            }
        }
        if (state == COMPLETED) {
            return value;
        }
        throw new TimeoutException();
    }

}
