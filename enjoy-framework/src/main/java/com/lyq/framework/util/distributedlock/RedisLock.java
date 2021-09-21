package com.lyq.framework.util.distributedlock;

import com.lyq.framework.common.exception.AppException;
import com.lyq.framework.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 最终实现，解决了不可重入和死锁，锁延期
 *
 * @author lixinyu 2021年8月12日
 */
public class RedisLock implements DistributedLock{
	private static Logger logger = LoggerFactory.getLogger(RedisLock.class);
	
	// 默认30秒后自动释放锁
	private static long defaultExpireTime = 30 * 1000;// 30秒
	
	// redis锁的前缀
	private static String redisLockPrefix = "lock_";

	// 用于锁延时任务的执行
	private static ScheduledThreadPoolExecutor renewExpirationExecutor;
	
	// 加锁和解锁的lua脚本
	private static String lockScript;
	private static String unlockScript;
	// 锁延时脚本
	private static String renewScript;
	
	static {
		StringBuilder sb = new StringBuilder();
		sb.setLength(0);
		sb.append(" if (redis.call('exists', KEYS[1]) == 0) then ");// 如果不存在这个lockKey锁
		sb.append("     redis.call('hset', KEYS[1], ARGV[1], 1) ");// 设置锁 ，hash结构，hashkey为当前线程id，加锁数为1
		sb.append("     redis.call('pexpire', KEYS[1], ARGV[2]) ");// 设置锁超时时间
		sb.append("     return nil ");
		sb.append(" end ");
		sb.append(" if (redis.call('hexists', KEYS[1], ARGV[1]) == 1) then ");// 如果当前线程已经加锁
		sb.append("     redis.call('hincrby', KEYS[1], ARGV[1], 1) ");// 可重入，增加锁计数
		sb.append("     redis.call('pexpire', KEYS[1], ARGV[2]) ");// 重设置锁超时时间
		sb.append("     return nil ");
		sb.append(" end ");
		sb.append(" return redis.call('pttl', KEYS[1]) ");// 如果别的线程已经加锁，返回剩余时间
		lockScript = sb.toString();
		
		sb.setLength(0);
		sb.append(" if (redis.call('exists', KEYS[1]) == 0) then ");// 不存在锁，返回1表示解锁成功
		sb.append("     return 1 ");
		sb.append(" end ");
		sb.append(" if (redis.call('hexists', KEYS[1], ARGV[1]) == 0) then ");// 存在锁，不是本线程加的，返回0失败
		sb.append("     return 0 ");
		sb.append(" end ");
		sb.append(" local counter = redis.call('hincrby', KEYS[1], ARGV[1], -1) ");// 存在自己加的锁，锁计数减一
		sb.append(" if (counter > 0) then ");// 判断是否要删除锁，或重置超时时间
		sb.append("     redis.call('pexpire', KEYS[1], ARGV[2]) ");
		sb.append("     return 0 ");
		sb.append(" else ");
		sb.append("     redis.call('del', KEYS[1]) ");
		sb.append("     return 1 ");
		sb.append(" end ");
		sb.append(" return nil ");
		unlockScript = sb.toString();
		
		sb.setLength(0);
		sb.append(" if (redis.call('hexists', KEYS[1], ARGV[1]) == 1) then ");// 锁还存在
		sb.append("     redis.call('pexpire', KEYS[1], ARGV[2]) ");// 重置超时时间
		sb.append("     return 1 ");
		sb.append(" end ");
		sb.append(" return 0 ");
		renewScript = sb.toString();
		
		renewExpirationExecutor = new ScheduledThreadPoolExecutor(2);
	}
	
	
	// 单例的redis操作对象
	private RedisUtil redisUtil = RedisUtil.getInstance();
	
	// 事务ID，加锁的对象
	private String busiId;
	
	public RedisLock(String busiId) {
		if (StringUtils.isEmpty(busiId)) {
			throw new IllegalArgumentException("RedisLock实例化出错，业务编号为空");
		}
		this.busiId = busiId;
	}

	@Override
	public boolean tryLock() {
		String lockKey = redisLockPrefix + busiId;// 锁key
		long threadId = Thread.currentThread().getId();// 当前线程id
		String lockValue = redisUtil.getId().toString() + threadId;// 相当于jvmID+线程id
		
		DefaultRedisScript<Long> redisScript = new DefaultRedisScript<Long>();
		redisScript.setScriptText(lockScript);
		redisScript.setResultType(Long.class);
		Long result = redisUtil.execute(redisScript, Collections.singletonList(lockKey), new Object[]{lockValue, defaultExpireTime});
		
		boolean isSuccess = result == null;
		if (isSuccess) {
			// 若成功，增加延时任务
			scheduleExpirationRenew(threadId);
		}
		
		return isSuccess;
	}

	@Override
	public void unlock(){
		String lockKey = redisLockPrefix + busiId;// 锁key
		long threadId = Thread.currentThread().getId();// 当前线程id
		String lockValue = redisUtil.getId().toString() + threadId;// 相当于jvmID+线程id
		
		DefaultRedisScript<Long> redisScript = new DefaultRedisScript<Long>();
		redisScript.setScriptText(unlockScript);
		redisScript.setResultType(Long.class);
		redisUtil.execute(redisScript, Collections.singletonList(lockKey), new Object[]{lockValue, defaultExpireTime});
	}

	@Override
	public void lock() throws AppException {
		if (!tryLock()) {
			throw new AppException("获取【" + busiId + "】锁失败，正在执行中，请稍后再试！");
		}
	}

	/**
	 * 锁延时，定时任务队列
	 */
	private void scheduleExpirationRenew(final long threadId) {
		Runnable renewTask = new Runnable(){
			
			@Override
			public void run() {
				String lockKey = redisLockPrefix + busiId;// 锁key
				String lockValue = redisUtil.getId().toString() + threadId;// 相当于jvmID+线程id
				
				DefaultRedisScript<Long> redisScript = new DefaultRedisScript<Long>();
				redisScript.setScriptText(renewScript);
				redisScript.setResultType(Long.class);
				Long result = redisUtil.execute(redisScript, Collections.singletonList(lockKey), new Object[]{lockValue, defaultExpireTime});
			
				if (result == 1) {
					// 延时成功，再定时执行
					scheduleExpirationRenew(threadId);

					logger.info("redis锁【" + lockKey + "】延时成功！");
				}
			}
		};
		
		renewExpirationExecutor.schedule(renewTask, defaultExpireTime / 3, TimeUnit.MILLISECONDS);
	}
}
