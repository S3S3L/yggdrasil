package io.github.s3s3l.yggdrasil.rpc.grpc.test.nr;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Arrays;

import io.github.s3s3l.yggdrasil.rpc.grpc.test.Server;
import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;

public class HelloNameResolver extends NameResolver {
    private Listener2 listener;
    private String serviceName;

    public HelloNameResolver(URI uri) {
        this.serviceName = uri.getPath()
                .substring(1);
    }

    @Override
    public void start(Listener2 listener) {
        this.listener = listener;
        this.refresh();
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void refresh() {
        ResolutionResult resolutionResult = ResolutionResult.newBuilder()
                .setAddresses(
                        Arrays.asList(new EquivalentAddressGroup(new InetSocketAddress("localhost", Server.PORT))))
                .build();
        this.listener.onResult(resolutionResult);
    }

    @Override
    public String getServiceAuthority() {
        return this.serviceName;
    }

}
