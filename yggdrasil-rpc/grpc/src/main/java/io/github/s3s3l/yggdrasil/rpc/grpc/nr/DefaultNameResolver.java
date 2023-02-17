package io.github.s3s3l.yggdrasil.rpc.grpc.nr;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Arrays;

import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;

public class DefaultNameResolver extends NameResolver {
    private Listener2 listener;
    private final String serviceName;
    private final String host;
    private final int port;

    public DefaultNameResolver(URI uri) {
        this.serviceName = uri.getPath()
                .substring(1);
        this.host = uri.getHost();
        this.port = uri.getPort();
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
                .setAddresses(Arrays.asList(new EquivalentAddressGroup(new InetSocketAddress(host, port))))
                .build();
        this.listener.onResult(resolutionResult);
    }

    @Override
    public String getServiceAuthority() {
        return this.serviceName;
    }

}
