package com.lyq.framework.util.distributedlock;

import com.lyq.framework.common.exception.AppException;
import com.lyq.framework.util.RedisUtil;
import org.springframework.util.StringUtils;


/**
 * 版本一，最简单，也最不安全
 * 1、严重！非原子性操作，多个线程同时判断exists为不存在，同时走到了if判断中进行set，错误的以为自己获取到了锁
 *
 * 2、严重！任何线程都可以解开不属于自己的锁，只要是调用了unlock()
 *
 * 3、不可重入，同线程再次调用lock()会报错
 *
 * 4、死锁，程序执行了lock，宕机没有执行unlock，进入死锁状态需要人为干预
 *
 * @author lixinyu 2021年8月12日
 */
public class RedisLock1 implements DistributedLock{
	// redis锁的前缀
	private static String redisLockPrefix = "lock_";

	// 单例的redis操作对象
	private RedisUtil redisUtil = RedisUtil.getInstance();

	// 业务号，加锁的对象
	private String busiId;

	public RedisLock1(String busiId) {
		if (StringUtils.isEmpty(busiId)) {
			throw new IllegalArgumentException("RedisLock1.new : busiId should not be empty");
		}
		this.busiId = busiId;
	}

	@Override
	public void lock() throws AppException {
		if (!tryLock()) {
			throw new AppException("获取【" + busiId + "】锁失败，业务正在执行中，请稍后再试！");
		}
	}

	@Override
	public boolean tryLock() {
		String lockKey = redisLockPrefix + busiId;// 锁key

		// 是否已经被加锁
		boolean exists = redisUtil.exists(lockKey);
		if (!exists) {
			// 不存在key表示可以加锁，value设置空
			redisUtil.set(lockKey, "");
		}
		return !exists;
	}

	@Override
	public void unlock(){
		String lockKey = redisLockPrefix + busiId;// 锁key
		// 删除锁
		redisUtil.del(lockKey);
	}
}
