package io.github.s3s3l.yggdrasil.game.core.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 缓存清理策略测试
 */
@DisplayName("缓存清理策略测试")
class CacheEvictionPolicyTest {

    private TestCacheContext context;

    @BeforeEach
    void setUp() {
        context = new TestCacheContext();
    }

    @Test
    @DisplayName("LRU 策略 - 清理最久未访问的条目")
    void testLRUPolicy() {
        LRUEvictionPolicy<String, String> policy = LRUEvictionPolicy.getInstance();

        // 设置测试数据
        context.put("key1", "value1", 1, 1000);  // 最后访问：1000
        context.put("key2", "value2", 1, 2000);  // 最后访问：2000
        context.put("key3", "value3", 1, 3000);  // 最后访问：3000
        context.put("key4", "value4", 1, 4000);  // 最后访问：4000

        // 选择 2 个条目进行清理
        Collection<String> toEvict = policy.selectForEviction(context, 2);

        assertEquals(2, toEvict.size());
        assertTrue(toEvict.contains("key1"));
        assertTrue(toEvict.contains("key2"));
    }

    @Test
    @DisplayName("LFU 策略 - 清理访问最少的条目")
    void testLFUPolicy() {
        LFUEvictionPolicy<String, String> policy = LFUEvictionPolicy.getInstance();

        // 设置测试数据
        context.put("key1", "value1", 1, 1000);   // 访问 1 次
        context.put("key2", "value2", 5, 1000);   // 访问 5 次
        context.put("key3", "value3", 2, 1000);   // 访问 2 次
        context.put("key4", "value4", 10, 1000);  // 访问 10 次

        // 选择 2 个条目进行清理
        Collection<String> toEvict = policy.selectForEviction(context, 2);

        assertEquals(2, toEvict.size());
        assertTrue(toEvict.contains("key1"));
        assertTrue(toEvict.contains("key3"));
    }

    @Test
    @DisplayName("基于时间的策略 - 清理超时的条目")
    void testTimeBasedPolicy() {
        long now = System.currentTimeMillis();

        // 创建策略：存活时间 1 秒
        TimeBasedEvictionPolicy<String, String> policy = new TimeBasedEvictionPolicy<>(1000);

        // 设置测试数据
        context.put("key1", "value1", 1, now - 2000);  // 2 秒前创建（已超时）
        context.put("key2", "value2", 1, now - 500);   // 0.5 秒前创建（未超时）
        context.put("key3", "value3", 1, now - 1500);  // 1.5 秒前创建（已超时）
        context.put("key4", "value4", 1, now);         // 刚创建（未超时）

        // 选择条目进行清理
        Collection<String> toEvict = policy.selectForEviction(context, 10);

        assertEquals(2, toEvict.size());
        assertTrue(toEvict.contains("key1"));
        assertTrue(toEvict.contains("key3"));
    }

    @Test
    @DisplayName("清理数量为 0 时返回空集合")
    void testZeroCount() {
        LRUEvictionPolicy<String, String> policy = LRUEvictionPolicy.getInstance();

        context.put("key1", "value1", 1, 1000);

        Collection<String> toEvict = policy.selectForEviction(context, 0);

        assertTrue(toEvict.isEmpty());
    }

    @Test
    @DisplayName("清理数量大于缓存大小时返回所有键")
    void testExceedCount() {
        LRUEvictionPolicy<String, String> policy = LRUEvictionPolicy.getInstance();

        context.put("key1", "value1", 1, 1000);
        context.put("key2", "value2", 1, 2000);
        context.put("key3", "value3", 1, 3000);

        Collection<String> toEvict = policy.selectForEviction(context, 100);

        assertEquals(3, toEvict.size());
    }

    @Test
    @DisplayName("TimeBasedEvictionPolicy 静态工厂方法")
    void testTimeBasedPolicyFactory() {
        // 测试 withTTL（秒）
        TimeBasedEvictionPolicy<String, String> policy1 = TimeBasedEvictionPolicy.withTTL(60);
        assertNotNull(policy1);

        // 测试 withTTLMMinutes（分钟）
        TimeBasedEvictionPolicy<String, String> policy2 = TimeBasedEvictionPolicy.withTTLMMinutes(5);
        assertNotNull(policy2);
    }

    /**
     * 测试用缓存上下文
     */
    static class TestCacheContext implements CacheEvictionPolicy.CacheContext<String, String> {
        private final Map<String, String> values = new HashMap<>();
        private final Map<String, Integer> accessCounts = new HashMap<>();
        private final Map<String, Long> lastAccessTimes = new HashMap<>();
        private final Map<String, Long> creationTimes = new HashMap<>();

        public void put(String key, String value, int accessCount, long lastAccessTime) {
            values.put(key, value);
            accessCounts.put(key, accessCount);
            lastAccessTimes.put(key, lastAccessTime);
            creationTimes.put(key, lastAccessTime);
        }

        @Override
        public String get(String key) {
            return values.get(key);
        }

        @Override
        public Collection<String> keys() {
            return values.keySet();
        }

        @Override
        public int getAccessCount(String key) {
            return accessCounts.getOrDefault(key, 0);
        }

        @Override
        public long getLastAccessTime(String key) {
            return lastAccessTimes.getOrDefault(key, 0L);
        }

        @Override
        public long getCreationTime(String key) {
            return creationTimes.getOrDefault(key, 0L);
        }

        @Override
        public long getSize(String key) {
            return -1;
        }
    }
}
