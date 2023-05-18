package io.github.s3s3l.yggdrasil.orm.exception;

public class DataBindExpressNotFoundException extends RuntimeException {

    public DataBindExpressNotFoundException() {
        super();
    }

    public DataBindExpressNotFoundException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DataBindExpressNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataBindExpressNotFoundException(String message) {
        super(message);
    }

    public DataBindExpressNotFoundException(Throwable cause) {
        super(cause);
    }
    
}
