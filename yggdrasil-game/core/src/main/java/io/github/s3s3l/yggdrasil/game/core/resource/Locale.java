package io.github.s3s3l.yggdrasil.game.core.resource;

import java.util.Objects;

/**
 * 语言区域类
 * 用于标识和区分不同的语言和地区
 */
public class Locale {

    /**
     * 简体中文 - 中国
     */
    public static final Locale SIMPLIFIED_CHINESE = new Locale("zh", "CN", "zh_CN");

    /**
     * 繁体中文 - 台湾
     */
    public static final Locale TRADITIONAL_CHINESE = new Locale("zh", "TW", "zh_TW");

    /**
     * 英语 - 美国
     */
    public static final Locale ENGLISH_US = new Locale("en", "US", "en_US");

    /**
     * 英语 - 英国
     */
    public static final Locale ENGLISH_UK = new Locale("en", "GB", "en_GB");

    /**
     * 日语 - 日本
     */
    public static final Locale JAPANESE = new Locale("ja", "JP", "ja_JP");

    /**
     * 韩语 - 韩国
     */
    public static final Locale KOREAN = new Locale("ko", "KR", "ko_KR");

    /**
     * 语言代码（如 "zh"）
     */
    private final String language;

    /**
     * 国家/地区代码（如 "CN"）
     */
    private final String country;

    /**
     * 完整标识符（如 "zh_CN"）
     */
    private final String identifier;

    /**
     * 创建语言区域
     *
     * @param language 语言代码
     * @param country  国家/地区代码
     * @param identifier 完整标识符
     */
    public Locale(String language, String country, String identifier) {
        this.language = language;
        this.country = country;
        this.identifier = identifier;
    }

    /**
     * 从标识符创建语言区域
     *
     * @param identifier 标识符（如 "zh_CN"）
     * @return 语言区域对象
     */
    public static Locale forIdentifier(String identifier) {
        switch (identifier) {
            case "zh_CN":
                return SIMPLIFIED_CHINESE;
            case "zh_TW":
                return TRADITIONAL_CHINESE;
            case "en_US":
                return ENGLISH_US;
            case "en_GB":
                return ENGLISH_UK;
            case "ja_JP":
                return JAPANESE;
            case "ko_KR":
                return KOREAN;
            default:
                String[] parts = identifier.split("_");
                if (parts.length == 2) {
                    return new Locale(parts[0], parts[1], identifier);
                }
                return new Locale(identifier, "", identifier);
        }
    }

    /**
     * 获取语言代码
     *
     * @return 语言代码
     */
    public String getLanguage() {
        return language;
    }

    /**
     * 获取国家/地区代码
     *
     * @return 国家/地区代码
     */
    public String getCountry() {
        return country;
    }

    /**
     * 获取完整标识符
     *
     * @return 完整标识符
     */
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Locale locale = (Locale) o;
        return Objects.equals(identifier, locale.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }

    @Override
    public String toString() {
        return identifier;
    }
}
