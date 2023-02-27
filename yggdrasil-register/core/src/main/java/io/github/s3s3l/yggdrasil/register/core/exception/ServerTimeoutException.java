package io.github.s3s3l.yggdrasil.register.core.exception;

public class ServerTimeoutException extends RuntimeException {

    public ServerTimeoutException() {
    }

    public ServerTimeoutException(String message) {
        super(message);
    }

    public ServerTimeoutException(Throwable cause) {
        super(cause);
    }

    public ServerTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerTimeoutException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
