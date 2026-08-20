package io.github.s3s3l.yggdrasil.game.core.runtime;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Schedules non-GL frame work. The default scheduler is deliberately serial.
 */
public interface GameTaskScheduler extends AutoCloseable {

    void execute(List<? extends Runnable> tasks);

    @Override
    default void close() {
    }

    static GameTaskScheduler serial() {
        return tasks -> {
            RuntimeException failure = null;
            for (Runnable task : tasks) {
                try {
                    Objects.requireNonNull(task, "task").run();
                } catch (RuntimeException e) {
                    if (failure == null) {
                        failure = e;
                    } else {
                        failure.addSuppressed(e);
                    }
                }
            }
            if (failure != null) {
                throw failure;
            }
        };
    }

    static GameTaskScheduler parallel(int threadCount) {
        if (threadCount < 1) {
            throw new IllegalArgumentException("threadCount must be positive");
        }
        return new ExecutorGameTaskScheduler(Executors.newFixedThreadPool(threadCount, runnable -> {
            Thread thread = new Thread(runnable, "yggdrasil-game-worker");
            thread.setDaemon(true);
            return thread;
        }));
    }

    final class ExecutorGameTaskScheduler implements GameTaskScheduler {

        private final ExecutorService executor;

        private ExecutorGameTaskScheduler(ExecutorService executor) {
            this.executor = executor;
        }

        @Override
        public void execute(List<? extends Runnable> tasks) {
            try {
                List<Future<Void>> futures = executor.invokeAll(tasks.stream()
                        .map(task -> (java.util.concurrent.Callable<Void>) () -> {
                            Objects.requireNonNull(task, "task").run();
                            return null;
                        })
                        .toList());
                RuntimeException failure = null;
                for (Future<?> future : futures) {
                    try {
                        future.get();
                    } catch (ExecutionException e) {
                        RuntimeException taskFailure = new IllegalStateException("Scheduled task failed", e.getCause());
                        if (failure == null) {
                            failure = taskFailure;
                        } else {
                            failure.addSuppressed(taskFailure);
                        }
                    }
                }
                if (failure != null) {
                    throw failure;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Interrupted while executing scheduled tasks", e);
            }
        }

        @Override
        public void close() {
            executor.shutdownNow();
        }
    }
}
