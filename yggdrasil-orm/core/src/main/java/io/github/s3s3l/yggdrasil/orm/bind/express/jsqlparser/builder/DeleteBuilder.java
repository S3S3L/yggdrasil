package io.github.s3s3l.yggdrasil.orm.bind.express.jsqlparser.builder;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;

public class DeleteBuilder {

    private Table table;
    private Expression where;

    public DeleteBuilder table(Table table) {
        this.table = table;
        return this;
    }

    public DeleteBuilder where(Expression where) {
        this.where = where;
        return this;
    }

    public Delete build() {
        Delete delete = new Delete();
        delete.setTable(table);
        delete.setWhere(where);

        return delete;
    }
}
