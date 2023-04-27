package io.github.s3s3l.yggdrasil.boot.bean.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AnnotatableMeta {
    protected AnnotationMeta annotationMeta;
}
