package org.s3s3l.yggdrasil.orm.exception;

/**
 * 
 * <p>
 * </p>
 * ClassName: DataSourceInitalizingException <br>
 * date: Sep 20, 2019 11:31:11 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class DataSourceInitalizingException extends RuntimeException {

    /**
     * @since JDK 1.8
     */
    private static final long serialVersionUID = -8922287935011647435L;

    public DataSourceInitalizingException() {
        super();
    }

    public DataSourceInitalizingException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DataSourceInitalizingException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataSourceInitalizingException(String message) {
        super(message);
    }

    public DataSourceInitalizingException(Throwable cause) {
        super(cause);
    }

}
