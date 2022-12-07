package io.github.s3s3l.yggdrasil.http;

/**
 * <p>
 * </p>
 * ClassName:HttpException <br>
 * Date: Sep 6, 2017 5:55:14 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class HttpException extends RuntimeException {

    private final int code;

    /**
     * @since JDK 1.8
     */
    private static final long serialVersionUID = -1496502533536324610L;

    public HttpException(int code) {
        super();
        this.code = code;
    }

    public HttpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = 500;
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);
        this.code = 500;
    }

    public HttpException(int code, String message) {
        super(message);
        this.code = code;
    }

    public HttpException(Throwable cause) {
        super(cause);
        this.code = 500;
    }

    public int getCode() {
        return code;
    }

}
