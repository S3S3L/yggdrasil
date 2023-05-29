package io.github.s3s3l.yggdrasil.orm.proxy.meta;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProxyMeta {
    private Class<?> iface;
    /**
     * key: methodName
     */
    @Builder.Default
    private Map<String, ProxyMethodMeta> methods = new HashMap<>();

    public ProxyMethodMeta getMethod(Method method) {
        if (method == null) {
            return null;
        }
        return getMethod(method.getName());
    }

    public ProxyMethodMeta getMethod(String methodName) {
        return methods.get(methodName);
    }

}
