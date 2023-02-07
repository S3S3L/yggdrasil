package io.github.s3s3l.yggdrasil.register.core.register.exception;

public class ListenerRegisterException extends RuntimeException {

    public ListenerRegisterException() {
    }

    public ListenerRegisterException(String message) {
        super(message);
    }

    public ListenerRegisterException(Throwable cause) {
        super(cause);
    }

    public ListenerRegisterException(String message, Throwable cause) {
        super(message, cause);
    }

    public ListenerRegisterException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
