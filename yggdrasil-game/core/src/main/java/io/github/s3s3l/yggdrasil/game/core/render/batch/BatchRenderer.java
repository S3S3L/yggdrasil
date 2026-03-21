package io.github.s3s3l.yggdrasil.game.core.render.batch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 批次渲染器
 * 合并相同纹理/材质的渲染调用，减少 Draw Call 数量
 *
 * @param <T> 渲染数据类型
 */
public class BatchRenderer<T extends BatchRenderData> {

    /**
     * 批次数据映射（纹理/材质 ID -> 批次列表）
     */
    private final Map<String, RenderBatch<T>> batches;

    /**
     * 当前正在构建的批次
     */
    private RenderBatch<T> currentBatch;

    /**
     * 最大批次大小
     */
    private final int maxBatchSize;

    /**
     * 是否正在批次构建中
     */
    private boolean isBuilding;

    /**
     * 渲染统计信息
     */
    private BatchStats stats;

    /**
     * 创建批次渲染器
     *
     * @param maxBatchSize 最大批次大小
     */
    public BatchRenderer(int maxBatchSize) {
        this.maxBatchSize = maxBatchSize;
        this.batches = new HashMap<>();
        this.stats = new BatchStats();
    }

    /**
     * 创建批次渲染器（默认批次大小）
     */
    public BatchRenderer() {
        this(1000);
    }

    /**
     * 开始批次构建
     */
    public void begin() {
        if (isBuilding) {
            throw new IllegalStateException("Batch building already in progress");
        }
        isBuilding = true;
        batches.clear();
        currentBatch = null;
        stats.recordBegin();
    }

    /**
     * 添加渲染数据到批次
     *
     * @param data 渲染数据
     */
    public void add(T data) {
        if (!isBuilding) {
            throw new IllegalStateException("Must call begin() before adding render data");
        }

        String key = data.getBatchKey();

        // 如果当前批次为空或批次键不同，创建新批次
        if (currentBatch == null || !currentBatch.getBatchKey().equals(key)) {
            currentBatch = batches.computeIfAbsent(key, k -> new RenderBatch<>(key));
        }

        // 如果批次已满，创建新批次
        if (currentBatch.getDataList().size() >= maxBatchSize) {
            currentBatch = new RenderBatch<>(key);
            batches.put(key + "_" + currentBatch.hashCode(), currentBatch);
        }

        currentBatch.getDataList().add(data);
        stats.recordAdd();
    }

    /**
     * 结束批次构建
     *
     * @return 批次列表
     */
    public List<RenderBatch<T>> end() {
        if (!isBuilding) {
            throw new IllegalStateException("Not currently building batches");
        }
        isBuilding = false;

        // 计算总渲染数据数量
        int totalData = 0;
        for (RenderBatch<T> batch : batches.values()) {
            totalData += batch.getDataList().size();
        }

        stats.recordEnd(batches.size());
        stats.update(totalData, batches.size());
        return new ArrayList<>(batches.values());
    }

    /**
     * 获取所有批次
     *
     * @return 批次映射
     */
    public Map<String, RenderBatch<T>> getBatches() {
        return new HashMap<>(batches);
    }

    /**
     * 获取批次数量
     *
     * @return 批次数量
     */
    public int getBatchCount() {
        return batches.size();
    }

    /**
     * 清空所有批次
     */
    public void clear() {
        batches.clear();
        currentBatch = null;
        isBuilding = false;
    }

    /**
     * 获取渲染统计信息
     *
     * @return 渲染统计
     */
    public BatchStats getStats() {
        return stats;
    }

    /**
     * 获取最大批次大小
     *
     * @return 最大批次大小
     */
    public int getMaxBatchSize() {
        return maxBatchSize;
    }

    /**
     * 检查是否正在构建批次
     *
     * @return true 如果正在构建
     */
    public boolean isBuilding() {
        return isBuilding;
    }
}
