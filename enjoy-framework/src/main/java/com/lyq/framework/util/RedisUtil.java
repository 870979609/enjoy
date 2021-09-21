package com.lyq.framework.util;


import com.lyq.framework.spring.SpringBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);
    private static final Long RELEASE_LOCK_RESULT_SUCCESS = 1l;
    private UUID id;

    @Resource(name = "redisTemplate")
    RedisTemplate<String, Object> redisTemplate;

    private RedisUtil() {
        setId(UUID.randomUUID());
    }

    public static RedisUtil getInstance() {
        return SpringBeanUtil.getBean(RedisUtil.class);
    }

    public void setId(UUID uuid) {
        this.id = uuid;
    }

    public UUID getId() {
        return this.id;
    }

    public boolean exists(String key) {
        return this.redisTemplate.hasKey(key);
    }

    public void set(String key, Object value) {
        this.redisTemplate.opsForValue().set(key, value, 1L, TimeUnit.DAYS);
    }

    public void set(String key, Object value, long time) {
        if (time > 0L) {
            this.redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
        } else {
            set(key, value);
        }
    }

    public Object get(String key) {
        return this.redisTemplate.opsForValue().get(key);
    }

    public void del(String key) {
        this.redisTemplate.delete(key);
    }

    public void del(String[] key) {
        if ((key != null) && (key.length > 0)) {
            if (key.length == 1)
                this.redisTemplate.delete(key[0]);
            else
                this.redisTemplate.delete(CollectionUtils.arrayToList(key));
        }
    }

    public void del(List key) {
        this.redisTemplate.delete(key);
    }

    public Map<Object, Object> hmget(String key) {
        return this.redisTemplate.opsForHash().entries(key);
    }

    public void hmset(String key, Map<String, Object> map) {
        this.redisTemplate.opsForHash().putAll(key, map);
    }

    public void hset(String key, String item, Object value) {
        this.redisTemplate.opsForHash().put(key, item, value);
    }

    public Long remove(String key, Object[] values) {
        return this.redisTemplate.opsForSet().remove(key, values);
    }

    public boolean hasKey(String key) {
        return this.redisTemplate.hasKey(key).booleanValue();
    }

    public boolean expire(String key, long time) {
        return this.redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    public Long incr(String key, long delta) {
        return this.redisTemplate.opsForValue().increment(key, delta);
    }

    public Object hget(String key, String item) {
        return this.redisTemplate.opsForHash().get(key, item);
    }

    public void hdel(String key, Object... item) {
        this.redisTemplate.opsForHash().delete(key, item);
    }

    public boolean hHasKey(String key, String item) {
        return this.redisTemplate.opsForHash().hasKey(key, item).booleanValue();
    }

    public Long hincr(String key, String item, long by) {
        return this.redisTemplate.opsForHash().increment(key, item, by);
    }

    //============================set=============================
    public boolean sIsMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    public Long sadd(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);

    }

    public Set<Object> smembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    public Long sremove(String key, Object value) {
        return redisTemplate.opsForSet().remove(key, value);
    }

    public Object spop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    public boolean setIfAbsent(String lockKey, String lockValue, long time, TimeUnit unit) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, time, unit);
        if (result != null) {
            return result.booleanValue();
        }
        return false;
    }

    public boolean setIfAbsent(String lockKey, String lockValue) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue);
        if (result != null) {
            return result.booleanValue();
        }
        return false;
    }

    public <T> T execute(RedisScript<T> script, List<String> keys, Object args[]) {
        return this.redisTemplate.execute(script, keys, args);
    }
}