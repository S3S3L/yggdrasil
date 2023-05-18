package io.github.s3s3l.yggdrasil.orm.meta;

import java.lang.reflect.Field;

import io.github.s3s3l.yggdrasil.orm.handler.TypeHandler;
import io.github.s3s3l.yggdrasil.orm.validator.Validator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnMeta {

    /**
     * 数据库中字段名
     */
    private String name;
    /**
     * 别名
     */
    private String alias;
    /**
     * 表别名
     */
    private String tableAlias;
    /**
     * 数据库字段类型
     */
    private DbType dbType;
    /**
     * 对应JAVA对象中的字段名
     */
    private Field field;
    /**
     * 校验器（校验JAVA对象中字段值的有效性）
     */
    private Validator validator;
    private TypeHandler typeHandler;
}
