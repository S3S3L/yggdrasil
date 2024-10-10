package io.github.s3s3l.yggdrasil.promise;

import java.util.concurrent.ExecutionException;

@FunctionalInterface
interface Operator<T> {
    T operate() throws InterruptedException, ExecutionException;
}
