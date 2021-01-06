package org.s3s3l.yggdrasil.utils.reflect.exception;

/**
 * ClassName:ReflectException <br>
 * Date: 2016年6月27日 下午2:14:39 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class ReflectException extends RuntimeException {

    /**
     * @since JDK 1.8
     */
    private static final long serialVersionUID = -8343923497347525699L;

    public ReflectException() {
        super();
    }

    public ReflectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ReflectException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectException(String message) {
        super(message);
    }

    public ReflectException(Throwable cause) {
        super(cause);
    }

}
