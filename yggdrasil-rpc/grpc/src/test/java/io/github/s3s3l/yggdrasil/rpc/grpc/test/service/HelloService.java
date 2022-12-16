package io.github.s3s3l.yggdrasil.rpc.grpc.test.service;

import io.github.s3s3l.yggdrasil.rpc.grpc.test.hello.HelloGrpc.HelloImplBase;
import io.github.s3s3l.yggdrasil.rpc.grpc.test.hello.Message;
import io.github.s3s3l.yggdrasil.rpc.grpc.test.hello.People;
import io.grpc.stub.StreamObserver;

public class HelloService extends HelloImplBase {

    @Override
    public void sayHello(People request, StreamObserver<Message> responseObserver) {
        responseObserver.onNext(Message.newBuilder()
                .setFrom("local server")
                .setWords(String.format("Hello %s %s", request.getFirstName(), request.getLastName()))
                .build());
        responseObserver.onCompleted();
    }

}
