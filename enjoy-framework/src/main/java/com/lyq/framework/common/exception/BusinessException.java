package com.lyq.framework.common.exception;

public class BusinessException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	private Integer code;

	public BusinessException(Integer code, String message) {
		super(message);
		this.code = code;
	}
	public BusinessException(String message) {
		super(message);
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "BusinessException{" + "code=" + code + ", message=" + this.getMessage() + '}';
	}
}
