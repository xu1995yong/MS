package com.xu.seckill.mq;

import com.xu.seckill.bean.Order;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MQSender {

    private static Logger log = LoggerFactory.getLogger(MQSender.class);
    private String topic = "MS";


    @Resource
    RocketMQTemplate rocketMQTemplate;

    public void asyncSendMessage(Order order) {
        // Send user-defined object
        rocketMQTemplate.asyncSend(topic, order, new SendCallback() {
            public void onSuccess(SendResult var1) {
                System.out.printf("async onSucess SendResult=%s %n", var1);
            }

            public void onException(Throwable var1) {
                System.out.printf("async onException Throwable=%s %n", var1);
            }

        });
    }


}
