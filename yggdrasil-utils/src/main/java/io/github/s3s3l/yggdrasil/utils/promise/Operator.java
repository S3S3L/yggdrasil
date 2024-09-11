package io.github.s3s3l.yggdrasil.utils.promise;

import java.util.concurrent.ExecutionException;

@FunctionalInterface
interface Operator<T> {
    T operate() throws InterruptedException, ExecutionException;
}
