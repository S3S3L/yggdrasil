package io.github.s3s3l.yggdrasil.boot.old.component.exceptions;

public class ComponentNotRegistException extends ComponentRuntimeException {

	/**
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = 5886948311739998706L;

	public ComponentNotRegistException() {
		super();
	}

	public ComponentNotRegistException(String message) {
		super(message);
	}

	public ComponentNotRegistException(String... messages) {
		super(messages);
	}

	public ComponentNotRegistException(Throwable cause) {
		super();
	}

	public ComponentNotRegistException(Throwable cause, String message) {
		super(cause, message);
	}

	public ComponentNotRegistException(Throwable cause, String... messages) {
		super(cause, messages);
	}

}
