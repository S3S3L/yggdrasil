package io.github.s3s3l.yggdrasil.boot.old.component.exceptions;  

public class NoImplementInterfaceException extends ComponentRuntimeException {

	/** 
	 * @since JDK 1.8
	 */  
	private static final long serialVersionUID = -2261210391242484974L;

	public NoImplementInterfaceException() {
		super();
	}

	public NoImplementInterfaceException(String message) {
		super(message);
	}

	public NoImplementInterfaceException(String... messages) {
		super(messages);
	}

	public NoImplementInterfaceException(Throwable cause) {
		super();
	}

	public NoImplementInterfaceException(Throwable cause, String message) {
		super(cause, message);
	}

	public NoImplementInterfaceException(Throwable cause, String... messages) {
		super(cause, messages);
	}

}
  