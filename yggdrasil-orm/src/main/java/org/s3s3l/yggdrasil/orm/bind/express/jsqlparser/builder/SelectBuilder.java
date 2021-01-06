package org.s3s3l.yggdrasil.orm.bind.express.jsqlparser.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.s3s3l.yggdrasil.utils.collection.CollectionUtils;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.GroupByElement;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.Offset;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;

public class SelectBuilder {

    private Table table;
    private Expression where;
    private List<SelectItem> selectItems;
    private List<Expression> groupByExpressions = new ArrayList<>();
    private List<OrderByElement> orderByElements;
    private Offset offset;
    private Limit limit;

    public SelectBuilder table(Table table) {
        this.table = table;
        return this;
    }

    public SelectBuilder selectItems(List<SelectItem> selectItems) {
        this.selectItems = selectItems;
        return this;
    }

    public SelectBuilder where(Expression where) {
        this.where = where;
        return this;
    }

    public SelectBuilder groupByExpressions(List<Expression> groupByExpressions) {
        this.groupByExpressions = groupByExpressions;
        return this;
    }

    public SelectBuilder orderByElements(List<OrderByElement> orderByElements) {
        this.orderByElements = orderByElements;
        return this;
    }

    public SelectBuilder limit(Limit limit) {
        this.limit = limit;
        return this;
    }

    public SelectBuilder offset(Offset offset) {
        this.offset = offset;
        return this;
    }

    public Select build() {
        PlainSelect selectBody = new PlainSelect();
        selectBody.setFromItem(table);
        selectBody.setSelectItems(selectItems);
        selectBody.setWhere(where);
        if (!CollectionUtils.isEmpty(groupByExpressions)) {
            GroupByElement groupBy = new GroupByElement();
            groupBy.setGroupByExpressions(groupByExpressions);
            selectBody.setGroupByElement(groupBy);
        }
        if (!CollectionUtils.isEmpty(orderByElements)) {
            selectBody.setOrderByElements(orderByElements);
        }
        selectBody.setOffset(offset);
        selectBody.setLimit(limit);

        Select select = new Select();
        select.setSelectBody(selectBody);

        return select;
    }

    public static void main(String[] args) {
        OrderByElement ob = new OrderByElement();
        ob.setExpression(new Column("age"));
        Offset offset = new Offset();
        offset.setOffset(10);
        Limit limit = new Limit();
        limit.setRowCount(new LongValue(5));

        System.out.println(new SelectBuilder().table(new Table("t_user"))
                .selectItems(Arrays.asList(new AllColumns()))
                .groupByExpressions(Arrays.asList(new Column("phone")))
                .orderByElements(Arrays.asList(ob))
                .offset(offset)
                .limit(limit)
                .build());
    }
}
