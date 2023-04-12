package io.github.s3s3l.yggdrasil.boot.bean.creation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import io.github.s3s3l.yggdrasil.utils.promise.Promise;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CreationContext<T> {
    private Constructor<T> constructor;
    private Method factoryMethod;
    private Promise<Object> factoryBean;
    private Promise<Object[]> params;
}
