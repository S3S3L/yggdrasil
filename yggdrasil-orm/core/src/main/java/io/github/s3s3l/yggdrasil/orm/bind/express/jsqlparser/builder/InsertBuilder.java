package io.github.s3s3l.yggdrasil.orm.bind.express.jsqlparser.builder;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.values.ValuesStatement;

public class InsertBuilder {

    private Table table;
    private List<Column> columns;
    /**
     * JdbcParameter for '?' <br>
     * Column for absolute value<br>
     * Function for database function<br>
     */
    private MultiExpressionList itemsList;

    public InsertBuilder table(Table table) {
        this.table = table;
        return this;
    }

    public InsertBuilder columns(List<Column> columns) {
        this.columns = columns;
        return this;
    }

    public InsertBuilder columns(Column... columns) {
        this.columns = new LinkedList<>();
        Arrays.stream(columns)
                .forEach(this.columns::add);
        return this;
    }

    public InsertBuilder appendColumn(Column... columns) {
        if (this.columns == null) {
            this.columns = new LinkedList<>();
        }
        Arrays.stream(columns)
                .forEach(this.columns::add);
        return this;
    }

    /**
     * JdbcParameter for '?' <br>
     * Column for absolute value<br>
     * Function for database function<br>
     */
    public InsertBuilder itemsLists(ExpressionList... itemsLists) {
        this.itemsList = new MultiExpressionList();
        Arrays.stream(itemsLists)
                .forEach(this.itemsList::addExpressionList);
        return this;
    }

    /**
     * JdbcParameter for '?' <br>
     * Column for absolute value<br>
     * Function for database function<br>
     */
    public InsertBuilder itemsLists(Collection<ExpressionList> itemsLists) {
        this.itemsList = new MultiExpressionList();
        itemsLists.forEach(this.itemsList::addExpressionList);
        return this;
    }

    public Insert build() {
        Insert insert = new Insert();

        insert.setTable(table);
        insert.setColumns(columns);
        // insert.setWithItemsList(itemsList);
        Select select = new Select();
        SetOperationList body = new SetOperationList();
        body.addSelects(new ValuesStatement(itemsList));
        select.setSelectBody(body);
        insert.setSelect(select);
        return insert;
    }
}
