package io.github.s3s3l.yggdrasil.orm.meta;

import io.github.s3s3l.yggdrasil.bean.verify.Examine;
import io.github.s3s3l.yggdrasil.bean.verify.Expectation;
import io.github.s3s3l.yggdrasil.orm.handler.TypeHandlerManager;
import io.github.s3s3l.yggdrasil.orm.validator.DefaultValidatorFactory;
import io.github.s3s3l.yggdrasil.orm.validator.ValidatorFactory;
import io.github.s3s3l.yggdrasil.utils.reflect.scan.ClassScanner;
import io.github.s3s3l.yggdrasil.utils.reflect.scan.Scanner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetaManagerConfig {
    @Examine(value = Expectation.NOT_NULL)
    private String[] tableDefinePackages;
    @Examine(value = Expectation.NOT_NULL)
    private String[] proxyDefinePackages;
    @Examine(value = Expectation.NOT_NULL)
    private String[] proxyConfigLocations;
    @Examine(value = Expectation.NOT_NULL)
    private Class<? extends ValidatorFactory> validatorFactory;
    @Examine(value = Expectation.NOT_NULL)
    private Class<? extends TypeHandlerManager> typeHandlerManager;
    @Examine(value = Expectation.NOT_NULL)
    private Class<? extends Scanner> scanner;

    public static MetaManagerConfigBuilder defaultBuilder() {
        return MetaManagerConfig.builder()
                .tableDefinePackages(new String[] {})
                .proxyDefinePackages(new String[] {})
                .proxyConfigLocations(new String[] {})
                .validatorFactory(DefaultValidatorFactory.class)
                .typeHandlerManager(TypeHandlerManager.class)
                .scanner(ClassScanner.class);
    }
}
