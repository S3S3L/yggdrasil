package org.s3s3l.yggdrasil.bean.exception;

/**
 * <p>
 * </p>
 * ClassName:ConfigurationException <br>
 * Date: Apr 4, 2019 4:34:34 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class ConfigurationException extends RuntimeException {

    /**
     * @since JDK 1.8
     */
    private static final long serialVersionUID = 1170929727140154413L;

    public ConfigurationException() {
        super();
    }

    public ConfigurationException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(Throwable cause) {
        super(cause);
    }

}
