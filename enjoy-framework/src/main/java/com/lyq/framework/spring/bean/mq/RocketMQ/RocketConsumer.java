package com.lyq.framework.spring.bean.mq.RocketMQ;

import org.apache.rocketmq.client.consumer.listener.MessageListener;

public interface RocketConsumer {

    /**
     * 初始化消费者
     */
    public abstract void init();

    /**
     * 注册监听
     *
     * @param messageListener
     */
    public void registerMessageListener(MessageListener messageListener);

}