package io.github.s3s3l.yggdrasil.boot.old.component.exceptions;

import io.github.s3s3l.yggdrasil.utils.common.StringUtils;

public abstract class ComponentRuntimeException extends RuntimeException {

	/**
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = -2583499200346498585L;

	public ComponentRuntimeException() {

	}

	public ComponentRuntimeException(String message) {
		super(message);
	}

	public ComponentRuntimeException(String... messages) {
		super(String.join(StringUtils.EMPTY_STRING, messages));
	}

	public ComponentRuntimeException(Throwable cause) {
		super(cause);
	}

	public ComponentRuntimeException(Throwable cause, String message) {
		super(message, cause);
	}

	public ComponentRuntimeException(Throwable cause, String... messages) {
		super(String.join(StringUtils.EMPTY_STRING, messages), cause);
	}

}
