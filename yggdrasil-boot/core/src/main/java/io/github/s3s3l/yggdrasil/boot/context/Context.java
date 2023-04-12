package io.github.s3s3l.yggdrasil.boot.context;

import java.util.List;
import java.util.Map;

import io.github.s3s3l.yggdrasil.boot.bean.BeanDefinition;

public interface Context {
    void registerBean(BeanDefinition beanDefinition);

    <T> T getBean(Class<T> beanType);

    <T> T getBean(String beanName, Class<T> beanType);

    <T> List<T> getBeans(Class<T> beanType);

    <T> Map<String, T> getBeanMap(Class<T> beanType);
}
