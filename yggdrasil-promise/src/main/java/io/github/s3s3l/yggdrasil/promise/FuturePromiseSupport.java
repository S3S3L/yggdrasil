package io.github.s3s3l.yggdrasil.promise;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class FuturePromiseSupport<T> extends PromiseSupport<T> {
    private Future<T> future;

    FuturePromiseSupport(ExecutorService executorService) {
        super(executorService);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (future == null) {
            return false;
        }
        return future.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        if (future == null) {
            return false;
        }
        return future.isCancelled();
    }

    @Override
    public boolean isDone() {
        if (future == null) {
            return false;
        }
        return future.isDone();
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        if (future == null) {
            return null;
        }
        return future.get();
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (future == null) {
            return null;
        }
        return future.get(timeout, unit);
    }

    @Override
    public void fullfill() {
        if (future == null) {
            return;
        }
        future = CompletableFuture.completedFuture(null);
    }

    @Override
    void fulfill(Operator<T> operator) {
        future = executorService.submit(() -> {
            try {
                return operator.operate();
            } catch (Exception e) {
                fulfillWithException(e);
                return null;
            }
        });
    }

    @Override
    public void fulfillWithException(Exception exception) {
        this.exception = exception;
        handleException(exception);
        log.debug("Promise fulfillWithException", exception);
        fullfill();
    }
    
}
