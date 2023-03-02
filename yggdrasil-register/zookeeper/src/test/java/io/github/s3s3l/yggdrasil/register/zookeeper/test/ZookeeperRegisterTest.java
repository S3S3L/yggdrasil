package io.github.s3s3l.yggdrasil.register.zookeeper.test;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.github.s3s3l.yggdrasil.register.core.event.BasicEvent;
import io.github.s3s3l.yggdrasil.register.core.event.BasicEventType;
import io.github.s3s3l.yggdrasil.register.core.exception.ServerTimeoutException;
import io.github.s3s3l.yggdrasil.register.core.listener.ListenType;
import io.github.s3s3l.yggdrasil.register.core.listener.Listener;
import io.github.s3s3l.yggdrasil.register.core.node.Node;
import io.github.s3s3l.yggdrasil.register.core.register.Register;
import io.github.s3s3l.yggdrasil.register.core.register.RegisterFactoryManager;
import io.github.s3s3l.yggdrasil.test.zookeeper.ZooKeeperExtension;
import io.github.s3s3l.yggdrasil.test.zookeeper.ZooKeeperExtensionConfig;

public class ZookeeperRegisterTest {

    @RegisterExtension
    public ZooKeeperExtension zookeeper = new ZooKeeperExtension(ZooKeeperExtensionConfig.builder()
            .build());
    public static BasicEvent event = null;

    private static Register<Node, byte[], BasicEventType, BasicEvent> register;

    private static RegisterFactoryManager registerFactoryManager = new RegisterFactoryManager();

    private CountDownLatch countDownLatch = null;
    private Listener<byte[], BasicEventType, BasicEvent> listener = new Listener<>() {
        @Override
        public void onEvent(BasicEvent event) {
            ZookeeperRegisterTest.event = event;
            countDownLatch.countDown();
        }
    };

    @BeforeAll
    public static void beforeAll() {
        registerFactoryManager.init("io.github.s3s3l.yggdrasil.register");
    }

    @BeforeEach
    public void init() throws InterruptedException {
        register = registerFactoryManager.getRegister(String.join(":", "zookeeper", zookeeper.getEndPoint()));
    }

    @AfterEach
    public void clean() {
        register.removeListener(listener);
        event = null;
    }

    @Test
    public void registerTest() throws InterruptedException {
        countDownLatch = new CountDownLatch(2);

        Node node = Node.builder()
                .group("registerTest")
                .host("host1")
                .build();

        register.addListener(node, listener, ListenType.TREE);

        String data = "registerTestData";
        register.register(node, data.getBytes(StandardCharsets.UTF_8));

        if (!countDownLatch.await(100, TimeUnit.MILLISECONDS)) {
            throw new ServerTimeoutException("等待变更通知超时");
        }

        Assertions.assertEquals(data, new String(ZookeeperRegisterTest.event.data(), StandardCharsets.UTF_8));
        Assertions.assertEquals(BasicEventType.CREATE, ZookeeperRegisterTest.event.eventType());
    }

    @Test
    public void updateTest() throws InterruptedException {
        countDownLatch = new CountDownLatch(3);

        Node node = Node.builder()
                .group("updateTest")
                .host("host1")
                .build();

        register.addListener(node, listener, ListenType.TREE);

        String data = "updateTestData";
        register.register(node, data.getBytes(StandardCharsets.UTF_8));
        String newData = "updateTestData2";
        register.update(node, newData.getBytes(StandardCharsets.UTF_8));

        if (!countDownLatch.await(100, TimeUnit.MILLISECONDS)) {
            throw new ServerTimeoutException("等待变更通知超时");
        }

        Assertions.assertEquals(data, new String(ZookeeperRegisterTest.event.oldData(), StandardCharsets.UTF_8));
        Assertions.assertEquals(newData, new String(ZookeeperRegisterTest.event.data(), StandardCharsets.UTF_8));
        Assertions.assertEquals(BasicEventType.CHANGE, ZookeeperRegisterTest.event.eventType());
    }

    @Test
    public void removeTest() throws InterruptedException {
        countDownLatch = new CountDownLatch(3);

        Node node = Node.builder()
                .group("removeTest")
                .host("host1")
                .build();

        register.addListener(node, listener, ListenType.TREE);

        String data = "removeTestData";
        register.register(node, data.getBytes(StandardCharsets.UTF_8));
        register.remove(node);

        if (!countDownLatch.await(100, TimeUnit.MILLISECONDS)) {
            throw new ServerTimeoutException("等待变更通知超时");
        }

        Assertions.assertEquals(data, new String(ZookeeperRegisterTest.event.oldData(), StandardCharsets.UTF_8));
        Assertions.assertEquals(BasicEventType.DELETE, ZookeeperRegisterTest.event.eventType());
    }
}
