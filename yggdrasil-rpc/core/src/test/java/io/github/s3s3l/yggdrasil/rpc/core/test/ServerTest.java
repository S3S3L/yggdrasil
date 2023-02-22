package io.github.s3s3l.yggdrasil.rpc.core.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.rpc.core.server.ServerConfig;
import io.github.s3s3l.yggdrasil.rpc.core.server.ServerStatus;
import io.github.s3s3l.yggdrasil.rpc.core.server.exception.ServerException;
import io.github.s3s3l.yggdrasil.rpc.core.test.server.TestServer;

public class ServerTest {
    private TestServer server = new TestServer(ServerConfig.builder()
            .name("TestServer")
            .port(1234)
            .build());

    @Test
    public void initTest() {
        server.init();
        Assertions.assertEquals(ServerStatus.INITIALIZED, server.status());
    }

    @Test
    public void startTest() {
        server.init();
        server.start();
        Assertions.assertEquals(ServerStatus.RUNNING, server.status());
    }

    @Test
    public void startFailTest() {
        Assertions.assertThrows(ServerException.class, () -> server.start());
    }

    @Test
    public void stopTest() {
        server.init();
        server.start();
        server.stop();
        Assertions.assertEquals(ServerStatus.STOPED, server.status());
    }

    @Test
    public void stopFailTest() {
        Assertions.assertThrows(ServerException.class, () -> server.stop());
    }

    @Test
    public void destroyTest() {
        server.init();
        server.start();
        server.stop();
        server.destroy();
        Assertions.assertEquals(ServerStatus.DESTROYED, server.status());
    }

    @Test
    public void destroyFailTest() {
        Assertions.assertThrows(ServerException.class, () -> server.destroy());
    }
}
