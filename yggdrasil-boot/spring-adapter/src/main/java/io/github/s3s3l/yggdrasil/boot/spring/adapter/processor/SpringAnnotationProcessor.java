package io.github.s3s3l.yggdrasil.boot.spring.adapter.processor;

import java.lang.annotation.Annotation;

import io.github.s3s3l.yggdrasil.boot.processor.AnnotationProcessor;

public abstract class SpringAnnotationProcessor<T extends Annotation> implements AnnotationProcessor<T> {
    public static final long PRIORITY = AnnotationProcessor.DEFAULT_PRIORITY - 1000;

    @Override
    public long getPriority() {
        return PRIORITY;
    }
    
}
