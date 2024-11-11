package io.github.s3s3l.yggdrasil.otel.pool;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.context.Context;
import lombok.NonNull;

public class OtelDelagateExecutorService implements ExecutorService {
    private final ExecutorService delegate;
    private final TaskWrapperFactory taskWrapperFactory;

    public OtelDelagateExecutorService(ExecutorService delegate, @NonNull OpenTelemetry sdk) {
        this.delegate = delegate;
        this.taskWrapperFactory = new TaskWrapperFactory(sdk);
    }

    @Override
    public void execute(Runnable command) {
        delegate.execute(taskWrapperFactory.wrap(command, Context.current()));
    }

    @Override
    public void shutdown() {
        delegate.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return delegate.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return delegate.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return delegate.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return delegate.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        Callable<T> wrappedTask = taskWrapperFactory.wrap(task, Context.current());
        return delegate.submit(wrappedTask);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        Runnable wrappedTask = taskWrapperFactory.wrap(task, Context.current());
        return delegate.submit(wrappedTask, result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        Runnable wrappedTask = taskWrapperFactory.wrap(task, Context.current());
        return delegate.submit(wrappedTask);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        Collection<TaskWrapper<T>> wrapperedTasks = taskWrapperFactory.wrapCallables(tasks, Context.current());
        return delegate.invokeAll(wrapperedTasks);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException {
        Collection<TaskWrapper<T>> wrapperedTasks = taskWrapperFactory.wrapCallables(tasks, Context.current());
        return delegate.invokeAll(wrapperedTasks, timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        Collection<TaskWrapper<T>> wrapperedTasks = taskWrapperFactory.wrapCallables(tasks, Context.current());
        return delegate.invokeAny(wrapperedTasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException,
            ExecutionException,
            TimeoutException {
        Collection<TaskWrapper<T>> wrapperedTasks = taskWrapperFactory.wrapCallables(tasks, Context.current());
        return delegate.invokeAny(wrapperedTasks, timeout, unit);
    }
}
