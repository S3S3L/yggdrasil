package io.github.s3s3l.yggdrasil.rpc.grpc.service;

import java.lang.reflect.InvocationTargetException;

import com.google.protobuf.ByteString;

import io.github.s3s3l.yggdrasil.rpc.core.bean.ResponseCode;
import io.github.s3s3l.yggdrasil.rpc.core.service.ServiceManager;
import io.github.s3s3l.yggdrasil.rpc.core.service.ServiceMeta;
import io.github.s3s3l.yggdrasil.rpc.grpc.exception.SerializerNotFoundException;
import io.github.s3s3l.yggdrasil.rpc.grpc.exception.ServiceNotFoundException;
import io.github.s3s3l.yggdrasil.rpc.grpc.serialize.Serializer;
import io.github.s3s3l.yggdrasil.rpc.grpc.serialize.SerializerManager;
import io.github.s3s3l.yggdrasil.rpc.grpc.yrpc.Request;
import io.github.s3s3l.yggdrasil.rpc.grpc.yrpc.Response;
import io.github.s3s3l.yggdrasil.rpc.grpc.yrpc.YrpcGrpc.YrpcImplBase;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class YrpcService extends YrpcImplBase {
    private ServiceManager serviceManager;
    private SerializerManager serializerManager;

    @Override
    public void request(Request request, StreamObserver<Response> responseObserver) {
        ServiceMeta serviceMeta = serviceManager.getService(request.getService());

        if (serviceMeta == null) {
            log.error("Service {} not found.", request.getService());
            responseObserver.onError(new ServiceNotFoundException(Status.NOT_FOUND));
            return;
        }

        Serializer serializer = serializerManager.getSerializer(request.getSerializeType());

        if (serializer == null) {
            log.error("Serializer {} not found.", request.getSerializeType());
            responseObserver.onError(new SerializerNotFoundException(Status.INVALID_ARGUMENT));
            return;
        }

        Object[] args = new Object[request.getArgsCount()];

        for (int i = 0; i < request.getArgsCount(); i++) {
            try {
                args[i] = serializer.toObject(request.getArgs(i)
                        .toByteArray(), Class.forName(request.getArgTypes(i)));
            } catch (ClassNotFoundException e) {
                log.error("Argument type not found.", e);
                responseObserver.onError(new SerializerNotFoundException(Status.INVALID_ARGUMENT));
                return;
            }
        }

        Object result;

        try {
            result = serviceMeta.getMethod()
                    .invoke(serviceMeta.getTarget(), args);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            log.error("Method invoke error.", e);
            responseObserver.onError(new SerializerNotFoundException(Status.INVALID_ARGUMENT));
            return;
        } catch (Exception e) {
            log.error("Method invoke error.", e);
            responseObserver.onError(new SerializerNotFoundException(Status.INTERNAL));
            return;
        }

        responseObserver.onNext(Response.newBuilder()
                .setCode(ResponseCode.OK.name())
                .setMsg(ResponseCode.OK.defMsg())
                .setData(ByteString.copyFrom(serializer.toBytes(result)))
                .build());
        responseObserver.onCompleted();
    }

}
