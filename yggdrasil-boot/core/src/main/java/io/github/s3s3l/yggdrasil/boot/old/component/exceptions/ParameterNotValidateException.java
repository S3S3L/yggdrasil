package io.github.s3s3l.yggdrasil.boot.old.component.exceptions;

public class ParameterNotValidateException extends ComponentRuntimeException {

	/**
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = 8993566129918079675L;

	public ParameterNotValidateException() {
		super();
	}

	public ParameterNotValidateException(String message) {
		super(message);
	}

	public ParameterNotValidateException(String... messages) {
		super(messages);
	}

	public ParameterNotValidateException(Throwable cause) {
		super();
	}

	public ParameterNotValidateException(Throwable cause, String message) {
		super(cause, message);
	}

	public ParameterNotValidateException(Throwable cause, String... messages) {
		super(cause, messages);
	}

}
