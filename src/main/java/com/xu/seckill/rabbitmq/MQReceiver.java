package com.xu.seckill.rabbitmq;

import com.xu.seckill.bean.SeckillOrder;
import com.xu.seckill.bean.User;
import com.xu.seckill.exception.GlobalException;
import com.xu.seckill.redis.RedisService;
import com.xu.seckill.result.CodeMsg;
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
        User user = userService.getById(userId);

        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goodsVo.getStockCount();
        if (stock <= 0) {
            throw new GlobalException(CodeMsg.STOCK_ERROR);
        }

        // 减库存 下订单 写入秒杀订单
        seckillService.seckill(user, goodsVo);
    }

}
