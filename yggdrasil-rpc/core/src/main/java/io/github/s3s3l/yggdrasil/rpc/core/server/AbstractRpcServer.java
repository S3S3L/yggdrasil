package io.github.s3s3l.yggdrasil.rpc.core.server;

import java.util.concurrent.atomic.AtomicReference;

import io.github.s3s3l.yggdrasil.register.core.node.Node;
import io.github.s3s3l.yggdrasil.rpc.core.server.exception.ServerException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractRpcServer<C extends Node> implements RpcServer {
    protected final C serverConfig;
    protected AtomicReference<ServerStatus> status = new AtomicReference<>(ServerStatus.CREATED);

    public AbstractRpcServer(C serverConfig) {
        this.serverConfig = serverConfig;
    }

    @Override
    public void init() {

        if (!status.compareAndSet(ServerStatus.CREATED, ServerStatus.INITIALIZING)) {
            throw new ServerException("Failing to init server. Status: " + status.get());
        }

        log.info("Server initializing... " + serverConfig.getName());

        internalInit();

        status.compareAndSet(ServerStatus.INITIALIZING, ServerStatus.INITIALIZED);

        log.info("Server initialized. " + serverConfig);
    }

    @Override
    public void start() {
        if (!status.compareAndSet(ServerStatus.INITIALIZED, ServerStatus.STARTING)
                && !status.compareAndSet(ServerStatus.STOPED, ServerStatus.STARTING)) {
            throw new ServerException("Failing to start server. Status: " + status.get());
        }

        log.info("Server starting... " + serverConfig.getName());

        internalStart();

        status.compareAndSet(ServerStatus.STARTING, ServerStatus.RUNNING);

        afterStarted();

        log.info("Server started. " + serverConfig.getName());
    }

    @Override
    public void stop() {

        if (!status.compareAndSet(ServerStatus.RUNNING, ServerStatus.STOPING)) {
            throw new ServerException("Failing to stop server. Status: " + status.get());
        }

        log.info("Server stoping... " + serverConfig.getName());

        internalStop();

        status.compareAndSet(ServerStatus.STOPING, ServerStatus.STOPED);

        afterStoped();

        log.info("Server stoped. " + serverConfig.getName());
    }

    @Override
    public ServerStatus status() {
        return status.get();
    }

    @Override
    public void destroy() {

        if (!status.compareAndSet(ServerStatus.STOPED, ServerStatus.DESTROYING)) {
            throw new ServerException("Failing to destroy server. Status: " + status.get());
        }

        log.info("Server destroying... " + serverConfig.getName());

        internalDestroy();

        status.compareAndSet(ServerStatus.DESTROYING, ServerStatus.DESTROYED);

        log.info("Server destroyed. " + serverConfig.getName());

    }

    protected void internalInit() {

    }

    protected abstract void internalStart();

    protected void afterStarted() {

    }

    protected abstract void internalStop();

    protected void afterStoped() {

    }

    protected void internalDestroy() {

    }

}
