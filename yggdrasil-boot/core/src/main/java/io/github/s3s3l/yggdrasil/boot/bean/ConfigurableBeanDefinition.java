package io.github.s3s3l.yggdrasil.boot.bean;

import java.lang.reflect.Method;

public interface ConfigurableBeanDefinition extends BeanDefinition {
    void setType(Class<?> type);

    void setFactoryMethod(Method factoryMethod);
}
