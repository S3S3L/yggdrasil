package io.github.s3s3l.yggdrasil.rpc.grpc.test;

import io.github.s3s3l.yggdrasil.rpc.grpc.test.hello.HelloGrpc;
import io.github.s3s3l.yggdrasil.rpc.grpc.test.hello.HelloGrpc.HelloBlockingStub;
import io.github.s3s3l.yggdrasil.rpc.grpc.test.hello.HelloGrpc.HelloStub;
import io.github.s3s3l.yggdrasil.rpc.grpc.test.hello.People;
import io.github.s3s3l.yggdrasil.rpc.grpc.test.nr.HelloNameResolverProvider;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.LoadBalancerRegistry;
import io.grpc.ManagedChannel;
import io.grpc.NameResolverRegistry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Client {
    public static void main(String[] args) {
        NameResolverRegistry.getDefaultRegistry()
                .register(new HelloNameResolverProvider());
        LoadBalancerRegistry.getDefaultRegistry()
                .register(null);
        // ManagedChannel channel =
        // ManagedChannelBuilder.forAddress("localhost", Server.PORT)
        // .usePlaintext()
        // .build();
        ManagedChannel channel = Grpc.newChannelBuilder("hello:///hello-service", InsecureChannelCredentials.create())
                .build();

        HelloBlockingStub blockingStub = HelloGrpc.newBlockingStub(channel);
        HelloStub asyncStub = HelloGrpc.newStub(channel);

        System.out.println(blockingStub.sayHello(People.newBuilder()
                .setFirstName("He wei")
                .setLastName("Ke")
                .build()));

    }
}
