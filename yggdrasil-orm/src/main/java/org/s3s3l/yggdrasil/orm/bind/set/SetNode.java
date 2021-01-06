package org.s3s3l.yggdrasil.orm.bind.set;

import org.s3s3l.yggdrasil.orm.bind.ColumnStruct;
import org.s3s3l.yggdrasil.orm.bind.DataBindNode;
import org.s3s3l.yggdrasil.orm.bind.sql.DefaultSqlStruct;
import org.s3s3l.yggdrasil.orm.bind.sql.SqlStruct;
import org.s3s3l.yggdrasil.orm.meta.ColumnMeta;
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
public class SetNode implements DataBindNode {

    private ColumnMeta meta;

    public SetNode() {

    }

    public SetNode(ColumnStruct column) {
        this.meta = column.getMeta();
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

        StringBuilder sb = new StringBuilder(" ");

        sb.append(this.meta.getName())
                .append(" = ?");

        struct.setSql(sb.toString());
        struct.addParam(param);

        return struct;
    }

}
