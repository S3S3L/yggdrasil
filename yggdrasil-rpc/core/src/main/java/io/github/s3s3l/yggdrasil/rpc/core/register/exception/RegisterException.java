package io.github.s3s3l.yggdrasil.rpc.core.register.exception;

public class RegisterException extends RuntimeException {

    public RegisterException() {
    }

    public RegisterException(String message) {
        super(message);
    }

    public RegisterException(Throwable cause) {
        super(cause);
    }

    public RegisterException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegisterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
