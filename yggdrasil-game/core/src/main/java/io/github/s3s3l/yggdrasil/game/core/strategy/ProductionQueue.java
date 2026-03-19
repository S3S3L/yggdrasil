package io.github.s3s3l.yggdrasil.game.core.strategy;

import io.github.s3s3l.yggdrasil.game.core.basic.ID;

import java.util.*;

/**
 * 生产队列
 * 管理建筑和单位的生产队列
 */
public class ProductionQueue implements ID {

    private final long numericId;
    private final String name;
    private final List<ProductionItem> queue;
    private final int maxQueueSize;
    private ProductionItem currentItem;
    private int progress;

    private ProductionQueue(Builder builder) {
        this.numericId = builder.id;
        this.name = builder.name;
        this.queue = builder.queue;
        this.maxQueueSize = builder.maxQueueSize;
        this.currentItem = builder.currentItem;
        this.progress = builder.progress;
    }

    @Override
    public long getId() {
        return numericId;
    }

    @Override
    public void setId(long id) {
        // ID is immutable
    }

    public String getName() {
        return name;
    }

    /**
     * 获取生产队列
     *
     * @return 队列列表
     */
    public List<ProductionItem> getQueue() {
        return Collections.unmodifiableList(queue);
    }

    /**
     * 获取最大队列大小
     *
     * @return 最大队列大小
     */
    public int getMaxQueueSize() {
        return maxQueueSize;
    }

    /**
     * 获取当前生产项目
     *
     * @return 当前项目
     */
    public ProductionItem getCurrentItem() {
        return currentItem;
    }

    /**
     * 获取当前进度
     *
     * @return 进度 (0-100)
     */
    public int getProgress() {
        return progress;
    }

    /**
     * 检查队列是否已满
     *
     * @return true 如果队列已满
     */
    public boolean isFull() {
        return queue.size() >= maxQueueSize;
    }

    /**
     * 检查是否正在生产
     *
     * @return true 如果正在生产
     */
    public boolean isProducing() {
        return currentItem != null;
    }

    /**
     * 添加生产项目
     *
     * @param item 生产项目
     * @return true 如果添加成功
     */
    public boolean addToQueue(ProductionItem item) {
        if (isFull()) {
            return false;
        }

        if (currentItem == null) {
            currentItem = item;
            progress = 0;
        } else {
            queue.add(item);
        }
        return true;
    }

    /**
     * 移除生产项目
     *
     * @param index 索引
     * @return 被移除的项目
     */
    public ProductionItem removeItem(int index) {
        if (index >= 0 && index < queue.size()) {
            return queue.remove(index);
        }
        return null;
    }

    /**
     * 取消当前生产
     *
     * @return 被取消的项目
     */
    public ProductionItem cancelCurrent() {
        ProductionItem cancelled = currentItem;
        currentItem = null;
        progress = 0;
        return cancelled;
    }

    /**
     * 完成当前生产
     *
     * @return 完成的项目
     */
    public ProductionItem completeCurrent() {
        ProductionItem completed = currentItem;
        if (completed != null) {
            currentItem = null;
            progress = 0;

            // 开始下一个
            if (!queue.isEmpty()) {
                currentItem = queue.remove(0);
            }
        }
        return completed;
    }

    /**
     * 增加进度
     *
     * @param amount 进度增量
     * @return true 如果完成生产
     */
    public boolean addProgress(int amount) {
        if (currentItem == null) {
            return false;
        }

        progress += amount;
        if (progress >= 100) {
            completeCurrent();
            return true;
        }
        return false;
    }

    /**
     * 清空队列
     */
    public void clear() {
        queue.clear();
        currentItem = null;
        progress = 0;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long id;
        private String name;
        private List<ProductionItem> queue = new ArrayList<>();
        private int maxQueueSize = 5;
        private ProductionItem currentItem;
        private int progress;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder queue(List<ProductionItem> queue) {
            this.queue = queue;
            return this;
        }

        public Builder maxQueueSize(int maxQueueSize) {
            this.maxQueueSize = maxQueueSize;
            return this;
        }

        public Builder currentItem(ProductionItem currentItem) {
            this.currentItem = currentItem;
            return this;
        }

        public Builder progress(int progress) {
            this.progress = progress;
            return this;
        }

        public ProductionQueue build() {
            return new ProductionQueue(this);
        }
    }
}
