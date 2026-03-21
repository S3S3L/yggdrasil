package io.github.s3s3l.yggdrasil.game.core.event;

import io.github.s3s3l.yggdrasil.game.core.resource.Locale;

/**
 * 语言切换事件
 * 当语言发生改变时触发此事件
 */
public class LanguageChangeEvent implements Event {

    /**
     * 之前的语言区域
     */
    private final Locale oldLocale;

    /**
     * 新的语言区域
     */
    private final Locale newLocale;

    /**
     * 创建语言切换事件
     *
     * @param oldLocale 之前的语言区域
     * @param newLocale 新的语言区域
     */
    public LanguageChangeEvent(Locale oldLocale, Locale newLocale) {
        this.oldLocale = oldLocale;
        this.newLocale = newLocale;
    }

    /**
     * 获取之前的语言区域
     *
     * @return 之前的语言区域
     */
    public Locale getOldLocale() {
        return oldLocale;
    }

    /**
     * 获取新的语言区域
     *
     * @return 新的语言区域
     */
    public Locale getNewLocale() {
        return newLocale;
    }

    @Override
    public String toString() {
        return "LanguageChangeEvent{" +
                "oldLocale=" + oldLocale +
                ", newLocale=" + newLocale +
                '}';
    }
}
