package io.github.s3s3l.yggdrasil.boot.bean.meta;

import java.lang.reflect.Method;

import io.github.s3s3l.yggdrasil.utils.common.StringUtils;
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
public class FactoryMethodMeta extends AnnotatableMeta {
    private Method factoryMethod;

    public String getFactoryBeanName() {
        if (StringUtils.isNotEmpty(annotationMeta.getName())) {
            return annotationMeta.getName();
        }

        return factoryMethod.getDeclaringClass()
                .getSimpleName();
    }
}
