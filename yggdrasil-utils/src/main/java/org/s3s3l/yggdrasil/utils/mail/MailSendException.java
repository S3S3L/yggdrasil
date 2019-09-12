package org.s3s3l.yggdrasil.utils.mail;

/**
 * <p>
 * </p>
 * ClassName:MailSendException <br>
 * Date: May 23, 2017 2:55:37 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class MailSendException extends RuntimeException {

    /**
     * @since JDK 1.8
     */
    private static final long serialVersionUID = -3795932121305988208L;

    public MailSendException() {
        super();
    }

    public MailSendException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MailSendException(String message, Throwable cause) {
        super(message, cause);
    }

    public MailSendException(String message) {
        super(message);
    }

    public MailSendException(Throwable cause) {
        super(cause);
    }

}
