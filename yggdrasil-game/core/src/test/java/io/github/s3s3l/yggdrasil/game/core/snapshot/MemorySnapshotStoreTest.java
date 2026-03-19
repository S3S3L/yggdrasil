package io.github.s3s3l.yggdrasil.game.core.snapshot;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * MemorySnapshotStore 单元测试
 */
class MemorySnapshotStoreTest {

    private MemorySnapshotStore store;

    @BeforeEach
    void setUp() {
        store = new MemorySnapshotStore();
    }

    @Test
    void testSaveAndGet() {
        // 创建测试快照
        ObjectSnapshot snapshot = new ObjectSnapshot(1L, TestGameAtom.class);

        // 保存
        store.save(snapshot);

        // 获取
        Snapshot retrieved = store.get(1L);

        // 验证
        assertNotNull(retrieved);
        assertEquals(1L, retrieved.getId());
    }

    @Test
    void testGetNonExistent() {
        // 获取不存在的快照
        Snapshot snapshot = store.get(999L);
        assertNull(snapshot);
    }

    @Test
    void testDelete() {
        // 保存快照
        store.save(new ObjectSnapshot(1L, TestGameAtom.class));

        // 验证存在
        assertTrue(store.exists(1L));

        // 删除
        boolean deleted = store.delete(1L);

        // 验证删除成功
        assertTrue(deleted);
        assertFalse(store.exists(1L));
    }

    @Test
    void testExists() {
        // 初始不存在
        assertFalse(store.exists(1L));

        // 保存后存在
        store.save(new ObjectSnapshot(1L, TestGameAtom.class));
        assertTrue(store.exists(1L));
    }

    @Test
    void testCount() {
        // 初始为 0
        assertEquals(0, store.count());

        // 保存 3 个
        for (long i = 1; i <= 3; i++) {
            store.save(new ObjectSnapshot(i, TestGameAtom.class));
        }
        assertEquals(3, store.count());

        // 删除 1 个
        store.delete(1L);
        assertEquals(2, store.count());
    }

    @Test
    void testGetAll() {
        // 保存多个快照
        for (long i = 1; i <= 3; i++) {
            store.save(new ObjectSnapshot(i, TestGameAtom.class));
        }

        // 获取所有
        Collection<Snapshot> all = store.getAll();

        // 验证
        assertNotNull(all);
        assertEquals(3, all.size());
    }

    @Test
    void testClear() {
        // 保存多个快照
        for (long i = 1; i <= 5; i++) {
            store.save(new ObjectSnapshot(i, TestGameAtom.class));
        }

        // 清空
        store.clear();

        // 验证已清空
        assertEquals(0, store.count());
    }

    @Test
    void testOverwrite() {
        // 保存快照
        ObjectSnapshot snapshot1 = new ObjectSnapshot(1L, TestGameAtom.class);
        snapshot1.addProps("version", "1");
        store.save(snapshot1);

        // 覆盖保存
        ObjectSnapshot snapshot2 = new ObjectSnapshot(1L, TestGameAtom.class);
        snapshot2.addProps("version", "2");
        store.save(snapshot2);

        // 验证后保存的覆盖之前的
        assertEquals(1, store.count());
        ObjectSnapshot retrieved = (ObjectSnapshot) store.get(1L);
        assertEquals("2", retrieved.getProps().getProperty("version"));
    }

    /**
     * 测试用的 GameAtom 实现
     */
    static class TestGameAtom implements io.github.s3s3l.yggdrasil.game.core.basic.GameAtom {
        private long id = 1L;

        @Override
        public void afterPropertiesSet() {}

        @Override
        public long getId() {
            return id;
        }

        @Override
        public void setId(long id) {
            this.id = id;
        }

        @Override
        public void onWindowResize(int width, int height) {}

        @Override
        public void calculate(float delta) {}

        @Override
        public void preRender() {}

        @Override
        public void render() {}

        @Override
        public boolean isDirty() {
            return false;
        }

        @Override
        public void markDirty() {}

        @Override
        public void clearDirty() {}

        @Override
        public io.github.s3s3l.yggdrasil.game.core.snapshot.ObjectSnapshot snapshot() {
            return null;
        }

        @Override
        public void restore(io.github.s3s3l.yggdrasil.game.core.snapshot.ObjectSnapshot snapshot) {}
    }
}
