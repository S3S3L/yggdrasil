package io.github.s3s3l.yggdrasil.utils.promise;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Promise<V> extends PromiseSupport<V> {

    private static final ExecutorService DEFAULT_EXECUTOR_SERVICE = Executors.newCachedThreadPool(r -> {
        Thread t = new Thread(r);
        t.setName("promise-default-thread");
        return t;
    });

    private static Map<Class<?>, Interceptor> interceptors = new ConcurrentHashMap<>();

    private ExecutorService executorService;
    private Runnable action;
    private Consumer<? super Throwable> exceptionHandler;
    private Map<Class<?>, Object> context = new ConcurrentHashMap<>();

    synchronized public static void registerInterceptor(Interceptor interceptor) {
        interceptors.put(interceptor.getType(), interceptor);
    }

    public static <V> Promise<V> async(Supplier<V> supplier) {
        Promise<V> promise = new Promise<>();
        promise.start(supplier);
        return promise;
    }

    public static <V> Promise<V> async(Supplier<V> supplier, ExecutorService executorService) {
        Promise<V> promise = new Promise<>();
        promise.with(executorService).start(supplier);
        return promise;
    }

    public static Promise<Object[]> all(Promise<?>... promises) throws InterruptedException, ExecutionException {
        Object[] res = new Object[promises.length];
        for (int i = 0; i < promises.length; i++) {
            res[i] = promises[i].get();
        }
        Promise<Object[]> resPromise = new Promise<>();
        resPromise.fulfill(res);
        return resPromise;
    }

    public static <T> Promise<T> empty() {
        Promise<T> promise = new Promise<>();
        promise.fulfill(null);
        return promise;
    }

    Promise() {
        executorService = DEFAULT_EXECUTOR_SERVICE;
    }

    private void start(Supplier<V> supplier) {
        interceptors.entrySet().forEach(entry -> {
            context.put(entry.getKey(), entry.getValue().buildContext());
        });
        executorService.submit(() -> {
            try {
                interceptors.entrySet().forEach(entry -> {
                    entry.getValue().beforeAction(context.get(entry.getKey()));
                });
                fulfill(supplier.get());
            } catch (Exception e) {
                fulfillWithException(e);
            }
        });
    }

    @Override
    void fulfill(V value) {
        super.fulfill(value);
        fireAction();
    }

    @Override
    void fulfillWithException(Exception exception) {
        super.fulfillWithException(exception);
        handleException(exception);
    }

    private void fireAction() {
        if (action != null) {
            action.run();
        }
    }

    private void handleException(Exception exception) {
        if (exceptionHandler != null && exception != null) {
            exceptionHandler.accept(exception);
        }
    }

    private Promise<V> with(ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }

    public Promise<Void> then(Consumer<V> then) {
        Promise<Void> dest = new Promise<Void>();
        action = () -> {
            try {
                then.accept(get());
                dest.fulfill(null);
            } catch (Throwable t) {
                dest.fulfillWithException((Exception) t);
            }
        };
        super.fullfill();
        return dest;
    }

    public <R> Promise<R> then(Function<V, R> then) {
        Promise<R> dest = new Promise<R>();
        action = () -> {
            try {
                dest.fulfill(then.apply(get()));
            } catch (Throwable t) {
                dest.fulfillWithException((Exception) t);
            }
        };
        if (this.isDone()) {
            fireAction();
        }
        return dest;
    }

    public Promise<V> error(Consumer<? super Throwable> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        if (this.isDone()) {
            handleException(getException());
        }
        return this;
    }
}
