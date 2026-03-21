package io.github.s3s3l.yggdrasil.game.core.render.batch;

/**
 * 批次渲染数据接口
 * 所有需要批次渲染的数据都应实现此接口
 */
public interface BatchRenderData {

    /**
     * 获取批次键
     * 批次键用于标识哪些渲染数据可以合并到同一批次
     * 通常使用纹理 ID 或材质 ID
     *
     * @return 批次键
     */
    String getBatchKey();

    /**
     * 获取渲染优先级
     * 数值越小优先级越高，越先渲染
     *
     * @return 渲染优先级
     */
    default int getRenderOrder() {
        return 0;
    }

    /**
     * 检查数据是否有效
     *
     * @return true 如果数据有效
     */
    default boolean isValid() {
        return true;
    }
}
