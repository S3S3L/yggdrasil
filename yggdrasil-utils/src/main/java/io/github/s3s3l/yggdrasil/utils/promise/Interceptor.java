package io.github.s3s3l.yggdrasil.utils.promise;

interface Interceptor {
    Class<?> getType();

    Object buildContext();

    void beforeAction(Object context);
}
