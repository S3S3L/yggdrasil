package io.github.s3s3l.yggdrasil.template.exception;

public class TemplateNotFoundException extends RuntimeException {

    public TemplateNotFoundException() {
    }

    public TemplateNotFoundException(String message) {
        super(message);
    }

    public TemplateNotFoundException(Throwable cause) {
        super(cause);
    }

    public TemplateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemplateNotFoundException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
