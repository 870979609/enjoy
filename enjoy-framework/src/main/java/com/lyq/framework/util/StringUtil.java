package com.lyq.framework.util;

import com.lyq.framework.common.exception.Alert;
import com.lyq.framework.common.exception.AppException;

public class StringUtil{

	public static boolean isEmpty(String obj) {
		return obj == null || "".equals(obj);
	}
	public static boolean isNull(String obj) {
		return obj == null;
	}

	public static double stringToDouble(String s) throws AppException {
		double i = 0.0D;
		if ((s == null) || (s.equalsIgnoreCase(""))) {
			Alert.isNull("传入参数为空!");
		}
		s = s.trim();
		try {
			i = Double.valueOf(s).doubleValue();
		} catch (NumberFormatException e) {
			Alert.formatError("StringUtil.stringToDouble出错，传入的字符串[" + s + "]不是一个包含数字的字符串!");
		}

		return i;
	}

	public static int stringToInt(String intString) throws AppException {
		int iRet = 0;

		if ((intString == null) || (intString.equalsIgnoreCase(""))) {
			Alert.isNull("传入参数为空!");
		}

		intString = intString.trim();
		try {
			iRet = Integer.valueOf(intString).intValue();
		} catch (NumberFormatException e) {
			try {
				iRet = (int) Double.valueOf(intString).doubleValue();
			} catch (NumberFormatException e1) {
				Alert.formatError("StringUtil.stringToDouble出错，传入的字符串[" + intString + "]不能被转换为整型!");
			}
		}

		return iRet;
	}
}
