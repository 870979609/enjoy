package com.lyq.framework.common.exception;

public class Alert{
	public static void isNull(String message) throws AppException {
		throw new AppException(ExceptionCode.isNull, message);
	}

	public static void notExist(String message) throws AppException {
		throw new AppException(ExceptionCode.notExist, message);
	}

	public static void formatError(String message) throws AppException {
		throw new AppException(ExceptionCode.formatError, message);
	}

	public static void sqlError(String message) throws AppException {
		throw new AppException(ExceptionCode.sqlError, message);
	}

	public static void iOError(String message) throws AppException {
		throw new AppException(ExceptionCode.iOError, message);
	}
	
	public static void runError(String message) throws AppException {
		throw new AppException(ExceptionCode.runError, message);
	}

}