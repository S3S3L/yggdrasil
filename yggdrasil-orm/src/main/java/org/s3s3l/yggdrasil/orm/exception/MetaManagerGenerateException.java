package org.s3s3l.yggdrasil.orm.exception;

public class MetaManagerGenerateException extends RuntimeException {

    public MetaManagerGenerateException() {
    }

    public MetaManagerGenerateException(String message) {
        super(message);
    }

    public MetaManagerGenerateException(Throwable cause) {
        super(cause);
    }

    public MetaManagerGenerateException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetaManagerGenerateException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
