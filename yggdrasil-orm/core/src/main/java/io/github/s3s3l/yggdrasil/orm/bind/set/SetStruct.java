package io.github.s3s3l.yggdrasil.orm.bind.set;

import java.util.ArrayList;
import java.util.List;

import io.github.s3s3l.yggdrasil.orm.bind.DataBindNode;
import io.github.s3s3l.yggdrasil.orm.bind.sql.DefaultSqlStruct;
import io.github.s3s3l.yggdrasil.orm.bind.sql.SqlStruct;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;
import io.github.s3s3l.yggdrasil.utils.reflect.ReflectionBean;
import io.github.s3s3l.yggdrasil.utils.verify.Verify;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 
 * <p>
 * </p>
 * ClassName: SetStruct <br>
 * date: Sep 20, 2019 11:29:34 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@AllArgsConstructor
@NoArgsConstructor
public class SetStruct implements DataBindNode {

    private List<SetNode> nodes = new ArrayList<>();

    public void addNode(SetNode node) {
        this.nodes.add(node);
    }

    @Override
    public SqlStruct toSqlStruct(ReflectionBean bean) {
        Verify.notNull(bean);

        if (nodes.isEmpty()) {
            return null;
        }

        DefaultSqlStruct struct = new DefaultSqlStruct();
        struct.setSql(" SET");

        for (DataBindNode node : nodes) {
            SqlStruct nodeStruct = node.toSqlStruct(bean);
            if (nodeStruct == null) {
                continue;
            }
            struct.appendSql(",")
                    .appendSql(nodeStruct.getSql());
            struct.addParams(nodeStruct.getParams());
        }

        struct.setSql(struct.getSql()
                .replaceFirst(",", StringUtils.EMPTY_STRING));

        return struct;
    }
}
