package io.github.s3s3l.yggdrasil.boot.bean;

import java.lang.reflect.Method;

public interface BeanDefinition {
    Class<?> getType();

    Method getFactoryMethod();
}
