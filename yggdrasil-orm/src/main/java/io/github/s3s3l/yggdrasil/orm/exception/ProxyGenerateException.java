package io.github.s3s3l.yggdrasil.orm.exception;

public class ProxyGenerateException extends RuntimeException {

    public ProxyGenerateException() {
    }

    public ProxyGenerateException(String message) {
        super(message);
    }

    public ProxyGenerateException(Throwable cause) {
        super(cause);
    }

    public ProxyGenerateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProxyGenerateException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
