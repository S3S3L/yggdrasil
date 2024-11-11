package io.github.s3s3l.yggdrasil.boot.processor;

import java.lang.annotation.Annotation;

import io.github.s3s3l.yggdrasil.bean.Sortable;
import io.github.s3s3l.yggdrasil.boot.bean.meta.AnnotationMeta;

public interface AnnotationProcessor<T extends Annotation> extends Sortable {

    public static final long DEFAULT_PRIORITY = 0;
    
    AnnotationMeta process(AnnotationMeta pre, T annotation);

    @Override
    default long getPriority() {
        return DEFAULT_PRIORITY;
    }

    Class<T> annotationType();
}
