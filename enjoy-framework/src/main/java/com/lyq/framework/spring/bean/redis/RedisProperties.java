package com.lyq.framework.spring.bean.redis;

public abstract class RedisProperties{
	public static String REDIS_HOST;
	public static String REDIS_PASSWORD = "";
	public static int REDIS_PORT;
	public static String REDIS_CLUSTER_NODES;
	
	// 集群模式下，集群最大转发的数量
	public static int REDIS_CLUSTER_MAXREDIRECTS;
	
	// 连接池最大阻塞等待时间，使用负值表示没有限制
	public static int REDIS_POOL_MAXWAIT = 1000;
	
	// 最小空闲连接数量，使用正值才有效果
	public static int REDIS_POOL_MINIDLE = 5;
	
	// 连接池的最大空闲连接数量，使用负值表示无限数量的空闲连接
	public static int REDIS_POOL_MAXIDLE = 10;
	
	// 连接池的最大活动连接数量，使用负值无限制
	public static int REDIS_POOL_MAXACTIVE = 20;

	public static int REDIS_TIMEOUT = 0;

	public static String REDIS_PASSWORD_ENCRYPT = "false";
	public static final String DEFAULT_CACHE_TYPE = "cache";
}
