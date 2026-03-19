package io.github.s3s3l.yggdrasil.game.core.snapshot;

import java.util.Collection;

/**
 * 快照存储接口
 * 提供快照的持久化存储和检索功能
 */
public interface SnapshotStore {

    /**
     * 保存快照
     *
     * @param snapshot 快照对象
     */
    void save(Snapshot snapshot);

    /**
     * 根据 ID 获取快照
     *
     * @param id 快照 ID
     * @return 快照对象，如果不存在则返回 null
     */
    Snapshot get(long id);

    /**
     * 删除快照
     *
     * @param id 快照 ID
     * @return true 如果删除成功
     */
    boolean delete(long id);

    /**
     * 检查快照是否存在
     *
     * @param id 快照 ID
     * @return true 如果快照存在
     */
    boolean exists(long id);

    /**
     * 获取所有快照
     *
     * @return 所有快照的集合
     */
    Collection<Snapshot> getAll();

    /**
     * 清除所有快照
     */
    void clear();

    /**
     * 获取快照数量
     *
     * @return 快照数量
     */
    int count();
}
