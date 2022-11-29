package org.s3s3l.yggdrasil.orm.proxy.meta;

import java.lang.reflect.Method;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProxyMethodMeta {
    private Method method;
    private String sql;
}
