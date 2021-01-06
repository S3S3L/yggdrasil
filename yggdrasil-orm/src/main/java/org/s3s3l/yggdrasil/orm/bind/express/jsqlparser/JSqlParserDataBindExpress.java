package org.s3s3l.yggdrasil.orm.bind.express.jsqlparser;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.s3s3l.yggdrasil.bean.exception.ResourceNotFoundException;
import org.s3s3l.yggdrasil.orm.bind.express.DataBindExpress;
import org.s3s3l.yggdrasil.orm.bind.express.jsqlparser.builder.DeleteBuilder;
import org.s3s3l.yggdrasil.orm.bind.express.jsqlparser.builder.InsertBuilder;
import org.s3s3l.yggdrasil.orm.bind.express.jsqlparser.builder.SelectBuilder;
import org.s3s3l.yggdrasil.orm.bind.express.jsqlparser.builder.UpdateBuilder;
import org.s3s3l.yggdrasil.orm.bind.express.jsqlparser.postgresql.ArrayValue;
import org.s3s3l.yggdrasil.orm.bind.sql.DefaultSqlStruct;
import org.s3s3l.yggdrasil.orm.bind.sql.JSqlParserSqlStruct;
import org.s3s3l.yggdrasil.orm.bind.sql.SqlStruct;
import org.s3s3l.yggdrasil.orm.enumerations.ComparePattern;
import org.s3s3l.yggdrasil.orm.exception.DataMapException;
import org.s3s3l.yggdrasil.orm.meta.ColumnMeta;
import org.s3s3l.yggdrasil.orm.meta.ConditionMeta;
import org.s3s3l.yggdrasil.orm.meta.LimitMeta;
import org.s3s3l.yggdrasil.orm.meta.MetaManager;
import org.s3s3l.yggdrasil.orm.meta.OffsetMeta;
import org.s3s3l.yggdrasil.orm.meta.TableMeta;
import org.s3s3l.yggdrasil.utils.collection.CollectionUtils;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.reflect.PropertyDescriptorReflectionBean;
import org.s3s3l.yggdrasil.utils.reflect.ReflectionBean;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.Offset;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.util.SelectUtils;

public class JSqlParserDataBindExpress implements DataBindExpress {
    private final MetaManager metaManager;
    private Class<?> modelType;

    public JSqlParserDataBindExpress(Class<?> modelType, MetaManager metaManager) {
        this.metaManager = metaManager;
        express(modelType);
    }

    @Override
    public DataBindExpress express(Class<?> modelType) {
        if (!this.metaManager.isResolved(modelType)) {
            throw new ResourceNotFoundException("type '" + modelType.getName() + "' have not been resolved.");
        }
        this.modelType = modelType;
        return this;
    }

    @Override
    public String getAlias(String name) {
        return this.metaManager.getAlias(modelType, name);
    }

    @Override
    public SqlStruct getInsert(List<?> models) {
        TableMeta table = this.metaManager.getTable(this.modelType);
        DefaultSqlStruct sqlStruct = new DefaultSqlStruct();
        List<Column> insertColumns = new LinkedList<>();
        List<ExpressionList> expressionLists = new LinkedList<>();
        for (ColumnMeta column : table.getColumns()) {
            insertColumns.add(new Column(column.getName()));
        }

        for (Object model : models) {
            ReflectionBean rb = new PropertyDescriptorReflectionBean(model);
            List<Expression> expressions = new LinkedList<>();
            for (ColumnMeta column : table.getColumns()) {
                Field field = column.getField();
                Class<?> fieldType = field.getType();
                Object fieldValue = rb.getFieldValue(field.getName());
                if (fieldType.isArray()) {
                    List<Expression> arrayExpressions = new LinkedList<>();
                    if (column.getValidator()
                            .isValid(fieldValue)) {
                        Arrays.stream(((Object[]) fieldValue))
                                .forEach(obj -> {
                                    sqlStruct.addParam(obj);
                                    arrayExpressions.add(new JdbcParameter());
                                });
                    }

                    expressions.add(new ArrayValue(arrayExpressions));
                } else if (Collection.class.isAssignableFrom(fieldType)) {
                    List<Expression> arrayExpressions = new LinkedList<>();
                    if (column.getValidator()
                            .isValid(fieldValue)) {
                        ((Collection<?>) fieldValue).forEach(obj -> {
                            sqlStruct.addParam(obj);
                            arrayExpressions.add(new JdbcParameter());
                        });
                    }
                    expressions.add(new ArrayValue(arrayExpressions));
                } else {
                    sqlStruct.addParam(fieldValue);
                    expressions.add(new JdbcParameter());
                }
            }
            expressionLists.add(new ExpressionList(expressions));
        }
        sqlStruct.appendSql(new InsertBuilder().table(new Table(table.getName()))
                .columns(insertColumns)
                .itemsLists(expressionLists)
                .build()
                .toString());
        return sqlStruct;
    }

    @Override
    public SqlStruct getDelete(Object model) {
        DefaultSqlStruct sqlStruct = new DefaultSqlStruct();
        TableMeta table = this.metaManager.getTable(this.modelType);
        JSqlParserSqlStruct whereStruct = buildWhere(this.metaManager.getDeleteCondition(this.modelType),
                new PropertyDescriptorReflectionBean(model));
        sqlStruct.appendSql(new DeleteBuilder().table(new Table(table.getName()))
                .where(whereStruct.getExpression())
                .build()
                .toString());
        sqlStruct.addParams(whereStruct.getParams());
        return sqlStruct;
    }

    @Override
    public SqlStruct getUpdate(Object model) {
        DefaultSqlStruct sqlStruct = new DefaultSqlStruct();
        TableMeta table = this.metaManager.getTable(this.modelType);
        ReflectionBean rb = new PropertyDescriptorReflectionBean(model);
        JSqlParserSqlStruct whereStruct = buildWhere(this.metaManager.getUpdateCondition(this.modelType),
                new PropertyDescriptorReflectionBean(model));
        UpdateBuilder builder = new UpdateBuilder().table(new Table(table.getName()));
        for (ColumnMeta columnMeta : table.getColumns()) {
            String fieldName = columnMeta.getField()
                    .getName();
            if (!rb.hasField(fieldName)) {
                continue;
            }
            builder.addSet(new Column(columnMeta.getName()), new JdbcParameter());
            sqlStruct.addParam(rb.getFieldValue(fieldName));
        }
        builder.where(whereStruct.getExpression());
        sqlStruct.addParam(whereStruct.getParams());
        sqlStruct.appendSql(builder.build()
                .toString());
        return sqlStruct;
    }

    @Override
    public SqlStruct getSelect(Object model) {
        DefaultSqlStruct sqlStruct = new DefaultSqlStruct();
        ReflectionBean rb = new PropertyDescriptorReflectionBean(model);
        TableMeta table = metaManager.getTable(modelType);
        OffsetMeta offset = metaManager.getOffset(modelType);
        LimitMeta limit = metaManager.getLimit(modelType);
        JSqlParserSqlStruct whereStruct = buildWhere(this.metaManager.getSelectCondition(modelType), rb);
        SelectBuilder builder = new SelectBuilder().table(new Table(table.getName()))
                .selectItems(table.getColumns()
                        .stream()
                        .map(col -> {
                            SelectExpressionItem selectItem = new SelectExpressionItem(new Column(col.getName()));
                            selectItem.setAlias(new Alias(col.getAlias()));
                            return selectItem;
                        })
                        .collect(Collectors.toList()))
                .groupByExpressions(metaManager.getGroupBy(modelType)
                        .getColumns()
                        .stream()
                        .map(Column::new)
                        .collect(Collectors.toList()))
                .orderByElements(metaManager.getOrderBy(modelType)
                        .stream()
                        .map(obm -> {
                            OrderByElement ob = new OrderByElement();
                            ob.setExpression(new Column(obm.getName()));
                            ob.setAsc(!obm.isDesc());
                            return ob;
                        })
                        .collect(Collectors.toList()))
                .where(whereStruct.getExpression());

        if (offset != null) {
            Object offsetCount = rb.getFieldValue(offset.getField()
                    .getName());
            if (offsetCount != null) {
                Offset os = new Offset();
                os.setOffset((long) offsetCount);
                builder.offset(os);
            }
        }

        if (limit != null) {
            Object limitCount = rb.getFieldValue(limit.getField()
                    .getName());
            if (limitCount != null) {
                Limit l = new Limit();
                l.setRowCount(new LongValue((long) limitCount));
                builder.limit(l);
            }
        }

        sqlStruct.appendSql(builder.build()
                .toString());
        sqlStruct.addParams(whereStruct.getParams());

        return sqlStruct;
    }

    private JSqlParserSqlStruct buildWhere(List<ConditionMeta> conditions, ReflectionBean rb) {
        JSqlParserSqlStruct whereStruct = new JSqlParserSqlStruct();

        if (CollectionUtils.isEmpty(conditions)) {
            return whereStruct;
        }

        JSqlParserSqlStruct sqlStruct = toExpression(conditions.get(0), rb);
        Expression expression = sqlStruct.getExpression();
        whereStruct.addParams(sqlStruct.getParams());

        for (int i = 1; i < conditions.size(); i++) {
            sqlStruct = toExpression(conditions.get(i), rb);
            if (sqlStruct == null) {
                continue;
            }
            expression = new AndExpression(expression, sqlStruct.getExpression());
            whereStruct.addParams(sqlStruct.getParams());
        }

        whereStruct.setExpression(expression);

        return whereStruct;
    }

    private JSqlParserSqlStruct toExpression(ConditionMeta condition, ReflectionBean rb) {
        JSqlParserSqlStruct struct = new JSqlParserSqlStruct();
        Field field = condition.getField();
        ColumnMeta column = condition.getColumn();
        Object value = rb.getFieldValue(field.getName());
        Class<?> fieldType = field.getType();
        ComparePattern pattern = condition.getPattern();

        if (!condition.getValidator()
                .isValid(value)) {
            return null;
        }

        switch (pattern) {
            case END_WITH:
                LikeExpression endWith = new LikeExpression();
                endWith.setLeftExpression(new Column(column.getName()));
                endWith.setRightExpression(new JdbcParameter());
                struct.addParam(StringUtils.isEmpty(value) ? "%" : "%" + value.toString());
                struct.setExpression(endWith);
                break;
            case EQUAL:
                EqualsTo et = new EqualsTo();
                et.setLeftExpression(new Column(column.getName()));
                et.setRightExpression(new JdbcParameter());
                struct.addParam(value);
                struct.setExpression(et);
                break;
            case IN:
                InExpression in = new InExpression();
                in.setLeftExpression(new Column(column.getName()));
                List<Expression> expressions = new LinkedList<>();
                if (fieldType.isArray()) {
                    Arrays.stream(((Object[]) value))
                            .forEach(obj -> {
                                struct.addParam(obj);
                                expressions.add(new JdbcParameter());
                            });
                } else if (Collection.class.isAssignableFrom(fieldType)) {
                    ((Collection<?>) value).forEach(obj -> {
                        struct.addParam(obj);
                        expressions.add(new JdbcParameter());
                    });
                } else {
                    throw new DataMapException(String.format("field '%s' of type '%s' can not use 'IN' operation.",
                            field.getName(), fieldType.getName()));
                }
                if (expressions.isEmpty()) {
                    return null;
                }
                in.setRightItemsList(new ExpressionList(expressions));
                struct.setExpression(in);
                break;
            case LAGER:
                GreaterThan gt = new GreaterThan();
                gt.setLeftExpression(new Column(column.getName()));
                gt.setRightExpression(new JdbcParameter());
                struct.addParam(value);
                struct.setExpression(gt);
                break;
            case LESS:
                MinorThan mt = new MinorThan();
                mt.setLeftExpression(new Column(column.getName()));
                mt.setRightExpression(new JdbcParameter());
                struct.addParam(value);
                struct.setExpression(mt);
                break;
            case LIKE:
                LikeExpression like = new LikeExpression();
                like.setLeftExpression(new Column(column.getName()));
                like.setRightExpression(new JdbcParameter());
                struct.addParam(value);
                struct.setExpression(like);
                break;
            case NON_NULL:
                IsNullExpression nonNull = new IsNullExpression();
                nonNull.setLeftExpression(new Column(column.getName()));
                nonNull.setNot(true);
                struct.setExpression(nonNull);
                break;
            case NOT_LAGER:
                MinorThanEquals mte = new MinorThanEquals();
                mte.setLeftExpression(new Column(column.getName()));
                mte.setRightExpression(new JdbcParameter());
                struct.addParam(value);
                struct.setExpression(mte);
                break;
            case NOT_LESS:
                GreaterThanEquals gte = new GreaterThanEquals();
                gte.setLeftExpression(new Column(column.getName()));
                gte.setRightExpression(new JdbcParameter());
                struct.addParam(value);
                struct.setExpression(gte);
                break;
            case NULL:
                IsNullExpression isNull = new IsNullExpression();
                isNull.setLeftExpression(new Column(column.getName()));
                struct.setExpression(isNull);
                break;
            case START_WITH:
                LikeExpression startWith = new LikeExpression();
                startWith.setLeftExpression(new Column(column.getName()));
                startWith.setRightExpression(new JdbcParameter());
                struct.addParam(StringUtils.isEmpty(value) ? "%" : value.toString() + '%');
                struct.setExpression(startWith);
                break;
            case UNEQUAL:
                NotEqualsTo net = new NotEqualsTo();
                net.setLeftExpression(new Column(column.getName()));
                net.setRightExpression(new JdbcParameter());
                struct.addParam(value);
                struct.setExpression(net);
                break;
            default:
                throw new DataMapException(String.format("Operation '%s' is not supported.", pattern));
        }
        return struct;
    }

    public static void main(String[] args) throws JSQLParserException {
        Select stmt = (Select) CCJSqlParserUtil.parse(
                "SELECT col1 AS a, col2 AS b, col3 AS c FROM table WHERE col_1 = 10 AND col_2 = 20 AND (col_3 = 30 OR col_4 = 40) GROUP BY col1,col2,col3 ORDER BY col_1, col_2 DESC OFFSET 10 LIMIT 10");
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

        System.out.println(SelectUtils.buildSelectFromTableAndExpressions(new Table("mytable"), new Column("a"),
                new Column("b"), equalsTo));

        Statement insert = CCJSqlParserUtil.parse("INSERT INTO t_user " + "( id, name, age, create_date, is_delete) "
                + "VALUES (?, ?, ?, now(), false), (?, 'kehewei', ?, now(), false)");
        System.out.println(insert);

        Statement update = CCJSqlParserUtil.parse("update t_user set name = ?, age = ? where id = ?");
        System.out.println(update);

        Statement delete = CCJSqlParserUtil.parse("delete from t_user where " + "id = ? " + "or name in ( ?, ?) "
                + "or name LIKE ? " + "AND age >= 0 " + "AND age <= 18 " + "AND is_delete is false "
                + "AND create_date > ?" + "AND create_date is not null");
        System.out.println(delete);

        Delete d = new Delete();
        d.setTable(new Table("t_user"));
        d.setWhere(equalsTo);

        System.out.println(d);
    }

}
