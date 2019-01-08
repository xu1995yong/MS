package com.xu.seckill.rabbitmq;

import com.xu.seckill.bean.Goods;
import com.xu.seckill.bean.User;
import com.xu.seckill.exception.GlobalException;
import com.xu.seckill.redis.RedisService;
import com.xu.seckill.result.CodeMsg;
import com.xu.seckill.service.GoodsService;
import com.xu.seckill.service.OrderService;
import com.xu.seckill.service.SeckillService;
import com.xu.seckill.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class MQReceiver {
    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void receive(SeckillMessage msg) {
        log.debug("接受到消息:" + msg);
//        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
//         SeckillMessage m = converter.fromMessage(message);
        long userId = msg.getUserId();
        long goodsId = msg.getGoodsId();
        User user = userService.getById(userId);
        Goods goods = goodsService.getGoodsById(goodsId);
        int stock = goods.getStock();
        if (stock <= 0) {
            throw new GlobalException(CodeMsg.STOCK_ERROR);
        }
        // 减库存 下订单 写入秒杀订单
        seckillService.seckill(user, goods);
    }

}
