package io.github.s3s3l.yggdrasil.game.core.render.batch;

import java.util.ArrayList;
import java.util.List;

/**
 * 渲染批次
 * 包含相同纹理/材质的渲染数据
 *
 * @param <T> 渲染数据类型
 */
public class RenderBatch<T extends BatchRenderData> {

    /**
     * 批次键（纹理/材质 ID）
     */
    private final String batchKey;

    /**
     * 渲染数据列表
     */
    private final List<T> dataList;

    /**
     * 批次是否已提交渲染
     */
    private boolean submitted;

    /**
     * 创建渲染批次
     *
     * @param batchKey 批次键
     */
    public RenderBatch(String batchKey) {
        this.batchKey = batchKey;
        this.dataList = new ArrayList<>();
        this.submitted = false;
    }

    /**
     * 获取批次键
     *
     * @return 批次键
     */
    public String getBatchKey() {
        return batchKey;
    }

    /**
     * 获取渲染数据列表
     *
     * @return 渲染数据列表
     */
    public List<T> getDataList() {
        return dataList;
    }

    /**
     * 获取批次大小
     *
     * @return 渲染数据数量
     */
    public int getSize() {
        return dataList.size();
    }

    /**
     * 检查批次是否为空
     *
     * @return true 如果为空
     */
    public boolean isEmpty() {
        return dataList.isEmpty();
    }

    /**
     * 标记批次已提交
     */
    public void markSubmitted() {
        this.submitted = true;
    }

    /**
     * 检查批次是否已提交
     *
     * @return true 如果已提交
     */
    public boolean isSubmitted() {
        return submitted;
    }

    /**
     * 清空批次
     */
    public void clear() {
        dataList.clear();
        submitted = false;
    }

    @Override
    public String toString() {
        return "RenderBatch{key='" + batchKey + "', size=" + dataList.size() + "}";
    }
}
