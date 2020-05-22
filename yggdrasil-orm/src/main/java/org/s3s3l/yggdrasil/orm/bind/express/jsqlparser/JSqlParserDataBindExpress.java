package org.s3s3l.yggdrasil.orm.bind.express.jsqlparser;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.s3s3l.yggdrasil.orm.bind.annotation.Condition;
import org.s3s3l.yggdrasil.orm.bind.annotation.TableDefine;
import org.s3s3l.yggdrasil.orm.bind.express.DataBindExpress;
import org.s3s3l.yggdrasil.orm.bind.express.jsqlparser.builder.DeleteBuilder;
import org.s3s3l.yggdrasil.orm.bind.express.jsqlparser.builder.InsertBuilder;
import org.s3s3l.yggdrasil.orm.bind.express.jsqlparser.postgresql.ArrayValue;
import org.s3s3l.yggdrasil.orm.bind.sql.DefaultSqlStruct;
import org.s3s3l.yggdrasil.orm.bind.sql.JSqlParserSqlStruct;
import org.s3s3l.yggdrasil.orm.bind.sql.SqlStruct;
import org.s3s3l.yggdrasil.orm.enumerations.ComparePattern;
import org.s3s3l.yggdrasil.orm.exception.DataBindExpressException;
import org.s3s3l.yggdrasil.orm.exception.DataMapException;
import org.s3s3l.yggdrasil.orm.handler.TypeHandlerManager;
import org.s3s3l.yggdrasil.orm.meta.ColumnMeta;
import org.s3s3l.yggdrasil.orm.meta.ConditionMeta;
import org.s3s3l.yggdrasil.orm.validator.ValidatorFactory;
import org.s3s3l.yggdrasil.utils.collection.CollectionUtils;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.reflect.PropertyDescriptorReflectionBean;
import org.s3s3l.yggdrasil.utils.reflect.ReflectionBean;
import org.s3s3l.yggdrasil.utils.reflect.ReflectionUtils;
import org.s3s3l.yggdrasil.utils.verify.Verify;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.JdbcParameter;
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
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.SelectUtils;

public class JSqlParserDataBindExpress implements DataBindExpress {
    private final ValidatorFactory validatorFactory;
    private final TypeHandlerManager typeHandlerManager;
    private final List<ColumnMeta> columns = new LinkedList<>();
    private final List<ConditionMeta> selectConditions = new LinkedList<>();
    private final List<ConditionMeta> deleteConditions = new LinkedList<>();
    private final List<ConditionMeta> updateConditions = new LinkedList<>();

    private Table table;
    private Map<String, String> nameMap = new ConcurrentHashMap<>();

    public JSqlParserDataBindExpress(Class<?> modelType, ValidatorFactory validatorFactory,
            TypeHandlerManager typeHandlerManager) {
        this.validatorFactory = validatorFactory;
        this.typeHandlerManager = typeHandlerManager;
        express(modelType);
    }

    @Override
    public DataBindExpress express(Class<?> modelType) {
        Verify.notNull(modelType);
        if (!modelType.isAnnotationPresent(TableDefine.class)) {
            throw new DataBindExpressException("No 'SqlModel' annotation found.");
        }

        TableDefine sqlModel = modelType.getAnnotation(TableDefine.class);

        if (StringUtils.isEmpty(sqlModel.table())) {
            throw new DataBindExpressException("Table name can`t be empty.");
        }

        this.table = new Table(sqlModel.table());

        for (Field field : ReflectionUtils.getFields(modelType)) {

            org.s3s3l.yggdrasil.orm.bind.annotation.Column column = field
                    .getAnnotation(org.s3s3l.yggdrasil.orm.bind.annotation.Column.class);

            ColumnMeta columnMeta = null;

            if (column != null) {
                columnMeta = ColumnMeta.builder()
                        .field(field)
                        .name(StringUtils.isEmpty(column.name()) ? field.getName() : column.name())
                        .alias(field.getName())
                        .validator(this.validatorFactory.getValidator(column.validator()))
                        .typeHandler(typeHandlerManager.getOrNew(column.typeHandler()))
                        .build();
                this.columns.add(columnMeta);
            }

            Condition condition = field.getAnnotation(Condition.class);

            if (condition != null) {
                if (columnMeta == null) {
                    columnMeta = ColumnMeta.builder()
                            .name(condition.column())
                            .build();
                }

                ConditionMeta conditionMeta = ConditionMeta.builder()
                        .field(field)
                        .column(columnMeta)
                        .pattern(condition.pattern())
                        .validator(this.validatorFactory.getValidator(condition.validator()))
                        .build();
                if (condition.forSelect()) {
                    this.selectConditions.add(conditionMeta);
                }

                if (condition.forDelete()) {
                    this.deleteConditions.add(conditionMeta);
                }

                if (condition.forUpdate()) {
                    this.updateConditions.add(conditionMeta);
                }
            }
        }
        return this;
    }

    @Override
    public String getAlias(String name) {
        return this.nameMap.get(name);
    }

    @Override
    public SqlStruct getInsert(List<?> models) {
        DefaultSqlStruct sqlStruct = new DefaultSqlStruct();
        List<Column> insertColumns = new LinkedList<>();
        List<ExpressionList> expressionLists = new LinkedList<>();
        for (ColumnMeta column : columns) {
            insertColumns.add(new Column(column.getName()));
        }

        for (Object model : models) {
            ReflectionBean rb = new PropertyDescriptorReflectionBean(model);
            List<Expression> expressions = new LinkedList<>();
            for (ColumnMeta column : columns) {
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
        sqlStruct.appendSql(new InsertBuilder().table(this.table)
                .columns(insertColumns)
                .itemsLists(expressionLists)
                .build()
                .toString());
        return sqlStruct;
    }

    @Override
    public SqlStruct getDelete(Object model) {
        DefaultSqlStruct sqlStruct = new DefaultSqlStruct();
        JSqlParserSqlStruct whereStruct = buildWhere(this.deleteConditions,
                new PropertyDescriptorReflectionBean(model));
        sqlStruct.appendSql(new DeleteBuilder().table(this.table)
                .where(whereStruct.getExpression())
                .build()
                .toString());
        sqlStruct.addParams(whereStruct.getParams());
        return sqlStruct;
    }

    @Override
    public SqlStruct getUpdate(Object model) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SqlStruct getSelect(Object model) {
        // Select select = SelectUtils.buildSelectFromTable(table)
        // TODO Auto-generated method stub
        return null;
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
