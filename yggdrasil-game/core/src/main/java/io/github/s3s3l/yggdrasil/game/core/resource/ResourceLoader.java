package io.github.s3s3l.yggdrasil.game.core.resource;

import java.io.IOException;

/**
 * 资源加载器接口
 * 定义资源加载的基本操作
 *
 * @param <T> 资源类型
 */
public interface ResourceLoader<T> {

    /**
     * 从指定路径加载资源
     *
     * @param path 资源路径
     * @return 加载的资源对象
     * @throws IOException 加载失败时抛出
     */
    T load(String path) throws IOException;

    /**
     * 从指定路径加载资源，带参数
     *
     * @param path   资源路径
     * @param params 加载参数
     * @return 加载的资源对象
     * @throws IOException 加载失败时抛出
     */
    T load(String path, LoadParams params) throws IOException;

    /**
     * 卸载资源
     *
     * @param resource 要卸载的资源
     */
    void unload(T resource);

    /**
     * 检查资源是否已加载
     *
     * @param path 资源路径
     * @return true 如果资源已加载
     */
    boolean isLoaded(String path);

    /**
     * 获取加载器支持的资源类型
     *
     * @return 资源类型类
     */
    Class<T> getResourceType();
}
