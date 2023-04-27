package io.github.s3s3l.yggdrasil.boot.bean.meta;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BeanMeta extends AnnotatableMeta {
    private String name;
    private Class<?> type;
    private ConstructorMeta constructorMeta;
    private FactoryBeanMeta factoryBeanMeta;
    private FactoryMethodMeta factoryMethodMeta;
    private List<ParamMeta> params;
    private List<FieldMeta> fields;
}
