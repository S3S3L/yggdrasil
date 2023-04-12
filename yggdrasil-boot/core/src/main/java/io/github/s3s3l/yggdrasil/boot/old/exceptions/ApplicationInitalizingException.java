package io.github.s3s3l.yggdrasil.boot.old.exceptions;

public class ApplicationInitalizingException extends Exception {

	/**
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = 6040505020468747125L;

	public ApplicationInitalizingException() {
		super();
	}

	public ApplicationInitalizingException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ApplicationInitalizingException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationInitalizingException(String message) {
		super(message);
	}

	public ApplicationInitalizingException(Throwable cause) {
		super(cause);
	}

}
