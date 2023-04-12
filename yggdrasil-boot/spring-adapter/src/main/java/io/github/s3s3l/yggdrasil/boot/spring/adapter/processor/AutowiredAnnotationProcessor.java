package io.github.s3s3l.yggdrasil.boot.spring.adapter.processor;

import org.springframework.beans.factory.annotation.Autowired;

import io.github.s3s3l.yggdrasil.boot.annotation.InnerComponent;
import io.github.s3s3l.yggdrasil.boot.bean.meta.AnnotationMeta;

@InnerComponent
public class AutowiredAnnotationProcessor extends SpringAnnotationProcessor<Autowired> {

    @Override
    public AnnotationMeta process(AnnotationMeta pre, Autowired annotation) {
        pre.setInject(true);
        pre.setRequired(annotation.required());

        return pre;
    }

    @Override
    public Class<Autowired> annotationType() {
        return Autowired.class;
    }
}
