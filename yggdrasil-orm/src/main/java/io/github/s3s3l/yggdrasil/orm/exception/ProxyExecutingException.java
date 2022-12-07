package io.github.s3s3l.yggdrasil.orm.exception;

public class ProxyExecutingException extends RuntimeException {

    public ProxyExecutingException() {
    }

    public ProxyExecutingException(String message) {
        super(message);
    }

    public ProxyExecutingException(Throwable cause) {
        super(cause);
    }

    public ProxyExecutingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProxyExecutingException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
