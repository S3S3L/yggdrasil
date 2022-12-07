package io.github.s3s3l.yggdrasil.redis.exception;

/**
 * <p>
 * </p>
 * ClassName:RedisExcuteException <br>
 * Date: Jan 13, 2017 3:38:26 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class RedisExcuteException extends RuntimeException {

    /**
     * @since JDK 1.8
     */
    private static final long serialVersionUID = 8611443895189876328L;

    public RedisExcuteException() {
        super();
    }

    public RedisExcuteException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public RedisExcuteException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedisExcuteException(String message) {
        super(message);
    }

    public RedisExcuteException(Throwable cause) {
        super(cause);
    }

}
