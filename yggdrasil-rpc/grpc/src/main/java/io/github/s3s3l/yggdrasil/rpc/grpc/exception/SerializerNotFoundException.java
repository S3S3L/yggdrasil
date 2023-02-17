package io.github.s3s3l.yggdrasil.rpc.grpc.exception;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;

public class SerializerNotFoundException extends StatusRuntimeException {

    public SerializerNotFoundException(Status status) {
        super(status);
    }

}
