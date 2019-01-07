package com.xu.seckill.rabbitmq;

import com.xu.seckill.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {

    private static Logger log = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    AmqpTemplate amqpTemplate;

    public void sendMessage(SeckillMessage message) {
//        String msg = RedisService.beanToString(message);
//        amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);
    }
}
