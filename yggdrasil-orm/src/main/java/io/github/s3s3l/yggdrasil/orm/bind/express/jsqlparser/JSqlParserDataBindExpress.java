package io.github.s3s3l.yggdrasil.orm.bind.express.jsqlparser;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import io.github.s3s3l.yggdrasil.orm.bind.express.DataBindExpress;
import io.github.s3s3l.yggdrasil.orm.bind.express.ExpressBuilderType;
import io.github.s3s3l.yggdrasil.orm.bind.express.jsqlparser.builder.CreateBuilder;
import io.github.s3s3l.yggdrasil.orm.bind.express.jsqlparser.builder.DeleteBuilder;
import io.github.s3s3l.yggdrasil.orm.bind.express.jsqlparser.builder.InsertBuilder;
import io.github.s3s3l.yggdrasil.orm.bind.express.jsqlparser.builder.SelectBuilder;
import io.github.s3s3l.yggdrasil.orm.bind.express.jsqlparser.builder.UpdateBuilder;
import io.github.s3s3l.yggdrasil.orm.bind.express.jsqlparser.postgresql.ArrayValue;
import io.github.s3s3l.yggdrasil.orm.bind.sql.DefaultSqlStruct;
import io.github.s3s3l.yggdrasil.orm.bind.sql.JSqlParserSqlStruct;
import io.github.s3s3l.yggdrasil.orm.bind.sql.SqlStruct;
import io.github.s3s3l.yggdrasil.orm.enumerations.ComparePattern;
import io.github.s3s3l.yggdrasil.orm.exception.DataMapException;
import io.github.s3s3l.yggdrasil.orm.meta.ColumnMeta;
import io.github.s3s3l.yggdrasil.orm.meta.ConditionMeta;
import io.github.s3s3l.yggdrasil.orm.meta.GroupByMeta;
import io.github.s3s3l.yggdrasil.orm.meta.LimitMeta;
import io.github.s3s3l.yggdrasil.orm.meta.MetaManager;
import io.github.s3s3l.yggdrasil.orm.meta.OffsetMeta;
import io.github.s3s3l.yggdrasil.orm.meta.TableMeta;
import io.github.s3s3l.yggdrasil.utils.collection.CollectionUtils;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;
import io.github.s3s3l.yggdrasil.utils.reflect.PropertyDescriptorReflectionBean;
import io.github.s3s3l.yggdrasil.utils.reflect.ReflectionBean;
import io.github.s3s3l.yggdrasil.utils.verify.Verify;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.Function;
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
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.Offset;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.util.SelectUtils;

public abstract class JSqlParserDataBindExpress implements DataBindExpress {
    private final MetaManager metaManager;

    public JSqlParserDataBindExpress(MetaManager metaManager) {
        this.metaManager = metaManager;
    }

    @Override
    public ExpressBuilderType expressBuilderType() {
        return ExpressBuilderType.JSQLPARSER;
    }

    @Override
    public SqlStruct getInsert(List<?> models) {
        Verify.notEmpty(models);

        Class<?> modelType = models.get(0)
                .getClass();
        TableMeta table = this.metaManager.getTable(modelType);
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
    public SqlStruct getDelete(Object condition) {
        Class<?> conditionType = condition.getClass();
        DefaultSqlStruct sqlStruct = new DefaultSqlStruct();
        TableMeta table = this.metaManager.getTable(conditionType);
        JSqlParserSqlStruct whereStruct = buildWhere(this.metaManager.getDeleteCondition(conditionType),
                new PropertyDescriptorReflectionBean(condition));
        sqlStruct.appendSql(new DeleteBuilder().table(new Table(table.getName()))
                .where(whereStruct.getExpression())
                .build()
                .toString());
        sqlStruct.addParams(whereStruct.getParams());
        return sqlStruct;
    }

    @Override
    public SqlStruct getUpdate(Object source, Object condition) {
        Class<?> sourceType = source.getClass();
        Class<?> conditionType = condition.getClass();
        DefaultSqlStruct sqlStruct = new DefaultSqlStruct();
        TableMeta table = this.metaManager.getTable(sourceType);
        ReflectionBean rb = new PropertyDescriptorReflectionBean(source);
        JSqlParserSqlStruct whereStruct = buildWhere(this.metaManager.getUpdateCondition(conditionType),
                new PropertyDescriptorReflectionBean(condition));
        UpdateBuilder builder = new UpdateBuilder().table(new Table(table.getName()));
        for (ColumnMeta columnMeta : table.getColumns()) {
            String fieldName = columnMeta.getField()
                    .getName();
            Object fieldValue = rb.getFieldValue(fieldName);
            if (!rb.hasField(fieldName) || !columnMeta.getValidator()
                    .isValid(fieldValue)) {
                continue;
            }
            builder.addSet(new Column(columnMeta.getName()), new JdbcParameter());
            sqlStruct.addParam(fieldValue);
        }
        builder.where(whereStruct.getExpression());
        if (CollectionUtils.isNotEmpty(whereStruct.getParams())) {
            sqlStruct.addParams(whereStruct.getParams());
        }
        sqlStruct.appendSql(builder.build()
                .toString());
        return sqlStruct;
    }

    @Override
    public SqlStruct getSelect(Object condition) {
        return getSelect(condition, false);
    }

    @Override
    public SqlStruct getSelectCount(Object condition) {
        return getSelect(condition, true);
    }

    @Override
    public SqlStruct getCreate(Class<?> tableType, boolean force) {
        DefaultSqlStruct sqlStruct = new DefaultSqlStruct();
        TableMeta table = metaManager.getTable(tableType);
        sqlStruct.appendSql(new CreateBuilder(table, force).build()
                .toString());
        return sqlStruct;
    }

    @Override
    public SqlStruct getDrop(Class<?> tableType) {
        DefaultSqlStruct sqlStruct = new DefaultSqlStruct();
        TableMeta table = metaManager.getTable(tableType);
        Drop drop = new Drop();
        drop.setIfExists(true);
        drop.setType("TABLE");
        drop.setName(new Table(table.getName()));

        sqlStruct.appendSql(drop.toString());
        return sqlStruct;
    }

    private JSqlParserSqlStruct buildWhere(List<ConditionMeta> conditions, ReflectionBean rb) {
        JSqlParserSqlStruct whereStruct = new JSqlParserSqlStruct();

        if (CollectionUtils.isEmpty(conditions)) {
            return whereStruct;
        }

        JSqlParserSqlStruct sqlStruct = toExpression(conditions.get(0), rb);
        if (sqlStruct == null) {
            return whereStruct;
        }
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

    private SqlStruct getSelect(Object condition, boolean count) {
        Class<?> conditionType = condition.getClass();
        DefaultSqlStruct sqlStruct = new DefaultSqlStruct();
        ReflectionBean rb = new PropertyDescriptorReflectionBean(condition);
        TableMeta table = metaManager.getTable(conditionType);
        OffsetMeta offset = metaManager.getOffset(conditionType);
        LimitMeta limit = metaManager.getLimit(conditionType);
        GroupByMeta groupBy = metaManager.getGroupBy(conditionType);
        JSqlParserSqlStruct whereStruct = buildWhere(this.metaManager.getSelectCondition(conditionType), rb);

        boolean hasGroupBy = groupBy != null && !CollectionUtils.isEmpty(groupBy.getColumns());
        List<SelectItem> selectItems;
        if (count) {
            Function func = new Function();
            func.setName("COUNT");
            ExpressionList expressionList = new ExpressionList();
            expressionList.addExpressions(new AllColumns());
            func.setParameters(expressionList);
            SelectExpressionItem selectItem = new SelectExpressionItem(func);
            selectItems = Arrays.asList(selectItem);
        } else {
            selectItems = table.getColumns()
                    .stream()
                    .filter(col -> !hasGroupBy || groupBy.getColumns()
                            .contains(col.getName()))
                    .map(col -> {
                        SelectExpressionItem selectItem = new SelectExpressionItem(new Column(col.getName()));
                        selectItem.setAlias(new Alias(col.getAlias()));
                        return selectItem;
                    })
                    .collect(Collectors.toList());
        }

        SelectBuilder builder = new SelectBuilder().table(new Table(table.getName()))
                .selectItems(selectItems)
                .where(whereStruct.getExpression());

        if (count) {
            sqlStruct.appendSql(builder.build()
                    .toString());
            sqlStruct.addParams(whereStruct.getParams());

            return sqlStruct;
        }

        groupBy(groupBy, builder);

        orderBy(conditionType, builder);

        offsetLimit(rb, offset, limit, builder);

        sqlStruct.appendSql(builder.build()
                .toString());
        sqlStruct.addParams(whereStruct.getParams());

        return sqlStruct;
    }

    protected void orderBy(Class<?> conditionType, SelectBuilder builder) {
        builder.orderByElements(metaManager.getOrderBy(conditionType)
                .stream()
                .map(obm -> {
                    OrderByElement ob = new OrderByElement();
                    ob.setExpression(new Column(obm.getName()));
                    ob.setAsc(!obm.isDesc());
                    return ob;
                })
                .collect(Collectors.toList()));
    }

    protected void groupBy(GroupByMeta groupBy, SelectBuilder builder) {
        builder.groupByExpressions(groupBy.getColumns()
                .stream()
                .map(Column::new)
                .collect(Collectors.toList()));
    }

    protected void offsetLimit(ReflectionBean rb, OffsetMeta offset, LimitMeta limit, SelectBuilder builder) {
        if (offset != null) {
            Object offsetCount = rb.getFieldValue(offset.getField()
                    .getName());
            if (offsetCount != null) {
                Offset os = new Offset();
                os.setOffset(new LongValue((long) offsetCount));
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

        String fieldName = column.getName();
        if (StringUtils.isEmpty(fieldName)) {
            fieldName = field.getName();
        }
        switch (pattern) {
            case END_WITH:
                LikeExpression endWith = new LikeExpression();
                endWith.setLeftExpression(new Column(fieldName));
                endWith.setRightExpression(new JdbcParameter());
                struct.addParam(StringUtils.isEmpty(value) ? "%" : "%" + value.toString());
                struct.setExpression(endWith);
                break;
            case EQUAL:
                EqualsTo et = new EqualsTo();
                et.setLeftExpression(new Column(fieldName));
                et.setRightExpression(new JdbcParameter());
                struct.addParam(value);
                struct.setExpression(et);
                break;
            case IN:
                InExpression in = new InExpression();
                in.setLeftExpression(new Column(fieldName));
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
                gt.setLeftExpression(new Column(fieldName));
                gt.setRightExpression(new JdbcParameter());
                struct.addParam(value);
                struct.setExpression(gt);
                break;
            case LESS:
                MinorThan mt = new MinorThan();
                mt.setLeftExpression(new Column(fieldName));
                mt.setRightExpression(new JdbcParameter());
                struct.addParam(value);
                struct.setExpression(mt);
                break;
            case LIKE:
                LikeExpression like = new LikeExpression();
                like.setLeftExpression(new Column(fieldName));
                like.setRightExpression(new JdbcParameter());
                struct.addParam(value);
                struct.setExpression(like);
                break;
            case NON_NULL:
                IsNullExpression nonNull = new IsNullExpression();
                nonNull.setLeftExpression(new Column(fieldName));
                nonNull.setNot(true);
                struct.setExpression(nonNull);
                break;
            case NOT_LAGER:
                MinorThanEquals mte = new MinorThanEquals();
                mte.setLeftExpression(new Column(fieldName));
                mte.setRightExpression(new JdbcParameter());
                struct.addParam(value);
                struct.setExpression(mte);
                break;
            case NOT_LESS:
                GreaterThanEquals gte = new GreaterThanEquals();
                gte.setLeftExpression(new Column(fieldName));
                gte.setRightExpression(new JdbcParameter());
                struct.addParam(value);
                struct.setExpression(gte);
                break;
            case NULL:
                IsNullExpression isNull = new IsNullExpression();
                isNull.setLeftExpression(new Column(fieldName));
                struct.setExpression(isNull);
                break;
            case START_WITH:
                LikeExpression startWith = new LikeExpression();
                startWith.setLeftExpression(new Column(fieldName));
                startWith.setRightExpression(new JdbcParameter());
                struct.addParam(StringUtils.isEmpty(value) ? "%" : value.toString() + '%');
                struct.setExpression(startWith);
                break;
            case UNEQUAL:
                NotEqualsTo net = new NotEqualsTo();
                net.setLeftExpression(new Column(fieldName));
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

        Statement create = CCJSqlParserUtil.parse(
                "create table if not exists t_test(id varchar (64) primary key, name varchar (32) NOT NULL, sex int)");
        System.out.println(create);

        Statement select = CCJSqlParserUtil
                .parse("select count(*) from t_user where start > '2022-11-01' and end < '2022-11-21'");
        System.out.println(select);

        Statement drop = CCJSqlParserUtil.parse("DROP TABLE IF EXISTS t_user");
        System.out.println(drop);

        // HSQLDB
        Statement alter = CCJSqlParserUtil
                .parse("ALTER TABLE PUBLIC.T_USER ADD TEST VARCHAR(25) DEFAULT '123' NOT NULL");
        System.out.println(alter);

        Statement alterDrop = CCJSqlParserUtil.parse("ALTER TABLE PUBLIC.T_USER DROP COLUMN GOOGLEUSERID");
        System.out.println(alterDrop);

        Statement alterChange = CCJSqlParserUtil.parse("ALTER TABLE PUBLIC.T_USER ALTER COLUMN GOOGLEUSERID BIGINT");
        System.out.println(alterChange);

        // MYSQL
        alter = CCJSqlParserUtil.parse(
                "ALTER TABLE `market`.`t_user` ADD COLUMN `test1` VARCHAR(45) NOT NULL DEFAULT 'test' AFTER `securetKey`;");
        System.out.println(alter);

        alterDrop = CCJSqlParserUtil.parse("ALTER TABLE PUBLIC.T_USER DROP COLUMN GOOGLEUSERID");
        System.out.println(alterDrop);

        alterChange = CCJSqlParserUtil.parse(
                "ALTER TABLE `market`.`t_user` CHANGE COLUMN `securetKey` `securetKey` VARCHAR(128) NOT NULL DEFAULT 'test' ;");
        System.out.println(alterChange);

        // Postgresql
        alter = CCJSqlParserUtil.parse(
                "ALTER TABLE IF EXISTS public.t_test ADD COLUMN test character varying(64) NOT NULL DEFAULT 'test';");
        System.out.println(alter);

        alterDrop = CCJSqlParserUtil.parse("ALTER TABLE IF EXISTS public.t_test DROP COLUMN IF EXISTS createtime;");
        System.out.println(alterDrop);

        alterChange = CCJSqlParserUtil.parse("ALTER TABLE IF EXISTS public.t_test ALTER COLUMN createtime TYPE date;");
        System.out.println(alterChange);
    }

}
