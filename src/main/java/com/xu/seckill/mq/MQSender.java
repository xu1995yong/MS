package com.xu.seckill.mq;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.xu.seckill.bean.Order;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendCallback;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

import javax.annotation.Resource;

@Component
public class MQSender {

    private static Logger log = LoggerFactory.getLogger(MQSender.class);
    private String topic = "MS";


    @Autowired
    RocketMQTemplate rocketMQTemplate;

    public void asyncSendMessage(Order order) {

        rocketMQTemplate.asyncSend(topic, order, new SendCallback() {
            public void onSuccess(SendResult var1) {
                log.debug("async onSucess SendResult{}", var1);
            }

            public void onException(Throwable var1) {
                log.error("async onException Throwable={}", var1);
            }
        });
    }

    public void sendDelayMessage(Order order) {
        long timeout = 3000;

        Message message = MessageBuilder.withPayload(order).build();
        rocketMQTemplate.syncSend("OverTimeOrder", message, timeout, 6);
    }


}
