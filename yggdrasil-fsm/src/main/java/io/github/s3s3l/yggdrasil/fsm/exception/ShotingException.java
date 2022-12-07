package io.github.s3s3l.yggdrasil.fsm.exception;  

/**
 * <p>
 * </p> 
 * Date:     Sep 17, 2019 8:14:32 PM <br>
 *  
 * @author   kehw_zwei 
 * @version  1.0.0
 * @since    JDK 1.8
 */
public class ShotingException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 6439260658107215670L;

    public ShotingException() {
        super();
    }

    public ShotingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ShotingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShotingException(String message) {
        super(message);
    }

    public ShotingException(Throwable cause) {
        super(cause);
    }

}
  