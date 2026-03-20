package io.github.s3s3l.yggdrasil.game.core.config;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

/**
 * JSON 配置加载器
 * 使用纯 Java 实现 JSON 配置加载和保存
 */
public class JsonConfigLoader implements ConfigLoader {

    private final Map<String, Object> cache;
    private final Map<String, Long> fileTimestamps;

    public JsonConfigLoader() {
        this.cache = new HashMap<>();
        this.fileTimestamps = new HashMap<>();
    }

    @Override
    public <T> T load(Class<T> clazz, String path) throws Exception {
        Path file = getFilePath(path);
        if (!Files.exists(file)) {
            throw new FileNotFoundException("Config file not found: " + path);
        }

        // 尝试从缓存加载
        String cacheKey = getCacheKey(path);
        if (cache.containsKey(cacheKey)) {
            return clazz.cast(cache.get(cacheKey));
        }

        // 从文件加载
        String jsonContent = new String(Files.readAllBytes(file));
        T config = parseJson(clazz, jsonContent);
        cache.put(cacheKey, config);
        fileTimestamps.put(path, Files.getLastModifiedTime(file).toMillis());

        return config;
    }

    @Override
    public void save(Object config, String path) throws Exception {
        Path file = getFilePath(path);
        String jsonContent = toJson(config);

        // 确保父目录存在
        Path parent = file.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }

        Files.writeString(file, jsonContent);

        // 更新缓存
        String cacheKey = getCacheKey(path);
        cache.put(cacheKey, config);
        fileTimestamps.put(path, Files.getLastModifiedTime(file).toMillis());
    }

    @Override
    public boolean exists(String path) {
        return Files.exists(getFilePath(path));
    }

    @Override
    public String getName() {
        return "JSON";
    }

    /**
     * 清空缓存
     */
    public void clearCache() {
        cache.clear();
        fileTimestamps.clear();
    }

    /**
     * 移除指定缓存
     *
     * @param path 配置路径
     */
    public void removeCache(String path) {
        String cacheKey = getCacheKey(path);
        cache.remove(cacheKey);
        fileTimestamps.remove(path);
    }

    /**
     * 检查配置是否已修改
     *
     * @param path 配置路径
     * @return 是否已修改
     */
    public boolean isModified(String path) {
        Path file = getFilePath(path);
        if (!Files.exists(file)) {
            return false;
        }

        Long cachedTimestamp = fileTimestamps.get(path);
        if (cachedTimestamp == null) {
            return true;
        }

        try {
            return Files.getLastModifiedTime(file).toMillis() != cachedTimestamp;
        } catch (IOException e) {
            return true;
        }
    }

    /**
     * 重新加载已修改的配置
     *
     * @param path 配置路径
     * @param clazz 配置类
     * @return 重新加载的配置对象，如果未修改则返回 null
     */
    public <T> T reloadIfModified(Class<T> clazz, String path) throws Exception {
        if (isModified(path)) {
            removeCache(path);
            return load(clazz, path);
        }
        return null;
    }

    /**
     * 获取文件路径
     *
     * @param path 路径
     * @return 文件路径
     */
    private Path getFilePath(String path) {
        // 尝试绝对路径
        Path file = Paths.get(path);
        if (Files.exists(file)) {
            return file;
        }

        // 尝试当前目录
        file = Paths.get(System.getProperty("user.dir"), path);
        if (Files.exists(file)) {
            return file;
        }

        // 尝试 classpath
        return Paths.get(path);
    }

    /**
     * 获取缓存键
     *
     * @param path 路径
     * @return 缓存键
     */
    private String getCacheKey(String path) {
        return path;
    }

    /**
     * 检查缓存中是否存在指定配置
     *
     * @param path 配置路径
     * @return 是否存在
     */
    public boolean hasCache(String path) {
        return cache.containsKey(getCacheKey(path));
    }

    /**
     * 解析 JSON 为对象（简单实现）
     *
     * @param clazz 目标类
     * @param json JSON 字符串
     * @return 解析的对象
     */
    @SuppressWarnings("unchecked")
    private <T> T parseJson(Class<T> clazz, String json) throws Exception {
        T obj = clazz.getDeclaredConstructor().newInstance();

        // 简单的 JSON 解析（支持基本类型和字符串）
        json = json.trim();
        if (json.startsWith("{") && json.endsWith("}")) {
            json = json.substring(1, json.length() - 1).trim();

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                String value = extractJsonValue(json, fieldName);

                if (value != null) {
                    setFieldValue(obj, field, value);
                }
            }
        }

        return obj;
    }

    /**
     * 提取 JSON 值
     *
     * @param json JSON 字符串
     * @param fieldName 字段名
     * @return 字段值
     */
    private String extractJsonValue(String json, String fieldName) {
        String keyPattern = "\"" + fieldName + "\"";
        int keyIndex = json.indexOf(keyPattern);
        if (keyIndex == -1) {
            return null;
        }

        int colonIndex = json.indexOf(':', keyIndex);
        if (colonIndex == -1) {
            return null;
        }

        // 跳过空白字符
        int valueStart = colonIndex + 1;
        while (valueStart < json.length() && Character.isWhitespace(json.charAt(valueStart))) {
            valueStart++;
        }

        if (valueStart >= json.length()) {
            return null;
        }

        char valueChar = json.charAt(valueStart);
        int valueEnd;

        if (valueChar == '"') {
            // 字符串值
            valueEnd = json.indexOf('"', valueStart + 1);
            if (valueEnd == -1) {
                return null;
            }
            return json.substring(valueStart + 1, valueEnd);
        } else if (valueChar == '{' || valueChar == '[') {
            // 对象或数组（简单处理）
            return "{}";
        } else {
            // 数字或布尔值
            valueEnd = valueStart;
            while (valueEnd < json.length() && json.charAt(valueEnd) != ',' &&
                   json.charAt(valueEnd) != '}' && json.charAt(valueEnd) != ']') {
                valueEnd++;
            }
            return json.substring(valueStart, valueEnd).trim();
        }
    }

    /**
     * 设置字段值
     *
     * @param obj 对象
     * @param field 字段
     * @param value 值字符串
     */
    private void setFieldValue(Object obj, Field field, String value) throws Exception {
        Class<?> type = field.getType();

        if (type == String.class) {
            field.set(obj, value);
        } else if (type == int.class || type == Integer.class) {
            field.set(obj, Integer.parseInt(value));
        } else if (type == long.class || type == Long.class) {
            field.set(obj, Long.parseLong(value));
        } else if (type == float.class || type == Float.class) {
            field.set(obj, Float.parseFloat(value));
        } else if (type == double.class || type == Double.class) {
            field.set(obj, Double.parseDouble(value));
        } else if (type == boolean.class || type == Boolean.class) {
            field.set(obj, Boolean.parseBoolean(value));
        }
    }

    /**
     * 对象转换为 JSON 字符串
     *
     * @param obj 对象
     * @return JSON 字符串
     */
    private String toJson(Object obj) throws Exception {
        StringBuilder json = new StringBuilder();
        json.append("{");

        Field[] fields = obj.getClass().getDeclaredFields();
        boolean first = true;

        for (Field field : fields) {
            // 跳过 static 和 transient 字段
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) ||
                java.lang.reflect.Modifier.isTransient(field.getModifiers())) {
                continue;
            }

            field.setAccessible(true);
            Object value = field.get(obj);

            if (value == null) {
                continue;
            }

            if (!first) {
                json.append(",");
            }
            first = false;

            json.append("\"").append(field.getName()).append("\":");

            if (value instanceof String) {
                json.append("\"").append(value).append("\"");
            } else if (value instanceof Boolean) {
                json.append(value);
            } else if (value instanceof Number) {
                json.append(value);
            } else {
                json.append("\"").append(value.toString()).append("\"");
            }
        }

        json.append("}");
        return json.toString();
    }
}
