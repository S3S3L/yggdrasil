package org.s3s3l.yggdrasil.orm.bind.express.jsqlparser;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.s3s3l.yggdrasil.orm.bind.SqlStruct;
import org.s3s3l.yggdrasil.orm.bind.annotation.SqlModel;
import org.s3s3l.yggdrasil.orm.bind.express.DataBindExpress;
import org.s3s3l.yggdrasil.orm.exception.DataBindExpressException;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.verify.Verify;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.SelectUtils;

public class JSqlParserDataBindExpress implements DataBindExpress {
    private String table;
    private Map<String, String> nameMap = new ConcurrentHashMap<>();

    public JSqlParserDataBindExpress(Class<?> modelType) {
        express(modelType);
    }

    @Override
    public DataBindExpress express(Class<?> modelType) {
        Verify.notNull(modelType);
        if (!modelType.isAnnotationPresent(SqlModel.class)) {
            throw new DataBindExpressException("No 'SqlModel' annotation found.");
        }

        SqlModel sqlModel = modelType.getAnnotation(SqlModel.class);

        if (StringUtils.isEmpty(sqlModel.table())) {
            throw new DataBindExpressException("Table name can`t be empty.");
        }

        this.table = sqlModel.table();
        return this;
    }

    @Override
    public String getAlias(String name) {
        return this.nameMap.get(name);
    }

    @Override
    public SqlStruct getInsert(List<?> model) {
        
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SqlStruct getDelete(Object model) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SqlStruct getUpdate(Object model) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SqlStruct getSelect(Object model) {
//        Select select = SelectUtils.buildSelectFromTable(table)
        // TODO Auto-generated method stub
        return null;
    }

    public static void main(String[] args) throws JSQLParserException {
        Select stmt = (Select) CCJSqlParserUtil.parse(
                "SELECT col1 AS a, col2 AS b, col3 AS c FROM table WHERE col_1 = 10 AND col_2 = 20 AND col_3 = 30");
        System.out.println("before " + stmt.toString());

        ((PlainSelect) stmt.getSelectBody()).getWhere()
                .accept(new ExpressionVisitorAdapter() {
                    @Override
                    public void visit(Column column) {
                        column.setColumnName(column.getColumnName()
                                .replace("_", ""));
                    }
                });

        System.out.println("after " + stmt.toString());
        
        BinaryExpression equalsTo = new EqualsTo();
        equalsTo.setLeftExpression(new Column("id"));
        equalsTo.setRightExpression(new Column("?"));
        
        System.out.println(SelectUtils.buildSelectFromTableAndExpressions(new Table("mytable"), 
                new Column("a"), new Column("b"), equalsTo));
    }

}
