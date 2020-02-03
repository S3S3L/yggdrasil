package org.s3s3l.yggdrasil.mybatis.sql.statement.select;

import java.util.Iterator;
import java.util.List;

import org.s3s3l.yggdrasil.mybatis.sql.enumerations.DatabaseType;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.OracleHierarchicalExpression;
import net.sf.jsqlparser.statement.select.Distinct;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.Top;

/**
 * ClassName:ExtPlainSelect <br>
 * Date: 2016年2月29日 下午3:12:33 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class ExtPlainSelect extends PlainSelect {

    private DatabaseType databaseType;

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    public ExtPlainSelect() {

    }

    public ExtPlainSelect(PlainSelect plainSelect, DatabaseType databaseType) {
        this.setDistinct(plainSelect.getDistinct());
        this.setTop(plainSelect.getTop());
        this.setSelectItems(plainSelect.getSelectItems());
        this.setFromItem(plainSelect.getFromItem());
        this.setJoins(plainSelect.getJoins());
        this.setWhere(plainSelect.getWhere());
        this.setOracleHierarchical(plainSelect.getOracleHierarchical());
        this.setGroupByElement(plainSelect.getGroupBy());
        this.setHaving(plainSelect.getHaving());
        this.setLimit(plainSelect.getLimit());
        this.setOrderByElements(plainSelect.getOrderByElements());
        this.setOracleSiblings(plainSelect.isOracleSiblings());
        this.databaseType = databaseType;
    }

    @Override
    public String toString() {
        StringBuilder sql = new StringBuilder("SELECT ");
        Distinct distinct = this.getDistinct();
        Top top = this.getTop();
        List<SelectItem> selectItems = this.getSelectItems();
        FromItem fromItem = this.getFromItem();
        List<Join> joins = this.getJoins();
        Expression where = this.getWhere();
        OracleHierarchicalExpression oracleHierarchical = this.getOracleHierarchical();
        List<Expression> groupByColumnReferences = this.getGroupBy()
                .getGroupByExpressions();
        Expression having = this.getHaving();
        ExtLimit limit = new ExtLimit(this.getLimit(), databaseType);
        List<OrderByElement> orderByElements = this.getOrderByElements();
        boolean oracleSiblings = this.isOracleSiblings();

        if (distinct != null) {
            sql.append(distinct)
                    .append(" ");
        }
        if (top != null) {
            sql.append(top)
                    .append(" ");
        }
        sql.append(getStringList(selectItems));
        if (fromItem != null) {
            sql.append(" FROM ")
                    .append(fromItem);
            if (joins != null) {
                Iterator<Join> it = joins.iterator();
                while (it.hasNext()) {
                    Join join = it.next();
                    if (join.isSimple()) {
                        sql.append(", ")
                                .append(join);
                    } else {
                        sql.append(" ")
                                .append(join);
                    }
                }
            }
            if (where != null) {
                sql.append(" WHERE ")
                        .append(where);
            }
            if (oracleHierarchical != null) {
                sql.append(oracleHierarchical.toString());
            }
            sql.append(getFormatedList(groupByColumnReferences, "GROUP BY"));
            if (having != null) {
                sql.append(" HAVING ")
                        .append(having);
            }
            sql.append(orderByToString(oracleSiblings, orderByElements));
            sql.append(limit);
        }
        return sql.toString();
    }

}
