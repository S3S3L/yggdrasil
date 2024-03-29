package io.github.s3s3l.yggdrasil.orm.meta;

import java.lang.reflect.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OffsetMeta {

    private Field field;
}
