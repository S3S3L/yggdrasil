package io.github.s3s3l.yggdrasil.otel.pool;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.context.Context;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class TaskWrapperFactory {
    private final OpenTelemetry sdk;

    public <T> TaskWrapper<T> wrap(@NonNull Runnable runnable, @NonNull Context context) {
        return new TaskWrapper<>(runnable, context, sdk);
    }

    public <T> TaskWrapper<T> wrap(@NonNull Callable<T> callable, @NonNull Context context) {
        return new TaskWrapper<>(callable, context, sdk);
    }

    public <T> Collection<TaskWrapper<Object>> wrapRunables(@NonNull Collection<Runnable> runnables,
            @NonNull Context context) {
        return runnables.stream()
                .map(runnable -> wrap(runnable, context))
                .collect(Collectors.toList());
    }

    public <T> Collection<TaskWrapper<T>> wrapCallables(@NonNull Collection<? extends Callable<T>> callables,
            @NonNull Context context) {
        return callables.stream()
                .map(callable -> wrap(callable, context))
                .collect(Collectors.toList());
    }
}
