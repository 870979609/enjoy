package com.lyq.framework.util.sequence;

import com.lyq.framework.common.exception.AppException;
import com.lyq.framework.util.PropertiesUtil;
import com.lyq.framework.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class KeyStrategyFactory {
    public static final String SNOWFLAKE = "snowflake";
    public static final String DBLOCK = "dblock";
    public static final String ZOOKEEPER = "zookeeper";
    public static final String REDIS = "redis";
    private static final Map<String, Class> typeClassMap = new HashMap<String, Class>();
    static {
        typeClassMap.put(SNOWFLAKE, SnowFlakeStrategy.class);
        typeClassMap.put(DBLOCK, DBLockStrategy.class);
        typeClassMap.put(ZOOKEEPER, ZooKeeperStrategy.class);
        typeClassMap.put(REDIS, RedisStrategy.class);
    }

    public static KeyStrategy getInstance() throws AppException {

        String strategy = PropertiesUtil.getString("enjoy.generateKeyStrategy", "dblock");

        return getInstance(strategy);
    }

    public static KeyStrategy getInstance(String strategy) throws AppException {
        if (StringUtil.isEmpty(strategy)) {
            throw new AppException(" Key Strategy do not allow null value!");
        }

        KeyStrategy keyStrategy = null;

        if (!typeClassMap.containsKey(strategy)) {
            throw new AppException(" Key Strategy [" + strategy + "]do not supported in enjoy!");
        }

        Class strategyClass = typeClassMap.get(strategy);

        try {
            keyStrategy = (KeyStrategy) strategyClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (keyStrategy == null) {
            throw new AppException("no available Strategy for generate Key !");
        }

        return keyStrategy;
    }
}
