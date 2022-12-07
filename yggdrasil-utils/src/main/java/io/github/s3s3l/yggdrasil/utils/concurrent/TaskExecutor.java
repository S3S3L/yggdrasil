package io.github.s3s3l.yggdrasil.utils.concurrent;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import io.github.s3s3l.yggdrasil.bean.combine.Combinable;

/**
 * <p>
 * </p>
 * ClassName:TaskExecutor <br>
 * Date: Mar 21, 2017 4:53:11 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface TaskExecutor {

    <T> List<T> execute(List<Callable<Collection<T>>> tasks);

    <T, U> List<T> execute(Function<U, Collection<T>> task, List<U> conditions);

    void execute(Runnable task);

    <T> T execute(List<Callable<T>> tasks, Combinable<T> combine);

    <T, U> T execute(Function<U, T> task, List<U> conditions, Combinable<T> combine);

    void stopAll();

    boolean awaitTermination(long timeout, TimeUnit unit);
}
