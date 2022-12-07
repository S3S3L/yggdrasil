package io.github.s3s3l.yggdrasil.orm.proxy.config;

import java.util.List;

import io.github.s3s3l.yggdrasil.bean.verify.Examine;
import io.github.s3s3l.yggdrasil.bean.verify.Expectation;

import lombok.Data;

/**
 * proxy配置类，用于加载配置文件
 */
@Data
public class ProxyConfig {
    @Examine(value = Expectation.NOT_NULL)
    private Class<?> iface;
    @Examine(value = Expectation.NOT_EMPTY)
    @Examine(value = Expectation.EXAMINED, withinTheCollection = true)
    private List<ProxyMethod> methods;
}
