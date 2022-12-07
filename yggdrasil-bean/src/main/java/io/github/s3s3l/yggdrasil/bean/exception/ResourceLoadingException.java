package io.github.s3s3l.yggdrasil.bean.exception;

/**
 * <p>
 * </p>
 * ClassName:ResourceLoadingException <br>
 * Date: Dec 27, 2017 2:58:56 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class ResourceLoadingException extends RuntimeException {

    /**
     * @since JDK 1.8
     */
    private static final long serialVersionUID = 1344837356577468109L;

    public ResourceLoadingException() {
        super();
    }

    public ResourceLoadingException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ResourceLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceLoadingException(String message) {
        super(message);
    }

    public ResourceLoadingException(Throwable cause) {
        super(cause);
    }

}
