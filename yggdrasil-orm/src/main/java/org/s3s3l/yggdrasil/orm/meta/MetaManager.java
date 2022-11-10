package org.s3s3l.yggdrasil.orm.meta;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.s3s3l.yggdrasil.bean.exception.ResourceNotFoundException;
import org.s3s3l.yggdrasil.orm.bind.annotation.Column;
import org.s3s3l.yggdrasil.orm.bind.annotation.Condition;
import org.s3s3l.yggdrasil.orm.bind.annotation.GroupBy;
import org.s3s3l.yggdrasil.orm.bind.annotation.Limit;
import org.s3s3l.yggdrasil.orm.bind.annotation.Offset;
import org.s3s3l.yggdrasil.orm.bind.annotation.OrderBy;
import org.s3s3l.yggdrasil.orm.bind.annotation.SqlModel;
import org.s3s3l.yggdrasil.orm.bind.annotation.TableDefine;
import org.s3s3l.yggdrasil.orm.exception.DataBindExpressException;
import org.s3s3l.yggdrasil.orm.handler.TypeHandlerManager;
import org.s3s3l.yggdrasil.orm.validator.DefaultValidatorFactory;
import org.s3s3l.yggdrasil.orm.validator.ValidatorFactory;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.reflect.ReflectionUtils;
import org.s3s3l.yggdrasil.utils.reflect.scan.ClassScanner;
import org.s3s3l.yggdrasil.utils.reflect.scan.Scanner;
import org.s3s3l.yggdrasil.utils.verify.Verify;

import com.google.common.base.VerifyException;

import lombok.Getter;

public class MetaManager {
    private final String[] packages;
    private final Scanner scanner = new ClassScanner();
    @Getter
    private final ValidatorFactory validatorFactory;
    @Getter
    private final TypeHandlerManager typeHandlerManager;

    private Map<Class<?>, Map<String, String>> aliasMap = new ConcurrentHashMap<>();
    private Map<Class<?>, TableMeta> tables = new ConcurrentHashMap<>();
    private Map<Class<?>, ConditionContext> condition = new ConcurrentHashMap<>();

    public MetaManager(String... packages) {
        this(new DefaultValidatorFactory(), new TypeHandlerManager(), packages);
    }

    public MetaManager(ValidatorFactory validatorFactory, TypeHandlerManager typeHandlerManager, String... packages) {
        this.packages = packages;
        this.validatorFactory = validatorFactory;
        this.typeHandlerManager = typeHandlerManager;
        refresh();
    }

    public boolean isResolved(Class<?> type) {
        return this.tables.containsKey(type);
    }

    public TableMeta resolve(Class<?> type) {
        return tables.computeIfAbsent(type, key -> {
            if (ReflectionUtils.isAnnotationedWith(type, TableDefine.class)) {
                return resolveTableDefine(ReflectionUtils.getAnnotation(type, TableDefine.class), type);
            } else if (ReflectionUtils.isAnnotationedWith(type, SqlModel.class)) {
                return resolveSqlModel(ReflectionUtils.getAnnotation(type, SqlModel.class), type);
            }
            throw new DataBindExpressException(
                    "No 'TableDefine' or 'SqlModel' annotation found for type: " + type.getName());
        });

    }

    private synchronized void refresh() {
        Set<Class<?>> types = scanner.scan(packages);
        types.stream().filter(type -> ReflectionUtils.isAnnotationedWith(type,
                TableDefine.class)
                || ReflectionUtils.isAnnotationedWith(type,
                        SqlModel.class))
                .forEach(this::resolve);
    }

    private TableMeta resolveTableDefine(TableDefine tableDefine, Class<?> type) {
        TableMeta table = TableMeta.builder()
                .name(tableDefine.table())
                .build();
        resolveType(type, table, true);

        return table;
    }

    private TableMeta resolveSqlModel(SqlModel sqlModel, Class<?> type) {
        TableMeta table = resolve(sqlModel.table());
        if (table == null) {
            throw new ResourceNotFoundException("table for type '" + type.getName() + "' not found.");
        }

        resolveType(type, table, false);

        return table;
    }

    private void resolveType(Class<?> type, TableMeta table, boolean newTable) {
        List<ColumnMeta> columns = new LinkedList<>();
        List<ConditionMeta> selectConditions = new LinkedList<>();
        List<ConditionMeta> updateConditions = new LinkedList<>();
        List<ConditionMeta> deleteConditions = new LinkedList<>();
        Set<String> groupBySet = new HashSet<>();
        List<OrderByMeta> orderByMetas = new LinkedList<>();
        Map<String, String> aliasMap = new HashMap<>();

        ConditionContext conditionContext = this.condition.computeIfAbsent(type, t -> new ConditionContext());

        for (Field field : ReflectionUtils.getFields(type)) {
            String columnName = field.getName();
            Class<?> fieldType = field.getType();

            Column column = field.getAnnotation(Column.class);

            ColumnMeta columnMeta = null;

            if (column != null) {
                columnName = StringUtils.isEmpty(column.name()) ? field.getName() : column.name();
                org.s3s3l.yggdrasil.orm.bind.annotation.DatabaseType dbType = column.dbType();
                columnMeta = ColumnMeta.builder()
                        .field(field)
                        .name(columnName)
                        .alias(field.getName())
                        .validator(this.validatorFactory.getValidator(column.validator()))
                        .typeHandler(typeHandlerManager.getOrNew(column.typeHandler()))
                        .dbType(DatabaseType.builder()
                                .type(dbType.type())
                                .args(Arrays.asList(dbType.args()))
                                .primary(dbType.primary())
                                .notNull(dbType.notNull())
                                .build())
                        .build();
                columns.add(columnMeta);
                aliasMap.put(columnName.toUpperCase(), field.getName());
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
                    selectConditions.add(conditionMeta);
                }

                if (condition.forDelete()) {
                    deleteConditions.add(conditionMeta);
                }

                if (condition.forUpdate()) {
                    updateConditions.add(conditionMeta);
                }
            }

            GroupBy gb = field.getAnnotation(GroupBy.class);

            if (gb != null) {
                groupBySet.add(columnName);
            }

            OrderBy ob = field.getAnnotation(OrderBy.class);
            if (ob != null) {
                orderByMetas.add(OrderByMeta.builder()
                        .name(columnName)
                        .desc(ob.desc())
                        .build());
            }

            Offset o = field.getAnnotation(Offset.class);

            if (o != null) {
                if (!long.class.isAssignableFrom(fieldType)) {
                    throw new VerifyException("@Offset can only mark on 'Long' type field.");
                }
                conditionContext.setOffset(OffsetMeta.builder()
                        .field(field)
                        .build());
            }

            Limit l = field.getAnnotation(Limit.class);

            if (l != null) {
                if (!long.class.isAssignableFrom(fieldType)) {
                    throw new VerifyException("@Limit can only mark on 'Long' type field.");
                }
                conditionContext.setLimit(LimitMeta.builder()
                        .field(field)
                        .build());
            }
        }

        if (newTable) {
            table.setColumns(columns);
        }
        this.aliasMap.put(type, aliasMap);
        conditionContext.setSelectConditions(selectConditions);
        conditionContext.setUpdateConditions(updateConditions);
        conditionContext.setDeleteConditions(deleteConditions);
        conditionContext.setGroupBy(GroupByMeta.builder()
                .columns(groupBySet)
                .build());
        conditionContext.setOrderBy(orderByMetas);
    }

    public TableMeta getTable(Class<?> type) {
        return this.tables.get(type);
    }

    public List<ConditionMeta> getSelectCondition(Class<?> type) {
        return this.condition.get(type)
                .getSelectConditions();
    }

    public List<ConditionMeta> getUpdateCondition(Class<?> type) {
        return this.condition.get(type)
                .getUpdateConditions();
    }

    public List<ConditionMeta> getDeleteCondition(Class<?> type) {
        return this.condition.get(type)
                .getDeleteConditions();
    }

    public GroupByMeta getGroupBy(Class<?> type) {
        return this.condition.get(type)
                .getGroupBy();
    }

    public List<OrderByMeta> getOrderBy(Class<?> type) {
        return this.condition.get(type)
                .getOrderBy();
    }

    public OffsetMeta getOffset(Class<?> type) {
        return this.condition.get(type)
                .getOffset();
    }

    public LimitMeta getLimit(Class<?> type) {
        return this.condition.get(type)
                .getLimit();
    }

    public String getAlias(Class<?> type, String columnName) {
        Verify.hasText(columnName);
        return this.aliasMap.get(type)
                .getOrDefault(columnName.toUpperCase(), columnName);
    }
}
