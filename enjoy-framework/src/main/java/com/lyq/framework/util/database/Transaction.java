package com.lyq.framework.util.database;

import java.util.HashMap;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.lyq.framework.common.exception.AppException;

public class Transaction{
	static ThreadLocal<HashMap<String, Transaction>> transLocalMap = new ThreadLocal<HashMap<String, Transaction>>();
	public static final int PROPAGATION_MANDATORY = 2;
	public static final int PROPAGATION_REQUIRED = 0;
	public static final int PROPAGATION_REQUIRES_NEW = 3;
	private PlatformTransactionManager ptm;
	private TransactionStatus status = null;

	private boolean transactionStarted = false;

	public boolean isUnderTransaction() {
		return (this.status != null) && (!this.status.isCompleted());
	}

	public boolean isRollbackOnly() {
		if (this.status == null) {
			return false;
		}
		return this.status.isRollbackOnly();
	}

	protected Transaction() {}

	protected Transaction(PlatformTransactionManager pPtm) {
		this.ptm = pPtm;
	}

	@SuppressWarnings("all")
	public static Transaction getTransaction(PlatformTransactionManager pPtm, String dbName) throws AppException {
		HashMap transMap = (HashMap) transLocalMap.get();
		if (null == transMap) {
			transMap = new HashMap();
		}
		Transaction transaction = (Transaction) transMap.get(dbName);
		if (transaction == null) {
			transaction = new Transaction(pPtm);
			transMap.put(dbName, transaction);
		} else if (transaction.isRollbackOnly() == true) {
			transaction.rollback();
			transaction = new Transaction(pPtm);
			transMap.put(dbName, transaction);
		}
		transLocalMap.set(transMap);
		return transaction;
	}

	public PlatformTransactionManager getPtm() {
		return this.ptm;
	}

	public int begin() {
		return begin(0);
	}

	public int begin(int propagation) {
		if (this.transactionStarted) {
			return 0;
		}
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(2);
		def.setPropagationBehavior(propagation);
		this.status = this.ptm.getTransaction(def);
		this.transactionStarted = true;
		return 1;
	}

	public void commit() throws AppException {
		commitWithoutStart();
		begin();
	}

	public void commitWithoutStart() throws AppException {
		if (null == this.status)
			throw new AppException("当前commit操作，未正常开启事务，请联系开发人员处理。");
		if (this.status.isCompleted()) {
			return;
		}
		if (this.status.isRollbackOnly()) {
			this.ptm.rollback(this.status);
			this.transactionStarted = false;
			throw new AppException("当前事务已经被锁定为rollback，请联系开发人员处理。");
		}
		this.ptm.commit(this.status);
		this.transactionStarted = false;
	}

	public void rollback() throws AppException {
		rollbackWithoutStart();
		begin();
	}

	public void rollbackWithoutStart() throws AppException {
		if (null == this.status)
			throw new AppException("当前rollback操作，未正常开启事务，请联系开发人员处理。");
		if (this.status.isCompleted()) {
			return;
		}
		this.ptm.rollback(this.status);
		this.transactionStarted = false;
	}
}
