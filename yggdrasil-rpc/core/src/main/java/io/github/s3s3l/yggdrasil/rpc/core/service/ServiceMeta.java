package io.github.s3s3l.yggdrasil.rpc.core.service;

import java.lang.reflect.Method;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceMeta {
    private String serviceName;
    private Object target;
    private Method method;
}
