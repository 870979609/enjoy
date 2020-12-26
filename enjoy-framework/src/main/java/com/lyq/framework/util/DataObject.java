package com.lyq.framework.util;

import com.lyq.framework.common.exception.Alert;
import com.lyq.framework.common.exception.AppException;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;

@SuppressWarnings("rawtypes")
@Primary
@Component
public class DataObject extends HashMap implements Serializable{

	private static final long serialVersionUID = -2709936267144551309L;

	protected DataObject() {}

	@SuppressWarnings("unchecked")
	protected DataObject(HashMap<String, Object> map) {
		super(map);
	}

	public static DataObject getInstance() {
		return new DataObject();
	}

	public static DataObject getInstance(HashMap map) {
		return new DataObject();
	}

	public Object getObject(String key) throws AppException {
		if (key == null || "".equals(key)) {
			Alert.isNull("关键字为空！");
		}

		if (!super.containsKey(key.toLowerCase())) {
			Alert.notExist("关键字列表不存在" + key);
		}

		return super.get(key.toLowerCase());
	}

	@SuppressWarnings("unchecked")
	public Object getObject(String key, Object pDefault) {
		return super.getOrDefault(key, pDefault);
	}

	public String getString(String key) throws AppException {
		Object o = getObject(key);

		if (o == null) {
			return null;
		}

		if (o instanceof String) {
			return (String) o;
		}

		Alert.formatError("关键字[" + key + "]对应的值[" + o + "]不是一个String类型的值！");

		return null;
	}

	public String getString(String key, String pDefault) throws AppException {
		Object o = getObject(key, pDefault);

		if (o == null) {
			return null;
		}

		if (o instanceof String) {
			return (String) o;
		}

		Alert.formatError("关键字[" + key + "]对应的值[" + o + "]不是一个String类型的值！");

		return null;
	}

	public double getDouble(String key) throws AppException {
		Object o = getObject(key);

		if (o == null || "".equals(o)) {
			return 0.0D;
		}

		if (o instanceof Double) {
			return ((Double) o).doubleValue();
		}

		return StringUtil.stringToDouble(o.toString());
	}

	public double getDouble(String key, double pDefault) throws AppException {
		Object o = getObject(key, pDefault);

		if (o == null || "".equals(o)) {
			return 0.0D;
		}

		if (o instanceof Double) {
			return ((Double) o).doubleValue();
		}

		return StringUtil.stringToDouble(o.toString());
	}

	public int getInt(String name) throws AppException {
		Object o = getObject(name);

		if ((o == null) || (o.equals(""))) {
			return 0;
		}
		if ((o instanceof Integer)) {
			return ((Integer) o).intValue();
		}

		return StringUtil.stringToInt(o.toString());
	}

	public int getInt(String name, int pDefault) throws AppException {
		Object o = getObject(name, pDefault);

		if ((o == null) || (o.equals(""))) {
			return 0;
		}
		if ((o instanceof Integer)) {
			return ((Integer) o).intValue();
		}

		return StringUtil.stringToInt(o.toString());
	}

	public java.util.Date getDate(String name) throws AppException {
		Object o = getObject(name);

		if ((o == null) || (o.toString().equals(""))) {
			return null;
		}

		if ((o instanceof java.util.Date)) {
			return (java.util.Date) o;
		}

		return DateUtil.stringToDate(o.toString());
	}

	public java.util.Date getDate(String name, String format) throws AppException {
		Object o = getObject(name);

		if ((o == null) || (o.toString().equals(""))) {
			return null;
		}

		if ((o instanceof java.util.Date)) {
			return (java.util.Date) o;
		}

		return DateUtil.stringToDate(o.toString(), format);
	}

	public java.util.Date getDate(String name, java.util.Date pdefault) throws AppException {
		Object o = getObject(name, pdefault);

		if ((o == null) || (o.toString().equals(""))) {
			return null;
		}

		if ((o instanceof java.util.Date)) {
			return (java.util.Date) o;
		}

		return DateUtil.stringToDate(o.toString());
	}

	public String getDateToString(String name, String tagetFormat) throws AppException {
		java.util.Date vdate = getDate(name);
		return DateUtil.dateToString(vdate, tagetFormat);
	}

	public boolean getBoolean(String name) throws AppException {
		Object o = getObject(name);

		if (o == null) {
			return false;
		}

		if ((o instanceof Boolean)) {
			return ((Boolean) o).booleanValue();
		}

		if ("true".equals(o.toString().toLowerCase())) {
			return true;
		}

		if ("false".equals(o.toString().toLowerCase())) {
			return false;
		}

		Alert.formatError("关键字['" + name + "']对应的值不是一个boolean类型的值！");

		return false;
	}

	public boolean getBoolean(String name, boolean pdefault) throws AppException {
		Object o = getObject(name, Boolean.valueOf(pdefault));

		if ((o == null) || (o.equals(""))) {
			return pdefault;
		}

		if ((o instanceof Boolean)) {
			return ((Boolean) o).booleanValue();
		}

		if ("true".equals(o.toString().toLowerCase())) {
			return true;
		}

		if ("false".equals(o.toString().toLowerCase())) {
			return false;
		}

		Alert.formatError("关键字['" + name + "']对应的值不是一个boolean类型的值！");

		return false;
	}

	public BigDecimal getBigDecimal(String name) throws AppException {
		Object o = getObject(name);

		if ((o == null) || (o.equals(""))) {
			return null;
		}

		if ((o instanceof BigDecimal)) {
			return (BigDecimal) o;
		}

		if ((o instanceof Double)) {
			return new BigDecimal(Double.toString(((Double) o).doubleValue()));
		}

		return new BigDecimal(o.toString());
	}

	public BigDecimal getBigDecimal(String name, BigDecimal pdefault) throws AppException {
		Object o = getObject(name, pdefault);

		if ((o == null) || (o.equals(""))) {
			return pdefault;
		}

		if ((o instanceof BigDecimal)) {
			return (BigDecimal) o;
		}

		if ((o instanceof Double)) {
			return new BigDecimal(Double.toString(((Double) o).doubleValue()));
		}

		return new BigDecimal(o.toString());
	}

	public boolean containsKey(String key) throws AppException {
		if ((key == null) || ("".equalsIgnoreCase(key))) {
			Alert.isNull("关键字为空");
		}

		key = key.toLowerCase();
		return super.containsKey(key);
	}

	@SuppressWarnings("unchecked")
	public Object put(String name, Object value) throws AppException {
		if ((name == null) || (name.equals(""))) {
			Alert.isNull("关键字为空");
		}

		name = name.toLowerCase();
		if ((value instanceof java.sql.Date)) {
			value = new java.util.Date(((java.sql.Date) value).getTime());
		}

		if ((value instanceof Timestamp)) {
			value = new java.util.Date(((Timestamp) value).getTime());
		}

		return super.put(name, value);
	}

	public Object put(String name, int value) throws AppException {
		return put(name, new Integer(value));
	}

	public Object put(String name, double value) throws AppException {
		return put(name, new Double(value));
	}

	public Object put(String name, boolean value) throws AppException {
		return put(name, new Boolean(value));
	}

	public DataObject clone() {
		return (DataObject) super.clone();
	}
}
