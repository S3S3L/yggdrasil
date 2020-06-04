package org.s3s3l.yggdrasil.fsm.exception;

/**
 * <p>
 * </p>
 * Date: Sep 17, 2019 5:01:59 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class RestoreException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 9106502717480395444L;

    public RestoreException() {
        super();
    }

    public RestoreException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public RestoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestoreException(String message) {
        super(message);
    }

    public RestoreException(Throwable cause) {
        super(cause);
    }

}
