package io.github.s3s3l.yggdrasil.bean.exception;

/**
 * <p>
 * </p>
 * ClassName:ModelCreateException <br>
 * Date: Jul 11, 2017 1:12:23 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class ModelCreateException extends RuntimeException {

    /**
     * @since JDK 1.8
     */
    private static final long serialVersionUID = 3024416622854038083L;

    public ModelCreateException() {
        super();
    }

    public ModelCreateException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ModelCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelCreateException(String message) {
        super(message);
    }

    public ModelCreateException(Throwable cause) {
        super(cause);
    }

}
