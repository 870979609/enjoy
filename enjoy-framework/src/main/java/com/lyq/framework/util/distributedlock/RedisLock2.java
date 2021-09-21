package com.lyq.framework.util.distributedlock;

import com.lyq.framework.common.exception.AppException;
import com.lyq.framework.util.RedisUtil;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.UUID;

/**
 * 是实现二，对实现一的缺陷一二进行改进
 *
 * @author lixinyu 2021年8月12日
 */
public class RedisLock2 implements DistributedLock{
	// redis锁的前缀
	private static String redisLockPrefix = "lock_";

	// 单例的redis操作对象
	private RedisUtil redisUtil = RedisUtil.getInstance();

	// 产生个uuid作为本应用jvm的标识
	private static String jvmID = UUID.randomUUID().toString();

	// 业务号，加锁的对象
	private String busiId;

	public RedisLock2(String busiId) {
		if (StringUtils.isEmpty(busiId)) {
			throw new IllegalArgumentException("RedisLock2.new : busiId should not be empty");
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
		String lockValue = jvmID + Thread.currentThread().getId();// 锁value = static uuid + 线程id

		// redisTemplete.setIfAbsent 就是  setNX
		return redisUtil.setIfAbsent(lockKey, lockValue);
	}

	@Override
	public void unlock(){
		String lockKey = redisLockPrefix + busiId;// 锁key
		String lockValue = jvmID + Thread.currentThread().getId();// 锁value = static uuid + 线程id

		// 删除锁, 脚本中先get判断是否为当前线程加的锁，如果是del，否则返回
		String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
		DefaultRedisScript<Long> redisScript = new DefaultRedisScript<Long>();
		redisScript.setScriptText(script);
		redisScript.setResultType(Long.class);
		Long execute = redisUtil.execute(redisScript, Collections.singletonList(lockKey), new String[]{lockValue});
	}
}
