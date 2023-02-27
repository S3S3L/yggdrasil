package io.github.s3s3l.yggdrasil.register.zookeeper.test;

import java.nio.charset.StandardCharsets;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.github.s3s3l.yggdrasil.register.core.event.BasicEvent;
import io.github.s3s3l.yggdrasil.register.core.event.BasicEventType;
import io.github.s3s3l.yggdrasil.register.core.listener.ListenType;
import io.github.s3s3l.yggdrasil.register.core.listener.Listener;
import io.github.s3s3l.yggdrasil.register.core.node.Node;
import io.github.s3s3l.yggdrasil.register.core.register.Register;
import io.github.s3s3l.yggdrasil.register.zookeeper.register.ZooKeeperRegister;
import io.github.s3s3l.yggdrasil.register.zookeeper.test.key.TestKeyGenerator;
import io.github.s3s3l.yggdrasil.test.zookeeper.ZooKeeperExtension;
import io.github.s3s3l.yggdrasil.test.zookeeper.ZooKeeperExtensionConfig;

public class ZookeeperRegisterTest {

    @RegisterExtension
    public ZooKeeperExtension zookeeper = new ZooKeeperExtension(ZooKeeperExtensionConfig.builder()
            .build());
    public static BasicEvent event = null;

    private static Register<Node, byte[], BasicEventType, BasicEvent> register;
    private static Node node = Node.builder()
            .group("group1")
            .host("host1")
            .build();

    @BeforeEach
    public void init() throws InterruptedException {
        CuratorFramework curator = CuratorFrameworkFactory.newClient(zookeeper.getEndPoint(), new RetryOneTime(100));

        curator.start();
        register = new ZooKeeperRegister(curator, new TestKeyGenerator());

    }

    @AfterEach
    public void clean() {
        event = null;
    }

    @Test
    public void registerTest() throws InterruptedException {
        Listener<byte[], BasicEventType, BasicEvent> listener = new Listener<>() {
            @Override
            public void onEvent(BasicEvent event) {
                ZookeeperRegisterTest.event = event;
            }
        };
        register.addListener(node, listener, ListenType.TREE);
        String data = "registerTestData";
        register.register(node, data.getBytes(StandardCharsets.UTF_8));

        Thread.sleep(100);
        Assertions.assertEquals(data, new String(ZookeeperRegisterTest.event.data(), StandardCharsets.UTF_8));
        Assertions.assertEquals(BasicEventType.CREATE, ZookeeperRegisterTest.event.eventType());

        register.removeListener(listener);
    }

    @Test
    public void updateTest() throws InterruptedException {

        Listener<byte[], BasicEventType, BasicEvent> listener = new Listener<>() {
            @Override
            public void onEvent(BasicEvent event) {
                ZookeeperRegisterTest.event = event;
            }
        };
        register.addListener(node, listener, ListenType.TREE);
        String data = "registerTestData";
        register.register(node, data.getBytes(StandardCharsets.UTF_8));
        String newData = "registerTestData2";
        register.update(node, newData.getBytes(StandardCharsets.UTF_8));

        Thread.sleep(100);
        Assertions.assertEquals(data, new String(ZookeeperRegisterTest.event.oldData(), StandardCharsets.UTF_8));
        Assertions.assertEquals(newData, new String(ZookeeperRegisterTest.event.data(), StandardCharsets.UTF_8));
        Assertions.assertEquals(BasicEventType.CHANGE, ZookeeperRegisterTest.event.eventType());

        register.removeListener(listener);
    }

    @Test
    public void removeTest() throws InterruptedException {

        Listener<byte[], BasicEventType, BasicEvent> listener = new Listener<>() {
            @Override
            public void onEvent(BasicEvent event) {
                ZookeeperRegisterTest.event = event;
            }
        };
        register.addListener(node, listener, ListenType.TREE);
        String data = "registerTestDat3";
        register.register(node, data.getBytes(StandardCharsets.UTF_8));
        register.remove(node);

        Thread.sleep(100);
        Assertions.assertEquals(data, new String(ZookeeperRegisterTest.event.oldData(), StandardCharsets.UTF_8));
        Assertions.assertEquals(BasicEventType.DELETE, ZookeeperRegisterTest.event.eventType());

        register.removeListener(listener);
    }
}
