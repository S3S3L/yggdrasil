package io.github.s3s3l.yggdrasil.orm.meta;

import java.lang.reflect.Field;

import io.github.s3s3l.yggdrasil.orm.enumerations.ComparePattern;
import io.github.s3s3l.yggdrasil.orm.validator.Validator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConditionMeta {

    /**
     * 对应数据库的行
     */
    private ColumnMeta column;
    /**
     * 对应JAVA对象中的字段
     */
    private Field field;
    /**
     * 操作符
     */
    private ComparePattern pattern;
    /**
     * 校验器（校验JAVA对象中字段值的有效性）
     */
    private Validator validator;
}
