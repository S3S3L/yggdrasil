package io.github.s3s3l.yggdrasil.boot.bean.meta;

import java.lang.reflect.Parameter;

import io.github.s3s3l.yggdrasil.utils.common.Named;
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
public class ParamMeta extends AnnotatableMeta implements Named {
    private Parameter param;

    @Override
    public String getName() {
        if (annotationMeta != null && StringUtils.isNotEmpty(annotationMeta.getName())) {
            return annotationMeta.getName();
        }

        return param.getType()
                .getSimpleName();
    }
}
