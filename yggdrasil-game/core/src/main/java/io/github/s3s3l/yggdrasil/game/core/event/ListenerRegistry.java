package io.github.s3s3l.yggdrasil.game.core.event;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import io.github.s3s3l.yggdrasil.game.core.object.GameObject;

class ListenerRegistry {

    private final Map<GameObject, Map<EventType, Map<EventPhase, CopyOnWriteArrayList<ListenerEntry>>>> registry = new ConcurrentHashMap<>();

    void add(GameObject target, EventType type, EventPhase phase, ListenerEntry entry) {
        registry.computeIfAbsent(target, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(type, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(phase, k -> new CopyOnWriteArrayList<>())
                .add(entry);
        registry.get(target).get(type).get(phase)
                .sort(Comparator.comparingInt(e -> -e.getOptions().getPriority()));
    }

    void remove(GameObject target, EventType type, EventPhase phase, ListenerEntry entry) {
        var phaseMap = registry.get(target);
        if (phaseMap == null) {
            return;
        }
        var typeMap = phaseMap.get(type);
        if (typeMap == null) {
            return;
        }
        var list = typeMap.get(phase);
        if (list == null) {
            return;
        }
        list.remove(entry);
        if (list.isEmpty()) {
            typeMap.remove(phase);
        }
        if (typeMap.isEmpty()) {
            phaseMap.remove(type);
        }
        if (phaseMap.isEmpty()) {
            registry.remove(target);
        }
    }

    List<ListenerEntry> get(GameObject target, EventType type, EventPhase phase) {
        var phaseMap = registry.get(target);
        if (phaseMap == null) {
            return Collections.emptyList();
        }
        var typeMap = phaseMap.get(type);
        if (typeMap == null) {
            return Collections.emptyList();
        }
        var list = typeMap.get(phase);
        if (list == null) {
            return Collections.emptyList();
        }
        return List.copyOf(list);
    }
}
