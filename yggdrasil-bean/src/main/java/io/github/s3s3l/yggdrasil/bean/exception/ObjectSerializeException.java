package io.github.s3s3l.yggdrasil.bean.exception;

/**
 * <p>
 * </p>
 * ClassName:ObjectSerializeException <br>
 * Date: Oct 26, 2017 1:44:56 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class ObjectSerializeException extends RuntimeException {

    /**
     * @since JDK 1.8
     */
    private static final long serialVersionUID = 4061735594304725018L;

    public ObjectSerializeException() {
        super();
    }

    public ObjectSerializeException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ObjectSerializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectSerializeException(String message) {
        super(message);
    }

    public ObjectSerializeException(Throwable cause) {
        super(cause);
    }

}
