package org.s3s3l.yggdrasil.bean.exception;

/**
 * <p>
 * </p>
 * ClassName:UploadException <br>
 * Date: Aug 20, 2019 9:51:01 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class UploadException extends RuntimeException {

    /** 
     * @since JDK 1.8
     */  
    private static final long serialVersionUID = 5894847875304277795L;

    public UploadException() {
        super();
    }

    public UploadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public UploadException(String message) {
        super(message);
    }

    public UploadException(Throwable cause) {
        super(cause);
    }

}
