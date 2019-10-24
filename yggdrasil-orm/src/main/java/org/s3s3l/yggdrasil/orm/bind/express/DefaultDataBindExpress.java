package org.s3s3l.yggdrasil.orm.bind.express;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.s3s3l.yggdrasil.orm.bind.ColumnStruct;
import org.s3s3l.yggdrasil.orm.bind.ConditionNode;
import org.s3s3l.yggdrasil.orm.bind.ConditionStruct;
import org.s3s3l.yggdrasil.orm.bind.SelectStruct;
import org.s3s3l.yggdrasil.orm.bind.SetNode;
import org.s3s3l.yggdrasil.orm.bind.SetStruct;
import org.s3s3l.yggdrasil.orm.bind.SqlStruct;
import org.s3s3l.yggdrasil.orm.bind.Table;
import org.s3s3l.yggdrasil.orm.bind.ValuesStruct;
import org.s3s3l.yggdrasil.orm.bind.annotation.Column;
import org.s3s3l.yggdrasil.orm.bind.annotation.Condition;
import org.s3s3l.yggdrasil.orm.bind.annotation.SqlModel;
import org.s3s3l.yggdrasil.orm.exception.DataBindExpressException;
import org.s3s3l.yggdrasil.orm.validator.ValidatorFactory;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.reflect.Reflection;
import org.s3s3l.yggdrasil.utils.reflect.ReflectionUtils;
import org.s3s3l.yggdrasil.utils.verify.Verify;

/**
 * 
 * <p>
 * </p>
 * ClassName: DefaultDataBindExpress <br>
 * date: Sep 20, 2019 11:27:43 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class DefaultDataBindExpress implements DataBindExpress {

    private ConditionStruct selectCondition = new ConditionStruct();
    private ConditionStruct updateCondtion = new ConditionStruct();
    private ConditionStruct deleteCondtion = new ConditionStruct();
    private SelectStruct select = new SelectStruct();
    private SetStruct set = new SetStruct();
    private ValuesStruct values = new ValuesStruct();
    private Table table = new Table();
    private ValidatorFactory validatorFactory;
    private final Lock expressBindLock = new ReentrantLock();

    public DefaultDataBindExpress(Class<?> modelType, ValidatorFactory validatorFactory) {
        this.validatorFactory = validatorFactory;
        express(modelType);
    }

    @Override
    public String getAlias(String name) {
        return this.select.getAlias(name);
    }

    @Override
    public DataBindExpress express(Class<?> modelType) {
        expressBindLock.lock();
        try {
            Verify.notNull(modelType);
            if (!modelType.isAnnotationPresent(SqlModel.class)) {
                throw new DataBindExpressException("No 'SqlModel' annotation found.");
            }

            SqlModel sqlModel = modelType.getAnnotation(SqlModel.class);

            if (StringUtils.isEmpty(sqlModel.table())) {
                throw new DataBindExpressException("Table name can`t be empty.");
            }

            this.table.setName(sqlModel.table());

            List<ColumnStruct> colums = new ArrayList<>();

            for (Field field : ReflectionUtils.getFields(modelType)) {
                if (!field.isAnnotationPresent(Column.class)) {
                    continue;
                }

                Column column = field.getAnnotation(Column.class);

                ColumnStruct columnStruct = new ColumnStruct();
                columnStruct.setField(field);
                columnStruct.setName(StringUtils.isEmpty(column.name()) ? field.getName() : column.name());
                columnStruct.setAlias(field.getName());
                columnStruct.setValidator(this.validatorFactory.getValidator(column.validator()));
                try {
                    columnStruct.setTypeHandler(column.typeHandler()
                            .newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

                colums.add(columnStruct);
                this.select.addNode(columnStruct);
                this.values.addNode(columnStruct);

                if (!column.isPrimary()) {
                    this.set.addNode(new SetNode(columnStruct));
                }

                if (field.isAnnotationPresent(Condition.class)) {
                    Condition condition = field.getAnnotation(Condition.class);
                    ConditionNode conditionNode = new ConditionNode(columnStruct);
                    conditionNode.setPattern(condition.pattern());
                    if (condition.forSelect()) {
                        this.selectCondition.addNode(conditionNode);
                    }
                    if (condition.forUpdate()) {
                        this.updateCondtion.addNode(conditionNode);
                    }
                    if (condition.forDelete()) {
                        this.deleteCondtion.addNode(conditionNode);
                    }
                }
            }

            return this;
        } finally {
            expressBindLock.unlock();
        }
    }

    @Override
    public SqlStruct getInsert(List<?> models) {
        Verify.notEmpty(models);

        SqlStruct struct = new SqlStruct();

        AtomicBoolean first = new AtomicBoolean(true);
        List<SqlStruct> valueStructs = models.stream()
                .map(r -> this.values.toSqlStruct(Reflection.create(r), first.getAndSet(false)))
                .collect(Collectors.toList());

        if (valueStructs == null) {
            return null;
        }

        struct.setSql("INSERT INTO ");
        struct.appendSql(this.table.getName());
        valueStructs.forEach(r -> {
            struct.appendSql(r.getSql());
            struct.addParams(r.getParams());
        });

        return struct;
    }

    @Override
    public SqlStruct getDelete(Object model) {
        Verify.notNull(model);

        SqlStruct struct = new SqlStruct();

        SqlStruct deleteStruct = this.deleteCondtion.toSqlStruct(Reflection.create(model));

        struct.setSql("DELETE FROM ");
        struct.appendSql(this.table.getName());

        if (deleteStruct != null) {
            struct.appendSql(deleteStruct.getSql());
            struct.addParams(deleteStruct.getParams());
        }
        return struct;
    }

    @Override
    public SqlStruct getUpdate(Object model) {
        Verify.notNull(model);

        SqlStruct struct = new SqlStruct();

        SqlStruct setStruct = this.set.toSqlStruct(Reflection.create(model));
        SqlStruct conditionStruct = this.updateCondtion.toSqlStruct(Reflection.create(model));

        if (setStruct == null) {
            return null;
        }

        struct.setSql("UPDATE ");
        struct.appendSql(this.table.getName())
                .appendSql(setStruct.getSql())
                .appendSql(conditionStruct.getSql());
        struct.addParams(setStruct.getParams())
                .addParams(conditionStruct.getParams());
        return struct;
    }

    @Override
    public SqlStruct getSelect(Object model) {
        Verify.notNull(model);

        SqlStruct struct = new SqlStruct();

        SqlStruct selectStruct = this.select.toSqlStruct(Reflection.create(model));
        SqlStruct conditionStruct = this.selectCondition.toSqlStruct(Reflection.create(model));

        if (selectStruct == null) {
            return null;
        }

        struct.setSql("SELECT ");
        struct.appendSql(selectStruct.getSql())
                .appendSql(" FROM ")
                .appendSql(this.table.getName());
        if (conditionStruct != null) {
            struct.appendSql(conditionStruct.getSql());
            struct.addParams(conditionStruct.getParams());
        }
        return struct;
    }
}
