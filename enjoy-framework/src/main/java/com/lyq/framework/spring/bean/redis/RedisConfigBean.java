package com.lyq.framework.spring.bean.redis;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import com.lyq.framework.common.GlobalNames;
import com.lyq.framework.util.DataObject;


@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfigBean{
	private static Logger logger = LoggerFactory.getLogger(RedisConfigBean.class);


	/*public RedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();

		boolean flag = false;

		if (GlobalNames.USE_CONFIG_CENTER)
			flag = getConfigCenterRedis();
		else {
			flag = getLocalResourceRedis();
		}

		if (!flag) {
			return jedisConnectionFactory;
		}

		JedisPoolConfig poolConfig = new JedisPoolConfig();

		poolConfig.setMaxIdle(RedisProperties.REDIS_POOL_MAXIDLE);
		poolConfig.setMinIdle(RedisProperties.REDIS_POOL_MINIDLE);
		poolConfig.setMaxWaitMillis(RedisProperties.REDIS_POOL_MAXWAIT);
		poolConfig.setMaxTotal(RedisProperties.REDIS_POOL_MAXACTIVE);
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnCreate(true);
		poolConfig.setTestWhileIdle(true);

		if (!StringUtils.isEmpty(RedisProperties.REDIS_CLUSTER_NODES)) {
			String nodes = RedisProperties.REDIS_CLUSTER_NODES;
			Map source = new HashMap();
			source.put("spring.redis.cluster.nodes", nodes);
			RedisClusterConfiguration redisClusterConfig = new RedisClusterConfiguration(new MapPropertySource("RedisClusterConfig", source));
			jedisConnectionFactory = new JedisConnectionFactory(redisClusterConfig, poolConfig);
			logger.info("##redis集群连接：" + nodes);
		} else {
			jedisConnectionFactory = new JedisConnectionFactory(poolConfig);
			jedisConnectionFactory.setHostName(RedisProperties.REDIS_HOST);
			jedisConnectionFactory.setPort(RedisProperties.REDIS_PORT);
			logger.info("##redis单机连接：" + RedisProperties.REDIS_HOST + ":" + RedisProperties.REDIS_PORT);
		}

		jedisConnectionFactory.setPassword(RedisProperties.REDIS_PASSWORD);
		logger.info("##redis连接密码：" + RedisProperties.REDIS_PASSWORD);
		jedisConnectionFactory.setTimeout(RedisProperties.REDIS_TIMEOUT);

		return jedisConnectionFactory;
	}


	public RedisTemplate<String, ?> redisTemplate() {
		RedisTemplate redisTemplate = new RedisTemplate();

		redisTemplate.setConnectionFactory(jedisConnectionFactory());

		redisTemplate.setKeySerializer(new StringRedisSerializer());

		redisTemplate.afterPropertiesSet();
		redisTemplate.setEnableTransactionSupport(false);

		return redisTemplate;
	}

	private boolean getConfigCenterRedis() {
		try {
			DataObject redisconfig = DataObject.getInstance();

			String host_ip = redisconfig.getString("host_ip", "");
			if (host_ip == null) {
				host_ip = "";
			}
			String host_port = redisconfig.getString("host_port", "6379");
			if ((host_port == null) || (host_port.equals(""))) {
				host_port = "6379";
			}
			String cluster_addr = redisconfig.getString("cluster_addr", "");
			if (cluster_addr == null) {
				cluster_addr = "";
			}
			String redis_password = redisconfig.getString("redis_password", "");
			String password_encrypt = redisconfig.getString("password_encrypt", "false");
			String max_active = redisconfig.getString("max_active", "20");
			String max_wait = redisconfig.getString("max_wait", "1000");
			String max_idle = redisconfig.getString("max_idle", "10");
			String min_idle = redisconfig.getString("min_idle", "5");
			String timeout = redisconfig.getString("timeout", "0");

			RedisProperties.REDIS_POOL_MAXACTIVE = Integer.parseInt(max_active);
			RedisProperties.REDIS_POOL_MAXIDLE = Integer.parseInt(max_idle);
			RedisProperties.REDIS_POOL_MINIDLE = Integer.parseInt(min_idle);
			RedisProperties.REDIS_POOL_MAXWAIT = Integer.parseInt(max_wait);

			RedisProperties.REDIS_CLUSTER_NODES = cluster_addr;

			RedisProperties.REDIS_HOST = host_ip;
			RedisProperties.REDIS_PORT = Integer.parseInt(host_port);

			RedisProperties.REDIS_TIMEOUT = Integer.parseInt(timeout);

			RedisProperties.REDIS_PASSWORD_ENCRYPT = password_encrypt;

			// 加密密码不支持，待研究
			if ((RedisProperties.REDIS_PASSWORD_ENCRYPT != null)
					&& ("true".equals(RedisProperties.REDIS_PASSWORD_ENCRYPT))) {
				
				 * String redis_pw_encrypt = redis_password; String
				 * redis_pw_decrypt = SecUtil.decryptMode(redis_pw_encrypt);
				 * RedisProperties.REDIS_PASSWORD = redis_pw_decrypt;
				 
			} else {
				RedisProperties.REDIS_PASSWORD = redis_password;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private boolean getLocalResourceRedis() {
		try {
			InputStream in = null;
			if ("true".equals(GlobalNames.CONFIGINWAR)) {
				in = RedisConfigBean.class.getResourceAsStream("/redisConfig.properties");
			} else {
				String path = "xyconf/" + GlobalNames.WEB_APP + "/redisConfig.properties";

				File file = new File(path);
				if (!file.exists()) {
					logger.warn("【redisConfig.properties】包外配置文件不存在");
					return false;
				}
				in = new FileInputStream(file);
			}

			if (in == null) {
				logger.warn("【redisConfig.properties】配置文件读取失败");
				return false;
			}

			Properties prop = new Properties();
			prop.load(in);

			RedisProperties.REDIS_POOL_MAXACTIVE = Integer.valueOf(prop.getProperty("redis.pool.max-active", "20"))
				.intValue();
			RedisProperties.REDIS_POOL_MAXIDLE = Integer.valueOf(prop.getProperty("redis.pool.max-idle", "10"))
				.intValue();
			RedisProperties.REDIS_POOL_MINIDLE = Integer.valueOf(prop.getProperty("redis.pool.min-idle", "5"))
				.intValue();
			RedisProperties.REDIS_POOL_MAXWAIT = Integer.valueOf(prop.getProperty("redis.pool.max-wait", "1000"))
				.intValue();

			RedisProperties.REDIS_CLUSTER_NODES = prop.getProperty("redis.cluster.nodes", "");

			RedisProperties.REDIS_HOST = prop.getProperty("redis.host", "");
			RedisProperties.REDIS_PORT = Integer.valueOf(prop.getProperty("redis.port", "6379")).intValue();
			RedisProperties.REDIS_TIMEOUT = Integer.valueOf(prop.getProperty("redis.timeout", "86400")).intValue();

			RedisProperties.REDIS_PASSWORD_ENCRYPT = prop.getProperty("redis.password.encrypt", "false");
			if ((RedisProperties.REDIS_PASSWORD_ENCRYPT != null)
					&& ("true".equals(RedisProperties.REDIS_PASSWORD_ENCRYPT))) {
				String redis_pw_encrypt = prop.getProperty("redis.password", "");
				String redis_pw_decrypt = SecUtil.decryptMode(redis_pw_encrypt);
				RedisProperties.REDIS_PASSWORD = redis_pw_decrypt;
			} else {
				RedisProperties.REDIS_PASSWORD = prop.getProperty("redis.password", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}*/
	
    @Bean(name = "redisTemplate")
    @Primary
    public RedisTemplate<String, Serializable> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        // 设置键（key）的序列化采用StringRedisSerializer。
        template.setKeySerializer(new StringRedisSerializer());
        // 设置值（value）的序列化采用jackson的序列化。
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
