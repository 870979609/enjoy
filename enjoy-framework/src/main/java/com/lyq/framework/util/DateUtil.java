package com.lyq.framework.util;

import com.lyq.framework.common.exception.Alert;
import com.lyq.framework.common.exception.AppException;
import com.lyq.framework.util.database.Sql;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil{

	public static Date stringToDate(String dateString) throws AppException {
		Date vdate = null;
		String vformat = null;
		if (dateString == null) {
			return null;
		}

		int length = dateString.length();
		
		if ((length != 4) && (length != 6) && (length != 7) && (length != 8) && (length != 10) && (length != 14)
				&& (length != 19)) {
			Alert.formatError("时间串【" + dateString + "】输入格式错误,请输入合法的日期格式!");
		}
		if (length == 4) {
			vformat = "yyyy";
		} else if (length == 6) {
			vformat = "yyyyMM";
		} else if (length == 7) {
			dateString = dateString.substring(0, 4) + dateString.substring(5, 7);

			vformat = "yyyyMM";
		} else if (length == 8) {
			vformat = "yyyyMMdd";
		} else if (length == 10) {
			dateString = dateString.substring(0, 4) + dateString.substring(5, 7) + dateString.substring(8, 10);
			vformat = "yyyyMMdd";
		} else if (length == 14) {
			vformat = "yyyyMMddHHmmss";
		} else if (length == 19) {
			vformat = "yyyy-MM-dd HH:mm:ss";
		}
		vdate = stringToDate(dateString, vformat);
		return vdate;
	}

	public static Date stringToDate(String dateString, String format) throws AppException {
		if (dateString == null) {
			return null;
		}
		if (dateString.equalsIgnoreCase("")) {
			Alert.isNull("传入参数中的[时间串]为空");
		}
		if ((format == null) || (format.equalsIgnoreCase(""))) {
			Alert.isNull("传入参数中的[时间格式]为空");
		}
		SimpleDateFormat df = new SimpleDateFormat(format);
		df.setLenient(false);// 不自动计算
		Date myDate = new Date();
		try {
			myDate = df.parse(dateString);
		} catch (ParseException e) {
			throw new AppException("日期格式转换出错!将【" + dateString + "】按照格式【" + format + "】转换成时间时出错");
		}

		return myDate;
	}

	public static String dateToString(Date date, String format) throws AppException {
		if (date == null) {
			return null;
		}
		if ((format == null) || (format.equalsIgnoreCase(""))) {
			Alert.isNull("传入参数中的[时间格式]为空");
		}

		SimpleDateFormat df = new SimpleDateFormat(format);

		return df.format(date);
	}

	public static Date getDBDate() throws AppException {

		return stringToDate(dateToString(getDBTime(), "yyyyMMdd"), "yyyyMMdd");
	}

	public static Date getDBTime() throws AppException {
		Sql sql = Sql.getInstance();

		sql.clearSql();
		sql.addSql("select now() now");
		DataStore ds = sql.executeQuery();

		if (ds.rowCount() == 0) {
			throw new AppException("未获取到数据库时间！");
		}

		return ds.getRow(0).getDate("now");
	}

	public static void main(String[] args) throws AppException {
		DateUtil.stringToDate("2020年02月02日", "yyyy年mm月dd日");
	}
}
