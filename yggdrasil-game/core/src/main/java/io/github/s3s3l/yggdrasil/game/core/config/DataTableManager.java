package io.github.s3s3l.yggdrasil.game.core.config;

import io.github.s3s3l.yggdrasil.game.core.basic.ID;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据表管理器
 * 管理游戏中的各种数据表
 */
public class DataTableManager {

    private static DataTableManager instance;

    private final Map<String, DataTable<?>> tables;
    private ConfigLoader configLoader;

    private DataTableManager() {
        this.tables = new ConcurrentHashMap<>();
        this.configLoader = new JsonConfigLoader();
    }

    /**
     * 获取单例实例
     *
     * @return 数据表管理器实例
     */
    public static synchronized DataTableManager getInstance() {
        if (instance == null) {
            instance = new DataTableManager();
        }
        return instance;
    }

    /**
     * 注册数据表
     *
     * @param table 数据表
     * @param <T>   数据类型
     */
    public <T extends ID> void registerTable(DataTable<T> table) {
        tables.put(table.getName(), table);
    }

    /**
     * 获取数据表
     *
     * @param <T>   数据类型
     * @param name  数据表名称
     * @return 数据表
     */
    @SuppressWarnings("unchecked")
    public <T extends ID> DataTable<T> getTable(String name) {
        return (DataTable<T>) tables.get(name);
    }

    /**
     * 检查数据表是否存在
     *
     * @param name 数据表名称
     * @return 是否存在
     */
    public boolean hasTable(String name) {
        return tables.containsKey(name);
    }

    /**
     * 从 JSON 文件加载数据表
     *
     * @param <T>   数据类型
     * @param name  数据表名称
     * @param clazz 数据类型
     * @param path  JSON 文件路径
     * @return 加载的数据表
     * @throws Exception 加载失败时抛出异常
     */
    public <T extends ID> DataTable<T> loadTable(String name, Class<T> clazz, String path) throws Exception {
        DataTable<T> table = new DataTable<>(name, clazz);

        // 加载 JSON 数组
        T[] items = configLoader.load(arrayType(clazz), path);
        if (items != null) {
            for (T item : items) {
                table.add(item);
                if (item instanceof DataRow) {
                    ((DataRow) item).onInit();
                }
            }
        }

        registerTable(table);
        return table;
    }

    /**
     * 从 JSON 文件加载数据表（带 ID 字段映射）
     *
     * @param <T>        数据类型
     * @param name       数据表名称
     * @param clazz      数据类型
     * @param path       JSON 文件路径
     * @param idFieldName ID 字段名称
     * @return 加载的数据表
     * @throws Exception 加载失败时抛出异常
     */
    public <T extends ID> DataTable<T> loadTableWithIdMapping(String name, Class<T> clazz, String path,
                                                               String idFieldName) throws Exception {
        DataTable<T> table = new DataTable<>(name, clazz);

        // 加载 JSON 数组
        T[] items = configLoader.load(arrayType(clazz), path);
        if (items != null) {
            for (T item : items) {
                // 如果 ID 为 0，尝试从指定字段获取 ID
                if (item.getId() == 0) {
                    java.lang.reflect.Field idField = clazz.getDeclaredField(idFieldName);
                    if (idField != null) {
                        idField.setAccessible(true);
                        Object idValue = idField.get(item);
                        if (idValue instanceof Number) {
                            ((ID) item).setId(((Number) idValue).longValue());
                        } else if (idValue instanceof String) {
                            // 使用字符串的哈希值作为 ID
                            ((ID) item).setId(idValue.toString().hashCode());
                        }
                    }
                }
                table.add(item);
                if (item instanceof DataRow) {
                    ((DataRow) item).onInit();
                }
            }
        }

        registerTable(table);
        return table;
    }

    /**
     * 卸载数据表
     *
     * @param name 数据表名称
     */
    public void unloadTable(String name) {
        DataTable<?> table = tables.remove(name);
        if (table != null) {
            table.clear();
        }
    }

    /**
     * 卸载所有数据表
     */
    public void unloadAllTables() {
        for (DataTable<?> table : tables.values()) {
            table.clear();
        }
        tables.clear();
    }

    /**
     * 获取所有注册的数据表
     *
     * @return 数据表名称列表
     */
    public Set<String> getAllTableNames() {
        return Collections.unmodifiableSet(tables.keySet());
    }

    /**
     * 获取数据表数量
     *
     * @return 数据表数量
     */
    public int getTableCount() {
        return tables.size();
    }

    /**
     * 设置配置加载器
     *
     * @param configLoader 配置加载器
     */
    public void setConfigLoader(ConfigLoader configLoader) {
        this.configLoader = configLoader;
    }

    /**
     * 创建数组类型
     *
     * @param componentType 组件类型
     * @return 数组类型
     */
    @SuppressWarnings("unchecked")
    private <T> Class<T[]> arrayType(Class<T> componentType) {
        return (Class<T[]>) java.lang.reflect.Array.newInstance(componentType, 0).getClass();
    }
}
