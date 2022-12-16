package io.github.s3s3l.yggdrasil.rpc.grpc.test.nr;

import java.net.URI;

import io.grpc.NameResolver;
import io.grpc.NameResolver.Args;
import io.grpc.NameResolverProvider;

public class HelloNameResolverProvider extends NameResolverProvider {

    public static final String SCHEME = "hello";

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
        return new HelloNameResolver(targetUri);
    }

    @Override
    public String getDefaultScheme() {
        return SCHEME;
    }
    
}
