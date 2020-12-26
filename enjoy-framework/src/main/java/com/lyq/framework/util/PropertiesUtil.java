package com.lyq.framework.util;

import com.lyq.framework.common.GlobalNames;
import com.lyq.framework.common.exception.AppException;
import com.lyq.framework.spring.listener.XYStartingPlugInListener;
import com.lyq.framework.util.properties.OrderedProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Properties;

public class PropertiesUtil{

	private static LinkedHashMap<String, String> paraMap = new LinkedHashMap<String, String>();

	public static Properties getProp(String fileName) throws AppException {
		Properties prop = new OrderedProperties();

		if ((GlobalNames.USE_CONFIG_CENTER) && (!"enjoy".equals(fileName)) && (!"AppPlugin".equals(fileName))) {
		} else {
			prop = getPropFromLocal(fileName);
		}

		return prop;
	}

	private static Properties getPropFromLocal(String fileName) throws AppException {
		Properties prop = new OrderedProperties();
		try {
			InputStream in;
			if (("true".equals(GlobalNames.CONFIGINWAR)) || ("enjoy".equals(fileName))) {
				in = XYStartingPlugInListener.class.getResourceAsStream("/" + fileName + ".properties");
			} else {
				String path = "lyq" + GlobalNames.WEB_APPID + "/" + fileName + ".properties";

				File file = new File(path);
				if (!file.exists()) {
					return null;
				}

				in = new FileInputStream(file);
			}
			prop.load(in);
		} catch (Exception e) {
			throw new AppException(e);
		}
		return prop;
	}

	public static String getString(String key){
		return paraMap.get(key);
	}

	public static String getString(String key, String pDefault){
		return paraMap.getOrDefault(key, pDefault);
	}

	public static void put(String key, String value) {
		paraMap.put(key, value);
	}
}
