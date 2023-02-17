package io.github.s3s3l.yggdrasil.rpc.core.client;

import java.util.concurrent.atomic.AtomicReference;

import io.github.s3s3l.yggdrasil.rpc.core.client.exception.ClientException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractRpcClient<C extends ClientConfig> implements RpcClient {
    protected final C clientConfig;
    protected AtomicReference<ClientStatus> status = new AtomicReference<>(ClientStatus.CREATED);

    public AbstractRpcClient(C clientConfig) {
        this.clientConfig = clientConfig;
    }

    @Override
    public void init() {

        if (!status.compareAndSet(ClientStatus.CREATED, ClientStatus.INITIALIZING)) {
            throw new ClientException("Failing to init client. Status: " + status.get());
        }

        log.info("Client initializing... " + clientConfig.getServerName());

        internalInit();

        status.compareAndSet(ClientStatus.INITIALIZING, ClientStatus.INITIALIZED);

        log.info("Client initialized. " + clientConfig);
    }

    @Override
    public void start() {
        if (!status.compareAndSet(ClientStatus.INITIALIZED, ClientStatus.STARTING)
                && !status.compareAndSet(ClientStatus.STOPED, ClientStatus.STARTING)) {
            throw new ClientException("Failing to start client. Status: " + status.get());
        }

        log.info("Client starting... " + clientConfig.getServerName());

        internalStart();

        status.compareAndSet(ClientStatus.STARTING, ClientStatus.RUNNING);

        afterStarted();

        log.info("Client started. " + clientConfig.getServerName());
    }

    @Override
    public void stop() {

        if (!status.compareAndSet(ClientStatus.RUNNING, ClientStatus.STOPING)) {
            throw new ClientException("Failing to stop client. Status: " + status.get());
        }

        log.info("Client stoping... " + clientConfig.getServerName());

        internalStop();

        status.compareAndSet(ClientStatus.STOPING, ClientStatus.STOPED);

        afterStoped();

        log.info("Client stoped. " + clientConfig.getServerName());
    }

    @Override
    public ClientStatus status() {
        return status.get();
    }

    @Override
    public void destroy() {

        if (!status.compareAndSet(ClientStatus.STOPED, ClientStatus.DESTROYING)) {
            throw new ClientException("Failing to destroy client. Status: " + status.get());
        }

        log.info("Client destroying... " + clientConfig.getServerName());

        internalDestroy();

        status.compareAndSet(ClientStatus.DESTROYING, ClientStatus.DESTROYED);

        log.info("Client destroyed. " + clientConfig.getServerName());

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
