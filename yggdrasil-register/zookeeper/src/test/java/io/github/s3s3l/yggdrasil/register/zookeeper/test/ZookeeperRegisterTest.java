package io.github.s3s3l.yggdrasil.register.zookeeper.test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;

import io.github.s3s3l.yggdrasil.register.core.event.BasicEvent;
import io.github.s3s3l.yggdrasil.register.core.event.BasicEventType;
import io.github.s3s3l.yggdrasil.register.core.listener.ListenType;
import io.github.s3s3l.yggdrasil.register.core.listener.Listener;
import io.github.s3s3l.yggdrasil.register.core.node.Node;
import io.github.s3s3l.yggdrasil.register.core.register.Register;
import io.github.s3s3l.yggdrasil.register.zookeeper.register.ZooKeeperRegister;
import io.github.s3s3l.yggdrasil.register.zookeeper.test.key.TestKeyGenerator;
import io.github.s3s3l.yggdrasil.utils.file.FileUtils;

public class ZookeeperRegisterTest {

    @Rule
    public GenericContainer<?> zookeeper = new GenericContainer<>(
            new ImageFromDockerfile().withDockerfile(Paths.get(FileUtils.getFirstExistResourcePath("zk.Dockerfile"))))
                    .withExposedPorts(2181)
                    .waitingFor(Wait.forLogMessage(".*PrepRequestProcessor .* started.*", 1));
    public static BasicEvent event = null;

    private static Register<Node, byte[], BasicEventType, BasicEvent> register;
    private static Node node = Node.builder()
            .group("group1")
            .host("host1")
            .build();

    @Before
    public void init() throws InterruptedException {
        zookeeper.start();
        CuratorFramework curator = CuratorFrameworkFactory.newClient(
                String.join(":", zookeeper.getHost(), String.valueOf(zookeeper.getMappedPort(2181))),
                new RetryOneTime(100));

        curator.start();
        register = new ZooKeeperRegister(curator, new TestKeyGenerator());

    }

    @After
    public void clean() {
        zookeeper.stop();
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
        Assert.assertEquals(data, new String(ZookeeperRegisterTest.event.data(), StandardCharsets.UTF_8));
        Assert.assertEquals(BasicEventType.CREATE, ZookeeperRegisterTest.event.eventType());

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
        Assert.assertEquals(data, new String(ZookeeperRegisterTest.event.oldData(), StandardCharsets.UTF_8));
        Assert.assertEquals(newData, new String(ZookeeperRegisterTest.event.data(), StandardCharsets.UTF_8));
        Assert.assertEquals(BasicEventType.CHANGE, ZookeeperRegisterTest.event.eventType());

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
        Assert.assertEquals(data, new String(ZookeeperRegisterTest.event.oldData(), StandardCharsets.UTF_8));
        Assert.assertEquals(BasicEventType.DELETE, ZookeeperRegisterTest.event.eventType());

        register.removeListener(listener);
    }
}
