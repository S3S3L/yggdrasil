package io.github.s3s3l.yggdrasil.game.core.render.dirty;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 脏标记统计信息
 */
public class DirtyStats {

    /**
     * 总标记次数
     */
    private final AtomicLong markCount = new AtomicLong(0);

    /**
     * 总清除次数
     */
    private final AtomicLong clearCount = new AtomicLong(0);

    /**
     * 批量更新开始次数
     */
    private final AtomicLong batchStartCount = new AtomicLong(0);

    /**
     * 批量更新期间标记的脏对象总数
     */
    private final AtomicLong batchMarkedTotal = new AtomicLong(0);

    /**
     * 清空所有标记的次数
     */
    private final AtomicLong clearAllCount = new AtomicLong(0);

    /**
     * 清空所有标记时清除的对象总数
     */
    private final AtomicLong clearAllObjectsTotal = new AtomicLong(0);

    /**
     * 记录标记操作
     */
    void recordMark() {
        markCount.incrementAndGet();
    }

    /**
     * 记录清除操作
     */
    void recordClear() {
        clearCount.incrementAndGet();
    }

    /**
     * 记录批量更新开始
     */
    void recordBatchStart() {
        batchStartCount.incrementAndGet();
    }

    /**
     * 记录批量更新结束
     *
     * @param newDirty 新增的脏对象数量
     */
    void recordBatchEnd(int newDirty) {
        batchMarkedTotal.addAndGet(newDirty);
    }

    /**
     * 记录清空所有操作
     *
     * @param count 清除的对象数量
     */
    void recordClearAll(int count) {
        clearAllCount.incrementAndGet();
        clearAllObjectsTotal.addAndGet(count);
    }

    /**
     * 获取总标记次数
     *
     * @return 标记次数
     */
    public long getMarkCount() {
        return markCount.get();
    }

    /**
     * 获取总清除次数
     *
     * @return 清除次数
     */
    public long getClearCount() {
        return clearCount.get();
    }

    /**
     * 获取批量更新开始次数
     *
     * @return 批量更新次数
     */
    public long getBatchStartCount() {
        return batchStartCount.get();
    }

    /**
     * 获取批量更新期间标记的脏对象总数
     *
     * @return 批量标记总数
     */
    public long getBatchMarkedTotal() {
        return batchMarkedTotal.get();
    }

    /**
     * 获取清空所有操作次数
     *
     * @return 清空次数
     */
    public long getClearAllCount() {
        return clearAllCount.get();
    }

    /**
     * 获取清空所有时清除的对象总数
     *
     * @return 清除对象总数
     */
    public long getClearAllObjectsTotal() {
        return clearAllObjectsTotal.get();
    }

    /**
     * 获取净脏对象数（标记次数 - 清除次数）
     *
     * @return 净脏对象数
     */
    public long getNetDirtyCount() {
        return markCount.get() - clearCount.get();
    }

    @Override
    public String toString() {
        return String.format("DirtyStats[mark=%d, clear=%d, batch=%d, net=%d]",
                markCount.get(), clearCount.get(), batchStartCount.get(), getNetDirtyCount());
    }
}
