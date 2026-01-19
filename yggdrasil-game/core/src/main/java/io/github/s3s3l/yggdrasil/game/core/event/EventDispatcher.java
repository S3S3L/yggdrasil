package io.github.s3s3l.yggdrasil.game.core.event;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

import io.github.s3s3l.yggdrasil.game.core.object.GameObject;

public class EventDispatcher {

    private final ListenerRegistry registry = new ListenerRegistry();
    private final EventRouter router = new EventRouter(registry);
    private final Queue<Event> pending = new ConcurrentLinkedQueue<>();

    public <E extends Event> EventBinding bind(GameObject target, EventType type, EventListener<E> listener,
            Consumer<EventOptions> optionsCustomizer) {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(listener, "listener");

        EventOptions options = new EventOptions();
        if (optionsCustomizer != null) {
            optionsCustomizer.accept(options);
        }

        @SuppressWarnings("unchecked")
        EventListener<Event> rawListener = (EventListener<Event>) listener;
        ListenerEntry entry = new ListenerEntry(rawListener, options);
        registry.add(target, type, options.getPhase(), entry);

        Runnable disposer = () -> registry.remove(target, type, options.getPhase(), entry);
        return new EventBinding(target, type, options.getPhase(), options.getPriority(), options.isOnce(), disposer);
    }

    public void unbind(EventBinding binding) {
        if (binding == null) {
            return;
        }
        binding.dispose();
    }

    public void dispatch(Event event) {
        router.route(event);
    }

    public void post(Event event) {
        pending.offer(event);
    }

    public void flush() {
        Event event;
        while ((event = pending.poll()) != null) {
            router.route(event);
        }
    }
}
