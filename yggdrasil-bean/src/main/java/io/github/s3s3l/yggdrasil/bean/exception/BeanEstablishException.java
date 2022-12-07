package io.github.s3s3l.yggdrasil.bean.exception;

/**
 * 
 * <p>
 * </p>
 * ClassName: BeanEstablishException <br> 
 * date: Sep 19, 2019 7:54:17 PM <br> 
 * 
 * @author kehw_zwei 
 * @version 1.0.0 
 * @since JDK 1.8
 */
public class BeanEstablishException extends RuntimeException {

	/**
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = -7616622987493485022L;

	public BeanEstablishException() {
		super();
	}

	public BeanEstablishException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public BeanEstablishException(String message, Throwable cause) {
		super(message, cause);
	}

	public BeanEstablishException(String message) {
		super(message);
	}

	public BeanEstablishException(Throwable cause) {
		super(cause);
	}

}
