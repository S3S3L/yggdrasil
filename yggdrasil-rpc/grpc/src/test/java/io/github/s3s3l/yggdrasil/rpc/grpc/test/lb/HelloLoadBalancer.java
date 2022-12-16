package io.github.s3s3l.yggdrasil.rpc.grpc.test.lb;

import io.grpc.LoadBalancer;
import io.grpc.Status;

public class HelloLoadBalancer extends LoadBalancer {

    @Override
    public void handleResolvedAddresses(ResolvedAddresses resolvedAddresses) {
        // TODO Auto-generated method stub
        super.handleResolvedAddresses(resolvedAddresses);
    }

    @Override
    public void handleNameResolutionError(Status error) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void shutdown() {
        // TODO Auto-generated method stub
        
    }
    
}
