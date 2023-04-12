package io.github.s3s3l.yggdrasil.boot.spring.adapter.processor;

import org.springframework.context.annotation.Configuration;

import io.github.s3s3l.yggdrasil.boot.annotation.InnerComponent;
import io.github.s3s3l.yggdrasil.boot.bean.meta.AnnotationMeta;

@InnerComponent
public class ConfigurationAnnotationProcessor extends SpringAnnotationProcessor<Configuration> {

    @Override
    public AnnotationMeta process(AnnotationMeta pre, Configuration annotation) {
        pre.setBean(true);
        pre.setName(annotation.value());
        
        return pre;
    }

    @Override
    public Class<Configuration> annotationType() {
        return Configuration.class;
    }
    
}
