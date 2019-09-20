package org.s3s3l.yggdrasil.orm.bind;

import org.s3s3l.yggdrasil.orm.enumerations.ComparePattern;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.reflect.ReflectionBean;
import org.s3s3l.yggdrasil.utils.verify.Verify;

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
public class ConditionNode extends ColumnStruct {

    private ComparePattern pattern;

    public ConditionNode() {

    }

    public ConditionNode(ColumnStruct column) {
        this.setName(column.getName());
        this.setAlias(column.getAlias());
        this.setField(column.getField());
        this.setTableAlias(column.getTableAlias());
        this.setValidator(column.getValidator());
    }

    public ComparePattern getPattern() {
        return pattern;
    }

    public void setPattern(ComparePattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public SqlStruct toSqlStruct(ReflectionBean bean) {
        Verify.notNull(bean);

        SqlStruct struct = new SqlStruct();

        if (StringUtils.isEmpty(this.getName()) || this.getField() == null || this.getValidator() == null) {
            return null;
        }

        Object param = bean.getFieldValue(this.getField()
                .getName());

        if (!this.getValidator()
                .isValid(param)) {
            return null;
        }

        StringBuilder sb = new StringBuilder(" AND ");

        if (!StringUtils.isEmpty(this.getTableAlias())) {
            sb.append(this.getTableAlias())
                    .append(".");
        }

        sb.append(this.getName());

        switch (this.pattern) {
            case NON_NULL:
            case NULL:
                sb.append(" ")
                        .append(this.pattern.operator());
                break;
            default:
                sb.append(" ")
                        .append(this.pattern.operator())
                        .append(" ?");
                break;
        }

        switch (this.pattern) {
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
            default:
                struct.addParam(param);
                break;
        }

        struct.setSql(sb.toString());

        return struct;
    }
}
