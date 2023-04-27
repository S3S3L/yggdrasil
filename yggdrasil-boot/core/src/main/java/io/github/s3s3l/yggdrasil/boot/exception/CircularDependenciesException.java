package io.github.s3s3l.yggdrasil.boot.exception;

public class CircularDependenciesException extends RuntimeException {

    public CircularDependenciesException() {
    }

    public CircularDependenciesException(String message) {
        super(message);
    }

    public CircularDependenciesException(Throwable cause) {
        super(cause);
    }

    public CircularDependenciesException(String message, Throwable cause) {
        super(message, cause);
    }

    public CircularDependenciesException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
