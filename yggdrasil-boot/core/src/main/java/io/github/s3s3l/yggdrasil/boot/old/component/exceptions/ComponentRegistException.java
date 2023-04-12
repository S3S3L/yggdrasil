package io.github.s3s3l.yggdrasil.boot.old.component.exceptions;  

public class ComponentRegistException extends ComponentRuntimeException {

	/** 
	 * @since JDK 1.8
	 */  
	private static final long serialVersionUID = 7567776581722756284L;

	public ComponentRegistException() {
		super();
	}

	public ComponentRegistException(String message) {
		super(message);
	}

	public ComponentRegistException(String... messages) {
		super(messages);
	}

	public ComponentRegistException(Throwable cause) {
		super();
	}

	public ComponentRegistException(Throwable cause, String message) {
		super(cause, message);
	}

	public ComponentRegistException(Throwable cause, String... messages) {
		super(cause, messages);
	}

}
  