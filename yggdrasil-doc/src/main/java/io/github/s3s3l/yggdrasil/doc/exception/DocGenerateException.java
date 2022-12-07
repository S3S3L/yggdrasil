package io.github.s3s3l.yggdrasil.doc.exception;

public class DocGenerateException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -3357602149509805291L;

    public DocGenerateException() {
    }

    public DocGenerateException(String message) {
        super(message);
    }

    public DocGenerateException(Throwable cause) {
        super(cause);
    }

    public DocGenerateException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocGenerateException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
