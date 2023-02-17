package io.github.s3s3l.yggdrasil.rpc.grpc.client;

import io.github.s3s3l.yggdrasil.register.core.event.BasicEvent;
import io.github.s3s3l.yggdrasil.register.core.event.BasicEventType;
import io.github.s3s3l.yggdrasil.register.core.key.KeyGenerator;
import io.github.s3s3l.yggdrasil.register.core.node.Node;
import io.github.s3s3l.yggdrasil.register.core.register.Register;
import io.github.s3s3l.yggdrasil.rpc.core.client.AbstractRpcClient;
import io.github.s3s3l.yggdrasil.rpc.grpc.lb.DefaultLoadBalancerProvider;
import io.github.s3s3l.yggdrasil.rpc.grpc.nr.DefaultNameResolverProvider;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.LoadBalancerRegistry;
import io.grpc.ManagedChannel;
import io.grpc.NameResolverRegistry;

public class GrpcClient extends AbstractRpcClient<GrpcClientConfig> {
    private Register<Node, byte[], BasicEventType, BasicEvent> register;
    private KeyGenerator keyGenerator;

    public GrpcClient(GrpcClientConfig clientConfig) {
        super(clientConfig);
    }

    @Override
    protected void internalInit() {
        NameResolverRegistry.getDefaultRegistry()
                .register(new DefaultNameResolverProvider());
        LoadBalancerRegistry.getDefaultRegistry()
                .register(new DefaultLoadBalancerProvider());
        ManagedChannel channel = Grpc.newChannelBuilder("yrpc:///hello-service", InsecureChannelCredentials.create())
                .build();
    }

    @Override
    protected void internalStart() {

    }

    @Override
    protected void afterStarted() {
    }

    @Override
    protected void internalStop() {
        
    }

    @Override
    protected void afterStoped() {
    }

    @Override
    protected void internalDestroy() {
    }
    
}
