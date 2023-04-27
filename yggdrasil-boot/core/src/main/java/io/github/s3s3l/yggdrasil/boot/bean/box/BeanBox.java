package io.github.s3s3l.yggdrasil.boot.bean.box;

import io.github.s3s3l.yggdrasil.boot.bean.lifecycle.BeanContext;
import io.github.s3s3l.yggdrasil.boot.bean.meta.BeanMeta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BeanBox {
    private BeanMeta meta;
    private BeanContext context;
    private Object bean;
}
