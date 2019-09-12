package org.s3s3l.yggdrasil.utils.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.s3s3l.yggdrasil.bean.combine.Combinable;
import org.s3s3l.yggdrasil.utils.json.IJacksonHelper;
import org.s3s3l.yggdrasil.utils.json.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;

/**
 * <p>
 * </p>
 * ClassName:AsyncTaskExecutor <br>
 * Date: Mar 21, 2017 4:59:32 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class CommonTaskExecutor implements TaskExecutor {
    private static final IJacksonHelper json = JacksonUtils.create()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private ExecutorService executor;
    private ExecutorBuilder builder;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private CommonTaskExecutor(ExecutorBuilder builder) {
        this.builder = builder;
        this.executor = builder.buid();
    }

    public static TaskExecutor create(ExecutorBuilder builder) {
        return new CommonTaskExecutor(builder);
    }

    private ExecutorService getExecutor() {
        if (executor == null || executor.isShutdown()) {
            executor = builder.buid();
        }
        return executor;
    }

    @Override
    public <T> List<T> execute(List<Callable<Collection<T>>> tasks) {
        List<T> result = new ArrayList<>();
        try {
            getExecutor().invokeAll(tasks)
                    .stream()
                    .forEach(future -> {
                        try {
                            result.addAll(future.get());
                        } catch (InterruptedException | ExecutionException e) {
                            logger.error(e.getMessage(), e);
                            Thread.currentThread()
                                    .interrupt();
                        }
                    });
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
            Thread.currentThread()
                    .interrupt();
        }
        return result;
    }

    @Override
    public <T, U> List<T> execute(Function<U, Collection<T>> task, List<U> conditions) {
        List<Callable<Collection<T>>> tasks = new ArrayList<>();
        for (final U condition : conditions) {
            tasks.add(() -> task.apply(condition));
        }
        return execute(tasks);
    }

    @Override
    public void execute(Runnable task) {
        getExecutor().submit(task);
    }

    @Override
    public <T> T execute(List<Callable<T>> tasks, Combinable<T> combine) {
        try {
            getExecutor().invokeAll(tasks)
                    .stream()
                    .forEach(future -> {
                        try {
                            combine.combine(future.get());
                        } catch (InterruptedException | ExecutionException e) {
                            logger.error(e.getMessage(), e);
                            Thread.currentThread()
                                    .interrupt();
                        }
                    });
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
            Thread.currentThread()
                    .interrupt();
        }
        return combine.get();
    }

    @Override
    public <T, U> T execute(Function<U, T> task, List<U> conditions, Combinable<T> combine) {
        List<Callable<T>> tasks = new ArrayList<>();
        for (final U condition : conditions) {
            tasks.add(() -> task.apply(condition));
        }
        return execute(tasks, combine);
    }

    @Override
    public void stopAll() {
        getExecutor().shutdownNow();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) {
        try {
            return getExecutor().awaitTermination(timeout, unit);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
            Thread.currentThread()
                    .interrupt();
        }
        return false;
    }

}
