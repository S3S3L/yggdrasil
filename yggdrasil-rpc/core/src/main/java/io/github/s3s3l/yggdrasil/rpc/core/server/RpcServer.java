package io.github.s3s3l.yggdrasil.rpc.core.server;

public interface RpcServer {

    void init();

    void start();

    void stop();

    ServerStatus status();

    void destroy();
}
