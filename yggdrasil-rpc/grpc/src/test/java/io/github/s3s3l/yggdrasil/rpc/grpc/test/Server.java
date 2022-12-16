package io.github.s3s3l.yggdrasil.rpc.grpc.test;

import java.io.IOException;

import io.github.s3s3l.yggdrasil.rpc.grpc.test.service.HelloService;
import io.grpc.ServerBuilder;

public class Server {
    public static final int PORT = 3742;

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerBuilder.forPort(PORT)
                .addService(new HelloService())
                .build()
                .start()
                .awaitTermination();
    }
}
