package com.xu.seckill.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.xu.seckill.bean.Goods;
import com.xu.seckill.bean.User;
import com.xu.seckill.rabbitmq.MQSender;
import com.xu.seckill.rabbitmq.SeckillMessage;
import com.xu.seckill.redis.RedisService;
import com.xu.seckill.redis.keysPrefix.GoodsKey;
import com.xu.seckill.result.CodeMsg;
import com.xu.seckill.result.Result;
import com.xu.seckill.service.GoodsService;
import com.xu.seckill.service.OrderService;
import com.xu.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Controller
@RequestMapping("/seckill")
public class SeckillController {
    private static Logger log = LoggerFactory.getLogger(SeckillController.class);
    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    @Autowired
    RedisService redisService;

    @Autowired
    MQSender sender;

    // 基于令牌桶算法的限流实现类
    RateLimiter rateLimiter = RateLimiter.create(10);


    /***
     * 为了防止用户不断调用秒杀地址接口来机器刷单，需要在秒杀开始时才创建商品秒杀的url，并保存在redis中。
     * 每次的url都不一样，只有真正点击秒杀按钮，才会根据商品和用户id生成对应的秒杀接口地址。
     *
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping("/getPath")
    @ResponseBody
    public Result<String> getPath(User user, @RequestParam("goodsId") long goodsId) {
        Goods mSGoods = goodsService.getMSGoodsById(goodsId);
        if (Objects.isNull(mSGoods)) {
            return Result.error(CodeMsg.BIND_ERROR);
        }
        log.debug(mSGoods.toString());

        long startTime = mSGoods.getStartDate().getTime();
        long endTime = mSGoods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        if (now < startTime || now > endTime) {
            return Result.error(CodeMsg.TIME_ERROR);
        } else {
            String path = seckillService.createPath(user, goodsId);
            return Result.success(path);
        }
    }

    @PostMapping(value = "/{uuidPath}/do_seckill")
    @ResponseBody
    public Result<Integer> doSeckill(Model model, User user, @RequestParam("goodsId") long goodsId,
                                     @PathVariable("uuidPath") String path) {

        if (!seckillService.validPath(path, user, goodsId)) {
            return Result.error(CodeMsg.PATH_ERROR);
        }

        if (!rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS)) {
            return Result.error(CodeMsg.ACCESS_LIMIT_REACHED);
        }

        model.addAttribute("user", user);

        boolean exist = redisService.exists(GoodsKey.GOODS_STOCK, "" + goodsId);
        if (!exist) {
            Goods mSGoods = goodsService.getMSGoodsById(goodsId);
            redisService.set(GoodsKey.GOODS_STOCK, "" + mSGoods.getId(), "" + mSGoods.getStock());
        }

        boolean success = redisService.decr(GoodsKey.GOODS_STOCK, "" + goodsId);
        if (!success) {
            return Result.error(CodeMsg.SECKILL_OVER);
        }
//         判断重复秒杀
//        SeckillOrder order = orderService.getOrderByUserIdGoodsId(user.getId(), goodsId);
//        if (order != null) {
//            return Result.error(CodeMsg.REPEATE_SECKILL);
//        }
        SeckillMessage message = new SeckillMessage(user.getId(), goodsId);
        log.debug(message.toString());

        sender.sendMessage(message);
        return Result.success(0);
    }

//    @GetMapping("/getResult")
//    public Result getResult(Model model, User user, @RequestParam("goodsId") long goodsId) {
//        SeckillOrder order = orderService.getOrderByUserIdGoodsId(user.getId(), goodsId);
//        if (order == null) {
//            return Result.error(CodeMsg.ORDER_NOT_EXIST);
//        }
//        return Resu
//
//    }

}
