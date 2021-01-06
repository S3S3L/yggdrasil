package org.s3s3l.yggdrasil.bean.exception;

/**
 * ClassName:VerifyException <br>
 * Date: 2016年7月8日 上午10:59:20 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class VerifyException extends RuntimeException {

    /**
     * @since JDK 1.8
     */
    private static final long serialVersionUID = -3217272044770522654L;

    public VerifyException() {
        super();
    }

    public VerifyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public VerifyException(String message, Throwable cause) {
        super(message, cause);
    }

    public VerifyException(String message) {
        super(message);
    }

    public VerifyException(Throwable cause) {
        super(cause);
    }

}
