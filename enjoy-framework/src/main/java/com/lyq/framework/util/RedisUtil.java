package com.lyq.framework.util;


import com.lyq.framework.spring.SpringBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    @Resource(name = "redisTemplate")
    RedisTemplate<String, Object> redisTemplate;

    private RedisUtil() {
    }

    public static RedisUtil getInstance() {
        return (RedisUtil) SpringBeanUtil.getBean(RedisUtil.class);
    }

    public boolean set(String key, Object value) {
        try {
            this.redisTemplate.opsForValue().set(key, value, 1L, TimeUnit.DAYS);
        } catch (Exception e) {
            logger.error("向Redis中存放数据出错：" + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0L) {
                this.redisTemplate.opsForValue()
                        .set(key, value, time, TimeUnit.SECONDS);
            } else
                set(key, value);
        } catch (Exception e) {
            logger.error("向Redis中存放数据出错：" + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return true;
    }

    public Object get(String key) {
        Object value = null;
        try {
            value = this.redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            logger.error("从Redis中获取key【" + key + "】出错：" + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return value;
    }

    public void del(String[] key) {
        try {
            if ((key != null) && (key.length > 0))
                if (key.length == 1)
                    this.redisTemplate.delete(key[0]);
                else
                    this.redisTemplate.delete(CollectionUtils.arrayToList(key));
        } catch (Exception e) {
            logger.error("从Redis中删除数据出错：" + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Map<Object, Object> hmget(String key) {
        Map hmap = new HashMap();
        try {
            hmap = this.redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            logger.error("从Redis中获取HashMap数据出错：" + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return hmap;
    }

    public boolean hmset(String key, Map<String, Object> map) {
        try {
            this.redisTemplate.opsForHash().putAll(key, map);
        } catch (Exception e) {
            logger.error("从Redis中放置HashMap数据出错：" + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return true;
    }

    public boolean hset(String key, String item, Object value) {
        try {
            this.redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean remove(String key, Object[] values) {
        try {
            this.redisTemplate.opsForSet().remove(key, values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean hasKey(String key) {
        try {
            return this.redisTemplate.hasKey(key).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void delete(String key) {
        this.redisTemplate.delete(key);
    }

    public boolean expire(String key, long time) {
        try {
            if (time > 0L) {
                this.redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public long incr(String key, long delta) {
        if (delta < 0L) {
            throw new RuntimeException("递增因子必须大于0");
        }

        long ddd = this.redisTemplate.opsForValue().increment(key, delta).longValue();

        return ddd;
    }

    public long incr(String key, long delta, long timeout) {
        if (delta < 0L) {
            throw new RuntimeException("递增因子必须大于0");
        }

        long ddd = this.redisTemplate.opsForValue().increment(key, delta).longValue();

        if (timeout > 0L) {
            this.redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        }

        return ddd;
    }

    public long decr(String key, long delta, long timeout) {
        if (delta < 0L) {
            throw new RuntimeException("递减因子必须大于0");
        }
        long ddd = this.redisTemplate.opsForValue().increment(key, -delta).longValue();

        if (timeout > 0L) {
            this.redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        }
        return ddd;
    }

    public long decr(String key, long delta) {
        if (delta < 0L) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return this.redisTemplate.opsForValue().increment(key, -delta).longValue();
    }

    public Object hget(String key, String item) {
        return this.redisTemplate.opsForHash().get(key, item);
    }

    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            this.redisTemplate.opsForHash().putAll(key, map);
            if (time > 0L) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean hset(String key, String item, Object value, long time) {
        try {
            this.redisTemplate.opsForHash().put(key, item, value);
            if (time > 0L) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void hdel(String key, Object... item) {
        this.redisTemplate.opsForHash().delete(key, item);
    }

    public boolean hHasKey(String key, String item) {
        return this.redisTemplate.opsForHash().hasKey(key, item).booleanValue();
    }

    public double hincr(String key, String item, double by) {
        return this.redisTemplate.opsForHash().increment(key, item, by).doubleValue();
    }

    public double hdecr(String key, String item, double by) {
        return this.redisTemplate.opsForHash().increment(key, item, -by).doubleValue();
    }

    //============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<Object> sget(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sset(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sset(String key, Object[] values, long time) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) expire(key, time);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @Description 
     * 随机返回并删除名称为key的set中一个元素
     * @Author lixinyu
     * @Date 2020/11/15 16:36
     **/
    public Object spop(String key) {
        try {
            Object value = redisTemplate.opsForSet().pop(key);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * @Description 
     * 返回名称为key的hash中所有键
     * @Author lixinyu
     * @Date 2020/11/15 16:42
     **/
    public Map hEntrys(String key) {
        try {
            Map entries = redisTemplate.opsForHash().entries(key);
            return entries;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * @Description
     * 返回名称为key的hash中所有键
     * @Author lixinyu
     * @Date 2020/11/15 16:42
     **/
    public Set<Object> hKeys(String key) {
        try {
            Set<Object> keys = redisTemplate.opsForHash().keys(key);
            return keys;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}