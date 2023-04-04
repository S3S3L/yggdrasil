package io.github.s3s3l.yggdrasil.orm.bind.express.jsqlparser.builder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.update.UpdateSet;

public class UpdateBuilder {

    private Table table;
    private Expression where;
    private List<Column> columns = new LinkedList<>();
    private List<Expression> expressions = new LinkedList<>();

    public UpdateBuilder table(Table table) {
        this.table = table;
        return this;
    }

    public UpdateBuilder where(Expression where) {
        this.where = where;
        return this;
    }

    public UpdateBuilder addSet(Column column, Expression expression) {
        this.columns.add(column);
        this.expressions.add(expression);
        return this;
    }

    public Update build() {
        Update update = new Update();
        update.setTable(table);
        UpdateSet updateSet = new UpdateSet();
        updateSet.setColumns(new ArrayList<>(columns));
        updateSet.setExpressions(new ArrayList<>(expressions));
        update.addUpdateSet(updateSet);
        update.setWhere(where);
        return update;
    }
}
