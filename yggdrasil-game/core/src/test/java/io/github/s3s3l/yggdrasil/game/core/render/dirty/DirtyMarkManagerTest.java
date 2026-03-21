package io.github.s3s3l.yggdrasil.game.core.render.dirty;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 脏标记管理器测试
 */
@DisplayName("脏标记管理器测试")
class DirtyMarkManagerTest {

    private DirtyMarkManager manager;

    @BeforeEach
    void setUp() {
        manager = new DirtyMarkManager();
    }

    @Test
    @DisplayName("标记对象为脏")
    void testMarkDirty() {
        TestDirtyMarkable obj = new TestDirtyMarkable(1);

        manager.markDirty(obj);

        assertTrue(manager.isDirty(obj));
        assertEquals(1, manager.getDirtyCount());
    }

    @Test
    @DisplayName("清除脏标记")
    void testClearDirty() {
        TestDirtyMarkable obj = new TestDirtyMarkable(1);

        manager.markDirty(obj);
        manager.clearDirty(obj);

        assertFalse(manager.isDirty(obj));
        assertEquals(0, manager.getDirtyCount());
    }

    @Test
    @DisplayName("批量更新")
    void testBatchUpdate() {
        TestDirtyMarkable obj1 = new TestDirtyMarkable(1);
        TestDirtyMarkable obj2 = new TestDirtyMarkable(2);

        manager.beginBatchUpdate();
        manager.markDirty(obj1);
        manager.markDirty(obj2);

        // 批量更新期间，对象不应立即被标记
        assertEquals(0, manager.getDirtyCount());

        manager.endBatchUpdate();

        // 批量更新后，对象应该被标记
        assertEquals(2, manager.getDirtyCount());
        assertTrue(manager.isDirty(obj1));
        assertTrue(manager.isDirty(obj2));
    }

    @Test
    @DisplayName("批量更新期间重复标记")
    void testBatchUpdateDuplicate() {
        TestDirtyMarkable obj1 = new TestDirtyMarkable(1);

        manager.markDirty(obj1);

        manager.beginBatchUpdate();
        manager.markDirty(obj1);  // 重复标记
        manager.endBatchUpdate();

        // 应该只有 1 个脏对象
        assertEquals(1, manager.getDirtyCount());
    }

    @Test
    @DisplayName("清空所有脏标记")
    void testClearAll() {
        TestDirtyMarkable obj1 = new TestDirtyMarkable(1);
        TestDirtyMarkable obj2 = new TestDirtyMarkable(2);

        manager.markDirty(obj1);
        manager.markDirty(obj2);

        manager.clearAll();

        assertEquals(0, manager.getDirtyCount());
        assertFalse(manager.isDirty(obj1));
        assertFalse(manager.isDirty(obj2));
    }

    @Test
    @DisplayName("统计信息")
    void testStats() {
        TestDirtyMarkable obj = new TestDirtyMarkable(1);

        manager.markDirty(obj);
        manager.clearDirty(obj);

        DirtyStats stats = manager.getStats();
        assertEquals(1, stats.getMarkCount());
        assertEquals(1, stats.getClearCount());
    }

    @Test
    @DisplayName("批量更新统计")
    void testBatchStats() {
        manager.beginBatchUpdate();
        manager.markDirty(new TestDirtyMarkable(1));
        manager.markDirty(new TestDirtyMarkable(2));
        manager.endBatchUpdate();

        DirtyStats stats = manager.getStats();
        assertEquals(1, stats.getBatchStartCount());
        assertEquals(2, stats.getBatchMarkedTotal());
    }

    @Test
    @DisplayName("null 对象处理")
    void testNullObject() {
        assertDoesNotThrow(() -> {
            manager.markDirty(null);
            manager.clearDirty(null);
            manager.isDirty(null);
        });
    }

    @Test
    @DisplayName("未开始批量更新就结束抛出异常")
    void testEndBatchWithoutBegin() {
        assertThrows(IllegalStateException.class, () -> {
            manager.endBatchUpdate();
        });
    }

    @Test
    @DisplayName("重复开始批量更新抛出异常")
    void testDoubleBeginBatch() {
        manager.beginBatchUpdate();
        assertThrows(IllegalStateException.class, () -> {
            manager.beginBatchUpdate();
        });
    }

    /**
     * 测试用脏标记对象
     */
    static class TestDirtyMarkable implements DirtyMarkable {
        private final long id;

        TestDirtyMarkable(long id) {
            this.id = id;
        }

        @Override
        public long getId() {
            return id;
        }
    }
}
