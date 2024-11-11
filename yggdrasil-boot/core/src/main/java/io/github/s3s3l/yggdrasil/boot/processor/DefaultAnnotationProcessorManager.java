package io.github.s3s3l.yggdrasil.boot.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import io.github.s3s3l.yggdrasil.boot.bean.meta.AnnotationMeta;
import io.github.s3s3l.yggdrasil.utils.verify.Verify;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DefaultAnnotationProcessorManager implements AnnotationProcessorManager {
    private static final Map<Class<? extends Annotation>, List<AnnotationProcessor>> processors = new ConcurrentHashMap<>();

    @Override
    public void register(AnnotationProcessor<?> processor) {
        Verify.notNull(processor);

        processors.compute(processor.annotationType(), (type, old) -> {
            List<AnnotationProcessor> list;
            if (old != null) {
                list = old;
            } else {
                list = new LinkedList<>();
            }

            list.add(processor);
            return list;
        });
    }

    @Override
    public AnnotationMeta process(AnnotatedElement element) {
        List<AnnotationBundle> bundles = Arrays.stream(element.getAnnotations())
                .flatMap(annotation -> processors.getOrDefault(annotation.annotationType(), Collections.emptyList())
                        .stream()
                        .map(process -> AnnotationBundle.builder()
                                .annotation(annotation)
                                .processor(process)
                                .build()))
                .sorted(Comparator.comparingLong(AnnotationBundle::getPriority))
                .collect(Collectors.toList());
        AnnotationMeta meta = new AnnotationMeta();

        for (AnnotationBundle bundle : bundles) {
            bundle.getProcessor()
                    .process(meta, bundle.getAnnotation());
        }

        return meta;
    }
}
