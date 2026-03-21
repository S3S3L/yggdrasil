package io.github.s3s3l.yggdrasil.game.core.render.dirty;

import java.util.HashSet;
import java.util.Set;

/**
 * 脏标记管理器
 * 优化脏标记机制，减少不必要的重渲染
 */
public class DirtyMarkManager {

    /**
     * 脏对象集合
     */
    private final Set<DirtyMarkable> dirtyObjects;

    /**
     * 是否正在批量更新中
     */
    private boolean batchUpdating;

    /**
     * 批量更新期间暂存的脏对象
     */
    private final Set<DirtyMarkable> pendingDirtyObjects;

    /**
     * 脏标记统计信息
     */
    private DirtyStats stats;

    /**
     * 创建脏标记管理器
     */
    public DirtyMarkManager() {
        this.dirtyObjects = new HashSet<>();
        this.pendingDirtyObjects = new HashSet<>();
        this.batchUpdating = false;
        this.stats = new DirtyStats();
    }

    /**
     * 标记对象为脏对象
     *
     * @param object 对象
     */
    public void markDirty(DirtyMarkable object) {
        if (object == null) {
            return;
        }

        if (batchUpdating) {
            pendingDirtyObjects.add(object);
        } else {
            if (dirtyObjects.add(object)) {
                stats.recordMark();
            }
        }
    }

    /**
     * 清除对象的脏标记
     *
     * @param object 对象
     */
    public void clearDirty(DirtyMarkable object) {
        if (object == null) {
            return;
        }

        if (dirtyObjects.remove(object)) {
            stats.recordClear();
        }
    }

    /**
     * 检查对象是否为脏对象
     *
     * @param object 对象
     * @return true 如果是脏对象
     */
    public boolean isDirty(DirtyMarkable object) {
        return dirtyObjects.contains(object);
    }

    /**
     * 获取所有脏对象
     *
     * @return 脏对象集合
     */
    public Set<DirtyMarkable> getDirtyObjects() {
        return new HashSet<>(dirtyObjects);
    }

    /**
     * 获取脏对象数量
     *
     * @return 脏对象数量
     */
    public int getDirtyCount() {
        return dirtyObjects.size();
    }

    /**
     * 开始批量更新
     * 在批量更新期间，所有标记操作会被暂存
     */
    public void beginBatchUpdate() {
        if (batchUpdating) {
            throw new IllegalStateException("Already in batch update");
        }
        batchUpdating = true;
        pendingDirtyObjects.clear();
        stats.recordBatchStart();
    }

    /**
     * 结束批量更新
     * 将暂存的脏对象合并到主集合中
     */
    public void endBatchUpdate() {
        if (!batchUpdating) {
            throw new IllegalStateException("Not in batch update");
        }

        batchUpdating = false;
        int newDirty = 0;
        for (DirtyMarkable object : pendingDirtyObjects) {
            if (dirtyObjects.add(object)) {
                newDirty++;
            }
        }
        stats.recordBatchEnd(newDirty);
        pendingDirtyObjects.clear();
    }

    /**
     * 清空所有脏标记
     */
    public void clearAll() {
        int count = dirtyObjects.size();
        dirtyObjects.clear();
        stats.recordClearAll(count);
    }

    /**
     * 获取统计信息
     *
     * @return 统计信息
     */
    public DirtyStats getStats() {
        return stats;
    }

    /**
     * 重置统计信息
     */
    public void resetStats() {
        this.stats = new DirtyStats();
    }

    /**
     * 检查是否正在批量更新
     *
     * @return true 如果正在批量更新
     */
    public boolean isBatchUpdating() {
        return batchUpdating;
    }
}
