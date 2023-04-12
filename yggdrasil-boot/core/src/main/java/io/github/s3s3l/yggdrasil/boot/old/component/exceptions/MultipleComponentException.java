package io.github.s3s3l.yggdrasil.boot.old.component.exceptions;

public class MultipleComponentException extends ComponentRuntimeException {

	/**
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = 1685706209591177886L;

	public MultipleComponentException() {
		super();
	}

	public MultipleComponentException(String message) {
		super(message);
	}

	public MultipleComponentException(String... messages) {
		super(messages);
	}

	public MultipleComponentException(Throwable cause) {
		super();
	}

	public MultipleComponentException(Throwable cause, String message) {
		super(cause, message);
	}

	public MultipleComponentException(Throwable cause, String... messages) {
		super(cause, messages);
	}

}
