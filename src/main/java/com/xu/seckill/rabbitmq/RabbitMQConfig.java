package com.xu.seckill.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE = "MS_queue";

    //Direct模式 交换机Exchange 发送者先发送到交换机上，然后交换机作为路由再将信息发到队列，
    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true);
    }


}