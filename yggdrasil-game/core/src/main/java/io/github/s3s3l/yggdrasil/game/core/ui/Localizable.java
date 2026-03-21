package io.github.s3s3l.yggdrasil.game.core.ui;

/**
 * 可本地化的 UI 组件接口
 * 实现此接口的 UI 组件会自动响应语言切换事件
 */
public interface Localizable {

    /**
     * 获取本地化键
     * 用于从语言包中获取对应的文本
     *
     * @return 本地化键
     */
    String getLocalizationKey();

    /**
     * 设置本地化键
     *
     * @param key 本地化键
     */
    void setLocalizationKey(String key);

    /**
     * 使用本地化文本更新组件
     * 此方法会在语言切换时被调用
     */
    void updateLocalization();
}
