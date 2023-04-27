package io.github.s3s3l.yggdrasil.boot.bean.def;

import java.lang.reflect.Method;

public interface BeanDefinition {
    Class<?> getType();

    Method getFactoryMethod();
}
