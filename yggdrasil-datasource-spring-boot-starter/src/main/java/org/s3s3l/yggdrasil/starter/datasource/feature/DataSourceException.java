package org.s3s3l.yggdrasil.starter.datasource.feature;

/**
 * <p>
 * </p>
 * ClassName:DataSourceException <br>
 * Date: Dec 1, 2017 1:46:21 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class DataSourceException extends RuntimeException {

    /**
     * @since JDK 1.8
     */
    private static final long serialVersionUID = -8373853640224335521L;

    public DataSourceException() {
        super();
    }

    public DataSourceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DataSourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataSourceException(String message) {
        super(message);
    }

    public DataSourceException(Throwable cause) {
        super(cause);
    }

}
