package org.s3s3l.yggdrasil.cache.exception;

/**
 * <p>
 * </p>
 * ClassName:CacheOperationException <br>
 * Date: Apr 8, 2019 10:13:55 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class CacheOperationException extends RuntimeException {

    /**
     * @since JDK 1.8
     */
    private static final long serialVersionUID = 8014479855325143857L;

    public CacheOperationException() {
        super();
    }

    public CacheOperationException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public CacheOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheOperationException(String message) {
        super(message);
    }

    public CacheOperationException(Throwable cause) {
        super(cause);
    }

}
