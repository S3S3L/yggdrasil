package io.github.s3s3l.yggdrasil.boot.processor;

import java.lang.reflect.AnnotatedElement;

import io.github.s3s3l.yggdrasil.boot.bean.meta.AnnotationMeta;

public interface AnnotationProcessorManager {

    void register(AnnotationProcessor<?> processor);

    AnnotationMeta process(AnnotatedElement element);

}
