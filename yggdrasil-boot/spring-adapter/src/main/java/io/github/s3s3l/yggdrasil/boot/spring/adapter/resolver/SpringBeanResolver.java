package io.github.s3s3l.yggdrasil.boot.spring.adapter.resolver;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.github.s3s3l.yggdrasil.boot.annotation.InnerComponent;
import io.github.s3s3l.yggdrasil.boot.bean.def.BeanDefinition;
import io.github.s3s3l.yggdrasil.boot.bean.def.DefaultBeanDefinition;
import io.github.s3s3l.yggdrasil.boot.bean.resolver.BeanResolver;
import io.github.s3s3l.yggdrasil.utils.reflect.ReflectionUtils;
import io.github.s3s3l.yggdrasil.utils.reflect.scan.ClassScanner;
import io.github.s3s3l.yggdrasil.utils.reflect.scan.Scanner;

@InnerComponent
public class SpringBeanResolver implements BeanResolver {
    private final Scanner scanner = new ClassScanner();

    @Override
    public List<BeanDefinition> resolve(String... packages) {
        Set<Class<?>> classes = scanner.scan(packages);
        return classes.stream()
                .flatMap(clz -> {
                    List<BeanDefinition> beanDefinitions = new LinkedList<>();
                    if (ReflectionUtils.isAnnotationedWiths(clz, Component.class, Service.class, Configuration.class)) {
                        beanDefinitions.add(DefaultBeanDefinition.builder()
                                .type(clz)
                                .build());
                    }

                    for (Method method : clz.getMethods()) {
                        if (ReflectionUtils.isAnnotationedWith(method, Bean.class)) {
                            beanDefinitions.add(DefaultBeanDefinition.builder()
                                    .factoryMethod(method)
                                    .build());
                        }
                    }
                    return beanDefinitions.stream();
                })
                .collect(Collectors.toList());
    }

}
