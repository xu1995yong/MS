package com.xu.seckill.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.xu.seckill.bean.Goods;
import com.xu.seckill.bean.User;
import com.xu.seckill.redis.RedisService;
import com.xu.seckill.result.CodeMsg;
import com.xu.seckill.result.Result;
import com.xu.seckill.service.GoodsService;
import com.xu.seckill.service.MSService;
import com.xu.seckill.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Controller
@RequestMapping("/seckill")
public class MSController {
    private static Logger log = LoggerFactory.getLogger(MSController.class);

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MSService seckillService;

    @Autowired
    RedisService redisService;


    // 基于令牌桶算法的限流实现类
    RateLimiter rateLimiter = RateLimiter.create(10);

    /***
     * 为了防止用户不断调用秒杀地址接口来，需要在秒杀开始时才创建商品秒杀的url，并保存在redis中。
     * 每次的url都不一样，只有真正点击秒杀按钮，才会根据商品生成对应的秒杀接口地址。
     */
    @RequestMapping("/getPath")
    @ResponseBody
    public Result<String> getPath(@RequestParam("goodsId") long goodsId) {

        Goods goods = goodsService.getGoodsById(goodsId);
        if (Objects.isNull(goods)) {
            return Result.error(CodeMsg.BIND_ERROR);
        }
        log.debug(goods.toString());

        long startTime = goods.getStartDate().getTime();
        long endTime = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        if (now < startTime || now > endTime) {
            return Result.error(CodeMsg.TIME_ERROR);
        } else {
            String path = seckillService.createPath(goodsId);
            log.debug("为{}号商品创建路径成功：path = {}", goodsId, path);
            return Result.success(path);
        }
    }


    @PostMapping(value = "/{uuidPath}/do_seckill")
    @ResponseBody
    public Result<String> doSeckill(User user, @RequestParam("goodsId") long goodsId, @PathVariable("uuidPath") String path) {

       
        if (!seckillService.validPath(path, goodsId)) {
            log.debug("路径验证错误");
            return Result.error(CodeMsg.PATH_ERROR);
        }

        if (!rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS)) {
            log.debug("秒杀失败，请重试");
            return Result.error(CodeMsg.ACCESS_LIMIT_REACHED);
        }

        String orderId = seckillService.seckill(user.getId(), goodsId, 1);
        if (Objects.isNull(orderId)) {
            log.debug("秒杀结束");
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        return Result.success(orderId);

    }

}
