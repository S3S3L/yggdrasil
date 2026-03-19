package io.github.s3s3l.yggdrasil.game.core.event;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import io.github.s3s3l.yggdrasil.game.core.event.handler.EventHandler;
import io.github.s3s3l.yggdrasil.game.core.event.handler.EventListenerWrapper;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

/**
 * 事件总线
 * 支持两种事件处理方式：
 * 1. 基于 RxJava 的响应式订阅
 * 2. 基于 EventHandler 的监听器注册
 */
public class EventBus {
    private static final EventBus INSTANCE = new EventBus();

    // RxJava 方式的 Subject 映射
    private final Map<Class<?>, PublishSubject<Object>> subjectMap = new ConcurrentHashMap<>();

    // EventHandler 方式的监听器注册表
    private final Map<Class<?>, List<EventListenerWrapper<?>>> listenerMap = new ConcurrentHashMap<>();

    private EventBus() {
    }

    public static EventBus get() {
        return INSTANCE;
    }

    // ==================== RxJava 方式 ====================

    /**
     * 发送事件（RxJava 方式）
     *
     * @param event 事件对象
     */
    @SuppressWarnings("unchecked")
    public <T> void post(T event) {
        // 先通过 EventHandler 方式处理
        handleByListeners((Event) event);

        // 再通过 RxJava 方式处理
        PublishSubject<Object> subject = subjectMap.get(event.getClass());
        if (subject != null) {
            subject.onNext(event);
        }
    }

    /**
     * 订阅事件（RxJava 方式）
     *
     * @param eventType 事件类型
     * @return Observable 事件流
     */
    public <T> Observable<T> observe(Class<T> eventType) {
        PublishSubject<Object> subject = subjectMap.computeIfAbsent(eventType, k -> PublishSubject.create());
        return subject.ofType(eventType);
    }

    // ==================== EventHandler 方式 ====================

    /**
     * 注册事件监听器
     *
     * @param eventType 事件类型
     * @param listener  事件监听器
     * @param <T>       事件类型
     */
    public <T extends Event> void register(Class<T> eventType, EventHandler<T> listener) {
        register(eventType, listener, EventPriority.NORMAL);
    }

    /**
     * 注册事件监听器（指定优先级）
     *
     * @param eventType 事件类型
     * @param listener  事件监听器
     * @param priority  优先级
     * @param <T>       事件类型
     */
    public <T extends Event> void register(Class<T> eventType, EventHandler<T> listener, EventPriority priority) {
        listenerMap.computeIfAbsent(eventType, k -> new ArrayList<>())
                .add(new EventListenerWrapper<>(listener, priority));
    }

    /**
     * 注销事件监听器
     *
     * @param eventType 事件类型
     * @param listener  事件监听器
     * @param <T>       事件类型
     */
    public <T extends Event> void unregister(Class<T> eventType, EventHandler<T> listener) {
        List<EventListenerWrapper<?>> listeners = listenerMap.get(eventType);
        if (listeners != null) {
            listeners.stream()
                    .filter(wrapper -> wrapper.getListener() == listener)
                    .findFirst()
                    .ifPresent(EventListenerWrapper::remove);
        }
    }

    /**
     * 注销所有事件监听器
     */
    public void unregisterAll() {
        listenerMap.clear();
    }

    /**
     * 注销指定类型的所有监听器
     *
     * @param eventType 事件类型
     */
    public void unregisterAll(Class<?> eventType) {
        listenerMap.remove(eventType);
    }

    /**
     * 通过监听器方式处理事件
     *
     * @param event 事件对象
     */
    @SuppressWarnings("unchecked")
    private <T extends Event> void handleByListeners(T event) {
        List<EventListenerWrapper<?>> listeners = listenerMap.get(event.getClass());
        if (listeners == null || listeners.isEmpty()) {
            return;
        }

        // 按优先级排序
        List<EventListenerWrapper<T>> sortedListeners = listeners.stream()
                .filter(EventListenerWrapper::isValid)
                .map(wrapper -> (EventListenerWrapper<T>) wrapper)
                .sorted(Comparator.comparingInt(w -> -w.getPriority().getValue()))
                .collect(Collectors.toList());

        // 按优先级顺序调用监听器
        EventContext context = null;
        if (event instanceof EventContext) {
            context = (EventContext) event;
        }

        for (EventListenerWrapper<T> wrapper : sortedListeners) {
            // 如果事件被取消，停止处理
            if (context != null && context.isCancelled()) {
                break;
            }
            wrapper.invoke(event);
        }
    }
}
