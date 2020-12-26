package com.lyq.framework.util.database;

import java.util.HashMap;

import javax.sql.DataSource;

import com.lyq.framework.common.exception.Alert;
import com.lyq.framework.common.exception.AppException;
import com.lyq.framework.spring.SpringBeanUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;


public class DatabaseSessionUtil{

	private static HashMap<String, DataSource> dsMap = new HashMap<String, DataSource>();
	private static HashMap<String, DataSourceTransactionManager> tmMap = new HashMap<String, DataSourceTransactionManager>();

	public static JdbcTemplate getCurrentSession(String dataSourseName) throws AppException {
		DataSource dataSource = null;
		if (dsMap.containsKey(dataSourseName)) {
			dataSource = dsMap.get(dataSourseName);
		} else {
			dataSource = getDataSource(dataSourseName);
			dsMap.put(dataSourseName, dataSource);
		}

		return new JdbcTemplate(dataSource);
	}

	private static DataSource getDataSource(String dataSourseName) throws AppException {
		Object bean = SpringBeanUtil.getBean(dataSourseName);

		if (bean instanceof DataSource) {
			return (DataSource) bean;
		}

		Alert.runError("暂不支持此数据源获取[" + dataSourseName + "]！");
		return null;
	}

	public static DataSourceTransactionManager getCurrentTM() throws AppException {
		return getCurrentTM("dataSource");
	}

	public static DataSourceTransactionManager getCurrentTM(String dbName) throws AppException {
		DataSourceTransactionManager txManager = null;
		if (!tmMap.containsKey(dbName)) {
			DataSource dataSource = getDataSource(dbName);
			txManager = new DataSourceTransactionManager(dataSource);
			tmMap.put(dbName, txManager);
		} else {
			txManager = (DataSourceTransactionManager) tmMap.get(dbName);
		}
		return txManager;
	}
}
