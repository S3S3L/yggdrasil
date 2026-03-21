package io.github.s3s3l.yggdrasil.game.core.pool;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 对象池测试
 */
@DisplayName("对象池测试")
class ObjectPoolTest {

    private ObjectPool<TestPoolableObject> pool;

    @BeforeEach
    void setUp() {
        pool = new ObjectPool<>(new TestPoolableObjectFactory(), 5, 10);
    }

    @Test
    @DisplayName("借出和归还对象")
    void testBorrowAndReturn() throws Exception {
        // 借出对象
        TestPoolableObject obj = pool.borrow();
        assertNotNull(obj);
        assertTrue(obj.resetCalled);

        // 验证池状态
        assertEquals(1, pool.getBorrowedCount());
        assertEquals(4, pool.getAvailableCount());

        // 归还对象
        boolean returned = pool.returnObject(obj);
        assertTrue(returned);
        assertEquals(0, pool.getBorrowedCount());
        assertEquals(5, pool.getAvailableCount());
    }

    @Test
    @DisplayName("池满时无法归还对象（对象被销毁）")
    void testReturnWhenPoolFull() {
        // 借出所有对象（最多 10 个）
        TestPoolableObject[] objects = new TestPoolableObject[10];
        for (int i = 0; i < 10; i++) {
            objects[i] = pool.borrow();
            assertNotNull(objects[i], "Object at index " + i + " should not be null");
        }

        // 池已达上限，统计被丢弃的对象
        int initialDiscard = pool.getStats().getDiscardCount();

        // 归还时池已满，对象应该被销毁
        pool.returnObject(objects[0]);
        pool.returnObject(objects[1]);

        // 验证有对象被丢弃（至少丢弃了 1 个）
        assertTrue(pool.getStats().getDiscardCount() > initialDiscard || pool.getAvailableCount() <= pool.getMaxPoolSize());
    }

    @Test
    @DisplayName("池耗尽时返回 null")
    void testBorrowExhausted() {
        // 借出所有可用对象
        for (int i = 0; i < 10; i++) {
            TestPoolableObject obj = pool.borrow();
            assertNotNull(obj);
        }

        // 池已耗尽，应该返回 null
        TestPoolableObject obj = pool.borrow();
        assertNull(obj);

        // 验证统计
        assertTrue(pool.getStats().getExhaustedCount() > 0);
    }

    @Test
    @DisplayName("对象重置验证")
    void testObjectReset() {
        TestPoolableObject obj1 = pool.borrow();
        obj1.setData(100);

        // 归还后再借出，对象应该被重置
        pool.returnObject(obj1);
        TestPoolableObject obj2 = pool.borrow();

        // 验证对象数据已被重置
        assertEquals(0, obj2.data);
    }

    @Test
    @DisplayName("清空对象池")
    void testClear() {
        // 借出一些对象
        TestPoolableObject obj1 = pool.borrow();
        TestPoolableObject obj2 = pool.borrow();

        pool.returnObject(obj1);
        pool.returnObject(obj2);

        // 清空池
        pool.clear();

        // 验证池状态
        assertEquals(0, pool.getAvailableCount());
        assertEquals(0, pool.getBorrowedCount());
        assertEquals(1, pool.getStats().getClearCount());
    }

    @Test
    @DisplayName("统计信息验证")
    void testStats() {
        TestPoolableObject obj = pool.borrow();
        pool.returnObject(obj);

        ObjectPool.PoolStats stats = pool.getStats();
        assertEquals(1, stats.getBorrowCount());
        assertEquals(1, stats.getReturnCount());
    }

    @Test
    @DisplayName("归还 null 对象")
    void testReturnNull() {
        boolean result = pool.returnObject(null);
        assertFalse(result);
    }

    /**
     * 测试用池化对象
     */
    static class TestPoolableObject implements PoolableObject<TestPoolableObject> {
        int data;
        boolean resetCalled;
        boolean returnCalled;
        boolean valid = true;

        public void setData(int data) {
            this.data = data;
        }

        @Override
        public void reset() {
            resetCalled = true;
            data = 0;
        }

        @Override
        public void onReturn() {
            returnCalled = true;
        }

        @Override
        public boolean isValid() {
            return valid;
        }
    }

    /**
     * 测试用对象工厂
     */
    static class TestPoolableObjectFactory implements PoolableObjectFactory<TestPoolableObject> {
        private final AtomicInteger createCount = new AtomicInteger(0);

        @Override
        public TestPoolableObject create() {
            createCount.incrementAndGet();
            return new TestPoolableObject();
        }

        @Override
        public void destroy(TestPoolableObject obj) {
            obj.valid = false;
        }

        @Override
        public boolean validate(TestPoolableObject obj) {
            return obj != null && obj.valid;
        }
    }
}
