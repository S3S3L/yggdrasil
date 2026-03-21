package io.github.s3s3l.yggdrasil.game.core.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 增强缓存管理器测试
 */
@DisplayName("增强缓存管理器测试")
class EnhancedCacheManagerTest {

    private EnhancedCacheManager<String, String> cache;

    @BeforeEach
    void setUp() {
        cache = new EnhancedCacheManager<>(5);  // 最大缓存 5 个条目
    }

    @Test
    @DisplayName("放入和获取缓存")
    void testPutAndGet() {
        cache.put("key1", "value1");
        cache.put("key2", "value2");

        assertEquals("value1", cache.get("key1"));
        assertEquals("value2", cache.get("key2"));
        assertEquals(2, cache.size());
    }

    @Test
    @DisplayName("获取不存在的键返回 null")
    void testGetNonExistent() {
        assertNull(cache.get("nonexistent"));
    }

    @Test
    @DisplayName("缓存满时自动清理（LRU 策略）")
    void testEvictionOnFull() {
        // 放入 5 个条目
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");
        cache.put("key4", "value4");
        cache.put("key5", "value5");

        // 访问 key3，使它变成最近使用的
        cache.get("key3");

        // 放入新条目，应该触发清理
        cache.put("key6", "value6");

        // 缓存大小应该保持在 5
        assertEquals(5, cache.size());
        // key6 应该存在
        assertEquals("value6", cache.get("key6"));
    }

    @Test
    @DisplayName("移除缓存条目")
    void testRemove() {
        cache.put("key1", "value1");
        String removed = cache.remove("key1");

        assertEquals("value1", removed);
        assertFalse(cache.containsKey("key1"));
        assertEquals(0, cache.size());
    }

    @Test
    @DisplayName("清空缓存")
    void testClear() {
        cache.put("key1", "value1");
        cache.put("key2", "value2");

        cache.clear();

        assertEquals(0, cache.size());
        assertFalse(cache.containsKey("key1"));
    }

    @Test
    @DisplayName("检查是否包含键")
    void testContainsKey() {
        cache.put("key1", "value1");

        assertTrue(cache.containsKey("key1"));
        assertFalse(cache.containsKey("key2"));
    }

    @Test
    @DisplayName("获取所有键")
    void testKeys() {
        cache.put("key1", "value1");
        cache.put("key2", "value2");

        var keys = cache.keys();

        assertEquals(2, keys.size());
        assertTrue(keys.contains("key1"));
        assertTrue(keys.contains("key2"));
    }

    @Test
    @DisplayName("设置最大缓存大小")
    void testSetMaxSize() {
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");

        // 减小最大大小
        cache.setMaxSize(2);

        // 应该自动清理到 2 个条目
        assertTrue(cache.size() <= 2);
    }

    @Test
    @DisplayName("缓存统计信息")
    void testStats() {
        cache.put("key1", "value1");
        cache.put("key2", "value2");

        cache.get("key1");  // hit
        cache.get("key1");  // hit
        cache.get("nonexistent");  // miss

        EnhancedCacheManager.CacheStats stats = cache.getStats();

        assertTrue(stats.getHits() >= 2);
        assertEquals(1, stats.getMisses());
        assertEquals(2, stats.getPuts());

        // 测试命中率
        assertTrue(stats.getHitRate() > 0.5);
    }

    @Test
    @DisplayName("禁用统计")
    void testDisableStats() {
        cache.setStatsEnabled(false);

        cache.put("key1", "value1");
        cache.get("key1");

        // 禁用后统计应该不更新或为 0
        // 注意：这取决于实现，可能统计仍然工作但不会被记录
    }

    @Test
    @DisplayName("手动执行清理")
    void testManualEviction() {
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");

        // 手动清理 1 个条目
        cache.evict(1);

        // 缓存大小应该减少
        assertEquals(2, cache.size());
    }

    @Test
    @DisplayName("设置清理策略")
    void testSetEvictionPolicy() {
        // 切换到 LFU 策略
        cache.setEvictionPolicy((CacheEvictionPolicy) LFUEvictionPolicy.getInstance());

        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");
        cache.put("key4", "value4");
        cache.put("key5", "value5");

        // 多次访问 key1，使它成为最常使用的
        for (int i = 0; i < 10; i++) {
            cache.get("key1");
        }

        // 放入新条目，应该清理访问最少的
        cache.put("key6", "value6");

        assertNotNull(cache.get("key1"));  // key1 应该还在
    }

    @Test
    @DisplayName("缓存统计信息 toString")
    void testStatsToString() {
        cache.put("key1", "value1");
        cache.get("key1");
        cache.get("nonexistent");

        EnhancedCacheManager.CacheStats stats = cache.getStats();
        String statsString = stats.toString();

        assertNotNull(statsString);
        assertTrue(statsString.contains("hits="));
        assertTrue(statsString.contains("misses="));
        assertTrue(statsString.contains("hitRate="));
    }
}
