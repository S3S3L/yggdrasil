package org.s3s3l.yggdrasil.orm.meta;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.google.common.base.VerifyException;

import org.s3s3l.yggdrasil.bean.exception.ResourceNotFoundException;
import org.s3s3l.yggdrasil.orm.bind.annotation.Column;
import org.s3s3l.yggdrasil.orm.bind.annotation.Condition;
import org.s3s3l.yggdrasil.orm.bind.annotation.GroupBy;
import org.s3s3l.yggdrasil.orm.bind.annotation.Limit;
import org.s3s3l.yggdrasil.orm.bind.annotation.Offset;
import org.s3s3l.yggdrasil.orm.bind.annotation.OrderBy;
import org.s3s3l.yggdrasil.orm.bind.annotation.SqlModel;
import org.s3s3l.yggdrasil.orm.bind.annotation.TableDefine;
import org.s3s3l.yggdrasil.orm.handler.TypeHandlerManager;
import org.s3s3l.yggdrasil.orm.validator.DefaultValidatorFactory;
import org.s3s3l.yggdrasil.orm.validator.ValidatorFactory;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.reflect.ReflectionUtils;
import org.s3s3l.yggdrasil.utils.reflect.scan.ClassScanner;
import org.s3s3l.yggdrasil.utils.reflect.scan.Scanner;

import lombok.Getter;

public class MetaManager {
    private final String[] packages;
    private final Scanner scanner = new ClassScanner();
    @Getter
    private final ValidatorFactory validatorFactory;
    @Getter
    private final TypeHandlerManager typeHandlerManager;
    private final Lock refreshLock = new ReentrantLock();

    private Set<Class<?>> types = new HashSet<>();
    private Map<Class<?>, TableMeta> tables = new ConcurrentHashMap<>();
    private Map<Class<?>, List<ConditionMeta>> selectConditions = new ConcurrentHashMap<>();
    private Map<Class<?>, List<ConditionMeta>> updateConditions = new ConcurrentHashMap<>();
    private Map<Class<?>, List<ConditionMeta>> deleteConditions = new ConcurrentHashMap<>();
    private Map<Class<?>, GroupByMeta> groupBy = new ConcurrentHashMap<>();
    private Map<Class<?>, List<OrderByMeta>> orderBy = new ConcurrentHashMap<>();
    private Map<Class<?>, OffsetMeta> offset = new ConcurrentHashMap<>();
    private Map<Class<?>, LimitMeta> limit = new ConcurrentHashMap<>();

    public MetaManager(String... packages) {
        this(new DefaultValidatorFactory(), new TypeHandlerManager(), packages);
    }

    public MetaManager(ValidatorFactory validatorFactory, TypeHandlerManager typeHandlerManager, String... packages) {
        this.packages = packages;
        this.validatorFactory = validatorFactory;
        this.typeHandlerManager = typeHandlerManager;
    }

    public void refresh() {
        refreshLock.lock();
        try {
            Set<Class<?>> types = scanner.scan(packages);
            types.parallelStream()
                    .filter(type -> ReflectionUtils.isAnnotationedWith(type, TableDefine.class))
                    .forEach(type -> resolveTableDefine(ReflectionUtils.getAnnotation(type, TableDefine.class), type));
            types.parallelStream()
                    .filter(type -> ReflectionUtils.isAnnotationedWith(type, SqlModel.class))
                    .forEach(type -> resolveSqlModel(ReflectionUtils.getAnnotation(type, SqlModel.class), type));
        } finally {
            refreshLock.unlock();
        }
    }

    public boolean isResolved(Class<?> type) {
        return this.types.contains(type);
    }

    private void resolveTableDefine(TableDefine tableDefine, Class<?> type) {
        TableMeta table = TableMeta.builder()
                .name(tableDefine.table())
                .build();
        resolveType(type, table, true);
    }

    private void resolveSqlModel(SqlModel sqlModel, Class<?> type) {
        TableMeta table = this.tables.get(sqlModel.table());
        if (table == null) {
            throw new ResourceNotFoundException("table for type '" + type.getName() + "' not found.");
        }

        resolveType(type, table, false);
    }

    private void resolveType(Class<?> type, TableMeta table, boolean newTable) {
        this.types.add(type);
        List<ColumnMeta> columns = new LinkedList<>();
        List<ConditionMeta> selectConditions = new LinkedList<>();
        List<ConditionMeta> updateConditions = new LinkedList<>();
        List<ConditionMeta> deleteConditions = new LinkedList<>();
        Set<String> groupBySet = new HashSet<>();
        List<OrderByMeta> orderByMetas = new LinkedList<>();

        for (Field field : ReflectionUtils.getFields(type)) {
            String columnName = field.getName();
            Class<?> fieldType = field.getType();

            Column column = field.getAnnotation(Column.class);

            ColumnMeta columnMeta = null;

            if (column != null) {
                columnName = StringUtils.isEmpty(column.name()) ? field.getName() : column.name();
                columnMeta = ColumnMeta.builder()
                        .field(field)
                        .name(columnName)
                        .alias(field.getName())
                        .validator(this.validatorFactory.getValidator(column.validator()))
                        .typeHandler(typeHandlerManager.getOrNew(column.typeHandler()))
                        .build();
                columns.add(columnMeta);
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
                offset.put(type, OffsetMeta.builder()
                        .field(field)
                        .build());
            }

            Limit l = field.getAnnotation(Limit.class);

            if (l != null) {
                if (!long.class.isAssignableFrom(fieldType)) {
                    throw new VerifyException("@Limit can only mark on 'Long' type field.");
                }
                limit.put(type, LimitMeta.builder()
                        .field(field)
                        .build());
            }
        }

        if (newTable) {
            table.setColumns(columns);
        }
        this.tables.put(type, table);
        this.selectConditions.put(type, selectConditions);
        this.updateConditions.put(type, updateConditions);
        this.deleteConditions.put(type, deleteConditions);
        this.groupBy.put(type, GroupByMeta.builder()
                .columns(groupBySet)
                .build());
        this.orderBy.put(type, orderByMetas);
    }

    public TableMeta getTable(Class<?> type) {
        return this.tables.get(type);
    }

    public List<ConditionMeta> getSelectCondition(Class<?> type) {
        return this.selectConditions.get(type);
    }

    public List<ConditionMeta> getUpdateCondition(Class<?> type) {
        return this.updateConditions.get(type);
    }

    public List<ConditionMeta> getDeleteCondition(Class<?> type) {
        return this.deleteConditions.get(type);
    }

    public GroupByMeta getGroupBy(Class<?> type) {
        return this.groupBy.get(type);
    }

    public List<OrderByMeta> getOrderBy(Class<?> type) {
        return this.orderBy.get(type);
    }

    public OffsetMeta getOffset(Class<?> type) {
        return this.offset.get(type);
    }

    public LimitMeta getLimit(Class<?> type) {
        return this.limit.get(type);
    }
}
