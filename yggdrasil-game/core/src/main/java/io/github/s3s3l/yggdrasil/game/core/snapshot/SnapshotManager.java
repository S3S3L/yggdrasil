package io.github.s3s3l.yggdrasil.game.core.snapshot;

import io.github.s3s3l.yggdrasil.game.core.basic.ID;

import java.util.Collection;

/**
 * 快照管理器
 * 负责管理游戏对象和场景的快照，提供保存、加载、删除等功能
 *
 * @author s3s3l
 */
public class SnapshotManager {
    private static final SnapshotManager INSTANCE = new SnapshotManager();

    /**
     * 默认使用内存存储
     */
    private SnapshotStore snapshotStore;

    /**
     * 默认使用 JSON 序列化器
     */
    private SnapshotSerializer serializer;

    private SnapshotManager() {
        this.snapshotStore = new MemorySnapshotStore();
        this.serializer = new JsonSnapshotSerializer();
    }

    public static SnapshotManager get() {
        return INSTANCE;
    }

    /**
     * 设置快照存储器
     *
     * @param snapshotStore 快照存储器
     */
    public void setSnapshotStore(SnapshotStore snapshotStore) {
        this.snapshotStore = snapshotStore;
    }

    /**
     * 设置快照序列化器
     *
     * @param serializer 快照序列化器
     */
    public void setSerializer(SnapshotSerializer serializer) {
        this.serializer = serializer;
    }

    /**
     * 获取当前快照存储器
     *
     * @return 快照存储器
     */
    public SnapshotStore getSnapshotStore() {
        return snapshotStore;
    }

    /**
     * 获取当前快照序列化器
     *
     * @return 快照序列化器
     */
    public SnapshotSerializer getSerializer() {
        return serializer;
    }

    /**
     * 保存快照
     *
     * @param snapshot 快照对象
     */
    public void saveSnapshot(ObjectSnapshot snapshot) {
        if (snapshot != null) {
            snapshotStore.save(snapshot);
        }
    }

    /**
     * 获取最新快照
     *
     * @param target 目标对象
     * @return 快照对象，如果不存在则返回 null
     */
    public ObjectSnapshot getLatestSnapshot(ID target) {
        if (target == null) {
            return null;
        }
        return (ObjectSnapshot) snapshotStore.get(target.getId());
    }

    /**
     * 获取指定 ID 的快照
     *
     * @param id 快照 ID
     * @return 快照对象，如果不存在则返回 null
     */
    public ObjectSnapshot getSnapshot(long id) {
        return (ObjectSnapshot) snapshotStore.get(id);
    }

    /**
     * 删除快照
     *
     * @param target 目标对象
     * @return true 如果删除成功
     */
    public boolean deleteSnapshot(ID target) {
        if (target == null) {
            return false;
        }
        return snapshotStore.delete(target.getId());
    }

    /**
     * 删除指定 ID 的快照
     *
     * @param id 快照 ID
     * @return true 如果删除成功
     */
    public boolean deleteSnapshot(long id) {
        return snapshotStore.delete(id);
    }

    /**
     * 检查快照是否存在
     *
     * @param target 目标对象
     * @return true 如果快照存在
     */
    public boolean hasSnapshot(ID target) {
        if (target == null) {
            return false;
        }
        return snapshotStore.exists(target.getId());
    }

    /**
     * 清除所有快照
     */
    public void clearAllSnapshots() {
        snapshotStore.clear();
    }

    /**
     * 获取快照数量
     *
     * @return 快照数量
     */
    public int getSnapshotCount() {
        return snapshotStore.count();
    }

    /**
     * 获取所有快照
     *
     * @return 所有快照的集合
     */
    public Collection<Snapshot> getAllSnapshots() {
        return snapshotStore.getAll();
    }
}
