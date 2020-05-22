package org.s3s3l.yggdrasil.orm.bind.condition;

import org.s3s3l.yggdrasil.orm.bind.ColumnStruct;
import org.s3s3l.yggdrasil.orm.bind.DataBindNode;
import org.s3s3l.yggdrasil.orm.bind.sql.DefaultSqlStruct;
import org.s3s3l.yggdrasil.orm.bind.sql.SqlStruct;
import org.s3s3l.yggdrasil.orm.enumerations.ComparePattern;
import org.s3s3l.yggdrasil.orm.meta.ColumnMeta;
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
public class RawConditionNode implements DataBindNode {

    private ComparePattern pattern;

    private ColumnMeta meta;

    public RawConditionNode() {

    }

    public RawConditionNode(ColumnStruct column) {
        this.meta = column.getMeta();
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

        DefaultSqlStruct struct = new DefaultSqlStruct();

        if (StringUtils.isEmpty(this.meta.getName()) || this.meta.getField() == null
                || this.meta.getValidator() == null) {
            return null;
        }

        Object param = bean.getFieldValue(this.meta.getField()
                .getName());

        if (!this.meta.getValidator()
                .isValid(param)) {
            return null;
        }

        StringBuilder sb = new StringBuilder(" AND ");

        if (!StringUtils.isEmpty(this.meta.getTableAlias())) {
            sb.append(this.meta.getTableAlias())
                    .append(".");
        }

        sb.append(this.meta.getName());

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
