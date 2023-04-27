package io.github.s3s3l.yggdrasil.boot.bean.def;

import java.lang.reflect.Method;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultBeanDefinition implements ConfigurableBeanDefinition {
    private Class<?> type;
    private Method factoryMethod;
}
