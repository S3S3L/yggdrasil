package io.github.s3s3l.yggdrasil.reigister.etcd.test;

import java.nio.charset.StandardCharsets;

import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.etcd.jetcd.Client;
import io.etcd.jetcd.test.EtcdClusterExtension;
import io.github.s3s3l.yggdrasil.register.core.event.BasicEvent;
import io.github.s3s3l.yggdrasil.register.core.event.BasicEventType;
import io.github.s3s3l.yggdrasil.register.core.listener.Listener;
import io.github.s3s3l.yggdrasil.register.core.node.Node;
import io.github.s3s3l.yggdrasil.register.core.register.Register;
import io.github.s3s3l.yggdrasil.register.etcd.register.EtcdRegister;
import io.github.s3s3l.yggdrasil.reigister.etcd.test.key.TestKeyGenerator;

public class EtcdRegisterTest {
    public static BasicEvent event = null;

    @RegisterExtension
    public static final EtcdClusterExtension cluster = EtcdClusterExtension.builder()
            .withNodes(1)
            .build();
    private static Register<Node, byte[], BasicEventType, BasicEvent> register;
    private static Node node = Node.builder()
            .group("group1")
            .host("host1")
            .build();

    @BeforeClass
    public static void init() throws InterruptedException {
        cluster.restart();
        Client client = Client.builder()
                .endpoints(cluster.clientEndpoints())
                .build();
        register = new EtcdRegister(client, new TestKeyGenerator());

    }

    @After
    public void clean(){
        event = null;
    }

    @Test
    public void registerTest() throws InterruptedException {
        Listener<byte[], BasicEventType, BasicEvent> listener = new Listener<>() {
            @Override
            public void onEvent(BasicEvent event) {
                EtcdRegisterTest.event = event;
            }
        };
        register.addListener(node, listener, null);
        String data = "registerTestData";
        register.register(node, data.getBytes(StandardCharsets.UTF_8));

        Thread.sleep(100);
        Assert.assertEquals(data, new String(EtcdRegisterTest.event.data(), StandardCharsets.UTF_8));
        Assert.assertEquals(BasicEventType.CHANGE, EtcdRegisterTest.event.eventType());

        register.removeListener(listener);
    }

    @Test
    public void updateTest() throws InterruptedException {
        
        Listener<byte[], BasicEventType, BasicEvent> listener = new Listener<>() {
            @Override
            public void onEvent(BasicEvent event) {
                EtcdRegisterTest.event = event;
            }
        };
        register.addListener(node, listener, null);
        String data = "registerTestData";
        register.register(node, data.getBytes(StandardCharsets.UTF_8));
        String newData = "registerTestData2";
        register.update(node, newData.getBytes(StandardCharsets.UTF_8));

        Thread.sleep(100);
        Assert.assertEquals(data, new String(EtcdRegisterTest.event.oldData(), StandardCharsets.UTF_8));
        Assert.assertEquals(newData, new String(EtcdRegisterTest.event.data(), StandardCharsets.UTF_8));
        Assert.assertEquals(BasicEventType.CHANGE, EtcdRegisterTest.event.eventType());

        register.removeListener(listener);
    }

    @Test
    public void removeTest() throws InterruptedException {

        Listener<byte[], BasicEventType, BasicEvent> listener = new Listener<>() {
            @Override
            public void onEvent(BasicEvent event) {
                EtcdRegisterTest.event = event;
            }
        };
        register.addListener(node, listener, null);
        String data = "registerTestDat3";
        register.register(node, data.getBytes(StandardCharsets.UTF_8));
        register.remove(node);

        Thread.sleep(100);
        Assert.assertEquals(data, new String(EtcdRegisterTest.event.oldData(), StandardCharsets.UTF_8));
        Assert.assertEquals(BasicEventType.DELETE, EtcdRegisterTest.event.eventType());

        register.removeListener(listener);
    }

}
