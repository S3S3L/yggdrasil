package io.github.s3s3l.yggdrasil.register.nacos.register;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.builder.InstanceBuilder;
import com.fasterxml.jackson.core.type.TypeReference;

import io.github.s3s3l.yggdrasil.register.core.event.BasicEvent;
import io.github.s3s3l.yggdrasil.register.core.event.BasicEventType;
import io.github.s3s3l.yggdrasil.register.core.listener.ListenType;
import io.github.s3s3l.yggdrasil.register.core.listener.Listener;
import io.github.s3s3l.yggdrasil.register.core.node.Node;
import io.github.s3s3l.yggdrasil.register.core.register.Register;
import io.github.s3s3l.yggdrasil.register.core.register.RegisterType;
import io.github.s3s3l.yggdrasil.register.core.register.exception.ListenerRegisterException;
import io.github.s3s3l.yggdrasil.register.core.register.exception.RegisterException;
import io.github.s3s3l.yggdrasil.register.nacos.event.NacosEventData;
import io.github.s3s3l.yggdrasil.register.nacos.listener.NacosListenerMeta;
import io.github.s3s3l.yggdrasil.utils.stuctural.StructuralHelper;

public class NacosRegister implements Register<Node, byte[], BasicEventType, BasicEvent> {
    private final Map<Listener<byte[], BasicEventType, BasicEvent>, NacosListenerMeta> listenerCache = new ConcurrentHashMap<>();
    private final NamingService namingService;
    private final StructuralHelper structuralHelper;

    public NacosRegister(NamingService namingService, StructuralHelper structuralHelper) {
        this.namingService = namingService;
        this.structuralHelper = structuralHelper;
    }

    @Override
    public void addListener(Node node, Listener<byte[], BasicEventType, BasicEvent> listener, ListenType type) {
        if (listenerCache.containsKey(listener)) {
            throw new ListenerRegisterException("Listener was registed.");
        }
        try {
            EventListener eventListener = event -> {
                if (!(event instanceof NamingEvent)) {
                    return;
                }

                NamingEvent namingEvent = (NamingEvent) event;
                listener.onEvent(BasicEvent.builder()
                        .eventType(BasicEventType.CHANGE)
                        .data(() -> structuralHelper.toStructuralBytes(NacosEventData.fromNamingEvent(namingEvent)))
                        .build());
            };
            namingService.subscribe(node.getName(), node.getGroup(), eventListener);
            listenerCache.put(listener, NacosListenerMeta.builder()
                    .node(node)
                    .eventListener(eventListener)
                    .build());
        } catch (NacosException e) {
            throw new ListenerRegisterException(e);
        }

    }

    @Override
    public void removeListener(Listener<byte[], BasicEventType, BasicEvent> listener) {
        listenerCache.computeIfPresent(listener, (k, meta) -> {
            try {
                namingService.unsubscribe(meta.getNode()
                        .getName(),
                        meta.getNode()
                                .getGroup(),
                        meta.getEventListener());
            } catch (NacosException e) {
                throw new ListenerRegisterException(e);
            }
            return null;
        });

    }

    @Override
    public boolean register(Node node, byte[] nodeInfo) {
        try {
            namingService.registerInstance(node.getName(), node.getGroup(), InstanceBuilder.newBuilder()
                    .setServiceName(node.getName())
                    .setIp(node.getHost())
                    .setPort(node.getPort())
                    .setMetadata(structuralHelper.toObject(nodeInfo, new TypeReference<Map<String, String>>() {
                    }))
                    .build());
            // namingService.registerInstance(node.getName(), node.getGroup(),
            // node.getHost(), node.getPort());
        } catch (NacosException e) {
            throw new RegisterException(e);
        }
        return true;
    }

    @Override
    public boolean update(Node node, byte[] nodeInfo) {
        return register(node, nodeInfo);
    }

    @Override
    public boolean remove(Node node) {
        try {
            namingService.deregisterInstance(node.getName(), node.getGroup(), node.getHost(), node.getPort());
        } catch (NacosException e) {
            throw new RegisterException(e);
        }
        return true;
    }

    @Override
    public RegisterType type() {
        return RegisterType.NACOS;
    }

}
