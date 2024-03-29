package io.github.s3s3l.yggdrasil.orm.meta;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.github.s3s3l.yggdrasil.bean.exception.ResourceNotFoundException;
import io.github.s3s3l.yggdrasil.bean.exception.VerifyException;
import io.github.s3s3l.yggdrasil.orm.bind.annotation.Column;
import io.github.s3s3l.yggdrasil.orm.bind.annotation.Condition;
import io.github.s3s3l.yggdrasil.orm.bind.annotation.DatabaseType;
import io.github.s3s3l.yggdrasil.orm.bind.annotation.ExecutorProxy;
import io.github.s3s3l.yggdrasil.orm.bind.annotation.GroupBy;
import io.github.s3s3l.yggdrasil.orm.bind.annotation.Limit;
import io.github.s3s3l.yggdrasil.orm.bind.annotation.Offset;
import io.github.s3s3l.yggdrasil.orm.bind.annotation.OrderBy;
import io.github.s3s3l.yggdrasil.orm.bind.annotation.Param;
import io.github.s3s3l.yggdrasil.orm.bind.annotation.SqlModel;
import io.github.s3s3l.yggdrasil.orm.bind.annotation.TableDefine;
import io.github.s3s3l.yggdrasil.orm.exception.DataBindExpressException;
import io.github.s3s3l.yggdrasil.orm.exception.MetaManagerGenerateException;
import io.github.s3s3l.yggdrasil.orm.handler.TypeHandlerManager;
import io.github.s3s3l.yggdrasil.orm.proxy.config.ProxyConfig;
import io.github.s3s3l.yggdrasil.orm.proxy.config.ProxyMethod;
import io.github.s3s3l.yggdrasil.orm.proxy.meta.ParamMeta;
import io.github.s3s3l.yggdrasil.orm.proxy.meta.ProxyMeta;
import io.github.s3s3l.yggdrasil.orm.proxy.meta.ProxyMethodMeta;
import io.github.s3s3l.yggdrasil.orm.validator.ValidatorFactory;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;
import io.github.s3s3l.yggdrasil.utils.file.FileUtils;
import io.github.s3s3l.yggdrasil.utils.reflect.ReflectionUtils;
import io.github.s3s3l.yggdrasil.utils.reflect.scan.Scanner;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;
import io.github.s3s3l.yggdrasil.utils.verify.CommonVerifier;
import io.github.s3s3l.yggdrasil.utils.verify.Verifier;
import io.github.s3s3l.yggdrasil.utils.verify.Verify;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MetaManager {
    private final Verifier verifier = new CommonVerifier();

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
            this.validatorFactory = config.getValidatorFactory()
                    .getConstructor()
                    .newInstance();
            this.typeHandlerManager = config.getTypeHandlerManager()
                    .getConstructor()
                    .newInstance();
            this.scanner = config.getScanner()
                    .getConstructor()
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new MetaManagerGenerateException(e);
        }
        refresh();
    }

    public TableMeta resolve(Class<?> type) {
        return tables.computeIfAbsent(type, key -> {
            if (ReflectionUtils.isAnnotationedWith(type, TableDefine.class)) {
                log.info("Resolve table define: {}", type.getName());
                return resolveTableDefine(ReflectionUtils.getAnnotation(type, TableDefine.class), type);
            } else if (ReflectionUtils.isAnnotationedWith(type, SqlModel.class)) {
                log.info("Resolve sql model: {}", type.getName());
                return resolveSqlModel(ReflectionUtils.getAnnotation(type, SqlModel.class), type);
            }
            throw new DataBindExpressException(
                    "No 'TableDefine' or 'SqlModel' annotation found for type: " + type.getName());
        });
    }

    public ProxyMeta resolveProxy(Class<?> type) {
        log.info("Resolve proxy meta: {}", type.getName());
        return proxyMetas.computeIfAbsent(type, key -> {
            ProxyMeta proxyMeta = new ProxyMeta();
            proxyMeta.setIface(type);
            proxyMeta.setMethods(Arrays.stream(type.getMethods())
                    .collect(Collectors.toMap(Method::getName, method -> {
                        List<ParamMeta> params = new LinkedList<>();
                        Parameter[] parameters = method.getParameters();
                        for (int paramIndex = 0; paramIndex < parameters.length; paramIndex++) {
                            Parameter parameter = parameters[paramIndex];
                            if (!parameter.isAnnotationPresent(Param.class)) {
                                continue;
                            }

                            Param param = parameter.getAnnotation(Param.class);

                            params.add(ParamMeta.builder()
                                    .index(paramIndex)
                                    .name(param.value())
                                    .build());
                        }
                        ProxyMethodMeta methodMeta = ProxyMethodMeta.builder()
                                .method(method)
                                .params(params)
                                .build();
                        return methodMeta;
                    }, (a, b) -> {
                        throw new MetaManagerGenerateException("Duplicate method name: " + a.getMethod()
                                .getName());
                    })));

            return proxyMeta;
        });
    }

    private void loadProxyConfig() {
        if (proxyConfigLocations == null || proxyConfigLocations.length <= 0) {
            return;
        }

        log.info("Loading proxy config. locations: {}", String.join(", ", proxyConfigLocations));

        for (String location : proxyConfigLocations) {
            log.info("Starting load proxy config from {}", location);
            for (File configFile : FileUtils.tree(location, file -> file.isFile() && (file.getName()
                    .endsWith(".yml")
                    || file.getName()
                            .endsWith(".yaml")))) {
                String absolutePath = configFile.getAbsolutePath();
                ProxyConfig proxyConfig = JacksonUtils.YAML.toObject(configFile, ProxyConfig.class);

                log.debug("Loaded proxy config from {}. config: {}", absolutePath, proxyConfig);

                verifier.verify(proxyConfig, ProxyConfig.class);

                String ifaceName = proxyConfig.getIface()
                        .getName();
                ProxyMeta proxyMeta = proxyMetas.get(proxyConfig.getIface());

                for (ProxyMethod proxyMethod : proxyConfig.getMethods()) {
                    String methodName = proxyMethod.getMethod();
                    ProxyMethodMeta proxyMethodMeta = proxyMeta.getMethod(methodName);
                    if (proxyMethodMeta == null) {
                        throw new MetaManagerGenerateException(
                                String.format("Method not found. iface: %s, method: %s", ifaceName, methodName));
                    }

                    log.trace("Proxy config is Loaded. iface: {}, method: {}", proxyConfig.getIface()
                            .getName(), methodName);
                    proxyMethodMeta.setSql(proxyMethod.getSql());
                    proxyMethodMeta.setType(proxyMethod.getType());
                }
            }
            log.info("Finished load proxy config from {}", location);
        }
    }

    private synchronized void refresh() {

        // refresh table
        Set<Class<?>> types = scanner.scan(tableDefinePackages);
        types.stream()
                .filter(type -> ReflectionUtils.isAnnotationedWith(type, TableDefine.class))
                .forEach(this::resolve);
        types.stream()
                .filter(type -> ReflectionUtils.isAnnotationedWith(type, SqlModel.class))
                .forEach(this::resolve);

        // refresh proxy
        Set<Class<?>> proxyTypes = scanner.scan(proxyDefinePackages);
        proxyTypes.stream()
                .filter(type -> ReflectionUtils.isAnnotationedWith(type, ExecutorProxy.class))
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
        Map<String, ColumnMeta> columnMap = table.getColumns()
                .stream()
                .collect(Collectors.toMap(ColumnMeta::getName, Function.identity()));
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
                DatabaseType dbType = column.dbType();
                columnMeta = ColumnMeta.builder()
                        .field(field)
                        .name(columnName)
                        .tableName(table.getName())
                        .alias(field.getName())
                        .validator(this.validatorFactory.getValidator(column.validator()))
                        .typeHandler(typeHandlerManager.getOrNew(column.typeHandler()))
                        .dbType(DbType.builder()
                                .type(dbType.type())
                                .array(dbType.array())
                                .args(Arrays.asList(dbType.args()))
                                .primary(dbType.primary())
                                .notNull(dbType.notNull() || dbType.primary()) // 主键必须为非空
                                .def(dbType.def())
                                .defValue(dbType.defValue())
                                .build())
                        .build();
                columns.add(columnMeta);
                columnMap.put(columnName, columnMeta);
                aliasMap.put(columnName.toUpperCase(), field.getName());
            }

            Condition condition = field.getAnnotation(Condition.class);

            if (condition != null) {
                String conditionColumnName = StringUtils.isEmpty(condition.column()) ? columnName : condition.column();
                ColumnMeta conditionColumnMeta = columnMap.getOrDefault(conditionColumnName, columnMeta);
                if (conditionColumnMeta == null) {
                    conditionColumnMeta = ColumnMeta.builder()
                            .name(StringUtils.isEmpty(condition.column()) ? columnName : condition.column())
                            .build();
                }

                ConditionMeta conditionMeta = ConditionMeta.builder()
                        .field(field)
                        .column(conditionColumnMeta)
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
                if (!long.class.isAssignableFrom(fieldType) && !Long.class.isAssignableFrom(fieldType)) {
                    throw new VerifyException("@Offset can only mark on 'Long' type field.");
                }
                conditionContext.setOffset(OffsetMeta.builder()
                        .field(field)
                        .build());
            }

            Limit l = field.getAnnotation(Limit.class);

            if (l != null) {
                if (!long.class.isAssignableFrom(fieldType) && !Long.class.isAssignableFrom(fieldType)) {
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
        ConditionContext conditionContext = this.condition.get(type);
        if (conditionContext == null) {
            return null;
        }
        return conditionContext.getSelectConditions();
    }

    public List<ConditionMeta> getUpdateCondition(Class<?> type) {
        ConditionContext conditionContext = this.condition.get(type);
        if (conditionContext == null) {
            return null;
        }
        return conditionContext.getUpdateConditions();
    }

    public List<ConditionMeta> getDeleteCondition(Class<?> type) {
        ConditionContext conditionContext = this.condition.get(type);
        if (conditionContext == null) {
            return null;
        }
        return conditionContext.getDeleteConditions();
    }

    public GroupByMeta getGroupBy(Class<?> type) {
        ConditionContext conditionContext = this.condition.get(type);
        if (conditionContext == null) {
            return null;
        }
        return conditionContext.getGroupBy();
    }

    public List<OrderByMeta> getOrderBy(Class<?> type) {
        ConditionContext conditionContext = this.condition.get(type);
        if (conditionContext == null) {
            return null;
        }
        return conditionContext.getOrderBy();
    }

    public OffsetMeta getOffset(Class<?> type) {
        ConditionContext conditionContext = this.condition.get(type);
        if (conditionContext == null) {
            return null;
        }
        return conditionContext.getOffset();
    }

    public LimitMeta getLimit(Class<?> type) {
        ConditionContext conditionContext = this.condition.get(type);
        if (conditionContext == null) {
            return null;
        }
        return conditionContext.getLimit();
    }

    public String getAlias(Class<?> type, String columnName) {
        Verify.hasText(columnName);
        return this.aliasMap.get(type)
                .getOrDefault(columnName.toUpperCase(), columnName);
    }

    public ProxyMeta getProxyMeta(Class<?> type) {
        return this.proxyMetas.get(type);
    }

    public Set<Class<?>> allProxies() {
        return this.proxyMetas.keySet();
    }
}
