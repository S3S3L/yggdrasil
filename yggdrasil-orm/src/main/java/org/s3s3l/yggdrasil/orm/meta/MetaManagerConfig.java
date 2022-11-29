package org.s3s3l.yggdrasil.orm.meta;

import org.s3s3l.yggdrasil.bean.verify.Examine;
import org.s3s3l.yggdrasil.bean.verify.Expectation;
import org.s3s3l.yggdrasil.orm.handler.TypeHandlerManager;
import org.s3s3l.yggdrasil.orm.validator.DefaultValidatorFactory;
import org.s3s3l.yggdrasil.orm.validator.ValidatorFactory;
import org.s3s3l.yggdrasil.utils.reflect.scan.ClassScanner;
import org.s3s3l.yggdrasil.utils.reflect.scan.Scanner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MetaManagerConfig {
    @Builder.Default
    @Examine(value = Expectation.NOT_NULL)
    private String[] tableDefinePackages = new String[] {};
    @Builder.Default
    @Examine(value = Expectation.NOT_NULL)
    private String[] proxyDefinePackages = new String[] {};
    @Builder.Default
    @Examine(value = Expectation.NOT_NULL)
    private String[] proxyConfigLocations = new String[] {};
    @Builder.Default
    @Examine(value = Expectation.NOT_NULL)
    private Class<? extends ValidatorFactory> validatorFactory = DefaultValidatorFactory.class;
    @Builder.Default
    @Examine(value = Expectation.NOT_NULL)
    private Class<? extends TypeHandlerManager> typeHandlerManager = TypeHandlerManager.class;
    @Builder.Default
    @Examine(value = Expectation.NOT_NULL)
    private Class<? extends Scanner> scanner = ClassScanner.class;
}
