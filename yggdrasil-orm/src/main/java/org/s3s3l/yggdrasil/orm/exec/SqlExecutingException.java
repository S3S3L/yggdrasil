package org.s3s3l.yggdrasil.orm.exec;

/**
 * 
 * <p>
 * </p>
 * ClassName: SqlExecutingException <br>
 * date: Sep 20, 2019 11:31:36 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class SqlExecutingException extends RuntimeException {

    /**
     * @since JDK 1.8
     */
    private static final long serialVersionUID = 8281002521111543371L;

    public SqlExecutingException() {
        super();
    }

    public SqlExecutingException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SqlExecutingException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlExecutingException(String message) {
        super(message);
    }

    public SqlExecutingException(Throwable cause) {
        super(cause);
    }

}
