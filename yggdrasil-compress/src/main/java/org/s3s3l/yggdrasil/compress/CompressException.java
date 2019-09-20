package org.s3s3l.yggdrasil.compress;

/**
 * <p>
 * </p>
 * ClassName:CompressException <br>
 * Date: Apr 3, 2019 2:38:26 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class CompressException extends RuntimeException {

    /**
     * @since JDK 1.8
     */
    private static final long serialVersionUID = 2805011706072335111L;

    public CompressException() {
        super();
    }

    public CompressException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public CompressException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompressException(String message) {
        super(message);
    }

    public CompressException(Throwable cause) {
        super(cause);
    }

}
