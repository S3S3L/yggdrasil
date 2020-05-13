package org.s3s3l.yggdrasil.orm.bind.express.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.s3s3l.yggdrasil.orm.bind.ColumnStruct;
import org.s3s3l.yggdrasil.orm.bind.SqlStruct;
import org.s3s3l.yggdrasil.orm.bind.Table;
import org.s3s3l.yggdrasil.orm.bind.annotation.Column;
import org.s3s3l.yggdrasil.orm.bind.annotation.Condition;
import org.s3s3l.yggdrasil.orm.bind.annotation.SqlModel;
import org.s3s3l.yggdrasil.orm.bind.condition.ConditionStruct;
import org.s3s3l.yggdrasil.orm.bind.condition.RawConditionNode;
import org.s3s3l.yggdrasil.orm.bind.express.DataBindExpress;
import org.s3s3l.yggdrasil.orm.bind.select.SelectStruct;
import org.s3s3l.yggdrasil.orm.bind.set.SetNode;
import org.s3s3l.yggdrasil.orm.bind.set.SetStruct;
import org.s3s3l.yggdrasil.orm.bind.value.ValuesStruct;
import org.s3s3l.yggdrasil.orm.exception.DataBindExpressException;
import org.s3s3l.yggdrasil.orm.meta.ColumnMeta;
import org.s3s3l.yggdrasil.orm.validator.ValidatorFactory;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.reflect.PropertyDescriptorReflectionBean;
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

    public DefaultDataBindExpress(Class<?> modelType, ValidatorFactory validatorFactory) {
        this.validatorFactory = validatorFactory;
        express(modelType);
    }

    @Override
    public String getAlias(String name) {
        return this.select.getAlias(name);
    }

    @Override
    public synchronized DataBindExpress express(Class<?> modelType) {
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

            ColumnStruct columnStruct;
            try {
                columnStruct = ColumnStruct.builder()
                        .meta(ColumnMeta.builder()
                                .field(field)
                                .name(StringUtils.isEmpty(column.name()) ? field.getName() : column.name())
                                .alias(field.getName())
                                .validator(this.validatorFactory.getValidator(column.validator()))
                                .typeHandler(column.typeHandler()
                                        .newInstance())
                                .build())
                        .build();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            colums.add(columnStruct);
            this.select.addNode(columnStruct);
            this.values.addNode(columnStruct.getMeta());

            if (!column.isPrimary()) {
                this.set.addNode(new SetNode(columnStruct));
            }

            if (field.isAnnotationPresent(Condition.class)) {
                Condition condition = field.getAnnotation(Condition.class);
                RawConditionNode conditionNode = new RawConditionNode(columnStruct);
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
    }

    @Override
    public SqlStruct getInsert(List<?> models) {
        Verify.notEmpty(models);

        SqlStruct struct = new SqlStruct();

        AtomicBoolean first = new AtomicBoolean(true);
        List<SqlStruct> valueStructs = models.stream()
                .map(r -> this.values.toSqlStruct(new PropertyDescriptorReflectionBean(r), first.getAndSet(false)))
                .collect(Collectors.toList());

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

        SqlStruct deleteStruct = this.deleteCondtion.toSqlStruct(new PropertyDescriptorReflectionBean(model));

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

        PropertyDescriptorReflectionBean ref = new PropertyDescriptorReflectionBean(model);
        SqlStruct setStruct = this.set.toSqlStruct(ref);
        SqlStruct conditionStruct = this.updateCondtion.toSqlStruct(ref);

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

        PropertyDescriptorReflectionBean ref = new PropertyDescriptorReflectionBean(model);
        SqlStruct selectStruct = this.select.toSqlStruct(ref);
        SqlStruct conditionStruct = this.selectCondition.toSqlStruct(ref);

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
