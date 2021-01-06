package org.s3s3l.yggdrasil.starter.rabbitmq;

/**
 * 
 * <p>
 * </p>
 * ClassName: RabbitmqStartupException <br>
 * date: Nov 10, 2018 4:27:57 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class RabbitmqStartupException extends RuntimeException {

    /**
     * @since JDK 1.8
     */
    private static final long serialVersionUID = -8685853961732973894L;

    public RabbitmqStartupException() {
        super();
    }

    public RabbitmqStartupException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public RabbitmqStartupException(String message, Throwable cause) {
        super(message, cause);
    }

    public RabbitmqStartupException(String message) {
        super(message);
    }

    public RabbitmqStartupException(Throwable cause) {
        super(cause);
    }

}
