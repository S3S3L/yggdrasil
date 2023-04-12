package io.github.s3s3l.yggdrasil.boot.spring.adapter.processor;

import org.springframework.stereotype.Service;

import io.github.s3s3l.yggdrasil.boot.annotation.InnerComponent;
import io.github.s3s3l.yggdrasil.boot.bean.meta.AnnotationMeta;

@InnerComponent
public class ServiceAnnotationProcessor extends SpringAnnotationProcessor<Service> {

    @Override
    public AnnotationMeta process(AnnotationMeta pre, Service annotation) {
        pre.setBean(true);
        pre.setName(annotation.value());

        return pre;
    }

    @Override
    public Class<Service> annotationType() {
        return Service.class;
    }
    
}
