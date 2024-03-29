package io.github.s3s3l.yggdrasil.orm.proxy.meta;

import java.lang.reflect.Method;
import java.util.List;

import io.github.s3s3l.yggdrasil.orm.proxy.config.SqlType;

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
    private List<ParamMeta> params;
    private String sql;
    private SqlType type;
}
