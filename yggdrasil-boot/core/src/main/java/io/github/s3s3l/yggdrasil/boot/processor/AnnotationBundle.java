package io.github.s3s3l.yggdrasil.boot.processor;

import java.lang.annotation.Annotation;

import io.github.s3s3l.yggdrasil.bean.Sortable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
class AnnotationBundle<T extends Annotation> implements Sortable {
    private T annotation;
    private AnnotationProcessor<T> processor;

    @Override
    public long getPriority() {
        return processor == null ? 0 : processor.getPriority();
    }
}
