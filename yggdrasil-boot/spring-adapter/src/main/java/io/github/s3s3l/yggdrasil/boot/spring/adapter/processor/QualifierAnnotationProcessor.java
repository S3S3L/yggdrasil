package io.github.s3s3l.yggdrasil.boot.spring.adapter.processor;

import org.springframework.beans.factory.annotation.Qualifier;

import io.github.s3s3l.yggdrasil.boot.annotation.InnerComponent;
import io.github.s3s3l.yggdrasil.boot.bean.meta.AnnotationMeta;

@InnerComponent
public class QualifierAnnotationProcessor extends SpringAnnotationProcessor<Qualifier> {

    @Override
    public AnnotationMeta process(AnnotationMeta pre, Qualifier annotation) {
        pre.setName(annotation.value());

        return pre;
    }

    @Override
    public int getPriority() {
        return SpringAnnotationProcessor.PRIORITY + 1;
    }

    @Override
    public Class<Qualifier> annotationType() {
        return Qualifier.class;
    }
    
}
