package io.github.s3s3l.yggdrasil.rpc.core.test;

import org.junit.Assert;
import org.junit.Test;

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
        Assert.assertEquals(ServerStatus.INITIALIZED, server.status());
    }

    @Test
    public void startTest() {
        server.init();
        server.start();
        Assert.assertEquals(ServerStatus.RUNNING, server.status());
    }

    @Test(expected = ServerException.class)
    public void startFailTest() {
        server.start();
    }

    @Test
    public void stopTest() {
        server.init();
        server.start();
        server.stop();
        Assert.assertEquals(ServerStatus.STOPED, server.status());
    }

    @Test(expected = ServerException.class)
    public void stopFailTest() {
        server.stop();
    }

    @Test
    public void destroyTest() {
        server.init();
        server.start();
        server.stop();
        server.destroy();
        Assert.assertEquals(ServerStatus.DESTROYED, server.status());
    }

    @Test(expected = ServerException.class)
    public void destroyFailTest() {
        server.destroy();
    }
}
