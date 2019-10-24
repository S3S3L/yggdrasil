package org.s3s3l.yggdrasil.orm.bind;

import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.reflect.ReflectionBean;
import org.s3s3l.yggdrasil.utils.verify.Verify;

/**
 * 
 * <p>
 * </p>
 * ClassName: SetNode <br>
 * date: Sep 20, 2019 11:29:27 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class SetNode extends ColumnStruct {

    public SetNode() {

    }

    public SetNode(ColumnStruct column) {
        this.setName(column.getName());
        this.setAlias(column.getAlias());
        this.setField(column.getField());
        this.setTableAlias(column.getTableAlias());
        this.setValidator(column.getValidator());
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

        StringBuilder sb = new StringBuilder(" ");

        sb.append(this.getName())
                .append(" = ?");

        struct.setSql(sb.toString());
        struct.addParam(param);

        return struct;
    }

}
