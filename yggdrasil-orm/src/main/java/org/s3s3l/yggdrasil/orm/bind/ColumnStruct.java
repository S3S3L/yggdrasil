package org.s3s3l.yggdrasil.orm.bind;

import org.s3s3l.yggdrasil.orm.bind.sql.DefaultSqlStruct;
import org.s3s3l.yggdrasil.orm.bind.sql.SqlStruct;
import org.s3s3l.yggdrasil.orm.meta.ColumnMeta;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.reflect.ReflectionBean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * <p>
 * </p>
 * ClassName: ColumnStruct <br>
 * date: Sep 20, 2019 11:28:19 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnStruct implements DataBindNode {

    private ColumnMeta meta;

    @Override
    public SqlStruct toSqlStruct(ReflectionBean bean) {

        DefaultSqlStruct struct = new DefaultSqlStruct();

        if (StringUtils.isEmpty(this.meta.getName()) || this.meta.getField() == null
                || this.meta.getValidator() == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder("");

        if (!StringUtils.isEmpty(this.meta.getTableAlias())) {
            sb.append(this.meta.getTableAlias())
                    .append(".");
        }

        sb.append(this.meta.getName());

        sb.append(String.format(" AS %s",
                StringUtils.isEmpty(this.meta.getAlias()) ? this.meta.getName() : this.meta.getAlias()));

        struct.setSql(sb.toString());

        return struct;
    }

}
