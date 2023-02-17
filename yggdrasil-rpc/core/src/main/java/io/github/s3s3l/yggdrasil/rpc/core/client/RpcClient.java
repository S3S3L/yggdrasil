package io.github.s3s3l.yggdrasil.rpc.core.client;

public interface RpcClient {
    
    void init();

    void start();

    void stop();

    ClientStatus status();

    void destroy();
}
