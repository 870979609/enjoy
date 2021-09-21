package com.lyq.framework.util.distributedlock;


import com.lyq.framework.common.exception.AppException;

public interface DistributedLock {
	boolean tryLock();
	
	void unlock();
	
	void lock() throws AppException;
}
