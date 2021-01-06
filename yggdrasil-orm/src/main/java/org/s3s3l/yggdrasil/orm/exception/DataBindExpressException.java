package org.s3s3l.yggdrasil.orm.exception;

/**
 * 
 * <p>
 * </p>
 * ClassName: DataBindExpressException <br>
 * date: Sep 20, 2019 11:30:47 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class DataBindExpressException extends RuntimeException {

    /**
     * @since JDK 1.8
     */
    private static final long serialVersionUID = 2632128003367977165L;

    public DataBindExpressException() {
        super();
    }

    public DataBindExpressException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DataBindExpressException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataBindExpressException(String message) {
        super(message);
    }

    public DataBindExpressException(Throwable cause) {
        super(cause);
    }

}
