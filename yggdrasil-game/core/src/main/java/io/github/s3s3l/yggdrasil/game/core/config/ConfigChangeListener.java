package io.github.s3s3l.yggdrasil.game.core.config;

/**
 * 配置变更监听器函数式接口
 */
@FunctionalInterface
public interface ConfigChangeListener {

    /**
     * 配置变更回调
     *
     * @param path   配置路径
     * @param config 新配置
     */
    void onConfigChanged(String path, Object config);
}
