package io.github.s3s3l.yggdrasil.game.core.config;

import io.github.s3s3l.yggdrasil.game.core.basic.ID;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据表
 * 通用的数据表格结构，支持按 ID 查询和过滤
 *
 * @param <T> 数据类型
 */
public class DataTable<T extends ID> implements Iterable<T> {

    private final Map<Long, T> data;
    private final String name;
    private final Class<T> type;

    public DataTable(String name, Class<T> type) {
        this.name = name;
        this.type = type;
        this.data = new ConcurrentHashMap<>();
    }

    /**
     * 添加数据
     *
     * @param item 数据项
     */
    public void add(T item) {
        data.put(item.getId(), item);
    }

    /**
     * 移除数据
     *
     * @param id 数据 ID
     * @return 被移除的数据项
     */
    public T remove(long id) {
        return data.remove(id);
    }

    /**
     * 获取数据
     *
     * @param id 数据 ID
     * @return 数据项
     */
    public T get(long id) {
        return data.get(id);
    }

    /**
     * 检查是否包含指定 ID 的数据
     *
     * @param id 数据 ID
     * @return 是否包含
     */
    public boolean contains(long id) {
        return data.containsKey(id);
    }

    /**
     * 获取所有数据
     *
     * @return 数据列表
     */
    public Collection<T> getAll() {
        return Collections.unmodifiableCollection(data.values());
    }

    /**
     * 获取数据大小
     *
     * @return 数据条数
     */
    public int size() {
        return data.size();
    }

    /**
     * 清空数据表
     */
    public void clear() {
        data.clear();
    }

    /**
     * 根据条件过滤数据
     *
     * @param predicate 过滤条件
     * @return 过滤后的数据列表
     */
    public List<T> filter(Function<T, Boolean> predicate) {
        return data.values().stream()
                .filter(predicate::apply)
                .collect(Collectors.toList());
    }

    /**
     * 根据条件查找第一个匹配的数据
     *
     * @param predicate 查找条件
     * @return 匹配的数据项，如果没有则返回 null
     */
    public T findFirst(Function<T, Boolean> predicate) {
        for (T item : data.values()) {
            if (predicate.apply(item)) {
                return item;
            }
        }
        return null;
    }

    /**
     * 获取数据表名称
     *
     * @return 数据表名称
     */
    public String getName() {
        return name;
    }

    /**
     * 获取数据类型
     *
     * @return 数据类型
     */
    public Class<T> getType() {
        return type;
    }

    /**
     * 检查数据表是否为空
     *
     * @return 是否为空
     */
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public Iterator<T> iterator() {
        return data.values().iterator();
    }

    /**
     * 转换为列表
     *
     * @return 数据列表
     */
    public List<T> toList() {
        return new ArrayList<>(data.values());
    }

    /**
     * 转换为映射
     *
     * @return 数据映射
     */
    public Map<Long, T> toMap() {
        return new HashMap<>(data);
    }
}
