package org.s3s3l.yggdrasil.bean.exception;

/**
 * <p>
 * </p>
 * ClassName:HttpRequestException <br>
 * Date: Sep 2, 2016 4:58:21 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class HttpRequestException extends RuntimeException {

    /**
     * @since JDK 1.8
     */
    private static final long serialVersionUID = -3968655525906720782L;

    private int httpStatus;

    public HttpRequestException(int httpStatus) {
        super();
        this.httpStatus = httpStatus;
    }

    public HttpRequestException(int httpStatus, String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.httpStatus = httpStatus;
    }

    public HttpRequestException(int httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public HttpRequestException(int httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpRequestException(int httpStatus, Throwable cause) {
        super(cause);
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

}
