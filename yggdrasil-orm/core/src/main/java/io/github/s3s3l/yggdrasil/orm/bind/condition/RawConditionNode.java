package io.github.s3s3l.yggdrasil.orm.bind.condition;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import io.github.s3s3l.yggdrasil.orm.bind.DataBindNode;
import io.github.s3s3l.yggdrasil.orm.bind.sql.DefaultSqlStruct;
import io.github.s3s3l.yggdrasil.orm.bind.sql.SqlStruct;
import io.github.s3s3l.yggdrasil.orm.exception.DataMapException;
import io.github.s3s3l.yggdrasil.orm.handler.TypeHandler;
import io.github.s3s3l.yggdrasil.orm.meta.ColumnMeta;
import io.github.s3s3l.yggdrasil.orm.meta.ConditionMeta;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;
import io.github.s3s3l.yggdrasil.utils.reflect.ReflectionBean;
import io.github.s3s3l.yggdrasil.utils.verify.Verify;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 * <p>
 * </p>
 * ClassName: ConditionNode <br>
 * date: Sep 20, 2019 11:28:26 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@AllArgsConstructor
public class RawConditionNode implements DataBindNode {

    @Getter
    private ConditionMeta conditionMeta;

    @Override
    public SqlStruct toSqlStruct(ReflectionBean bean) {
        Verify.notNull(bean);

        DefaultSqlStruct struct = new DefaultSqlStruct();
        ColumnMeta columnMeta = conditionMeta.getColumn();

        String columnName = columnMeta.getName();

        String fieldName = this.conditionMeta.getField()
                .getName();
        Class<?> fieldType = this.conditionMeta.getField()
                .getType();
        if (StringUtils.isEmpty(fieldName) || this.conditionMeta.getValidator() == null || !bean.hasField(fieldName)) {
            return null;
        }

        Object param = bean.getFieldValue(fieldName);

        if (!this.conditionMeta.getValidator()
                .isValid(param)) {
            return null;
        }

        TypeHandler typeHandler = columnMeta.getTypeHandler();
        if (typeHandler != null) {
            param = typeHandler.toJDBCType(param);
        }

        StringBuilder sb = new StringBuilder();

        if (!StringUtils.isEmpty(columnMeta.getTableAlias())) {
            sb.append(columnMeta.getTableAlias())
                    .append(".");
        }

        sb.append(columnName);

        switch (conditionMeta.getPattern()) {
            case NON_NULL:
            case NULL:
                sb.append(" ")
                        .append(conditionMeta.getPattern()
                                .operator());
                break;
            case IN:
                sb.append(" ")
                        .append(conditionMeta.getPattern()
                                .operator());
                break;
            default:
                sb.append(" ")
                        .append(conditionMeta.getPattern()
                                .operator())
                        .append(" ?");
                break;
        }

        switch (conditionMeta.getPattern()) {
            case NON_NULL:
            case NULL:
                break;
            case START_WITH:
                struct.addParam(String.join(StringUtils.EMPTY_STRING, param.toString(), "%"));
                break;
            case END_WITH:
                struct.addParam(String.join(StringUtils.EMPTY_STRING, "%", param.toString()));
                break;
            case LIKE:
                struct.addParam(String.join(StringUtils.EMPTY_STRING, "%", param.toString(), "%"));
                break;
            case IN:
                List<String> paramPlaceHolders = new LinkedList<>();
                if (fieldType.isArray()) {
                    Arrays.stream(((Object[]) param))
                            .forEach(obj -> {
                                paramPlaceHolders.add("?");
                                struct.addParam(obj);
                            });
                } else if (Collection.class.isAssignableFrom(fieldType)) {
                    ((Collection<?>) param).forEach(obj -> {
                        paramPlaceHolders.add("?");
                        struct.addParam(obj);
                    });
                } else {
                    throw new DataMapException(String.format("field '%s' of type '%s' can not use 'IN' operation.",
                            fieldName, fieldType.getName()));
                }
                sb.append(" (")
                        .append(String.join(", ", paramPlaceHolders))
                        .append(")");
                break;
            case EQUAL:
            case LAGER:
            case LESS:
            case NOT_LAGER:
            case NOT_LESS:
            case UNEQUAL:
            default:
                struct.addParam(param);
                break;
        }

        struct.setSql(sb.toString());

        return struct;
    }
}
