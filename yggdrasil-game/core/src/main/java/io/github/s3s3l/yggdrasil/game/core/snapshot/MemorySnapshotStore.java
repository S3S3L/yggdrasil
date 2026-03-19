package io.github.s3s3l.yggdrasil.game.core.snapshot;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存快照存储器
 * 使用 ConcurrentHashMap 存储快照，适用于临时存储和测试场景
 */
public class MemorySnapshotStore implements SnapshotStore {

    private final Map<Long, Snapshot> snapshots = new ConcurrentHashMap<>();

    @Override
    public void save(Snapshot snapshot) {
        snapshots.put(snapshot.getId(), snapshot);
    }

    @Override
    public Snapshot get(long id) {
        return snapshots.get(id);
    }

    @Override
    public boolean delete(long id) {
        return snapshots.remove(id) != null;
    }

    @Override
    public boolean exists(long id) {
        return snapshots.containsKey(id);
    }

    @Override
    public Collection<Snapshot> getAll() {
        return snapshots.values();
    }

    @Override
    public void clear() {
        snapshots.clear();
    }

    @Override
    public int count() {
        return snapshots.size();
    }
}
