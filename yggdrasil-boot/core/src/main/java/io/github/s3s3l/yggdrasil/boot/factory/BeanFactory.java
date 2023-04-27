package io.github.s3s3l.yggdrasil.boot.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import io.github.s3s3l.yggdrasil.boot.bean.BeanLifecycle;
import io.github.s3s3l.yggdrasil.boot.bean.box.BeanBox;
import io.github.s3s3l.yggdrasil.boot.bean.def.BeanDefinition;
import io.github.s3s3l.yggdrasil.boot.bean.lifecycle.BeanContext;
import io.github.s3s3l.yggdrasil.boot.bean.lifecycle.BeanContext.BeanContextBuilder;
import io.github.s3s3l.yggdrasil.boot.bean.lifecycle.BeanPipline;
import io.github.s3s3l.yggdrasil.boot.bean.meta.AnnotationMeta;
import io.github.s3s3l.yggdrasil.boot.bean.meta.BeanMeta;
import io.github.s3s3l.yggdrasil.boot.bean.meta.ConstructorMeta;
import io.github.s3s3l.yggdrasil.boot.bean.meta.FactoryBeanMeta;
import io.github.s3s3l.yggdrasil.boot.bean.meta.FactoryMethodMeta;
import io.github.s3s3l.yggdrasil.boot.bean.meta.FieldMeta;
import io.github.s3s3l.yggdrasil.boot.bean.meta.ParamMeta;
import io.github.s3s3l.yggdrasil.boot.exception.BeanCreationException;
import io.github.s3s3l.yggdrasil.boot.exception.BeanDefinitionException;
import io.github.s3s3l.yggdrasil.boot.processor.AnnotationProcessorManager;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;
import io.github.s3s3l.yggdrasil.utils.reflect.ReflectionUtils;
import io.github.s3s3l.yggdrasil.utils.verify.Verify;

public class BeanFactory {

    private final Map<String, BeanBox> beanCache = new ConcurrentHashMap<>();
    private final Map<Class<?>, Set<String>> beanNames = new ConcurrentHashMap<>();

    private BeanPipline creationPipline;
    private AnnotationProcessorManager annotationProcessorManager;

    public void registerBean(BeanDefinition beanDefinition) {
        Verify.notNull(beanDefinition);

        BeanMeta beanMeta = toBeanMeta(beanDefinition);

        // 将BeanMeta按beanName记录到map中，确保beanName全局唯一
        beanCache.compute(beanMeta.getName(), (k, v) -> {
            if (v != null) {
                throw new BeanDefinitionException("Duplicate bean name: " + k);
            }

            // 将beanName记录到当前类型以及所有父类上
            for (Class<?> type : ReflectionUtils.getAllParentTypes(beanMeta.getType())) {
                beanNames.compute(type, (t, names) -> {
                    if (names == null) {
                        names = new HashSet<>();
                    }

                    if (names.contains(k)) {
                        throw new BeanDefinitionException(
                                String.format("Duplicate bean name [%s] for type [%s]", k, t.getName()));
                    }

                    names.add(k);

                    return names;
                });
            }

            return BeanBox.builder()
                    .meta(beanMeta)
                    .build();
        });
    }

    private BeanMeta toBeanMeta(BeanDefinition beanDefinition) {
        Class<?> type = beanDefinition.getType();
        Method factoryMethod = beanDefinition.getFactoryMethod();
        BeanMeta beanMeta;
        if (factoryMethod != null) {
            beanMeta = toBeanMeta(factoryMethod);
        } else if (type != null) {
            beanMeta = toBeanMeta(type);
        } else {
            throw new BeanDefinitionException("BeanDefinition is not valid: " + beanDefinition);
        }

        return beanMeta;
    }

    private BeanMeta toBeanMeta(Method factoryMethod) {
        Class<?> beanType = factoryMethod.getReturnType();
        Class<?> factoryBeanType = factoryMethod.getDeclaringClass();
        AnnotationMeta methodAnnotationMeta = annotationProcessorManager.process(factoryMethod);
        String beanName = methodAnnotationMeta.getName();
        if (StringUtils.isEmpty(beanName)) {
            beanName = factoryMethod.getName();
        }
        return BeanMeta.builder()
                .name(beanName)
                .type(beanType)
                .annotationMeta(annotationProcessorManager.process(beanType))
                .factoryBeanMeta(FactoryBeanMeta.builder()
                        .type(factoryBeanType)
                        .annotationMeta(annotationProcessorManager.process(factoryBeanType))
                        .build())
                .factoryMethodMeta(FactoryMethodMeta.builder()
                        .factoryMethod(factoryMethod)
                        .annotationMeta(methodAnnotationMeta)
                        .build())
                .params(Arrays.stream(factoryMethod.getParameters())
                        .map(param -> ParamMeta.builder()
                                .param(param)
                                .annotationMeta(annotationProcessorManager.process(param))
                                .build())
                        .collect(Collectors.toList()))
                .fields(ReflectionUtils.getFields(beanType)
                        .stream()
                        .map(field -> FieldMeta.builder()
                                .field(field)
                                .annotationMeta(annotationProcessorManager.process(field))
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private BeanMeta toBeanMeta(Class<?> type) {
        Constructor<?> constructor = null;
        AnnotationMeta annotationMeta = null;
        for (Constructor<?> cons : type.getConstructors()) {
            annotationMeta = annotationProcessorManager.process(cons);
            if (annotationMeta.isBean()) {
                constructor = cons;
                break;
            }
        }

        if (constructor == null) {
            try {
                constructor = type.getConstructor();
                annotationMeta = annotationProcessorManager.process(constructor);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new BeanDefinitionException("Constructor annotation as bean or default constructor not found.",
                        e);
            }
        }

        AnnotationMeta typeAnnotationMeta = annotationProcessorManager.process(type);
        String beanName = typeAnnotationMeta.getName();
        if (StringUtils.isEmpty(beanName)) {
            beanName = type.getSimpleName();
        }

        return BeanMeta.builder()
                .name(beanName)
                .type(type)
                .annotationMeta(typeAnnotationMeta)
                .constructorMeta(ConstructorMeta.builder()
                        .constructor(constructor)
                        .annotationMeta(annotationMeta)
                        .build())
                .params(Arrays.stream(constructor.getParameters())
                        .map(param -> ParamMeta.builder()
                                .param(param)
                                .annotationMeta(annotationProcessorManager.process(param))
                                .build())
                        .collect(Collectors.toList()))
                .fields(ReflectionUtils.getFields(type)
                        .stream()
                        .map(field -> FieldMeta.builder()
                                .field(field)
                                .annotationMeta(annotationProcessorManager.process(field))
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private BeanContext toContext(String beanName, DependencyChain<String> dependencyChain) {
        BeanBox beanBox = beanCache.compute(beanName, (name, box) -> {
            if (box == null) {
                throw new BeanCreationException("Bean not found. " + name);
            }
            return box;
        });

        if (beanBox.getContext() != null) {
            return beanBox.getContext();
        }
        return toContext(beanBox.getMeta(), dependencyChain);
    }

    private BeanContext toContext(BeanMeta beanMeta, DependencyChain<String> dependencyChain) {
        dependencyChain.append(beanMeta.getName());

        BeanContextBuilder<?, ?> builder = BeanContext.builder()
                .name(beanMeta.getName());

        FactoryMethodMeta factoryMethodMeta = beanMeta.getFactoryMethodMeta();
        FactoryBeanMeta factoryBeanMeta = beanMeta.getFactoryBeanMeta();

        if (beanMeta.getConstructorMeta() != null) {
            builder.constructor(beanMeta.getConstructorMeta()
                    .getConstructor());
        } else if (factoryMethodMeta != null && factoryBeanMeta != null) {
            builder.factoryMethod(factoryMethodMeta.getFactoryMethod());
            String factoryBeanName = factoryBeanMeta.getName();

            BeanContext context = toContext(factoryBeanName, dependencyChain);
            builder.factoryBean(() -> createBeanInstance(factoryBeanName, context));
        }

        List<BeanContext> paramsCtx = beanMeta.getParams()
                .stream()
                .map(paramMeta -> toContext(paramMeta.getName(), dependencyChain))
                .collect(Collectors.toList());
        builder.params(() -> paramsCtx.stream()
                .map(ctx -> createBeanInstance(ctx.getName(), ctx))
                .toArray(Object[]::new));

        dependencyChain.pop();
        return builder.build();
    }

    @SuppressWarnings({ "unchecked" })
    public <T> T getBean(String beanName) {
        // TODO: 构建完整的bean
        return (T) createBeanInstance(beanName, new DependencyChain<>());
    }

    private Object createBeanInstance(String beanName, DependencyChain<String> dependencyChain) {
        BeanContext context = toContext(beanName, dependencyChain);
        return createBeanInstance(beanName, context);
    }

    private Object createBeanInstance(String beanName, BeanContext context) {
        return beanCache.compute(beanName, (name, box) -> {
            if (box == null || box.getMeta() == null) {
                throw new BeanCreationException("Bean not found. " + name);
            }

            if (box.getContext() == null) {
                box.setContext(context);
            }

            if (box.getBean() == null) {
                box.setBean(creationPipline.process(box, BeanLifecycle.CREATING));
            }

            return box;
        })
                .getBean();
    }
}
