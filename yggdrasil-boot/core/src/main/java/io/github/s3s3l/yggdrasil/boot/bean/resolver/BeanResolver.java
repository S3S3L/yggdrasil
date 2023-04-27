package io.github.s3s3l.yggdrasil.boot.bean.resolver;

import java.util.List;

import io.github.s3s3l.yggdrasil.boot.bean.def.BeanDefinition;

public interface BeanResolver {
    List<BeanDefinition> resolve(String... packages);
}
