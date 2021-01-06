package org.s3s3l.yggdrasil.bean.exception;

/**
 * ClassName: RollBackException <br>
 * date: 2016年3月14日 下午4:53:56 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class RollBackException extends RuntimeException {
	private static final long serialVersionUID = -4950598834382568086L;
	
	private String code;

	public RollBackException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public RollBackException(String code, String message) {
		super(message);
		this.code = code;
	}

	public RollBackException(String code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public RollBackException(String message) {
		super(message);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
