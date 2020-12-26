package com.lyq.framework.common.exception;

public class AppException extends Exception{
	private static final long serialVersionUID = 1L;
	private Integer errcode;
	private String errtext;

	public AppException() {
		this(ExceptionCode.defaultCode, "程序出现异常！");
	}

	public Integer getErrcode() {
		return errcode;
	}

	public String getErrtext() {
		return errtext;
	}

	public AppException(String errtext) {
		this(ExceptionCode.defaultCode, errtext);
	}

	public AppException(int errcode, String errtext) {
		super(errcode + errtext);
		this.errcode = errcode;
		this.errtext = errtext;
	}

	public AppException(Throwable cause) {
		this(ExceptionCode.defaultCode, cause == null ? "程序出现系统异常!" : cause.toString());
	}
}
