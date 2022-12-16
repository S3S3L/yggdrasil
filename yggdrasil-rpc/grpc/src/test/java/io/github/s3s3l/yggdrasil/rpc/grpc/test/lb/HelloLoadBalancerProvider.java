package io.github.s3s3l.yggdrasil.rpc.grpc.test.lb;

import io.grpc.LoadBalancer;
import io.grpc.LoadBalancer.Helper;
import io.grpc.LoadBalancerProvider;

public class HelloLoadBalancerProvider extends LoadBalancerProvider {
    public static final String NAME = "hello";

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public String getPolicyName() {
        return NAME;
    }

    @Override
    public LoadBalancer newLoadBalancer(Helper helper) {
        return null;
    }
    
}
