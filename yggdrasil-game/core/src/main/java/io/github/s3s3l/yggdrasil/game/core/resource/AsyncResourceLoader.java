package io.github.s3s3l.yggdrasil.game.core.resource;

import java.io.IOException;

/**
 * 异步资源加载器接口
 * 支持异步加载资源并回调进度
 *
 * @param <T> 资源类型
 */
public interface AsyncResourceLoader<T> extends ResourceLoader<T> {

    /**
     * 异步加载资源
     *
     * @param path     资源路径
     * @param callback 加载完成回调
     * @return 加载任务
     */
    AsyncLoadTask<T> loadAsync(String path, LoadCallback<T> callback);

    /**
     * 异步加载资源
     *
     * @param path     资源路径
     * @param params   加载参数
     * @param callback 加载完成回调
     * @return 加载任务
     */
    AsyncLoadTask<T> loadAsync(String path, LoadParams params, LoadCallback<T> callback);

    /**
     * 加载进度回调接口
     *
     * @param <T> 资源类型
     */
    interface LoadCallback<T> {
        /**
         * 加载开始时调用
         *
         * @param path 资源路径
         */
        default void onStart(String path) {
        }

        /**
         * 加载进度更新时调用
         *
         * @param path     资源路径
         * @param progress 进度（0.0 - 1.0）
         * @param message  进度消息
         */
        default void onProgress(String path, float progress, String message) {
        }

        /**
         * 加载完成时调用
         *
         * @param path     资源路径
         * @param resource 加载的资源
         */
        void onComplete(String path, T resource);

        /**
         * 加载失败时调用
         *
         * @param path      资源路径
         * @param exception 异常信息
         */
        default void onError(String path, Exception exception) {
        }
    }
}
