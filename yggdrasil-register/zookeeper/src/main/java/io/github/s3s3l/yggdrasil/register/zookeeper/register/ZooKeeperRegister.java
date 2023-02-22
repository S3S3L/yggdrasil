package io.github.s3s3l.yggdrasil.register.zookeeper.register;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCache.Options;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.zookeeper.CreateMode;

import io.github.s3s3l.yggdrasil.register.core.event.BasicEvent;
import io.github.s3s3l.yggdrasil.register.core.event.BasicEventType;
import io.github.s3s3l.yggdrasil.register.core.key.KeyGenerator;
import io.github.s3s3l.yggdrasil.register.core.listener.ListenType;
import io.github.s3s3l.yggdrasil.register.core.listener.Listener;
import io.github.s3s3l.yggdrasil.register.core.node.Node;
import io.github.s3s3l.yggdrasil.register.core.register.Register;
import io.github.s3s3l.yggdrasil.register.core.register.RegisterType;
import io.github.s3s3l.yggdrasil.register.core.register.exception.ListenerRegisterException;
import io.github.s3s3l.yggdrasil.register.core.register.exception.RegisterException;
import io.github.s3s3l.yggdrasil.register.zookeeper.listener.ZkListenerMeta;

public class ZooKeeperRegister implements Register<Node, byte[], BasicEventType, BasicEvent> {
    private final Map<ListenType, Map<String, CuratorCache>> cacheMap = new ConcurrentHashMap<>();
    private final Map<Listener<byte[], BasicEventType, BasicEvent>, ZkListenerMeta> listenerCache = new ConcurrentHashMap<>();
    private final CuratorFramework client;
    private final KeyGenerator keyGenerator;

    public ZooKeeperRegister(CuratorFramework client, KeyGenerator keyGenerator) {
        this.client = client;
        this.keyGenerator = keyGenerator;
    }

    @Override
    public void addListener(Node node, Listener<byte[], BasicEventType, BasicEvent> listener, ListenType type) {

        if (listenerCache.containsKey(listener)) {
            throw new ListenerRegisterException("Listener was registed.");
        }

        CuratorCacheListener curatorCacheListener = CuratorCacheListener.builder()
                .forInitialized(() -> {
                    listener.onEvent(BasicEvent.builder()
                            .eventType(BasicEventType.INITIALIZED)
                            .build());
                })
                .forCreates(data -> listener.onEvent(BasicEvent.builder()
                        .eventType(BasicEventType.CREATE)
                        .data(() -> data.getData())
                        .build()))
                .forChanges((oldData, newData) -> listener.onEvent(BasicEvent.builder()
                        .eventType(BasicEventType.CHANGE)
                        .oldData(() -> oldData.getData())
                        .data(() -> newData.getData())
                        .build()))
                .forDeletes(data -> listener.onEvent(BasicEvent.builder()
                        .eventType(BasicEventType.DELETE)
                        .oldData(() -> data.getData())
                        .build()))
                .build();

        String key = keyGenerator.getRegisterKey(node, "");
        CuratorCache curatorCache = cacheMap.computeIfAbsent(type, t -> new ConcurrentHashMap<>())
                .computeIfAbsent(key, k -> {
                    CuratorCache cache;
                    switch (type) {
                        case TREE:
                            cache = CuratorCache.build(client, key);
                            break;
                        case CHILDREN:
                            cache = CuratorCache.build(client, key);
                            break;
                        case CURRENT:
                        default:
                            cache = CuratorCache.build(client, key, Options.SINGLE_NODE_CACHE);
                            break;
                    }
                    return cache;
                });
        curatorCache.listenable()
                .addListener(curatorCacheListener);
        curatorCache.start();

        listenerCache.put(listener, ZkListenerMeta.builder()
                .curatorCache(curatorCache)
                .curatorCacheListener(curatorCacheListener)
                .build());
        
    }

    @Override
    public void removeListener(Listener<byte[], BasicEventType, BasicEvent> listener) {
        listenerCache.computeIfPresent(listener, (k, meta) -> {
            meta.getCuratorCache()
                    .listenable()
                    .removeListener(meta.getCuratorCacheListener());
            return null;
        });
    }

    @Override
    public boolean register(Node node, byte[] data) {
        try {
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(keyGenerator.getRegisterKey(node, node.getHost()), data);
        } catch (Exception e) {
            throw new RegisterException(e);
        }
        return true;
    }

    @Override
    public boolean update(Node node, byte[] data) {
        try {
            client.create()
                    .orSetData()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(keyGenerator.getRegisterKey(node, node.getHost()), data);
        } catch (Exception e) {
            throw new RegisterException(e);
        }
        return true;
    }

    @Override
    public boolean remove(Node node) {
        try {
            client.delete()
                    .forPath(keyGenerator.getRegisterKey(node, node.getHost()));
        } catch (Exception e) {
            throw new RegisterException(e);
        }
        return true;
    }

    @Override
    public RegisterType type() {
        return RegisterType.ZK;
    }

}