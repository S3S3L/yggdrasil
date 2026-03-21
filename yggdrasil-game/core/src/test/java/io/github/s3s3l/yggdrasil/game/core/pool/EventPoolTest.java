package io.github.s3s3l.yggdrasil.game.core.pool;

import io.github.s3s3l.yggdrasil.game.core.event.Event;
import io.github.s3s3l.yggdrasil.game.core.physics.CollisionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 事件对象池测试
 */
@DisplayName("事件对象池测试")
class EventPoolTest {

    private EventPool pool;

    @BeforeEach
    void setUp() {
        pool = EventPool.get();
        pool.clear();  // 清空之前的状态
    }

    @Test
    @DisplayName("借用和归还事件")
    void testBorrowAndReturnEvent() {
        // 借用事件
        CollisionEvent event = pool.borrowEvent(CollisionEvent.class);
        assertNotNull(event);

        // 设置事件数据
        event.setType(CollisionEvent.CollisionEventType.START);

        // 归还
        pool.returnEvent(event);
    }

    @Test
    @DisplayName("事件归还后重用")
    void testEventReuse() {
        // 借用并设置数据
        CollisionEvent event1 = pool.borrowEvent(CollisionEvent.class);
        event1.setType(CollisionEvent.CollisionEventType.START);

        // 归还
        pool.returnEvent(event1);

        // 再次借用，验证对象已被重置（类型被清空）
        CollisionEvent event2 = pool.borrowEvent(CollisionEvent.class);
        // 验证类型已被重置
        assertNull(event2.getType());
    }

    @Test
    @DisplayName("借用未注册的事件类型")
    void testBorrowUnregisteredEvent() {
        // 借用未注册的事件类型，应该直接创建新对象
        TestEvent event = pool.borrowEvent(TestEvent.class);
        assertNotNull(event);

        // 归还
        pool.returnEvent(event);
    }

    @Test
    @DisplayName("事件池统计信息")
    void testStats() {
        // 先注册事件类型
        pool.registerEvent(TestEvent.class, new TestEventFactory(), 5, 10);

        // 借用事件
        TestEvent event1 = pool.borrowEvent(TestEvent.class);
        TestEvent event2 = pool.borrowEvent(TestEvent.class);

        ObjectPool.PoolStats stats = pool.getStats(TestEvent.class);
        assertNotNull(stats);
        assertTrue(stats.getBorrowCount() >= 2);

        pool.returnEvent(event1);
        pool.returnEvent(event2);
    }

    @Test
    @DisplayName("归还 null 事件")
    void testReturnNull() {
        // 不应该抛出异常
        assertDoesNotThrow(() -> pool.returnEvent((TestEvent) null));
    }

    @Test
    @DisplayName("清空事件池")
    void testClear() {
        pool.registerEvent(TestEvent.class, new TestEventFactory(), 5, 10);

        TestEvent event = pool.borrowEvent(TestEvent.class);
        pool.returnEvent(event);

        pool.clear();

        ObjectPool<TestEvent> testEventPool = (ObjectPool<TestEvent>) getPoolInternal(pool, TestEvent.class);
        assertNotNull(testEventPool);
        assertEquals(0, testEventPool.getAvailableCount());
    }

    @Test
    @DisplayName("CollisionEvent 池化功能")
    void testCollisionEventPooling() {
        CollisionEvent event1 = pool.borrowEvent(CollisionEvent.class);
        event1.setType(CollisionEvent.CollisionEventType.START);
        event1.setTrigger(true);

        // 验证设置成功
        assertEquals(CollisionEvent.CollisionEventType.START, event1.getType());
        assertTrue(event1.isTrigger());

        // 归还
        pool.returnEvent(event1);

        // 再次借用，验证已被重置
        CollisionEvent event2 = pool.borrowEvent(CollisionEvent.class);
        // 验证类型和触发器状态已被重置
        assertNull(event2.getType());
        assertFalse(event2.isTrigger());
    }

    /**
     * 测试用事件
     */
    static class TestEvent implements Event, PoolableObject<TestEvent> {
        String data;
        boolean resetCalled;

        @Override
        public void reset() {
            resetCalled = true;
            data = null;
        }

        @Override
        public void onReturn() {
            reset();
        }

        @Override
        public boolean isValid() {
            return true;
        }
    }

    /**
     * 测试用事件工厂
     */
    static class TestEventFactory implements PoolableObjectFactory<TestEvent> {
        @Override
        public TestEvent create() {
            return new TestEvent();
        }

        @Override
        public void destroy(TestEvent obj) {
            // 无需特殊销毁逻辑
        }

        @Override
        public boolean validate(TestEvent obj) {
            return obj != null;
        }
    }

    // Helper method to access private map via reflection for testing
    private static Object getPoolInternal(EventPool pool, Class<?> eventType) {
        try {
            java.lang.reflect.Field field = EventPool.class.getDeclaredField("eventPools");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            java.util.Map<Class<? extends Event>, ObjectPool<? extends PoolableObject<?>>> map =
                (java.util.Map<Class<? extends Event>, ObjectPool<? extends PoolableObject<?>>>) field.get(pool);
            return map.get(eventType);
        } catch (Exception e) {
            return null;
        }
    }
}
