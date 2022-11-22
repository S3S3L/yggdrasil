package org.s3s3l.yggdrasil.orm.bind.condition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.s3s3l.yggdrasil.orm.bind.DataBindNode;
import org.s3s3l.yggdrasil.orm.bind.sql.DefaultSqlStruct;
import org.s3s3l.yggdrasil.orm.bind.sql.SqlStruct;
import org.s3s3l.yggdrasil.utils.reflect.ReflectionBean;
import org.s3s3l.yggdrasil.utils.verify.Verify;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 
 * <p>
 * </p>
 * ClassName: ConditionStruct <br>
 * date: Sep 20, 2019 11:29:03 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@AllArgsConstructor
@NoArgsConstructor
public class ConditionStruct implements DataBindNode {

    private List<RawConditionNode> nodes = new ArrayList<>();

    public void addNode(RawConditionNode node) {
        this.nodes.add(node);
    }

    public void addNodes(Collection<RawConditionNode> node) {
        this.nodes.addAll(node);
    }

    @Override
    public SqlStruct toSqlStruct(ReflectionBean bean) {
        Verify.notNull(bean);

        if (nodes.isEmpty()) {
            return null;
        }

        DefaultSqlStruct struct = new DefaultSqlStruct();
        boolean hasCondition = false;

        for (DataBindNode node : nodes) {
            SqlStruct nodeStruct = node.toSqlStruct(bean);
            if (nodeStruct == null) {
                continue;
            }

            if (!hasCondition) {
                struct.setSql(" WHERE ");
                hasCondition = true;
            } else {
                struct.appendSql(" AND ");
            }

            struct.appendSql(nodeStruct.getSql());
            struct.addParams(nodeStruct.getParams());
        }

        return struct;
    }
}
