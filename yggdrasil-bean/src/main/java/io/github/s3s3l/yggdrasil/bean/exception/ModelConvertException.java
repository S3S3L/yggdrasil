package io.github.s3s3l.yggdrasil.bean.exception;

/**
 * ClassName:ModelConvertException <br>
 * Date: 2016年6月29日 下午5:35:40 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class ModelConvertException extends RuntimeException {

	/**
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = -7679441633697553661L;

	public ModelConvertException() {
		super();
	}

	public ModelConvertException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ModelConvertException(String message, Throwable cause) {
		super(message, cause);
	}

	public ModelConvertException(String message) {
		super(message);
	}

	public ModelConvertException(Throwable cause) {
		super(cause);
	}

}
