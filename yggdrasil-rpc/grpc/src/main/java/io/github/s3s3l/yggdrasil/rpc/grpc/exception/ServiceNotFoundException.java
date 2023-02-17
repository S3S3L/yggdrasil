package io.github.s3s3l.yggdrasil.rpc.grpc.exception;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;

public class ServiceNotFoundException extends StatusRuntimeException {

    public ServiceNotFoundException(Status status) {
        super(status);
    }
    
}
