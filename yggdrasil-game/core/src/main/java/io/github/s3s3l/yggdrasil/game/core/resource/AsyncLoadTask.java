package io.github.s3s3l.yggdrasil.game.core.resource;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 异步加载任务
 * 封装异步加载操作的状态和控制
 *
 * @param <T> 资源类型
 */
public class AsyncLoadTask<T> {

    /**
     * 任务 ID
     */
    private final String taskId;

    /**
     * 资源路径
     */
    private final String path;

    /**
     * 资源类型
     */
    private final Class<T> resourceType;

    /**
     * 加载进度（0.0 - 1.0）
     */
    private volatile float progress;

    /**
     * 任务状态
     */
    private volatile TaskStatus status;

    /**
     * 是否取消
     */
    private final AtomicBoolean cancelled;

    /**
     * 关联的 Future
     */
    private Future<T> future;

    /**
     * 加载结果
     */
    private T result;

    /**
     * 错误信息
     */
    private Exception error;

    /**
     * 创建异步加载任务
     *
     * @param path         资源路径
     * @param resourceType 资源类型
     */
    public AsyncLoadTask(String path, Class<T> resourceType) {
        this.taskId = generateTaskId();
        this.path = path;
        this.resourceType = resourceType;
        this.progress = 0.0f;
        this.status = TaskStatus.PENDING;
        this.cancelled = new AtomicBoolean(false);
    }

    /**
     * 获取任务 ID
     *
     * @return 任务 ID
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * 获取资源路径
     *
     * @return 资源路径
     */
    public String getPath() {
        return path;
    }

    /**
     * 获取资源类型
     *
     * @return 资源类型
     */
    public Class<T> getResourceType() {
        return resourceType;
    }

    /**
     * 获取加载进度
     *
     * @return 进度（0.0 - 1.0）
     */
    public float getProgress() {
        return progress;
    }

    /**
     * 设置加载进度
     *
     * @param progress 进度
     */
    protected void setProgress(float progress) {
        this.progress = Math.max(0.0f, Math.min(1.0f, progress));
    }

    /**
     * 获取任务状态
     *
     * @return 任务状态
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * 设置任务状态
     *
     * @param status 任务状态
     */
    protected void setStatus(TaskStatus status) {
        this.status = status;
    }

    /**
     * 检查任务是否取消
     *
     * @return true 如果已取消
     */
    public boolean isCancelled() {
        return cancelled.get();
    }

    /**
     * 取消任务
     */
    public void cancel() {
        cancelled.set(true);
        if (future != null) {
            future.cancel(true);
        }
        status = TaskStatus.CANCELLED;
    }

    /**
     * 设置关联的 Future
     *
     * @param future Future 对象
     */
    protected void setFuture(Future<T> future) {
        this.future = future;
    }

    /**
     * 获取加载结果
     *
     * @return 加载的资源
     */
    public T getResult() {
        return result;
    }

    /**
     * 设置加载结果
     *
     * @param result 加载的资源
     */
    protected void setResult(T result) {
        this.result = result;
    }

    /**
     * 获取错误信息
     *
     * @return 错误信息
     */
    public Exception getError() {
        return error;
    }

    /**
     * 设置错误信息
     *
     * @param error 错误信息
     */
    protected void setError(Exception error) {
        this.error = error;
    }

    /**
     * 检查任务是否完成
     *
     * @return true 如果已完成
     */
    public boolean isDone() {
        return status == TaskStatus.COMPLETED ||
               status == TaskStatus.FAILED ||
               status == TaskStatus.CANCELLED;
    }

    /**
     * 生成任务 ID
     *
     * @return 任务 ID
     */
    private static String generateTaskId() {
        return "task_" + System.nanoTime() + "_" + Thread.currentThread().getId();
    }

    /**
     * 任务状态枚举
     */
    public enum TaskStatus {
        /**
         * 等待中
         */
        PENDING,
        /**
         * 加载中
         */
        LOADING,
        /**
         * 已完成
         */
        COMPLETED,
        /**
         * 失败
         */
        FAILED,
        /**
         * 已取消
         */
        CANCELLED
    }
}
