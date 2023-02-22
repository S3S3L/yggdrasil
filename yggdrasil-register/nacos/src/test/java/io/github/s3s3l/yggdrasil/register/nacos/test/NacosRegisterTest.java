package io.github.s3s3l.yggdrasil.register.nacos.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;

import io.github.s3s3l.yggdrasil.register.core.event.BasicEvent;
import io.github.s3s3l.yggdrasil.register.core.event.BasicEventType;
import io.github.s3s3l.yggdrasil.register.core.listener.ListenType;
import io.github.s3s3l.yggdrasil.register.core.listener.Listener;
import io.github.s3s3l.yggdrasil.register.core.node.Node;
import io.github.s3s3l.yggdrasil.register.core.register.Register;
import io.github.s3s3l.yggdrasil.register.nacos.event.NacosEventData;
import io.github.s3s3l.yggdrasil.register.nacos.register.NacosRegister;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;

public class NacosRegisterTest {

    @SuppressWarnings("deprecation")
    @Rule
    public FixedHostPortGenericContainer<?> nacos = new FixedHostPortGenericContainer<>("nacos/nacos-server")
            .withEnv("MODE", "standalone")
            .withFixedExposedPort(8848, 8848)
            .withFixedExposedPort(7848, 7848)
            .withFixedExposedPort(9848, 9848)
            .withFixedExposedPort(9849, 9849)
            .waitingFor(Wait.forLogMessage(".*Nacos started successfully in stand alone mode.*", 1));
    public static BasicEvent event = null;

    private static Register<Node, byte[], BasicEventType, BasicEvent> register;
    private static Node node = Node.builder()
            .name("service1")
            .group("group1")
            .host("host1")
            .port(8888)
            .build();
    private Listener<byte[], BasicEventType, BasicEvent> listener = new Listener<>() {
        @Override
        public void onEvent(BasicEvent event) {
            NacosRegisterTest.event = event;
        }
    };

    @Before
    public void init() throws NacosException, InterruptedException {
        nacos.start();
        NamingService namingService = NacosFactory.createNamingService(String.join(":", nacos.getHost(), "8848"));
        register = new NacosRegister(namingService, JacksonUtils.JSON);
    }

    @After
    public void clean() {
        register.removeListener(listener);
        nacos.stop();
        event = null;
    }

    @Test
    public void registerTest() throws InterruptedException {
        register.addListener(node, listener, ListenType.TREE);

        Map<String, String> data = new HashMap<>();
        data.put("test", "registerTestData");
        register.register(node, JacksonUtils.JSON.toStructuralBytes(data));

        Thread.sleep(2000);

        NacosEventData eventData = JacksonUtils.JSON.toObject(NacosRegisterTest.event.data(), NacosEventData.class);
        Assert.assertEquals(data.get("test"), eventData.getInstances()
                .get(0)
                .getMetadata()
                .get("test"));
        Assert.assertEquals(1, eventData.getInstances()
                .size());
        Assert.assertEquals(BasicEventType.CHANGE, NacosRegisterTest.event.eventType());
    }

    @Test
    public void updateTest() throws InterruptedException {
        register.addListener(node, listener, ListenType.TREE);

        Map<String, String> data = new HashMap<>();
        data.put("test", "registerTestData");
        register.register(node, JacksonUtils.JSON.toStructuralBytes(data));

        Map<String, String> newData = new HashMap<>();
        newData.put("test", "registerTestData2");
        register.update(node, JacksonUtils.JSON.toStructuralBytes(newData));

        Thread.sleep(2000);

        NacosEventData eventData = JacksonUtils.JSON.toObject(NacosRegisterTest.event.data(), NacosEventData.class);
        Assert.assertEquals(newData.get("test"), eventData.getInstances()
                .get(0)
                .getMetadata()
                .get("test"));
        Assert.assertEquals(1, eventData.getInstances()
                .size());
        Assert.assertEquals(BasicEventType.CHANGE, NacosRegisterTest.event.eventType());
    }

    @Test
    public void removeTest() throws InterruptedException {
        register.addListener(node, listener, ListenType.TREE);
        
        Map<String, String> data = new HashMap<>();
        data.put("test", "registerTestData");
        register.register(node, JacksonUtils.JSON.toStructuralBytes(data));
        register.remove(node);

        Thread.sleep(2000);

        Assert.assertEquals(0, JacksonUtils.JSON.toObject(NacosRegisterTest.event.data(), NacosEventData.class)
                .getInstances()
                .size());
        Assert.assertEquals(BasicEventType.CHANGE, NacosRegisterTest.event.eventType());
    }
}
