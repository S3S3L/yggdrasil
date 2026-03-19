package io.github.s3s3l.yggdrasil.game.core.snapshot;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * SnapshotManager 单元测试
 */
class SnapshotManagerTest {

    private SnapshotManager snapshotManager;
    private SnapshotStore snapshotStore;
    private SnapshotSerializer serializer;

    @BeforeEach
    void setUp() {
        snapshotManager = SnapshotManager.get();
        snapshotStore = new MemorySnapshotStore();
        serializer = new JsonSnapshotSerializer();

        // 设置测试用的存储和序列化器
        snapshotManager.setSnapshotStore(snapshotStore);
        snapshotManager.setSerializer(serializer);
        snapshotManager.clearAllSnapshots();
    }

    @Test
    void testSaveAndGetSnapshot() {
        // 创建测试快照
        long testId = 1L;
        ObjectSnapshot snapshot = new ObjectSnapshot(testId, TestGameAtom.class);
        snapshot.addProps("name", "TestObject");
        snapshot.addProps("value", "100");

        // 保存快照
        snapshotManager.saveSnapshot(snapshot);

        // 验证快照已保存
        assertTrue(snapshotManager.hasSnapshot(new TestID(testId)));

        // 获取快照
        ObjectSnapshot retrieved = snapshotManager.getSnapshot(testId);

        // 验证快照内容
        assertNotNull(retrieved);
        assertEquals(testId, retrieved.getId());
        assertEquals("TestObject", retrieved.getProps().getProperty("name"));
        assertEquals("100", retrieved.getProps().getProperty("value"));
    }

    @Test
    void testDeleteSnapshot() {
        // 创建并保存快照
        long testId = 1L;
        ObjectSnapshot snapshot = new ObjectSnapshot(testId, TestGameAtom.class);
        snapshotManager.saveSnapshot(snapshot);

        // 验证快照存在
        assertTrue(snapshotManager.hasSnapshot(new TestID(testId)));

        // 删除快照
        boolean deleted = snapshotManager.deleteSnapshot(testId);

        // 验证删除成功
        assertTrue(deleted);
        assertFalse(snapshotManager.hasSnapshot(new TestID(testId)));
    }

    @Test
    void testGetLatestSnapshot() {
        // 创建测试快照
        long testId = 1L;
        ObjectSnapshot snapshot = new ObjectSnapshot(testId, TestGameAtom.class);
        snapshot.addProps("key", "value");
        snapshotManager.saveSnapshot(snapshot);

        // 获取最新快照
        ObjectSnapshot retrieved = snapshotManager.getLatestSnapshot(new TestID(testId));

        // 验证快照内容
        assertNotNull(retrieved);
        assertEquals("value", retrieved.getProps().getProperty("key"));
    }

    @Test
    void testGetSnapshotCount() {
        // 初始快照数量为 0
        assertEquals(0, snapshotManager.getSnapshotCount());

        // 保存 3 个快照
        for (long i = 1; i <= 3; i++) {
            ObjectSnapshot snapshot = new ObjectSnapshot(i, TestGameAtom.class);
            snapshotManager.saveSnapshot(snapshot);
        }

        // 验证快照数量
        assertEquals(3, snapshotManager.getSnapshotCount());

        // 删除一个快照
        snapshotManager.deleteSnapshot(1L);

        // 验证剩余快照数量
        assertEquals(2, snapshotManager.getSnapshotCount());
    }

    @Test
    void testClearAllSnapshots() {
        // 保存多个快照
        for (long i = 1; i <= 5; i++) {
            ObjectSnapshot snapshot = new ObjectSnapshot(i, TestGameAtom.class);
            snapshotManager.saveSnapshot(snapshot);
        }

        // 验证快照数量
        assertEquals(5, snapshotManager.getSnapshotCount());

        // 清除所有快照
        snapshotManager.clearAllSnapshots();

        // 验证快照已清空
        assertEquals(0, snapshotManager.getSnapshotCount());
    }

    @Test
    void testNullHandling() {
        // 测试 null 参数处理
        assertFalse(snapshotManager.hasSnapshot(null));
        assertFalse(snapshotManager.deleteSnapshot(null));

        // 保存 null 快照应该被忽略
        snapshotManager.saveSnapshot(null);
        assertEquals(0, snapshotManager.getSnapshotCount());
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

    /**
     * 测试用的 Renderable 实现
     */
    static class TestRenderable implements io.github.s3s3l.yggdrasil.game.core.basic.Renderable {
        private long id;

        public TestRenderable(long id) {
            this.id = id;
        }

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
    }

    /**
     * 测试用的 ID 实现
     */
    static class TestID implements io.github.s3s3l.yggdrasil.game.core.basic.ID {
        private long id;

        public TestID(long id) {
            this.id = id;
        }

        @Override
        public long getId() {
            return id;
        }

        @Override
        public void setId(long id) {
            this.id = id;
        }
    }
}
