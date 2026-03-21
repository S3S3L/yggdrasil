package io.github.s3s3l.yggdrasil.game.core.render.batch;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 批次渲染统计信息
 */
public class BatchStats {

    /**
     * 开始批次构建次数
     */
    private final AtomicLong beginCount = new AtomicLong(0);

    /**
     * 结束批次构建次数
     */
    private final AtomicLong endCount = new AtomicLong(0);

    /**
     * 添加渲染数据次数
     */
    private final AtomicLong addCount = new AtomicLong(0);

    /**
     * 总批次数量（最后一次 end() 调用后）
     */
    private final AtomicLong totalBatches = new AtomicLong(0);

    /**
     * 总渲染数据数量（最后一次 end() 调用后）
     */
    private final AtomicLong totalRenderData = new AtomicLong(0);

    /**
     * 平均批次大小
     */
    private volatile float averageBatchSize;

    /**
     * 记录 begin() 调用
     */
    void recordBegin() {
        beginCount.incrementAndGet();
    }

    /**
     * 记录 end() 调用
     *
     * @param batchCount 批次数量
     */
    void recordEnd(int batchCount) {
        endCount.incrementAndGet();
        totalBatches.set(batchCount);
    }

    /**
     * 记录 add() 调用
     */
    void recordAdd() {
        addCount.incrementAndGet();
    }

    /**
     * 更新统计信息
     *
     * @param totalData 总渲染数据数量
     * @param batchCount 批次数量
     */
    public void update(int totalData, int batchCount) {
        totalRenderData.set(totalData);
        totalBatches.set(batchCount);
        averageBatchSize = batchCount > 0 ? (float) totalData / batchCount : 0;
    }

    /**
     * 获取开始批次构建次数
     *
     * @return 开始次数
     */
    public long getBeginCount() {
        return beginCount.get();
    }

    /**
     * 获取结束批次构建次数
     *
     * @return 结束次数
     */
    public long getEndCount() {
        return endCount.get();
    }

    /**
     * 获取添加渲染数据次数
     *
     * @return 添加次数
     */
    public long getAddCount() {
        return addCount.get();
    }

    /**
     * 获取总批次数量
     *
     * @return 批次数量
     */
    public long getTotalBatches() {
        return totalBatches.get();
    }

    /**
     * 获取总渲染数据数量
     *
     * @return 渲染数据数量
     */
    public long getTotalRenderData() {
        return totalRenderData.get();
    }

    /**
     * 获取平均批次大小
     *
     * @return 平均批次大小
     */
    public float getAverageBatchSize() {
        return averageBatchSize;
    }

    /**
     * 获取批次合并率（渲染数据数量/批次数量）
     * 数值越大表示合并效果越好
     *
     * @return 批次合并率
     */
    public float getBatchMergeRatio() {
        long batches = totalBatches.get();
        return batches > 0 ? (float) totalRenderData.get() / batches : 0;
    }

    @Override
    public String toString() {
        return String.format("BatchStats[batches=%d, data=%d, avgSize=%.2f, ratio=%.2f]",
                totalBatches.get(), totalRenderData.get(), averageBatchSize, getBatchMergeRatio());
    }
}
