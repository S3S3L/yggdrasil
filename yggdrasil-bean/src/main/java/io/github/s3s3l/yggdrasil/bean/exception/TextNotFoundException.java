package io.github.s3s3l.yggdrasil.bean.exception;

/**
 * <p>
 * </p>
 * ClassName:TextNotFoundException <br>
 * Date: Apr 25, 2019 9:20:39 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class TextNotFoundException extends RuntimeException {

    /**
     * @since JDK 1.8
     */
    private static final long serialVersionUID = 8123233276120550988L;

    public TextNotFoundException() {
        super();
    }

    public TextNotFoundException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public TextNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TextNotFoundException(String message) {
        super(message);
    }

    public TextNotFoundException(Throwable cause) {
        super(cause);
    }

}
