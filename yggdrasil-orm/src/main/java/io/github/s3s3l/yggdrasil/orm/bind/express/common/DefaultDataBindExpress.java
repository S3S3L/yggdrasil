package io.github.s3s3l.yggdrasil.orm.bind.express.common;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import io.github.s3s3l.yggdrasil.orm.bind.ColumnStruct;
import io.github.s3s3l.yggdrasil.orm.bind.condition.ConditionStruct;
import io.github.s3s3l.yggdrasil.orm.bind.condition.RawConditionNode;
import io.github.s3s3l.yggdrasil.orm.bind.express.DataBindExpress;
import io.github.s3s3l.yggdrasil.orm.bind.select.SelectStruct;
import io.github.s3s3l.yggdrasil.orm.bind.set.SetNode;
import io.github.s3s3l.yggdrasil.orm.bind.set.SetStruct;
import io.github.s3s3l.yggdrasil.orm.bind.sql.DefaultSqlStruct;
import io.github.s3s3l.yggdrasil.orm.bind.sql.SqlStruct;
import io.github.s3s3l.yggdrasil.orm.bind.value.ValuesStruct;
import io.github.s3s3l.yggdrasil.orm.exception.DataBindExpressException;
import io.github.s3s3l.yggdrasil.orm.meta.DatabaseType;
import io.github.s3s3l.yggdrasil.orm.meta.GroupByMeta;
import io.github.s3s3l.yggdrasil.orm.meta.LimitMeta;
import io.github.s3s3l.yggdrasil.orm.meta.MetaManager;
import io.github.s3s3l.yggdrasil.orm.meta.OffsetMeta;
import io.github.s3s3l.yggdrasil.orm.meta.OrderByMeta;
import io.github.s3s3l.yggdrasil.orm.meta.TableMeta;
import io.github.s3s3l.yggdrasil.utils.collection.CollectionUtils;
import io.github.s3s3l.yggdrasil.utils.reflect.PropertyDescriptorReflectionBean;
import io.github.s3s3l.yggdrasil.utils.verify.Verify;

/**
 * 
 * <p>
 * </p>
 * ClassName: DefaultDataBindExpress <br>
 * date: Sep 20, 2019 11:27:43 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class DefaultDataBindExpress implements DataBindExpress {
    private final MetaManager metaManager;

    public DefaultDataBindExpress(MetaManager metaManager) {
        this.metaManager = metaManager;
    }

    @Override
    public SqlStruct getInsert(List<?> sources) {
        Verify.notEmpty(sources);

        Class<?> modelType = sources.get(0).getClass();
        TableMeta table = metaManager.getTable(modelType);

        DefaultSqlStruct struct = new DefaultSqlStruct();

        AtomicBoolean first = new AtomicBoolean(true);
        List<DefaultSqlStruct> valueStructs = sources.stream()
                .map(r -> new ValuesStruct(table.getColumns())
                        .toSqlStruct(new PropertyDescriptorReflectionBean(r), first.getAndSet(false)))
                .collect(Collectors.toList());

        struct.setSql("INSERT INTO ");
        struct.appendSql(table.getName());
        valueStructs.forEach(r -> {
            struct.appendSql(r.getSql());
            struct.addParams(r.getParams());
        });

        return struct;
    }

    @Override
    public SqlStruct getDelete(Object condition) {
        Verify.notNull(condition);

        DefaultSqlStruct struct = new DefaultSqlStruct();
        Class<?> conditionType = condition.getClass();
        TableMeta table = metaManager.getTable(conditionType);

        SqlStruct deleteStruct = new ConditionStruct(metaManager.getDeleteCondition(
                conditionType).stream().map(
                        RawConditionNode::new)
                .collect(Collectors.toList()))
                .toSqlStruct(new PropertyDescriptorReflectionBean(condition));

        struct.setSql("DELETE FROM ");
        struct.appendSql(table.getName());

        if (deleteStruct != null) {
            struct.appendSql(deleteStruct.getSql());
            struct.addParams(deleteStruct.getParams());
        }
        return struct;
    }

    @Override
    public SqlStruct getUpdate(Object source, Object condition) {
        Verify.notNull(source);
        Verify.notNull(condition);

        DefaultSqlStruct struct = new DefaultSqlStruct();
        Class<?> sourceType = source.getClass();
        Class<?> conditionType = condition.getClass();

        TableMeta table = metaManager.getTable(sourceType);

        PropertyDescriptorReflectionBean sourceRef = new PropertyDescriptorReflectionBean(source);
        PropertyDescriptorReflectionBean conditionRef = new PropertyDescriptorReflectionBean(condition);

        SqlStruct setStruct = new SetStruct(
                table.getColumns().stream().map(SetNode::new).collect(Collectors.toList()))
                .toSqlStruct(sourceRef);
        SqlStruct conditionStruct = new ConditionStruct(metaManager.getUpdateCondition(
                conditionType).stream().map(
                        RawConditionNode::new)
                .collect(Collectors.toList())).toSqlStruct(conditionRef);

        if (setStruct == null) {
            return null;
        }

        struct.setSql("UPDATE ");
        struct.appendSql(table.getName())
                .appendSql(setStruct.getSql())
                .appendSql(conditionStruct.getSql());
        struct.addParams(setStruct.getParams())
                .addParams(conditionStruct.getParams());
        return struct;
    }

    @Override
    public SqlStruct getSelect(Object condition) {
        return getSelect(condition, false);
    }

    @Override
    public SqlStruct getSelectCount(Object condition) {
        return getSelect(condition, true);
    }

    private SqlStruct getSelect(Object condition, boolean count) {
        Verify.notNull(condition);

        DefaultSqlStruct struct = new DefaultSqlStruct();

        Class<?> conditionType = condition.getClass();

        TableMeta table = metaManager.getTable(conditionType);

        if (table == null) {
            throw new DataBindExpressException(String.format("type %s not resolved.", conditionType.getName()));
        }

        OffsetMeta offset = metaManager.getOffset(conditionType);
        LimitMeta limit = metaManager.getLimit(conditionType);
        GroupByMeta groupBy = metaManager.getGroupBy(conditionType);
        List<OrderByMeta> orderByMetas = metaManager.getOrderBy(conditionType);

        PropertyDescriptorReflectionBean ref = new PropertyDescriptorReflectionBean(condition);
        boolean hasGroupBy = groupBy != null && !CollectionUtils.isEmpty(groupBy.getColumns());
        SqlStruct selectStruct = new SelectStruct(
                table.getColumns().stream()
                        .filter(col -> !hasGroupBy || groupBy.getColumns().contains(col.getName()))
                        .map(ColumnStruct::new).collect(Collectors.toList()))
                .toSqlStruct(null);
        SqlStruct conditionStruct = new ConditionStruct(metaManager.getSelectCondition(
                conditionType).stream().map(
                        RawConditionNode::new)
                .collect(Collectors.toList())).toSqlStruct(ref);

        if (selectStruct == null) {
            return null;
        }

        struct.setSql("SELECT ");
        struct.appendSql(count ? "COUNT(*)" : selectStruct.getSql())
                .appendSql(" FROM ")
                .appendSql(table.getName());
        if (conditionStruct != null) {
            struct.appendSql(conditionStruct.getSql());
            struct.addParams(conditionStruct.getParams());
        }

        if (count) {
            return struct;
        }

        if (hasGroupBy) {
            struct.appendSql(" GROUP BY ").appendSql(String.join(", ", groupBy.getColumns()));
        }

        if (!CollectionUtils.isEmpty(orderByMetas)) {
            struct.appendSql(" ORDER BY ")
                    .appendSql(String.join(", ",
                            orderByMetas.stream()
                                    .map(ob -> String.join(" ", ob.getName(), ob.isDesc() ? "DESC" : "AESC"))
                                    .collect(Collectors.toList())));
        }

        if (limit != null) {
            struct.appendSql(" LIMIT ?");
            struct.addParam(ref.getFieldValue(limit.getField().getName()));
        }

        if (offset != null) {
            struct.appendSql(" OFFSET ?");
            struct.addParam(ref.getFieldValue(offset.getField().getName()));
        }
        return struct;
    }

    @Override
    public SqlStruct getCreate(Class<?> tableType, boolean force) {
        Verify.notNull(tableType);
        DefaultSqlStruct struct = new DefaultSqlStruct();
        TableMeta table = metaManager.getTable(tableType);

        struct.setSql("CREATE TABLE ");

        if (!force) {
            struct.appendSql("IF NOT EXISTS ");
        }

        struct.appendSql(table.getName() + " (");
        struct.appendSql(
                String.join(", ", table
                        .getColumns().stream().map(col -> {
                            List<String> args = new LinkedList<>();
                            args.add(col.getName());
                            DatabaseType dbType = col.getDbType();
                            args.add(dbType.getType());
                            if (!CollectionUtils.isEmpty(dbType.getArgs())) {
                                args.add(String.format("(%s)",
                                        String.join(", ", dbType.getArgs())));
                            }

                            StringBuilder sb = new StringBuilder(String.join(" ", args));

                            if (dbType.isPrimary()) {
                                sb.append(" PRIMARY KEY");
                            }

                            if (dbType.isNotNull()) {
                                sb.append(" NOT NULL");
                            }

                            return sb.toString();
                        })
                        .collect(Collectors.toList())));
        struct.appendSql(")");
        return struct;
    }

    @Override
    public SqlStruct getDrop(Class<?> tableType) {
        Verify.notNull(tableType);
        DefaultSqlStruct struct = new DefaultSqlStruct();
        TableMeta table = metaManager.getTable(tableType);

        struct.setSql("DROP TABLE IF EXISTS ");
        struct.appendSql(table.getName());

        return struct;
    }
}
