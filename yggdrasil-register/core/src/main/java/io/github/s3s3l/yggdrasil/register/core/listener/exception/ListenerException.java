package io.github.s3s3l.yggdrasil.register.core.listener.exception;

public class ListenerException extends RuntimeException {

    public ListenerException() {
    }

    public ListenerException(String message) {
        super(message);
    }

    public ListenerException(Throwable cause) {
        super(cause);
    }

    public ListenerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ListenerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
