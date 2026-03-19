package io.github.s3s3l.yggdrasil.game.core.event;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.event.handler.EventHandler;

/**
 * EventBus 单元测试
 */
class EventBusTest {

    private EventBus eventBus;

    @BeforeEach
    void setUp() {
        eventBus = EventBus.get();
        eventBus.unregisterAll(); // 清除所有监听器
    }

    /**
     * 测试事件：简单的测试事件类
     */
    static class TestEvent implements Event {
        private final String message;

        public TestEvent(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    @Test
    void testRegisterAndPost() {
        // 测试数据
        List<String> receivedMessages = new ArrayList<>();

        // 注册监听器
        EventHandler<TestEvent> handler = event -> {
            receivedMessages.add(event.getMessage());
        };
        eventBus.register(TestEvent.class, handler);

        // 发送事件
        TestEvent testEvent = new TestEvent("Hello, EventBus!");
        eventBus.post(testEvent);

        // 验证
        assertEquals(1, receivedMessages.size());
        assertEquals("Hello, EventBus!", receivedMessages.get(0));
    }

    @Test
    void testUnregister() {
        // 测试数据
        List<String> receivedMessages = new ArrayList<>();

        // 注册监听器
        EventHandler<TestEvent> handler = event -> {
            receivedMessages.add(event.getMessage());
        };
        eventBus.register(TestEvent.class, handler);

        // 发送事件
        eventBus.post(new TestEvent("First"));

        // 注销监听器
        eventBus.unregister(TestEvent.class, handler);

        // 再次发送事件
        eventBus.post(new TestEvent("Second"));

        // 验证：只有第一次事件被接收
        assertEquals(1, receivedMessages.size());
        assertEquals("First", receivedMessages.get(0));
    }

    @Test
    void testPriorityOrder() {
        // 测试数据
        List<String> executionOrder = new ArrayList<>();

        // 注册不同优先级的监听器
        eventBus.register(TestEvent.class, event -> executionOrder.add("LOW"), EventPriority.LOW);
        eventBus.register(TestEvent.class, event -> executionOrder.add("HIGH"), EventPriority.HIGH);
        eventBus.register(TestEvent.class, event -> executionOrder.add("HIGHEST"), EventPriority.HIGHEST);
        eventBus.register(TestEvent.class, event -> executionOrder.add("NORMAL"), EventPriority.NORMAL);

        // 发送事件
        eventBus.post(new TestEvent("Test"));

        // 验证：按优先级降序执行
        assertEquals(4, executionOrder.size());
        assertEquals("HIGHEST", executionOrder.get(0));
        assertEquals("HIGH", executionOrder.get(1));
        assertEquals("NORMAL", executionOrder.get(2));
        assertEquals("LOW", executionOrder.get(3));
    }

    @Test
    void testEventCancel() {
        // 测试数据
        List<String> receivedMessages = new ArrayList<>();
        final boolean[] highPriorityCalled = {false};

        // 高优先级监听器
        eventBus.register(TestEventContext.class, event -> {
            highPriorityCalled[0] = true;
            receivedMessages.add("HIGH");
            // 不取消事件
        }, EventPriority.HIGH);

        // 中优先级监听器取消事件
        eventBus.register(TestEventContext.class, event -> {
            receivedMessages.add("NORMAL");
            event.cancel();
        }, EventPriority.NORMAL);

        // 低优先级监听器
        eventBus.register(TestEventContext.class, event -> {
            receivedMessages.add("LOW");
        }, EventPriority.LOW);

        // 创建可取消的事件
        TestEventContext eventContext = new TestEventContext("Test");

        // 发送事件
        eventBus.post(eventContext);

        // 验证：只有高优先级和中优先级监听器被调用
        assertTrue(highPriorityCalled[0], "HIGH priority listener should be called");
        assertEquals(2, receivedMessages.size());
        assertEquals("HIGH", receivedMessages.get(0));
        assertEquals("NORMAL", receivedMessages.get(1));
    }

    /**
     * 测试用的 EventContext 实现
     */
    static class TestEventContext extends EventContext<TestEventContext> implements Event {
        private final String message;

        public TestEventContext(String message) {
            super(EventBusTest.class);
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    @Test
    void testMultipleListeners() {
        // 测试数据
        List<String> receivedMessages = new ArrayList<>();

        // 注册多个监听器
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            eventBus.register(TestEvent.class, event -> {
                receivedMessages.add("Listener" + finalI);
            });
        }

        // 发送事件
        eventBus.post(new TestEvent("Test"));

        // 验证：所有监听器都被调用
        assertEquals(5, receivedMessages.size());
        for (int i = 0; i < 5; i++) {
            assertTrue(receivedMessages.contains("Listener" + i));
        }
    }

    @Test
    void testUnregisterAll() {
        // 测试数据
        List<String> receivedMessages = new ArrayList<>();

        // 注册多个监听器
        for (int i = 0; i < 3; i++) {
            eventBus.register(TestEvent.class, event -> {
                receivedMessages.add("Message");
            });
        }

        // 注销所有监听器
        eventBus.unregisterAll();

        // 发送事件
        eventBus.post(new TestEvent("Test"));

        // 验证：没有监听器被调用
        assertEquals(0, receivedMessages.size());
    }
}
