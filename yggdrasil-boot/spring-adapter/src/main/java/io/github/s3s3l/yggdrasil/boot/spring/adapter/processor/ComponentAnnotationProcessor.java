package io.github.s3s3l.yggdrasil.boot.spring.adapter.processor;

import org.springframework.stereotype.Component;

import io.github.s3s3l.yggdrasil.boot.annotation.InnerComponent;
import io.github.s3s3l.yggdrasil.boot.bean.meta.AnnotationMeta;

@InnerComponent
public class ComponentAnnotationProcessor extends SpringAnnotationProcessor<Component> {

    @Override
    public AnnotationMeta process(AnnotationMeta pre, Component annotation) {
        pre.setBean(true);
        pre.setName(annotation.value());

        return pre;
    }

    @Override
    public Class<Component> annotationType() {
        return Component.class;
    }
    
}
