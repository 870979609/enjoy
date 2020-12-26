package com.lyq.framework.util.database;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.lyq.framework.common.exception.AppException;

public class TransactionManager{
	public static Transaction getTransaction() throws AppException {
		return getTransaction("dataSource");
	}

	public static Transaction getTransaction(String dbName) throws AppException {
		if ((dbName == null) || (dbName.equals(""))) {
			dbName = "dataSource";
		}

		DataSourceTransactionManager ptm = DatabaseSessionUtil.getCurrentTM(dbName);

		return Transaction.getTransaction(ptm, dbName);
	}
}
