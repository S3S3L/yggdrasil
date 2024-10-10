package io.github.s3s3l.yggdrasil.promise;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import lombok.AccessLevel;
import lombok.Getter;

abstract class PromiseSupport<T> implements Future<T> {

    protected final ExecutorService executorService;
    
    protected Consumer<? super Throwable> exceptionHandler;
    @Getter(AccessLevel.PACKAGE)
    protected Exception exception;

    PromiseSupport(ExecutorService executorService) {
        this.executorService = executorService;
    }
    
    abstract void fullfill();

    abstract void fulfill(Operator<T> operator);

    abstract void fulfillWithException(Exception exception);

    protected void handleException(Exception exception) {
        if (exceptionHandler != null && exception != null) {
            exceptionHandler.accept(exception);
        }
    }
}
