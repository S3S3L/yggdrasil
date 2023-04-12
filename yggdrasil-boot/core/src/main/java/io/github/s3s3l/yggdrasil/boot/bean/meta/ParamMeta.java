package io.github.s3s3l.yggdrasil.boot.bean.meta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ParamMeta {
    private Class<?> type;
    private String name;
    @Builder.Default
    private boolean required = true;
    private AnnotationMeta annotationMeta;
}
