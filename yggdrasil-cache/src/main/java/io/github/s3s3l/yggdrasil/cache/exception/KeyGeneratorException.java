package io.github.s3s3l.yggdrasil.cache.exception;

/**
 * <p>
 * </p>
 * ClassName:KeyGeneratorException <br>
 * Date: Sep 25, 2017 6:34:27 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class KeyGeneratorException extends RuntimeException {

    /**
     * @since JDK 1.8
     */
    private static final long serialVersionUID = -6178657794600899005L;

    public KeyGeneratorException() {
        super();
    }

    public KeyGeneratorException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public KeyGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }

    public KeyGeneratorException(String message) {
        super(message);
    }

    public KeyGeneratorException(Throwable cause) {
        super(cause);
    }

}
