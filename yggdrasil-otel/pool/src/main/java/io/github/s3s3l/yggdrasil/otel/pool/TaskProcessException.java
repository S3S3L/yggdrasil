package io.github.s3s3l.yggdrasil.otel.pool;

public class TaskProcessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public TaskProcessException(String message) {
        super(message);
    }

    public TaskProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskProcessException(Throwable cause) {
        super(cause);
    }
    
}
