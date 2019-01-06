package com.xu.seckill.rabbitmq;

import com.xu.seckill.bean.SeckillOrder;
import com.xu.seckill.bean.User;
import com.xu.seckill.redis.RedisService;
import com.xu.seckill.service.GoodsService;
import com.xu.seckill.service.OrderService;
import com.xu.seckill.service.SeckillService;
import com.xu.seckill.service.UserService;
import com.xu.seckill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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

    @RabbitListener(queues = MQConfig.QUEUE)
    public void receive(String message) {
        log.debug("接受到消息:" + message);
        SeckillMessage m = RedisService.stringToBean(message, SeckillMessage.class);
        long userId = m.getUserId();
        long goodsId = m.getGoodsId();

        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        User user = userService.getById(userId);
        int stock = goodsVo.getStockCount();
        if (stock <= 0) {
            return;
        }

        // 判断重复秒杀
        SeckillOrder order = orderService.getOrderByUserIdGoodsId(userId, goodsId);
        if (order != null) {
            return;
        }

        // 减库存 下订单 写入秒杀订单
        seckillService.seckill(user, goodsVo);
    }

//	@RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
//	public void receiveTopic1(String message) {
//		log.info(" topic  queue1 message:" + message);
//	}
//
//	@RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
//	public void receiveTopic2(String message) {
//		log.info(" topic  queue2 message:" + message);
//	}
}
