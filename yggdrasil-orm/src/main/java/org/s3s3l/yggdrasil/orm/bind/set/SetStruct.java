package org.s3s3l.yggdrasil.orm.bind.set;

import java.util.ArrayList;
import java.util.List;

import org.s3s3l.yggdrasil.orm.bind.DataBindNode;
import org.s3s3l.yggdrasil.orm.bind.SqlStruct;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.reflect.ReflectionBean;
import org.s3s3l.yggdrasil.utils.verify.Verify;

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

        SqlStruct struct = new SqlStruct();
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
