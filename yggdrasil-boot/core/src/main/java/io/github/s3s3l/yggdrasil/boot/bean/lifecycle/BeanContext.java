package io.github.s3s3l.yggdrasil.boot.bean.lifecycle;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.function.Supplier;

import io.github.s3s3l.yggdrasil.utils.common.Named;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BeanContext implements Named {
    private String name;
    private Constructor<?> constructor;
    private Method factoryMethod;
    private Supplier<Object> factoryBean;
    private Supplier<Object[]> params;
}
