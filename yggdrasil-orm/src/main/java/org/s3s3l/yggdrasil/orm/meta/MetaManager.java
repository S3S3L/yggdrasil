package org.s3s3l.yggdrasil.orm.meta;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.s3s3l.yggdrasil.bean.exception.ResourceNotFoundException;
import org.s3s3l.yggdrasil.orm.bind.annotation.Column;
import org.s3s3l.yggdrasil.orm.bind.annotation.Condition;
import org.s3s3l.yggdrasil.orm.bind.annotation.ExecutorProxy;
import org.s3s3l.yggdrasil.orm.bind.annotation.GroupBy;
import org.s3s3l.yggdrasil.orm.bind.annotation.Limit;
import org.s3s3l.yggdrasil.orm.bind.annotation.Offset;
import org.s3s3l.yggdrasil.orm.bind.annotation.OrderBy;
import org.s3s3l.yggdrasil.orm.bind.annotation.SqlModel;
import org.s3s3l.yggdrasil.orm.bind.annotation.TableDefine;
import org.s3s3l.yggdrasil.orm.exception.DataBindExpressException;
import org.s3s3l.yggdrasil.orm.exception.MetaManagerGenerateException;
import org.s3s3l.yggdrasil.orm.handler.TypeHandlerManager;
import org.s3s3l.yggdrasil.orm.proxy.config.ProxyConfig;
import org.s3s3l.yggdrasil.orm.proxy.config.ProxyMethod;
import org.s3s3l.yggdrasil.orm.proxy.meta.ProxyMeta;
import org.s3s3l.yggdrasil.orm.proxy.meta.ProxyMethodMeta;
import org.s3s3l.yggdrasil.orm.validator.ValidatorFactory;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.file.FileUtils;
import org.s3s3l.yggdrasil.utils.reflect.ReflectionUtils;
import org.s3s3l.yggdrasil.utils.reflect.scan.Scanner;
import org.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;
import org.s3s3l.yggdrasil.utils.verify.CommonVerifier;
import org.s3s3l.yggdrasil.utils.verify.Verifier;
import org.s3s3l.yggdrasil.utils.verify.Verify;
import org.yaml.snakeyaml.Yaml;

import com.google.common.base.VerifyException;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MetaManager {
    private final Verifier verifier = new CommonVerifier();
    private final Yaml yaml = new Yaml();

    private final String[] tableDefinePackages;
    private final String[] proxyDefinePackages;
    private final String[] proxyConfigLocations;

    private final Scanner scanner;
    @Getter
    private final ValidatorFactory validatorFactory;
    @Getter
    private final TypeHandlerManager typeHandlerManager;

    private final Map<Class<?>, Map<String, String>> aliasMap = new ConcurrentHashMap<>();
    private final Map<Class<?>, TableMeta> tables = new ConcurrentHashMap<>();
    private final Map<Class<?>, ConditionContext> condition = new ConcurrentHashMap<>();
    private final Map<Class<?>, ProxyMeta> proxyMetas = new ConcurrentHashMap<>();

    public MetaManager(MetaManagerConfig config) {
        verifier.verify(config, MetaManagerConfig.class);

        this.tableDefinePackages = config.getTableDefinePackages();
        this.proxyDefinePackages = config.getProxyDefinePackages();
        this.proxyConfigLocations = config.getProxyConfigLocations();
        try {
            this.validatorFactory = config.getValidatorFactory().getConstructor().newInstance();
            this.typeHandlerManager = config.getTypeHandlerManager().getConstructor().newInstance();
            this.scanner = config.getScanner().getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new MetaManagerGenerateException(e);
        }
        refresh();
    }

    public TableMeta resolve(Class<?> type) {
        log.debug("Resolve: {}", type.getName());
        return tables.computeIfAbsent(type, key -> {
            if (ReflectionUtils.isAnnotationedWith(type, TableDefine.class)) {
                log.trace("Resolve table define: {}", type.getName());
                return resolveTableDefine(ReflectionUtils.getAnnotation(type, TableDefine.class), type);
            } else if (ReflectionUtils.isAnnotationedWith(type, SqlModel.class)) {
                log.trace("Resolve sql model: {}", type.getName());
                return resolveSqlModel(ReflectionUtils.getAnnotation(type, SqlModel.class), type);
            }
            throw new DataBindExpressException(
                    "No 'TableDefine' or 'SqlModel' annotation found for type: " + type.getName());
        });
    }

    public ProxyMeta resolveProxy(Class<?> type) {
        log.debug("Resolve proxy meta: {}", type.getName());
        return proxyMetas.computeIfAbsent(type, key -> {
            ProxyMeta proxyMeta = new ProxyMeta();
            proxyMeta.setIface(type);
            proxyMeta.setMethods(
                    Arrays.stream(type.getMethods())
                            .collect(Collectors.toMap(Method::getName, method -> ProxyMethodMeta.builder()
                                    .method(method)
                                    .build(), (a, b) -> {
                                        throw new MetaManagerGenerateException(
                                                "Duplicate method name: " + a.getMethod().getName());
                                    })));

            return proxyMeta;
        });
    }

    private void loadProxyConfig() {
        log.debug("Loading proxy config. locations: {}", String.join(", ", proxyConfigLocations));
        for (String location : proxyConfigLocations) {
            log.debug("Starting load proxy config from {}", location);
            for (File configFile : FileUtils.tree(location,
                    file -> file.isFile() && (file.getName().endsWith(".yml") || file.getName().endsWith(".yaml")))) {
                String absolutePath = configFile.getAbsolutePath();
                // ProxyConfig proxyConfig = yaml.loadAs(FileUtils.getFirstExistResource(absolutePath), ProxyConfig.class);
                ProxyConfig proxyConfig = JacksonUtils.YAML.toObject(configFile, ProxyConfig.class);
                
                log.trace("Loaded proxy config from {}. config: {}", absolutePath, proxyConfig);

                verifier.verify(proxyConfig, ProxyConfig.class);

                String ifaceName = proxyConfig.getIface().getName();
                ProxyMeta proxyMeta = proxyMetas.get(proxyConfig.getIface());

                for (ProxyMethod proxyMethod : proxyConfig.getMethods()) {
                    String methodName = proxyMethod.getMethod();
                    ProxyMethodMeta proxyMethodMeta = proxyMeta.getMethod(methodName);
                    if (proxyMethodMeta == null) {
                        throw new MetaManagerGenerateException(String.format("Method not found. iface: %s, method: %s",
                                ifaceName, methodName));
                    }

                    log.trace("Proxy config is Loaded. iface: {}, method: {}", proxyConfig.getIface().getName(),
                            methodName);
                    proxyMethodMeta.setSql(proxyMethod.getSql());
                }
            }
            log.debug("Finished load proxy config from {}", location);
        }
    }

    private synchronized void refresh() {

        // refresh table
        Set<Class<?>> types = scanner.scan(tableDefinePackages);
        types.stream().filter(type -> ReflectionUtils.isAnnotationedWith(type,
                TableDefine.class))
                .forEach(this::resolve);
        types.stream().filter(type -> ReflectionUtils.isAnnotationedWith(type,
                SqlModel.class))
                .forEach(this::resolve);

        // refresh proxy
        Set<Class<?>> proxyTypes = scanner.scan(proxyDefinePackages);
        proxyTypes.stream().filter(type -> ReflectionUtils.isAnnotationedWith(type, ExecutorProxy.class))
                .forEach(this::resolveProxy);

        // load proxy config
        loadProxyConfig();
    }

    private TableMeta resolveTableDefine(TableDefine tableDefine, Class<?> type) {
        TableMeta table = TableMeta.builder()
                .name(tableDefine.table())
                .build();
        resolveType(type, table, true);

        return table;
    }

    private TableMeta resolveSqlModel(SqlModel sqlModel, Class<?> type) {
        TableMeta table = tables.get(sqlModel.table());
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
                            .name(StringUtils.isEmpty(condition.column()) ? columnName : condition.column())
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

    public Collection<TableMeta> allTables() {
        return this.tables.values();
    }

    public Set<Class<?>> allTableTypes() {
        return this.tables.keySet();
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

    public ProxyMeta getProxyMeta(Class<?> type) {
        return this.proxyMetas.get(type);
    }
}
