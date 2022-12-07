package io.github.s3s3l.yggdrasil.bean.aliyun;

/**
 * <p>
 * </p>
 * ClassName:OSSException <br>
 * Date: Dec 5, 2016 12:06:08 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class OSSException extends RuntimeException {

    /**
     * @since JDK 1.8
     */
    private static final long serialVersionUID = -501595747710344539L;

    public OSSException() {
        super();
    }

    public OSSException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public OSSException(String message, Throwable cause) {
        super(message, cause);
    }

    public OSSException(String message) {
        super(message);
    }

    public OSSException(Throwable cause) {
        super(cause);
    }

}
