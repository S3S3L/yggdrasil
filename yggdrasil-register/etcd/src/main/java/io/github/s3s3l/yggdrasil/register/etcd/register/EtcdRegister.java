package io.github.s3s3l.yggdrasil.register.etcd.register;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.Lease;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.options.WatchOption;
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
import io.github.s3s3l.yggdrasil.register.etcd.listener.EtcdListener;
import io.github.s3s3l.yggdrasil.register.etcd.listener.EtcdListenerMeta;
import io.github.s3s3l.yggdrasil.register.etcd.observer.LeaseObserver;

public class EtcdRegister implements Register<Node, byte[], BasicEventType, BasicEvent> {
    public static final long DEFAULT_TTL = 10;

    /**
     * k: key; v: EtcdListenerMeta
     */
    private final Map<ByteSequence, EtcdListenerMeta> watchers = new ConcurrentHashMap<>();
    /**
     * k: listener; v: key
     */
    private final Map<Listener<byte[], BasicEventType, BasicEvent>, ByteSequence> listenerCache = new ConcurrentHashMap<>();
    private final Client client;
    private final KeyGenerator keyGenerator;
    private final long leaseId;

    public EtcdRegister(Client client, KeyGenerator keyGenerator) {
        this(client, keyGenerator, DEFAULT_TTL);
    }

    /**
     * 
     * @param client
     * @param keyGenerator
     * @param ttl
     *            lease ttl
     */
    public EtcdRegister(Client client, KeyGenerator keyGenerator, long ttl) {
        this.client = client;
        this.keyGenerator = keyGenerator;

        Lease leaseClient = client.getLeaseClient();
        try {
            leaseId = leaseClient.grant(ttl)
                    .get()
                    .getID();
            leaseClient.keepAlive(leaseId, new LeaseObserver(leaseId));
        } catch (InterruptedException | ExecutionException e) {
            throw new RegisterException(e);
        }
    }

    @Override
    public void addListener(Node node, Listener<byte[], BasicEventType, BasicEvent> listener, ListenType type) {
        if (listenerCache.containsKey(listener)) {
            throw new ListenerRegisterException("Listener was registed.");
        }

        listenerCache.computeIfAbsent(listener, l -> {
            ByteSequence key = ByteSequence.from(keyGenerator.getRegisterKey(node, node.getHost()),
                    StandardCharsets.UTF_8);

            watchers.computeIfAbsent(key, k -> {
                EtcdListener etcdListener = new EtcdListener(listener);
                return EtcdListenerMeta.builder()
                        .watcher(client.getWatchClient()
                                .watch(k, WatchOption.newBuilder()
                                        .isPrefix(true)
                                        .withPrevKV(true)
                                        .build(), etcdListener))
                        .listener(etcdListener)
                        .build();
            });

            return key;
        });
    }

    @Override
    public void removeListener(Listener<byte[], BasicEventType, BasicEvent> listener) {
        listenerCache.computeIfPresent(listener, (l, k) -> {
            watchers.computeIfPresent(k, (key, meta) -> {
                meta.getWatcher()
                        .close();
                return null;
            });
            return null;
        });
    }

    @Override
    public boolean register(Node node, byte[] data) {
        return update(node, data);
    }

    @Override
    public boolean update(Node node, byte[] data) {
        ByteSequence key = ByteSequence.from(keyGenerator.getRegisterKey(node, node.getHost()), StandardCharsets.UTF_8);
        try {
            client.getKVClient()
                    .put(key, ByteSequence.from(data), PutOption.newBuilder()
                            .withLeaseId(leaseId)
                            .build())
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RegisterException(e);
        }
        return true;
    }

    @Override
    public boolean remove(Node node) {
        ByteSequence key = ByteSequence.from(keyGenerator.getRegisterKey(node, node.getHost()), StandardCharsets.UTF_8);
        try {
            client.getKVClient()
                    .delete(key)
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RegisterException(e);
        }
        return true;
    }

    @Override
    public RegisterType type() {
        return RegisterType.ETCD;
    }
}
