package io.github.s3s3l.yggdrasil.orm.bind.express.jsqlparser;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import io.github.s3s3l.yggdrasil.orm.bind.sql.DefaultSqlStruct;
import io.github.s3s3l.yggdrasil.orm.bind.sql.SqlStruct;
import io.github.s3s3l.yggdrasil.orm.enumerations.DatabaseType;
import io.github.s3s3l.yggdrasil.orm.meta.ColumnMeta;
import io.github.s3s3l.yggdrasil.orm.meta.DbType;
import io.github.s3s3l.yggdrasil.orm.meta.MetaManager;
import io.github.s3s3l.yggdrasil.utils.collection.CollectionUtils;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.alter.AlterExpression;
import net.sf.jsqlparser.statement.alter.AlterExpression.ColumnDataType;
import net.sf.jsqlparser.statement.alter.AlterOperation;
import net.sf.jsqlparser.statement.create.table.ColDataType;

public class PsqlJSqlParserDataBindExpress extends JSqlParserDataBindExpress {

    public PsqlJSqlParserDataBindExpress(MetaManager metaManager) {
        super(metaManager);
    }

    @Override
    public DatabaseType databaseType() {
        return DatabaseType.POSTGRESQL;
    }

    @Override
    public SqlStruct getColumnAdd(ColumnMeta columnMeta) {
        DefaultSqlStruct sql = new DefaultSqlStruct();

        Alter alter = new Alter();
        alter.setTable(new Table(columnMeta.getTableAlias()));

        DbType dbType = columnMeta.getDbType();
        ColDataType colDataType = new ColDataType(dbType.getType());
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
        }
        ColumnDataType columnDataType = new ColumnDataType(columnMeta.getName(), false, colDataType, columnSpecs);

        AlterExpression alterExpression = new AlterExpression();
        alterExpression.setUsingIfExists(true);
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
        alter.setTable(new Table(columnMeta.getTableAlias()));

        AlterExpression alterExpression = new AlterExpression();
        alterExpression.setUsingIfExists(true);
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
        alter.setTable(new Table(columnMeta.getTableAlias()));

        DbType dbType = columnMeta.getDbType();
        ColDataType colDataType = new ColDataType(dbType.getType());
        if (CollectionUtils.isNotEmpty(dbType.getArgs())) {
            colDataType.addArgumentsStringList(dbType.getArgs());
        }
        ColumnDataType columnDataType = new ColumnDataType(colName, true, colDataType, null);

        AlterExpression alterExpression = new AlterExpression();
        alterExpression.setUsingIfExists(true);
        alterExpression.setOperation(AlterOperation.ALTER);
        alterExpression.hasColumn(true);
        alterExpression.addColDataType(columnDataType);

        alter.setAlterExpressions(Arrays.asList(alterExpression));

        sql.appendSql(alter.toString());

        return sql;
    }
    
}
