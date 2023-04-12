package io.github.s3s3l.yggdrasil.boot.bean.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AnnotationMeta {
    /**
     * 名称
     */
    private String name;

    /**
     * 是否为bean
     */
    private boolean bean;

    /**
     * 是否需要自动注入
     */
    private boolean inject;
    /**
     * 是否为强依赖
     */
    private boolean required;
}
