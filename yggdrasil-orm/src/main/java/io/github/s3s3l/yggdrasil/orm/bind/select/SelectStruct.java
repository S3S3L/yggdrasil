package io.github.s3s3l.yggdrasil.orm.bind.select;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.s3s3l.yggdrasil.orm.bind.ColumnStruct;
import io.github.s3s3l.yggdrasil.orm.bind.DataBindNode;
import io.github.s3s3l.yggdrasil.orm.bind.sql.DefaultSqlStruct;
import io.github.s3s3l.yggdrasil.orm.bind.sql.SqlStruct;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;
import io.github.s3s3l.yggdrasil.utils.reflect.ReflectionBean;

import lombok.NoArgsConstructor;

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
@NoArgsConstructor
public class SelectStruct implements DataBindNode {

    private List<ColumnStruct> nodes = new ArrayList<>();
    private Map<String, String> map = new HashMap<>();

    public SelectStruct(List<ColumnStruct> nodes) {
        this.nodes = nodes;
        nodes.forEach(node -> map.put(node.getMeta().getName(), node.getMeta().getAlias()));
    }

    public void addNode(ColumnStruct node) {
        map.put(node.getMeta().getName(), node.getMeta().getAlias());
        this.nodes.add(node);
    }

    @Override
    public SqlStruct toSqlStruct(ReflectionBean bean) {

        DefaultSqlStruct struct = new DefaultSqlStruct();

        for (DataBindNode node : nodes) {
            SqlStruct nodeStruct = node.toSqlStruct(bean);
            struct.appendSql(", ")
                    .appendSql(nodeStruct.getSql());
        }

        struct.setSql(struct.getSql()
                .replaceFirst(", ", StringUtils.EMPTY_STRING));

        return struct;
    }
}
