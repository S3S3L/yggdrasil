package org.s3s3l.yggdrasil.orm.exception;

/**
 * 
 * <p>
 * </p>
 * ClassName: DataMapException <br>
 * date: Sep 20, 2019 11:30:55 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class DataMapException extends RuntimeException {

    /**
     * @since JDK 1.8
     */
    private static final long serialVersionUID = -2582810633523258250L;

    public DataMapException() {
        super();
    }

    public DataMapException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DataMapException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataMapException(String message) {
        super(message);
    }

    public DataMapException(Throwable cause) {
        super(cause);
    }

}
