package org.s3s3l.yggdrasil.utils.cache.exception;  
/**
 * <p>
 * </p> 
 * ClassName:CacheFetchException <br> 
 * Date:     Sep 25, 2017 7:00:05 PM <br>
 *  
 * @author   kehw_zwei 
 * @version  1.0.0
 * @since    JDK 1.8
 */
public class CacheFetchException extends RuntimeException {

    /** 
     * @since JDK 1.8
     */  
    private static final long serialVersionUID = 4128778111441691225L;

    public CacheFetchException() {
        super();
    }

    public CacheFetchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public CacheFetchException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheFetchException(String message) {
        super(message);
    }

    public CacheFetchException(Throwable cause) {
        super(cause);
    }

}
  