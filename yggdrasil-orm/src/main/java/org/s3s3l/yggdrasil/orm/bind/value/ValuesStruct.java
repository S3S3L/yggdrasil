package org.s3s3l.yggdrasil.orm.bind.value;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.s3s3l.yggdrasil.orm.bind.DataBindNode;
import org.s3s3l.yggdrasil.orm.bind.sql.DefaultSqlStruct;
import org.s3s3l.yggdrasil.orm.bind.sql.SqlStruct;
import org.s3s3l.yggdrasil.orm.meta.ColumnMeta;
import org.s3s3l.yggdrasil.utils.reflect.ReflectionBean;
import org.s3s3l.yggdrasil.utils.verify.Verify;

/**
 * 
 * <p>
 * </p>
 * ClassName: ValuesStruct <br>
 * date: Sep 20, 2019 11:29:55 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class ValuesStruct implements DataBindNode {

    private List<ColumnMeta> nodes = new ArrayList<>();

    public void addNode(ColumnMeta node) {
        this.nodes.add(node);
    }

    @Override
    public SqlStruct toSqlStruct(ReflectionBean bean) {
        return toSqlStruct(bean, false);
    }

    public DefaultSqlStruct toSqlStruct(ReflectionBean bean, boolean first) {
        Verify.notNull(bean);

        // TODO 是否要过滤有效字段
        // List<ColumnStruct> validatedNodes = this.nodes.stream()
        // .filter(r -> r.getValidator()
        // .isValid(bean.getFieldValue(r.getField()
        // .getName())))
        // .collect(Collectors.toList());
        List<ColumnMeta> validatedNodes = this.nodes;

        if (validatedNodes.isEmpty()) {
            return null;
        }

        DefaultSqlStruct struct = new DefaultSqlStruct();

        String values = String.join(",", validatedNodes.stream()
                .map(r -> " ?")
                .collect(Collectors.toList()));

        if (first) {
            String fields = String.join(",", validatedNodes.stream()
                    .map(ColumnMeta::getName)
                    .collect(Collectors.toList()));
            struct.setSql(String.format("(%s) VALUES (%s)", fields, values));
        } else {
            struct.setSql(String.format(",(%s)", values));
        }

        struct.addParams(validatedNodes.stream()
                .map(r -> r.getTypeHandler()
                        .toJDBCType(bean.getFieldValue(r.getField()
                                .getName())))
                .collect(Collectors.toList()));

        return struct;
    }
}
