package io.github.s3s3l.yggdrasil.otel.pool;

import java.util.concurrent.Callable;

import io.github.s3s3l.yggdrasil.utils.common.Named;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import lombok.Getter;
import lombok.NonNull;

public class TaskWrapper<T> implements Runnable, Callable<T>, Named {
    private static final String SCOPE_NAME = "TaskWrapper";

    private final String taskName;
    @Getter
    private final TaskType taskType;

    private final Runnable runnable;
    private final Callable<T> callable;

    private final Context context;
    private final OpenTelemetry sdk;

    public TaskWrapper(@NonNull Runnable runnable, @NonNull Context context, @NonNull OpenTelemetry sdk) {
        this.taskName = runnable.getClass()
                .getSimpleName();
        this.taskType = TaskType.RUNABLE;
        this.runnable = runnable;
        this.callable = null;
        this.context = context;
        this.sdk = sdk;
    }

    public TaskWrapper(@NonNull Callable<T> callable, @NonNull Context context, @NonNull OpenTelemetry sdk) {
        this.taskName = callable.getClass()
                .getSimpleName();
        this.taskType = TaskType.CALLABLE;
        this.runnable = null;
        this.callable = callable;
        this.context = context;
        this.sdk = sdk;
    }

    @Override
    public void run() {
        Span span = sdk.tracerBuilder(SCOPE_NAME)
                .build()
                .spanBuilder(taskName)
                .setParent(context)
                .startSpan();
        try (Scope scope = span.makeCurrent()) {
            runnable.run();
        } finally {
            span.end();
        }
    }

    public T call() throws Exception {
        Span span = sdk.tracerBuilder(SCOPE_NAME)
                .build()
                .spanBuilder(taskName)
                .setParent(context)
                .startSpan();
        try (Scope scope = span.makeCurrent()) {
            return callable.call();
        } finally {
            span.end();
        }
    }

    @Override
    public String getName() {
        return taskName;
    }

}
