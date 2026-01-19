package io.github.s3s3l.yggdrasil.game.core.event;

import java.util.ArrayList;
import java.util.List;

import io.github.s3s3l.yggdrasil.game.core.object.GameObject;

class EventRouter {

    private final ListenerRegistry registry;

    EventRouter(ListenerRegistry registry) {
        this.registry = registry;
    }

    void route(Event event) {
        if (event == null || event.getTarget() == null) {
            return;
        }

        List<GameObject> path = buildPath(event.getTarget());
        long now = System.nanoTime();

        // Capture phase: root -> target
        for (GameObject node : path) {
            if (dispatchPhase(event, node, EventPhase.CAPTURE, now)) {
                return;
            }
        }

        // Target phase
        if (dispatchPhase(event, event.getTarget(), EventPhase.TARGET, now)) {
            return;
        }

        // Bubble phase: target parent -> root
        for (int i = path.size() - 1; i >= 0; i--) {
            GameObject node = path.get(i);
            if (node == event.getTarget()) {
                continue;
            }
            if (dispatchPhase(event, node, EventPhase.BUBBLE, now)) {
                return;
            }
        }
    }

    private boolean dispatchPhase(Event event, GameObject current, EventPhase phase, long now) {
        event.setPhase(phase);
        event.setCurrent(current);
        var entries = registry.get(current, event.getType(), phase);
        if (entries.isEmpty()) {
            return event.isPropagationStopped();
        }

        for (ListenerEntry entry : entries) {
            if (event.isPropagationStopped()) {
                break;
            }
            if (!passesGuards(entry, event, current, now)) {
                continue;
            }

            EventListener<Event> listener = entry.getListener();
            EventResult result = listener.handle(new EventContext<>(event, current, 0f));

            entry.getLastFireNanos().set(now);
            // 一次性监听器移除
            if (entry.getOptions().isOnce()) {
                registry.remove(current, event.getType(), phase, entry);
            }

            // 判断是否停止传播
            if (result == EventResult.STOP || result == EventResult.CONSUME) {
                event.stopPropagation();
            }
            // 执行默认处理
            if (result == EventResult.CONSUME) {
                event.preventDefault();
            }
        }
        return event.isPropagationStopped();
    }

    private boolean passesGuards(ListenerEntry entry, Event event, GameObject current, long now) {
        var options = entry.getOptions();
        if (options.getPredicate() != null && !options.getPredicate().test(new EventContext<>(event, current, 0f))) {
            return false;
        }

        long last = entry.getLastFireNanos().get();
        if (options.getThrottleNanos() > 0 && (now - last) < options.getThrottleNanos()) {
            return false;
        }

        long scheduled = entry.getScheduledNanos().get();
        if (options.getDebounceNanos() > 0) {
            if (now < scheduled) {
                return false;
            }
            entry.getScheduledNanos().set(now + options.getDebounceNanos());
        }

        return true;
    }

    private List<GameObject> buildPath(GameObject target) {
        List<GameObject> path = new ArrayList<>();
        GameObject cursor = target.parent();
        while (cursor != null) {
            path.add(0, cursor);
            cursor = cursor.parent();
        }
        return path;
    }
}
