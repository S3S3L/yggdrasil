package io.github.s3s3l.yggdrasil.game.core.event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class EventBus {
    private static final EventBus INSTANCE = new EventBus();
    
    private final Map<Class<?>, PublishSubject<Object>> subjectMap = new ConcurrentHashMap<>();

    private EventBus() {
    }

    public static EventBus get() {
        return INSTANCE;
    }

    // 发送事件
    public <T> void post(T event) {
        PublishSubject<Object> subject = subjectMap.get(event.getClass());
        if (subject != null) {
            subject.onNext(event);
        }
    }

    // 订阅事件
    public <T> Observable<T> observe(Class<T> eventType) {
        PublishSubject<Object> subject = subjectMap.computeIfAbsent(eventType, k -> PublishSubject.create());
        return subject.ofType(eventType);
    }
}
