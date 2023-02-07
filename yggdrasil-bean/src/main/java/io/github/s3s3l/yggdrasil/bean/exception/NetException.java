package io.github.s3s3l.yggdrasil.bean.exception;

public class NetException extends RuntimeException {

    public NetException() {
    }

    public NetException(String message) {
        super(message);
    }

    public NetException(Throwable cause) {
        super(cause);
    }

    public NetException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
