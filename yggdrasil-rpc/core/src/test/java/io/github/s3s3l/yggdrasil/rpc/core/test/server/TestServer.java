package io.github.s3s3l.yggdrasil.rpc.core.test.server;

import io.github.s3s3l.yggdrasil.rpc.core.server.AbstractRpcServer;
import io.github.s3s3l.yggdrasil.rpc.core.server.ServerConfig;

public class TestServer extends AbstractRpcServer<ServerConfig> {

    public TestServer(ServerConfig serverConfig) {
        super(serverConfig);
    }

    @Override
    protected void internalInit() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void internalStart() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void internalStop() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void internalDestroy() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
