package org.s3s3l.yggdrasil.starter.apollo;

/**
 * <p>
 * </p>
 * ClassName:DocumentProcessException <br>
 * Date: Jan 28, 2019 4:02:52 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class DocumentProcessException extends RuntimeException {

    public DocumentProcessException() {
        super();
    }

    public DocumentProcessException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DocumentProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentProcessException(String message) {
        super(message);
    }

    public DocumentProcessException(Throwable cause) {
        super(cause);
    }

}
