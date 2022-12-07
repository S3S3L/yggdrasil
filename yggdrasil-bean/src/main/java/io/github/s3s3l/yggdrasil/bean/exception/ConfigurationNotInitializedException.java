package io.github.s3s3l.yggdrasil.bean.exception;

/**
 * <p>
 * </p>
 * ClassName:ConfigurationNotInitializedException <br>
 * Date: Apr 25, 2019 9:19:56 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class ConfigurationNotInitializedException extends RuntimeException {

    /**
     * @since JDK 1.8
     */
    private static final long serialVersionUID = -6828441908944137258L;

    public ConfigurationNotInitializedException() {
        super();
    }

    public ConfigurationNotInitializedException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ConfigurationNotInitializedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigurationNotInitializedException(String message) {
        super(message);
    }

    public ConfigurationNotInitializedException(Throwable cause) {
        super(cause);
    }

}
