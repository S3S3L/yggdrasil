package io.github.s3s3l.yggdrasil.game.core.config;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * 属性配置加载器
 * 支持标准的 Java Properties 格式
 */
public class PropertiesConfigLoader implements ConfigLoader {

    private final Properties properties;

    public PropertiesConfigLoader() {
        this.properties = new Properties();
    }

    @Override
    public <T> T load(Class<T> clazz, String path) throws Exception {
        properties.clear();

        InputStream input = getClass().getResourceAsStream("/" + path);
        if (input == null) {
            input = new FileInputStream(path);
        }

        try (InputStreamReader reader = new InputStreamReader(input, "UTF-8")) {
            properties.load(reader);
        }

        return createConfigFromProperties(clazz);
    }

    @Override
    public void save(Object config, String path) throws Exception {
        Properties props = new Properties();

        // 从配置对象提取属性
        Field[] fields = config.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(config);
            if (value != null) {
                props.setProperty(field.getName(), value.toString());
            }
        }

        try (OutputStream output = new FileOutputStream(path)) {
            props.store(output, "Game Configuration");
        }
    }

    @Override
    public boolean exists(String path) {
        InputStream input = getClass().getResourceAsStream("/" + path);
        if (input != null) {
            try {
                input.close();
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        return new File(path).exists();
    }

    @Override
    public String getName() {
        return "Properties";
    }

    /**
     * 获取属性值
     *
     * @param key 键
     * @return 属性值
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * 获取属性值（带默认值）
     *
     * @param key 键
     * @param defaultValue 默认值
     * @return 属性值
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * 获取整数属性
     *
     * @param key 键
     * @return 整数值
     */
    public int getIntProperty(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    /**
     * 获取浮点属性
     *
     * @param key 键
     * @return 浮点值
     */
    public float getFloatProperty(String key) {
        return Float.parseFloat(properties.getProperty(key));
    }

    /**
     * 获取布尔属性
     *
     * @param key 键
     * @return 布尔值
     */
    public boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }

    /**
     * 创建配置对象
     *
     * @param clazz 配置类
     * @return 配置对象
     */
    @SuppressWarnings("unchecked")
    private <T> T createConfigFromProperties(Class<T> clazz) throws Exception {
        T config = clazz.getDeclaredConstructor().newInstance();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String value = properties.getProperty(field.getName());
            if (value != null) {
                setFieldValue(config, field, value);
            }
        }

        return config;
    }

    /**
     * 设置字段值
     *
     * @param config 配置对象
     * @param field  字段
     * @param value  值
     */
    private void setFieldValue(Object config, Field field, String value) throws Exception {
        Class<?> type = field.getType();

        if (type == int.class || type == Integer.class) {
            field.set(config, Integer.parseInt(value));
        } else if (type == long.class || type == Long.class) {
            field.set(config, Long.parseLong(value));
        } else if (type == float.class || type == Float.class) {
            field.set(config, Float.parseFloat(value));
        } else if (type == double.class || type == Double.class) {
            field.set(config, Double.parseDouble(value));
        } else if (type == boolean.class || type == Boolean.class) {
            field.set(config, Boolean.parseBoolean(value));
        } else {
            field.set(config, value);
        }
    }
}
