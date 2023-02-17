package io.github.s3s3l.yggdrasil.reigister.etcd.test;

import java.nio.charset.StandardCharsets;

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
    public static byte[] eventData = null;

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

    @Test
    public void registerTest() throws InterruptedException {
        Listener<byte[], BasicEventType, BasicEvent> listener = new Listener<>() {
            @Override
            public void onEvent(BasicEvent event) {
                eventData = event.data();
            }
        };
        register.addListener(node, listener, null);
        String data = "registerTestData";
        register.register(node, data.getBytes(StandardCharsets.UTF_8));

        Thread.sleep(100);
        Assert.assertEquals(data, new String(eventData, StandardCharsets.UTF_8));

        register.removeListener(listener);
    }

}
