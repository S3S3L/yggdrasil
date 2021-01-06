package org.s3s3l.yggdrasil.fsm.exception;  

/**
 * <p>
 * </p> 
 * Date:     Sep 17, 2019 2:14:30 PM <br>
 *  
 * @author   kehw_zwei 
 * @version  1.0.0
 * @since    JDK 1.8
 */
public class TransitionException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 7390559143369761352L;

    public TransitionException() {
        super();
    }

    public TransitionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public TransitionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransitionException(String message) {
        super(message);
    }

    public TransitionException(Throwable cause) {
        super(cause);
    }

}
  