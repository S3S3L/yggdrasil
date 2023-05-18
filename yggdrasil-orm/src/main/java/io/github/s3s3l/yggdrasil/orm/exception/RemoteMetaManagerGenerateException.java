package io.github.s3s3l.yggdrasil.orm.exception;

public class RemoteMetaManagerGenerateException extends RuntimeException {
    
    public RemoteMetaManagerGenerateException() {
    }

    public RemoteMetaManagerGenerateException(String message) {
        super(message);
    }

    public RemoteMetaManagerGenerateException(Throwable cause) {
        super(cause);
    }

    public RemoteMetaManagerGenerateException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteMetaManagerGenerateException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
