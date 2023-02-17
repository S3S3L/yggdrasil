package io.github.s3s3l.yggdrasil.rpc.grpc.nr;

import java.net.URI;

import io.grpc.NameResolver;
import io.grpc.NameResolver.Args;
import io.grpc.NameResolverProvider;

public class DefaultNameResolverProvider extends NameResolverProvider {

    public static final String SCHEME = "yrpc";

    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    protected int priority() {
        return 10;
    }

    @Override
    public NameResolver newNameResolver(URI targetUri, Args args) {
        return new DefaultNameResolver(targetUri);
    }

    @Override
    public String getDefaultScheme() {
        return SCHEME;
    }
    
}
