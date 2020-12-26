package com.lyq.framework.util;

import com.alibaba.fastjson.JSON;
import com.lyq.framework.common.exception.AppException;
import com.lyq.framework.spring.SpringBeanUtil;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;

import java.util.HashMap;

/**
 * @Description
 * @Author lixinyu
 * @Date 2020/12/5 23:15
 * @Version 1.0
 **/
public class MessageUtil {

    private static String defaultTagName = "defaultTag";

    private static String defaultCharset = "UTF-8";

    private static MQtype mQtype = MQtype.ROCKET;

    private static MQProducer getProducer(){
        return SpringBeanUtil.getBean(DefaultMQProducer.class);
    }

    public static SendResult send(String topic, HashMap para) throws Exception {
        return send(topic, defaultTagName, JSON.toJSONString(para), defaultCharset);
    }

    public static SendResult send(String topic, String massageContent) throws Exception {
        return send(topic, defaultTagName, massageContent, defaultCharset);
    }

    public static SendResult send(String topic, String tag, HashMap para) throws Exception {
        return send(topic, tag, JSON.toJSONString(para), defaultCharset);
    }

    public static SendResult send(String topic, String tag, String massageContent) throws Exception {
        return send(topic, tag, massageContent, defaultCharset);
    }

    public static SendResult send(String topic, String tag, HashMap para, String charset) throws Exception {
        return send(topic, tag, JSON.toJSONString(para), charset);
    }

    public static SendResult send(String topic, String tag,  String massageContent, String charset) throws Exception {
        Message msg = new Message(topic, tag, massageContent.getBytes(charset));

        SendResult sendResult = getProducer().send(msg);

        if (!SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
            throw new AppException("消息发送失败, " + sendResult.toString());
        }

        return sendResult;
    }

    public enum MQtype {
        ROCKET,
        RABBIT,
        KAFKA,
        ACTIVE;

        private MQtype() { }
    }
}
