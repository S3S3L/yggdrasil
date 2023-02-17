package io.github.s3s3l.yggdrasil.rpc.grpc.lb;

import io.grpc.LoadBalancer;
import io.grpc.LoadBalancer.Helper;
import io.grpc.LoadBalancerProvider;

public class DefaultLoadBalancerProvider extends LoadBalancerProvider {
    public static final String NAME = "yrpc";

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
