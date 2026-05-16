package io.github.s3s3l.yggdrasil.game.core.pool;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

/**
 * 通用对象池
 * 支持对象的借用、归还、自动扩容和最大容量限制
 * 线程安全实现
 *
 * @param <T> 池化对象类型
 */
@Slf4j
public class ObjectPool<T extends PoolableObject<T>> {

    /**
     * 对象池队列
     */
    private final Queue<T> pool;

    /**
     * 最大池容量
     */
    private final int maxPoolSize;

    /**
     * 初始池大小
     */
    private final int initialPoolSize;

    /**
     * 当前池大小（包括已借出和可用的对象总数）
     */
    private final AtomicInteger currentPoolSize;

    /**
     * 已借出的对象数量
     */
    private final AtomicInteger borrowedCount;

    /**
     * 对象工厂接口
     */
    private final PoolableObjectFactory<T> factory;

    /**
     * 池统计信息
     */
    private PoolStats stats;

    /**
     * 创建对象池
     *
     * @param factory 对象工厂
     * @param initialPoolSize 初始池大小
     * @param maxPoolSize 最大池大小，-1 表示无限制
     */
    public ObjectPool(PoolableObjectFactory<T> factory, int initialPoolSize, int maxPoolSize) {
        if (factory == null) {
            throw new IllegalArgumentException("Factory cannot be null");
        }
        if (initialPoolSize <= 0) {
            throw new IllegalArgumentException("Initial pool size must be positive");
        }
        if (maxPoolSize != -1 && maxPoolSize < initialPoolSize) {
            throw new IllegalArgumentException("Max pool size must be >= initial pool size or -1");
        }

        this.factory = factory;
        this.initialPoolSize = initialPoolSize;
        this.maxPoolSize = maxPoolSize == -1 ? Integer.MAX_VALUE : maxPoolSize;
        this.pool = new ConcurrentLinkedQueue<>();
        this.currentPoolSize = new AtomicInteger(0);
        this.borrowedCount = new AtomicInteger(0);
        this.stats = new PoolStats();

        // 预创建初始对象
        prefill();
    }

    /**
     * 创建对象池（默认最大池大小为初始大小的 2 倍）
     *
     * @param factory 对象工厂
     * @param initialPoolSize 初始池大小
     */
    public ObjectPool(PoolableObjectFactory<T> factory, int initialPoolSize) {
        this(factory, initialPoolSize, initialPoolSize * 2);
    }

    /**
     * 预填充对象池
     */
    private void prefill() {
        for (int i = 0; i < initialPoolSize; i++) {
            try {
                T obj = factory.create();
                pool.offer(obj);
                currentPoolSize.incrementAndGet();
            } catch (Exception e) {
                // 记录创建失败，继续创建其他对象
                log.warn("Failed to create pooled object", e);
            }
        }
    }

    /**
     * 从池中借用对象
     * 如果池为空且未达上限，创建新对象；否则返回 null
     *
     * @return 可用的对象，无法提供时返回 null
     */
    public T borrow() {
        // 尝试从池中获取
        T obj = pool.poll();

        if (obj != null) {
            borrowedCount.incrementAndGet();
            stats.recordBorrow();
            // 重置对象状态
            obj.reset();
            return obj;
        }

        // 池为空，尝试创建新对象
        int currentSize = currentPoolSize.get();
        if (currentSize < maxPoolSize) {
            if (currentPoolSize.compareAndSet(currentSize, currentSize + 1)) {
                try {
                    obj = factory.create();
                    borrowedCount.incrementAndGet();
                    stats.recordBorrow();
                    return obj;
                } catch (Exception e) {
                    currentPoolSize.decrementAndGet();
                    stats.recordCreateFailure();
                    log.warn("Failed to create new pooled object", e);
                    return null;
                }
            }
            // CAS 失败，重试
            return borrow();
        }

        // 已达上限，无法提供对象
        stats.recordExhausted();
        return null;
    }

    /**
     * 归还对象到池中
     *
     * @param obj 要归还的对象
     * @return true 如果归还成功，false 如果对象不属于此池或池已满
     */
    public boolean returnObject(T obj) {
        if (obj == null) {
            return false;
        }

        // 检查对象是否已被池化（通过验证）
        if (!validateObject(obj)) {
            return false;
        }

        // 如果池未满，归还到池中
        if (pool.size() < maxPoolSize) {
            obj.onReturn();
            boolean added = pool.offer(obj);
            if (added) {
                borrowedCount.decrementAndGet();
                stats.recordReturn();
                return true;
            }
        }

        // 池已满，销毁对象
        borrowedCount.decrementAndGet();
        currentPoolSize.decrementAndGet();
        try {
            factory.destroy(obj);
        } catch (Exception e) {
            log.warn("Failed to destroy pooled object", e);
        }
        stats.recordDiscard();
        return true;
    }

    /**
     * 验证对象是否有效
     *
     * @param obj 要验证的对象
     * @return true 如果对象有效
     */
    protected boolean validateObject(T obj) {
        try {
            return factory.validate(obj);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取池中可用对象数量
     *
     * @return 可用对象数量
     */
    public int getAvailableCount() {
        return pool.size();
    }

    /**
     * 获取已借出对象数量
     *
     * @return 已借出对象数量
     */
    public int getBorrowedCount() {
        return borrowedCount.get();
    }

    /**
     * 获取当前池大小（包括已借出和可用的对象总数）
     *
     * @return 池大小
     */
    public int getCurrentPoolSize() {
        return currentPoolSize.get();
    }

    /**
     * 获取最大池大小
     *
     * @return 最大池大小
     */
    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    /**
     * 获取初始池大小
     *
     * @return 初始池大小
     */
    public int getInitialPoolSize() {
        return initialPoolSize;
    }

    /**
     * 获取池统计信息
     *
     * @return 池统计信息
     */
    public PoolStats getStats() {
        return stats;
    }

    /**
     * 清空对象池
     * 销毁所有对象
     */
    public void clear() {
        T obj;
        while ((obj = pool.poll()) != null) {
            try {
                factory.destroy(obj);
                currentPoolSize.decrementAndGet();
            } catch (Exception e) {
                log.warn("Failed to destroy object during clear", e);
            }
        }
        borrowedCount.set(0);
        stats.recordClear();
    }

    /**
     * 关闭对象池
     * 清空并禁用池
     */
    public void shutdown() {
        clear();
    }

    /**
     * 池统计信息
     */
    public static class PoolStats {
        private final AtomicInteger borrowCount = new AtomicInteger(0);
        private final AtomicInteger returnCount = new AtomicInteger(0);
        private final AtomicInteger createCount = new AtomicInteger(0);
        private final AtomicInteger destroyCount = new AtomicInteger(0);
        private final AtomicInteger exhaustedCount = new AtomicInteger(0);
        private final AtomicInteger createFailureCount = new AtomicInteger(0);
        private final AtomicInteger discardCount = new AtomicInteger(0);
        private final AtomicInteger clearCount = new AtomicInteger(0);

        private void recordBorrow() {
            borrowCount.incrementAndGet();
        }

        private void recordReturn() {
            returnCount.incrementAndGet();
        }

        private void recordCreateFailure() {
            createFailureCount.incrementAndGet();
        }

        private void recordExhausted() {
            exhaustedCount.incrementAndGet();
        }

        private void recordDiscard() {
            discardCount.incrementAndGet();
        }

        private void recordClear() {
            clearCount.incrementAndGet();
        }

        /**
         * 获取借用次数
         */
        public int getBorrowCount() {
            return borrowCount.get();
        }

        /**
         * 获取归还次数
         */
        public int getReturnCount() {
            return returnCount.get();
        }

        /**
         * 获取创建失败次数
         */
        public int getCreateFailureCount() {
            return createFailureCount.get();
        }

        /**
         * 获取池耗尽次数（请求时池为空且已达上限）
         */
        public int getExhaustedCount() {
            return exhaustedCount.get();
        }

        /**
         * 获取丢弃对象次数（归还时池已满）
         */
        public int getDiscardCount() {
            return discardCount.get();
        }

        /**
         * 获取清空次数
         */
        public int getClearCount() {
            return clearCount.get();
        }

        @Override
        public String toString() {
            return String.format("PoolStats[borrow=%d, return=%d, exhausted=%d, discard=%d, createFail=%d]",
                    borrowCount.get(), returnCount.get(), exhaustedCount.get(),
                    discardCount.get(), createFailureCount.get());
        }
    }
}
