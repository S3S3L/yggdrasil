package io.github.s3s3l.yggdrasil.rpc.grpc.lb;

import io.grpc.LoadBalancer;
import io.grpc.Status;

public class DefaultLoadBalancer extends LoadBalancer {

    @Override
    public void handleResolvedAddresses(ResolvedAddresses resolvedAddresses) {
        this.acceptResolvedAddresses(resolvedAddresses);
    }

    @Override
    public void handleNameResolutionError(Status error) {
    }

    @Override
    public void shutdown() {
    }
    
}
