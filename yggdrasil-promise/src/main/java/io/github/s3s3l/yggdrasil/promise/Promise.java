package io.github.s3s3l.yggdrasil.promise;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Promise<V> {

    private static final ExecutorService DEFAULT_EXECUTOR_SERVICE = Executors.newCachedThreadPool(r -> {
        Thread t = new Thread(r);
        t.setName("promise-default");
        return t;
    });

    private static PromiseImplements impl = PromiseImplements.NATIVE_LOCK;

    public static void configure(PromiseImplements impl) {
        Promise.impl = impl;
    }

    public static <V> Promise<V> async(Supplier<V> supplier) {
        return async(supplier, DEFAULT_EXECUTOR_SERVICE);
    }

    public static <V> Promise<V> async(Supplier<V> supplier, ExecutorService executorService) {
        Promise<V> promise = new Promise<>(executorService);
        promise.start(supplier::get);
        return promise;
    }

    public static Promise<Object[]> all(Supplier<?>... suppliers) {
        return all(DEFAULT_EXECUTOR_SERVICE, suppliers);
    }

    public static Promise<Object[]> all(ExecutorService executorService, Supplier<?>... suppliers) {
        Promise<?>[] promises = Arrays.stream(suppliers).map(supplier -> Promise.async(supplier, executorService))
                .toArray(size -> new Promise<?>[size]);
        return all(executorService, promises);
    }

    public static Promise<Object[]> all(Promise<?>... promises) {
        return all(DEFAULT_EXECUTOR_SERVICE, promises);
    }

    public static Promise<Object[]> all(ExecutorService executorService, Promise<?>... promises) {

        Promise<Object[]> resPromise = new Promise<>(executorService);
        resPromise.start(() -> {
            Object[] res = new Object[promises.length];
            for (int i = 0; i < promises.length; i++) {
                res[i] = promises[i].support.get();
            }
            return res;
        });
        return resPromise;
    }

    public static <T> Promise<T> empty() {
        Promise<T> promise = new Promise<>(DEFAULT_EXECUTOR_SERVICE);
        promise.support.fulfill(null);
        return promise;
    }

    private final PromiseSupport<V> support;
    private final ExecutorService executorService;

    Promise(ExecutorService executorService) {
        this.executorService = executorService;
        switch (impl) {
            case FUTURE:
                this.support = new FuturePromiseSupport<>(executorService);
                break;
            case NATIVE_LOCK:
            default:
                this.support = new NativeLockPromiseSupport<>(executorService);
                break;
        }
    }

    private void start(Operator<V> operator) {
        support.fulfill(operator);
    }

    public Promise<Void> then(Consumer<V> then) {
        Promise<Void> dest = new Promise<>(this.executorService);
        dest.start(() -> {
            then.accept(support.get());
            return null;
        });
        return dest;
    }

    public <R> Promise<R> then(Function<V, R> then) {
        Promise<R> dest = new Promise<>(this.executorService);

        dest.start(() -> then.apply(support.get()));
        return dest;
    }

    public Promise<V> error(Consumer<? super Throwable> exceptionHandler) {
        support.exceptionHandler = exceptionHandler;
        if (support.isDone()) {
            support.handleException(support.getException());
        }
        return this;
    }

    public V get() throws InterruptedException, ExecutionException {
        return support.get();
    }

    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return support.get(timeout, unit);
    }
}
