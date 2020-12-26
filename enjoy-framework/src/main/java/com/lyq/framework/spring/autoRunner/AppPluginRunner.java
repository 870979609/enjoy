package com.lyq.framework.spring.autoRunner;

import com.lyq.framework.common.exception.AppException;
import com.lyq.framework.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Properties;

@Component
@Order(1)
public class AppPluginRunner implements ApplicationRunner{

	private static Logger logger = LoggerFactory.getLogger(AppPluginRunner.class);

	@Override
	@SuppressWarnings({ "rawtypes" })
	public void run(ApplicationArguments args) throws Exception {
		logger.info("---------------------开始加载AppPlugin.xml------------------");
		LinkedHashMap map = readAppPluginProp();
		logger.info("---------------------【AppPlugin.xml】启动类信息读取成功------------------");
		execute(map);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private LinkedHashMap<String, String> readAppPluginProp() {
		LinkedHashMap appMap = new LinkedHashMap();
		try {
			Properties prop = PropertiesUtil.getProp("AppPlugin");
			if ((prop == null) || (prop.isEmpty())) {
				/*throw new AppException("配置文件AppPlugin.properties不存在！");*/
				return appMap;
			}
			Iterator it = prop.stringPropertyNames().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				String value = prop.getProperty(key);
				appMap.put(key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void execute(LinkedHashMap appMap) {
		Iterator it = appMap.keySet().iterator();
		Class appClass = null;
		Object appObj = null;
		Method method = null;
		Class[] paraTypes = { String.class };
		while (it.hasNext()) {
			String name = (String) it.next();
			String className = (String) appMap.get(name);
			Object[] paras = { name };
			logger.info("---------------------开始加载【" + name + "】启动类【" + className + "】---------------------");
			try {
				appClass = Class.forName(className);
				appObj = appClass.newInstance();
				method = appClass.getMethod("addContent", paraTypes);
				method.invoke(appObj, paras);
				logger.info("---------------------加载【" + name + "】启动类【" + className + "】完成---------------------");
			} catch (Exception e) {
				logger.error("---------------------加载【" + name + "】启动类【" + className + "】失败---------------------", e);
			}
		}

	}
}
