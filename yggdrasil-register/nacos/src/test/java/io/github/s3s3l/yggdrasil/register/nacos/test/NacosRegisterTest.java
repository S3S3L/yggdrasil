package io.github.s3s3l.yggdrasil.register.nacos.test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;

import io.github.s3s3l.yggdrasil.register.core.event.BasicEvent;
import io.github.s3s3l.yggdrasil.register.core.event.BasicEventType;
import io.github.s3s3l.yggdrasil.register.core.exception.ServerTimeoutException;
import io.github.s3s3l.yggdrasil.register.core.listener.ListenType;
import io.github.s3s3l.yggdrasil.register.core.listener.Listener;
import io.github.s3s3l.yggdrasil.register.core.node.Node;
import io.github.s3s3l.yggdrasil.register.core.register.Register;
import io.github.s3s3l.yggdrasil.register.nacos.event.NacosEventData;
import io.github.s3s3l.yggdrasil.register.nacos.register.NacosRegister;
import io.github.s3s3l.yggdrasil.test.nacos.NacosExtension;
import io.github.s3s3l.yggdrasil.test.nacos.NacosExtensionConfig;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;
import lombok.AllArgsConstructor;

public class NacosRegisterTest {

    @RegisterExtension
    public NacosExtension nacos = new NacosExtension(NacosExtensionConfig.builder()
            .port(8848)
            .build());
    public static BasicEvent event = null;

    private static Register<Node, byte[], BasicEventType, BasicEvent> register;

    @AllArgsConstructor
    public static class TestListener implements Listener<byte[], BasicEventType, BasicEvent> {
        private CountDownLatch countDownLatch;

        @Override
        public void onEvent(BasicEvent event) {
            NacosRegisterTest.event = event;
            countDownLatch.countDown();
        }
    }

    @BeforeEach
    public void init() throws NacosException, InterruptedException {
        NamingService namingService = NacosFactory.createNamingService(nacos.getEndPoint());
        register = new NacosRegister(namingService, JacksonUtils.JSON);
    }

    @AfterEach
    public void clean() {
        event = null;
    }

    @Test
    public void registerTest() throws InterruptedException {
        Node node = Node.builder()
                .name("service1")
                .group("registerTest")
                .host("host1")
                .port(8888)
                .build();
        CountDownLatch countDownLatch = new CountDownLatch(2);
        Listener<byte[], BasicEventType, BasicEvent> listener = new TestListener(countDownLatch);
        register.addListener(node, listener, ListenType.TREE);

        Map<String, String> data = new HashMap<>();
        data.put("test", "registerTestData");
        register.register(node, JacksonUtils.JSON.toStructuralBytes(data));

        if (!countDownLatch.await(30, TimeUnit.SECONDS)) {
            throw new ServerTimeoutException("等待变更通知超时");
        }

        NacosEventData eventData = JacksonUtils.JSON.toObject(NacosRegisterTest.event.data(), NacosEventData.class);
        Assertions.assertEquals(data.get("test"), eventData.getInstances()
                .get(0)
                .getMetadata()
                .get("test"));
        Assertions.assertEquals(1, eventData.getInstances()
                .size());
        Assertions.assertEquals(BasicEventType.CHANGE, NacosRegisterTest.event.eventType());
        
        register.removeListener(listener);
    }

    @Test
    public void updateTest() throws InterruptedException {
        Node node = Node.builder()
                .name("service1")
                .group("updateTest")
                .host("host1")
                .port(8888)
                .build();
        CountDownLatch countDownLatch = new CountDownLatch(2);
        Listener<byte[], BasicEventType, BasicEvent> listener = new TestListener(countDownLatch);
        register.addListener(node, listener, ListenType.TREE);

        Map<String, String> data = new HashMap<>();
        data.put("test", "registerTestData");
        register.register(node, JacksonUtils.JSON.toStructuralBytes(data));

        Map<String, String> newData = new HashMap<>();
        newData.put("test", "registerTestData2");
        register.update(node, JacksonUtils.JSON.toStructuralBytes(newData));

        if (!countDownLatch.await(30, TimeUnit.SECONDS)) {
            throw new ServerTimeoutException("等待变更通知超时");
        }

        NacosEventData eventData = JacksonUtils.JSON.toObject(NacosRegisterTest.event.data(), NacosEventData.class);
        Assertions.assertEquals(newData.get("test"), eventData.getInstances()
                .get(0)
                .getMetadata()
                .get("test"));
        Assertions.assertEquals(1, eventData.getInstances()
                .size());
        Assertions.assertEquals(BasicEventType.CHANGE, NacosRegisterTest.event.eventType());

        register.removeListener(listener);
    }

    @Test
    public void removeTest() throws InterruptedException {
        Node node = Node.builder()
                .name("service1")
                .group("removeTest")
                .host("host1")
                .port(8888)
                .build();
        CountDownLatch countDownLatch = new CountDownLatch(3);
        Listener<byte[], BasicEventType, BasicEvent> listener = new TestListener(countDownLatch);
        register.addListener(node, listener, ListenType.TREE);

        Map<String, String> data = new HashMap<>();
        data.put("test", "registerTestData");
        register.register(node, JacksonUtils.JSON.toStructuralBytes(data));
        Thread.sleep(1000);
        register.remove(node);

        if (!countDownLatch.await(30, TimeUnit.SECONDS)) {
            throw new ServerTimeoutException("等待变更通知超时");
        }
        NacosEventData eventData = JacksonUtils.JSON.toObject(NacosRegisterTest.event.data(), NacosEventData.class);

        Assertions.assertEquals(0, eventData.getInstances()
                .size());
        Assertions.assertEquals(BasicEventType.CHANGE, NacosRegisterTest.event.eventType());

        register.removeListener(listener);
    }
}
