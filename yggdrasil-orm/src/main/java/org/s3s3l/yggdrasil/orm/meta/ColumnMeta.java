package org.s3s3l.yggdrasil.orm.meta;

import java.lang.reflect.Field;

import org.s3s3l.yggdrasil.orm.handler.TypeHandler;
import org.s3s3l.yggdrasil.orm.validator.Validator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnMeta {

    private String name;
    private String alias;
    private String tableAlias;
    private Field field;
    private Validator validator;
    private TypeHandler typeHandler;
}
