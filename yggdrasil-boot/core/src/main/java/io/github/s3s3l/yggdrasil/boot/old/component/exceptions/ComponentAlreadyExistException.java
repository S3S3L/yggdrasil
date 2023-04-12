package io.github.s3s3l.yggdrasil.boot.old.component.exceptions;

public class ComponentAlreadyExistException extends ComponentRuntimeException {

	/** 
	 * @since JDK 1.8
	 */  
	private static final long serialVersionUID = -3214142935698159029L;

	public ComponentAlreadyExistException() {
		super();
	}

	public ComponentAlreadyExistException(String message) {
		super(message);
	}

	public ComponentAlreadyExistException(String... messages) {
		super(messages);
	}

	public ComponentAlreadyExistException(Throwable cause) {
		super();
	}

	public ComponentAlreadyExistException(Throwable cause, String message) {
		super(cause, message);
	}

	public ComponentAlreadyExistException(Throwable cause, String... messages) {
		super(cause, messages);
	}

}
  