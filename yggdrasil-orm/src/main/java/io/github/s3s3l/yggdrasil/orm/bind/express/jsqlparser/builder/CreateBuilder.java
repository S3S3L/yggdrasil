package io.github.s3s3l.yggdrasil.orm.bind.express.jsqlparser.builder;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import io.github.s3s3l.yggdrasil.orm.meta.DbType;
import io.github.s3s3l.yggdrasil.orm.meta.TableMeta;
import io.github.s3s3l.yggdrasil.utils.collection.CollectionUtils;
import lombok.AllArgsConstructor;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

@AllArgsConstructor
public class CreateBuilder {

    private TableMeta tableMeta;
    private boolean force = false;

    public CreateTable build() {
        CreateTable createTable = new CreateTable();
        createTable.setTable(new Table(tableMeta.getName()));
        createTable.setIfNotExists(!force);
        createTable.setColumnDefinitions(tableMeta.getColumns().stream().map(col -> {
            ColumnDefinition columnDefinition = new ColumnDefinition();
            columnDefinition.setColumnName(col.getName());

            DbType dbType = col.getDbType();
            ColDataType type = new ColDataType();
            type.setDataType(dbType.getType());
            type.setArgumentsStringList(CollectionUtils.isEmpty(dbType.getArgs()) ? null : dbType.getArgs());
            List<String> columnSpecStrings = new LinkedList<>();
            if (dbType.isPrimary()) {
                columnSpecStrings.add("PRIMARY");
                columnSpecStrings.add("KEY");
            }
            if (dbType.isNotNull()) {
                columnSpecStrings.add("NOT");
                columnSpecStrings.add("NULL");
            }
            columnDefinition.setColDataType(type);
            columnDefinition.setColumnSpecs(columnSpecStrings);
            return columnDefinition;
        }).collect(Collectors.toList()));
        return createTable;
    }
}
