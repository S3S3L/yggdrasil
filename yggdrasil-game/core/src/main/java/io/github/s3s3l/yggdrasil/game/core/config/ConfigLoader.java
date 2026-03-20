package io.github.s3s3l.yggdrasil.game.core.config;

/**
 * 配置加载器接口
 * 支持从不同来源加载配置
 */
public interface ConfigLoader {

    /**
     * 加载配置
     *
     * @param <T> 配置类型
     * @param clazz 配置类
     * @param path 配置路径
     * @return 配置对象
     * @throws Exception 加载失败时抛出异常
     */
    <T> T load(Class<T> clazz, String path) throws Exception;

    /**
     * 保存配置
     *
     * @param config 配置对象
     * @param path 保存路径
     * @throws Exception 保存失败时抛出异常
     */
    void save(Object config, String path) throws Exception;

    /**
     * 检查配置是否存在
     *
     * @param path 配置路径
     * @return 是否存在
     */
    boolean exists(String path);

    /**
     * 获取加载器名称
     *
     * @return 加载器名称
     */
    String getName();
}
