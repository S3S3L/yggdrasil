package io.github.s3s3l.yggdrasil.boot.old.component.exceptions;

public class ComponentInstantiateException extends ComponentRuntimeException {

	/**
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = -2798409354227468912L;

	public ComponentInstantiateException() {
		super();
	}

	public ComponentInstantiateException(String message) {
		super(message);
	}

	public ComponentInstantiateException(String... messages) {
		super(messages);
	}

	public ComponentInstantiateException(Throwable cause) {
		super();
	}

	public ComponentInstantiateException(Throwable cause, String message) {
		super(cause, message);
	}

	public ComponentInstantiateException(Throwable cause, String... messages) {
		super(cause, messages);
	}
}
