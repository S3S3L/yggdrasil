package org.s3s3l.yggdrasil.orm.bind.select;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.s3s3l.yggdrasil.orm.bind.ColumnStruct;
import org.s3s3l.yggdrasil.orm.bind.DataBindNode;
import org.s3s3l.yggdrasil.orm.bind.sql.DefaultSqlStruct;
import org.s3s3l.yggdrasil.orm.bind.sql.SqlStruct;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.reflect.ReflectionBean;
import org.s3s3l.yggdrasil.utils.verify.Verify;

/**
 * 
 * <p>
 * </p>
 * ClassName: SelectStruct <br>
 * date: Sep 20, 2019 11:29:18 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class SelectStruct implements DataBindNode {

    private List<ColumnStruct> nodes = new ArrayList<>();
    private Map<String, String> map = new HashMap<>();

    public void addNode(ColumnStruct node) {
        map.put(node.getMeta().getName(), node.getMeta().getAlias());
        this.nodes.add(node);
    }

    public String getAlias(String name) {
        if (map.containsKey(name)) {
            return map.get(name);
        }

        return name;
    }

    @Override
    public SqlStruct toSqlStruct(ReflectionBean bean) {
        Verify.notNull(bean);

        DefaultSqlStruct struct = new DefaultSqlStruct();

        for (DataBindNode node : nodes) {
            SqlStruct nodeStruct = node.toSqlStruct(bean);
            struct.appendSql(",")
                    .appendSql(nodeStruct.getSql());
        }

        struct.setSql(struct.getSql()
                .replaceFirst(",", StringUtils.EMPTY_STRING));

        return struct;
    }
}
