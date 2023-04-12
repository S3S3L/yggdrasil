package io.github.s3s3l.yggdrasil.boot.old.exceptions;

public class ApplicationRunTimeException extends RuntimeException {

	/**
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = -777570594915862276L;

	public ApplicationRunTimeException() {
		super();
	}

	public ApplicationRunTimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ApplicationRunTimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationRunTimeException(String message) {
		super(message);
	}

	public ApplicationRunTimeException(Throwable cause) {
		super(cause);
	}

}
