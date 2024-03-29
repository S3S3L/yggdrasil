package io.github.s3s3l.yggdrasil.orm.mysql.bind.express.bind.express;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import io.github.s3s3l.yggdrasil.orm.bind.express.jsqlparser.JSqlParserDataBindExpress;
import io.github.s3s3l.yggdrasil.orm.bind.express.jsqlparser.builder.SelectBuilder;
import io.github.s3s3l.yggdrasil.orm.bind.sql.DefaultSqlStruct;
import io.github.s3s3l.yggdrasil.orm.bind.sql.SqlStruct;
import io.github.s3s3l.yggdrasil.orm.enumerations.DatabaseType;
import io.github.s3s3l.yggdrasil.orm.factory.DbTypeHandlerFactory;
import io.github.s3s3l.yggdrasil.orm.meta.ColumnMeta;
import io.github.s3s3l.yggdrasil.orm.meta.DbType;
import io.github.s3s3l.yggdrasil.orm.meta.LimitMeta;
import io.github.s3s3l.yggdrasil.orm.meta.MetaManager;
import io.github.s3s3l.yggdrasil.orm.meta.OffsetMeta;
import io.github.s3s3l.yggdrasil.utils.collection.CollectionUtils;
import io.github.s3s3l.yggdrasil.utils.reflect.ReflectionBean;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.alter.AlterExpression;
import net.sf.jsqlparser.statement.alter.AlterExpression.ColumnDataType;
import net.sf.jsqlparser.statement.alter.AlterOperation;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.select.Limit;

public class MysqlJSqlParserDataBindExpress extends JSqlParserDataBindExpress {

    public MysqlJSqlParserDataBindExpress(MetaManager metaManager, DbTypeHandlerFactory dbTypeHandlerFactory) {
        super(metaManager, dbTypeHandlerFactory);
    }

    @Override
    public DatabaseType databaseType() {
        return DatabaseType.MYSQL;
    }

    @Override
    protected void offsetLimit(ReflectionBean rb, OffsetMeta offset, LimitMeta limit, SelectBuilder builder) {

        if (limit != null) {
            Object limitVal = rb.getFieldValue(limit.getField()
                    .getName());
            if (limitVal != null) {
                Limit l = new Limit();
                l.setRowCount(new LongValue((long) limitVal));
                builder.limit(l);

                if (offset != null) {
                    Object offsetVal = rb.getFieldValue(offset.getField()
                            .getName());
                    if (offsetVal != null) {
                        l.setOffset(new LongValue((long) offsetVal));
                    }
                }
            }
        }
    }

    @Override
    public SqlStruct getColumnAdd(ColumnMeta columnMeta) {
        DefaultSqlStruct sql = new DefaultSqlStruct();

        Alter alter = new Alter();
        alter.setTable(new Table(columnMeta.getTableName()));

        DbType dbType = columnMeta.getDbType();
        ColDataType colDataType = new ColDataType(dbTypeHandler.getDataTypeStr(dbType));
        if (CollectionUtils.isNotEmpty(dbType.getArgs())) {
            colDataType.addArgumentsStringList(dbType.getArgs());
        }
        List<String> columnSpecs = new LinkedList<>();
        if (dbType.isDef()) {
            columnSpecs.add("DEFAULT");
            columnSpecs.add(dbType.getDefValue());
        }
        if (dbType.isNotNull()) {
            columnSpecs.add("NOT");
            columnSpecs.add("NULL");
        } else {
            columnSpecs.add("NULL");
        }
        ColumnDataType columnDataType = new ColumnDataType(columnMeta.getName(), false, colDataType, columnSpecs);

        AlterExpression alterExpression = new AlterExpression();
        alterExpression.setOperation(AlterOperation.ADD);
        alterExpression.addColDataType(columnDataType);
        alterExpression.hasColumn(true);

        alter.setAlterExpressions(Arrays.asList(alterExpression));

        sql.appendSql(alter.toString());

        return sql;
    }

    @Override
    public SqlStruct getColumnDrop(ColumnMeta columnMeta) {
        DefaultSqlStruct sql = new DefaultSqlStruct();

        Alter alter = new Alter();
        alter.setTable(new Table(columnMeta.getTableName()));

        AlterExpression alterExpression = new AlterExpression();
        alterExpression.setOperation(AlterOperation.DROP);
        alterExpression.setColumnName(columnMeta.getName());
        alterExpression.hasColumn(true);

        alter.setAlterExpressions(Arrays.asList(alterExpression));

        sql.appendSql(alter.toString());

        return sql;
    }

    @Override
    public SqlStruct getColumnAlter(ColumnMeta columnMeta) {
        DefaultSqlStruct sql = new DefaultSqlStruct();
        String colName = columnMeta.getName();

        Alter alter = new Alter();
        alter.setTable(new Table(columnMeta.getTableName()));

        DbType dbType = columnMeta.getDbType();
        ColDataType colDataType = new ColDataType(dbTypeHandler.getDataTypeStr(dbType));
        if (CollectionUtils.isNotEmpty(dbType.getArgs())) {
            colDataType.addArgumentsStringList(dbType.getArgs());
        }
        List<String> columnSpecs = new LinkedList<>();
        if (dbType.isDef()) {
            columnSpecs.add("DEFAULT");
            columnSpecs.add(dbType.getDefValue());
        }
        if (dbType.isNotNull()) {
            columnSpecs.add("NOT");
            columnSpecs.add("NULL");
        } else {
            columnSpecs.add("NULL");
        }
        ColumnDataType columnDataType = new ColumnDataType(colName, false, colDataType, columnSpecs);

        AlterExpression alterExpression = new AlterExpression();
        alterExpression.setOperation(AlterOperation.CHANGE);
        alterExpression.setOptionalSpecifier("COLUMN");
        alterExpression.hasColumn(true);
        alterExpression.setColumnOldName(colName);
        alterExpression.addColDataType(columnDataType);

        alter.setAlterExpressions(Arrays.asList(alterExpression));

        sql.appendSql(alter.toString());

        return sql;
    }

}
