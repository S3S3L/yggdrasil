package io.github.s3s3l.yggdrasil.game.core.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.github.s3s3l.yggdrasil.game.core.event.EventBus;
import io.github.s3s3l.yggdrasil.game.core.event.LanguageChangeEvent;

/**
 * 本地化管理器
 * 单例类，管理多语言文本资源和语言切换
 *
 * <p>使用示例：</p>
 * <pre>
 * // 获取实例
 * LocalizationManager localization = LocalizationManager.get();
 *
 * // 加载语言包
 * localization.loadLanguage(Locale.SIMPLIFIED_CHINESE, inputStream);
 *
 * // 设置当前语言
 * localization.setLanguage(Locale.SIMPLIFIED_CHINESE);
 *
 * // 获取文本
 * String text = localization.get("game.title");
 *
 * // 监听语言切换事件
 * EventBus.get().observe(LanguageChangeEvent.class).subscribe(event -> {
 *     // 更新 UI
 * });
 * </pre>
 */
public class LocalizationManager {

    private static final LocalizationManager INSTANCE = new LocalizationManager();

    /**
     * 已加载的语言包映射（Locale -> LanguageBundle）
     */
    private final Map<Locale, LanguageBundle> languageBundles;

    /**
     * 当前语言区域
     */
    private Locale currentLocale;

    /**
     * 默认语言区域
     */
    private Locale defaultLocale;

    /**
     * 是否自动发布语言切换事件
     */
    private boolean autoPostEvent;

    /**
     * 私有构造函数（单例）
     */
    private LocalizationManager() {
        this.languageBundles = new HashMap<>();
        this.defaultLocale = Locale.SIMPLIFIED_CHINESE;
        this.autoPostEvent = true;
    }

    /**
     * 获取本地化管理器实例
     *
     * @return 本地化管理器实例
     */
    public static LocalizationManager get() {
        return INSTANCE;
    }

    /**
     * 加载语言包
     *
     * @param locale 语言区域
     * @param inputStream 语言包输入流（UTF-8 编码的 properties 文件）
     * @throws IOException 加载失败时抛出
     */
    public void loadLanguage(Locale locale, InputStream inputStream) throws IOException {
        Objects.requireNonNull(locale, "Locale cannot be null");
        Objects.requireNonNull(inputStream, "InputStream cannot be null");

        LanguageBundle bundle = LanguageBundle.fromStream(locale, inputStream);
        languageBundles.put(locale, bundle);
    }

    /**
     * 加载语言包
     *
     * @param locale 语言区域
     * @param bundle 语言包对象
     */
    public void loadLanguage(Locale locale, LanguageBundle bundle) {
        Objects.requireNonNull(locale, "Locale cannot be null");
        Objects.requireNonNull(bundle, "LanguageBundle cannot be null");

        languageBundles.put(locale, bundle);
    }

    /**
     * 卸载语言包
     *
     * @param locale 语言区域
     * @return 被卸载的语言包，如果不存在返回 null
     */
    public LanguageBundle unloadLanguage(Locale locale) {
        if (locale == null) {
            return null;
        }

        LanguageBundle removed = languageBundles.remove(locale);

        // 如果卸载的是当前语言，切换到默认语言
        if (removed != null && locale.equals(currentLocale)) {
            currentLocale = defaultLocale;
        }

        return removed;
    }

    /**
     * 设置当前语言
     *
     * @param locale 语言区域
     * @throws IllegalArgumentException 如果语言包未加载
     */
    public void setLanguage(Locale locale) {
        Objects.requireNonNull(locale, "Locale cannot be null");

        if (!languageBundles.containsKey(locale)) {
            throw new IllegalArgumentException("Language bundle not loaded for locale: " + locale);
        }

        Locale oldLocale = currentLocale;
        currentLocale = locale;

        // 发布语言切换事件
        if (autoPostEvent && !Objects.equals(oldLocale, locale)) {
            EventBus.get().post(new LanguageChangeEvent(oldLocale, locale));
        }
    }

    /**
     * 设置当前语言（如果语言包未加载则不切换）
     *
     * @param locale 语言区域
     * @return true 如果切换成功
     */
    public boolean trySetLanguage(Locale locale) {
        if (locale == null || !languageBundles.containsKey(locale)) {
            return false;
        }

        setLanguage(locale);
        return true;
    }

    /**
     * 获取当前语言区域
     *
     * @return 当前语言区域，如果未设置返回默认语言
     */
    public Locale getCurrentLocale() {
        return currentLocale != null ? currentLocale : defaultLocale;
    }

    /**
     * 设置默认语言区域
     *
     * @param defaultLocale 默认语言区域
     */
    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale != null ? defaultLocale : Locale.SIMPLIFIED_CHINESE;
    }

    /**
     * 获取默认语言区域
     *
     * @return 默认语言区域
     */
    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    /**
     * 获取文本
     *
     * @param key 文本键
     * @return 文本内容，如果键不存在返回键本身
     */
    public String get(String key) {
        return get(key, key);
    }

    /**
     * 获取文本
     *
     * @param key 文本键
     * @param defaultValue 默认值
     * @return 文本内容，如果键不存在返回默认值
     */
    public String get(String key, String defaultValue) {
        Locale locale = getCurrentLocale();
        LanguageBundle bundle = languageBundles.get(locale);

        if (bundle == null) {
            return defaultValue;
        }

        return bundle.getText(key, defaultValue);
    }

    /**
     * 获取文本（带格式化参数）
     *
     * @param key 文本键
     * @param args 格式化参数
     * @return 格式化后的文本
     */
    public String get(String key, Object... args) {
        Locale locale = getCurrentLocale();
        LanguageBundle bundle = languageBundles.get(locale);

        if (bundle == null) {
            return String.format(key, args);
        }

        return bundle.getText(key, args);
    }

    /**
     * 获取文本（指定语言）
     *
     * @param locale 语言区域
     * @param key 文本键
     * @return 文本内容，如果键不存在返回键本身
     */
    public String getInLocale(Locale locale, String key) {
        if (locale == null) {
            return get(key);
        }

        LanguageBundle bundle = languageBundles.get(locale);
        if (bundle == null) {
            return key;
        }

        return bundle.getText(key, key);
    }

    /**
     * 检查是否包含指定的文本键
     *
     * @param key 文本键
     * @return true 如果包含
     */
    public boolean containsKey(String key) {
        Locale locale = getCurrentLocale();
        LanguageBundle bundle = languageBundles.get(locale);
        return bundle != null && bundle.containsKey(key);
    }

    /**
     * 检查指定语言包是否已加载
     *
     * @param locale 语言区域
     * @return true 如果已加载
     */
    public boolean isLanguageLoaded(Locale locale) {
        return languageBundles.containsKey(locale);
    }

    /**
     * 获取已加载的所有语言区域
     *
     * @return 语言区域集合
     */
    public java.util.Set<Locale> getLoadedLocales() {
        return java.util.Collections.unmodifiableSet(languageBundles.keySet());
    }

    /**
     * 清空所有语言包
     */
    public void clear() {
        languageBundles.clear();
        currentLocale = null;
    }

    /**
     * 设置是否自动发布语言切换事件
     *
     * @param autoPostEvent true 为自动发布
     */
    public void setAutoPostEvent(boolean autoPostEvent) {
        this.autoPostEvent = autoPostEvent;
    }

    /**
     * 检查是否自动发布语言切换事件
     *
     * @return true 为自动发布
     */
    public boolean isAutoPostEvent() {
        return autoPostEvent;
    }

    /**
     * 获取当前语言包
     *
     * @return 当前语言包，如果未加载返回 null
     */
    public LanguageBundle getCurrentBundle() {
        Locale locale = getCurrentLocale();
        return languageBundles.get(locale);
    }

    /**
     * 添加或替换文本（用于动态文本）
     *
     * @param key 文本键
     * @param value 文本值
     */
    public void setText(String key, String value) {
        Locale locale = getCurrentLocale();
        LanguageBundle bundle = languageBundles.get(locale);

        if (bundle == null) {
            bundle = LanguageBundle.empty(locale);
            languageBundles.put(locale, bundle);
        }

        bundle.addText(key, value);
    }
}
