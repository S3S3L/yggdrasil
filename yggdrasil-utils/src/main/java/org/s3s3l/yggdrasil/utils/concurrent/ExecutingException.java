package org.s3s3l.yggdrasil.utils.concurrent;

/**
 * <p>
 * </p>
 * ClassName:ExecutingException <br>
 * Date: Feb 21, 2017 10:13:38 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class ExecutingException extends RuntimeException {

    /**
     * @since JDK 1.8
     */
    private static final long serialVersionUID = 4451685629744280752L;

    public ExecutingException() {
        super();
    }

    public ExecutingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ExecutingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExecutingException(String message) {
        super(message);
    }

    public ExecutingException(Throwable cause) {
        super(cause);
    }

}
