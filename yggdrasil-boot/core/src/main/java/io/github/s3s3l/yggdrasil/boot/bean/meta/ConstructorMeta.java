package io.github.s3s3l.yggdrasil.boot.bean.meta;

import java.lang.reflect.Constructor;

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
public class ConstructorMeta extends AnnotatableMeta {
    private Constructor<?> constructor;
}
