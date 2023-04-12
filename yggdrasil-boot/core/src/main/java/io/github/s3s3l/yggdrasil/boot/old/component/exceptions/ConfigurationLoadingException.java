package io.github.s3s3l.yggdrasil.boot.old.component.exceptions;

public class ConfigurationLoadingException extends ComponentRuntimeException {

	/**
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = -4662846750115063964L;

	public ConfigurationLoadingException() {
		super();
	}

	public ConfigurationLoadingException(String... messages) {
		super(messages);
	}

	public ConfigurationLoadingException(String message) {
		super(message);
	}

	public ConfigurationLoadingException(Throwable cause, String... messages) {
		super(cause, messages);
	}

	public ConfigurationLoadingException(Throwable cause, String message) {
		super(cause, message);
	}

	public ConfigurationLoadingException(Throwable cause) {
		super(cause);
	}

}
