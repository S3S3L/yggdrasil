package io.github.s3s3l.yggdrasil.document.excel.exception;

/**
 * <p>
 * </p>
 * ClassName:WorkbookException <br>
 * Date: Aug 23, 2017 4:54:34 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class WorkbookException extends RuntimeException {

    /**
     * @since JDK 1.8
     */
    private static final long serialVersionUID = 4090279021153819231L;

    public WorkbookException() {
        super();
    }

    public WorkbookException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public WorkbookException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorkbookException(String message) {
        super(message);
    }

    public WorkbookException(Throwable cause) {
        super(cause);
    }

}
