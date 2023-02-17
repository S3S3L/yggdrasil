package io.github.s3s3l.yggdrasil.rpc.grpc.server;

import java.io.IOException;

import io.github.s3s3l.yggdrasil.register.core.event.BasicEvent;
import io.github.s3s3l.yggdrasil.register.core.event.BasicEventType;
import io.github.s3s3l.yggdrasil.register.core.node.Node;
import io.github.s3s3l.yggdrasil.register.core.register.Register;
import io.github.s3s3l.yggdrasil.rpc.core.server.AbstractRpcServer;
import io.github.s3s3l.yggdrasil.rpc.core.server.exception.ServerException;
import io.github.s3s3l.yggdrasil.rpc.grpc.core.ServerNode;
import io.github.s3s3l.yggdrasil.rpc.grpc.service.YrpcService;
import io.github.s3s3l.yggdrasil.utils.common.NetUtils;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class GrpcServer extends AbstractRpcServer<GrpcServerConfig> {
    private Register<Node, byte[], BasicEventType, BasicEvent> register;
    private Server server;

    public GrpcServer(GrpcServerConfig serverConfig) {
        super(serverConfig);
        this.server = createServer();
    }

    private Server createServer() {
        return ServerBuilder.forPort(serverConfig.getPort())
                .addService(new YrpcService())
                .build();
    }

    @Override
    protected void internalInit() {
        register.register(serverConfig, JacksonUtils.JSON.toStructuralBytes(ServerNode.builder()
                .host(NetUtils.IPV4)
                .port(serverConfig.getPort())
                .status(status.get())
                .group(serverConfig.getGroup())
                .build()));
    }

    @Override
    protected void internalStart() {
        updateStatus();
        try {
            if (server.isShutdown() || server.isTerminated()) {
                this.server = createServer();
            }
            server.start()
                    .awaitTermination();
        } catch (InterruptedException | IOException e) {
            throw new ServerException(e);
        }
    }

    @Override
    protected void afterStarted() {
        updateStatus();
    }

    @Override
    protected void internalStop() {
        updateStatus();
        if (!server.isTerminated()) {
            server.shutdown();
        }
    }

    @Override
    protected void afterStoped() {
        updateStatus();
    }

    @Override
    protected void internalDestroy() {
        updateStatus();
        if (!server.isTerminated()) {
            server.shutdown();
        }

        register.remove(serverConfig);
    }

    private void updateStatus() {
        register.update(serverConfig, JacksonUtils.JSON.toStructuralBytes(ServerNode.builder()
                .host(NetUtils.IPV4)
                .port(serverConfig.getPort())
                .status(status.get())
                .group(serverConfig.getGroup())
                .build()));
    }

}
