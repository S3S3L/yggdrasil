package io.github.s3s3l.yggdrasil.game.core.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

/**
 * 语言包类
 * 管理一组指定语言的文本资源（键值对形式）
 */
public class LanguageBundle {

    /**
     * 语言区域
     */
    private final Locale locale;

    /**
     * 文本映射（键 -> 文本）
     */
    private final Map<String, String> texts;

    /**
     * 回退语言包（当当前语言包缺少某个键时，使用回退语言包）
     */
    private LanguageBundle fallbackBundle;

    /**
     * 从 Properties 加载语言包
     *
     * @param locale 语言区域
     * @param properties 属性对象
     */
    public LanguageBundle(Locale locale, Properties properties) {
        this.locale = locale;
        this.texts = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            texts.put(key, properties.getProperty(key));
        }
    }

    /**
     * 从输入流加载语言包
     *
     * @param locale 语言区域
     * @param inputStream 输入流（UTF-8 编码的 properties 文件）
     * @throws IOException 加载失败时抛出
     */
    public static LanguageBundle fromStream(Locale locale, InputStream inputStream) throws IOException {
        Properties properties = new Properties();
        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            properties.load(reader);
        }
        return new LanguageBundle(locale, properties);
    }

    /**
     * 创建空语言包
     *
     * @param locale 语言区域
     * @return 空语言包
     */
    public static LanguageBundle empty(Locale locale) {
        return new LanguageBundle(locale, new Properties());
    }

    /**
     * 获取语言区域
     *
     * @return 语言区域
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * 获取文本
     *
     * @param key 文本键
     * @return 文本内容，如果键不存在返回 null
     */
    public String getText(String key) {
        String text = texts.get(key);
        if (text == null && fallbackBundle != null) {
            return fallbackBundle.getText(key);
        }
        return text;
    }

    /**
     * 获取文本（带默认值）
     *
     * @param key 文本键
     * @param defaultValue 默认值
     * @return 文本内容，如果键不存在返回默认值
     */
    public String getText(String key, String defaultValue) {
        String text = getText(key);
        return text != null ? text : defaultValue;
    }

    /**
     * 获取文本（带格式化参数）
     *
     * @param key 文本键
     * @param args 格式化参数
     * @return 格式化后的文本
     */
    public String getText(String key, Object... args) {
        String text = getText(key);
        if (text == null) {
            return key; // 返回键本身作为占位
        }
        try {
            return String.format(text, args);
        } catch (Exception e) {
            return text; // 格式化失败时返回原文本
        }
    }

    /**
     * 检查是否包含指定的键
     *
     * @param key 键
     * @return true 如果包含
     */
    public boolean containsKey(String key) {
        return texts.containsKey(key);
    }

    /**
     * 获取所有键
     *
     * @return 键集合
     */
    public Set<String> keySet() {
        return Collections.unmodifiableSet(texts.keySet());
    }

    /**
     * 获取键的数量
     *
     * @return 键数量
     */
    public int size() {
        return texts.size();
    }

    /**
     * 设置回退语言包
     * 当当前语言包缺少某个键时，会从回退语言包中查找
     *
     * @param fallbackBundle 回退语言包
     */
    public void setFallbackBundle(LanguageBundle fallbackBundle) {
        this.fallbackBundle = fallbackBundle;
    }

    /**
     * 获取回退语言包
     *
     * @return 回退语言包
     */
    public LanguageBundle getFallbackBundle() {
        return fallbackBundle;
    }

    /**
     * 添加文本
     *
     * @param key 键
     * @param value 文本值
     */
    public void addText(String key, String value) {
        texts.put(key, value);
    }

    /**
     * 移除文本
     *
     * @param key 键
     * @return 被移除的值
     */
    public String removeText(String key) {
        return texts.remove(key);
    }

    /**
     * 清空所有文本
     */
    public void clear() {
        texts.clear();
    }

    /**
     * 合并另一个语言包
     * 将另一个语言包的文本添加到当前语言包中
     *
     * @param other 另一个语言包
     */
    public void merge(LanguageBundle other) {
        if (other != null) {
            texts.putAll(other.texts);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LanguageBundle that = (LanguageBundle) o;
        return Objects.equals(locale, that.locale) &&
                Objects.equals(texts, that.texts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locale, texts);
    }
}
