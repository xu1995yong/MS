package com.xu.seckill.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.xu.seckill.bean.User;
import com.xu.seckill.redis.RedisService;
import com.xu.seckill.result.CodeMsg;
import com.xu.seckill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.util.concurrent.RateLimiter;
import com.xu.seckill.rabbitmq.MQSender;
import com.xu.seckill.rabbitmq.SeckillMessage;
import com.xu.seckill.redis.GoodsKey;
import com.xu.seckill.result.Result;
import com.xu.seckill.service.GoodsService;
import com.xu.seckill.service.OrderService;
import com.xu.seckill.service.SeckillService;

@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {
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

    // 做标记，判断该商品是否被处理过了
    private Map<Long, Boolean> localOverMap = new HashMap<>();

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
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
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
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        if (!rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS)) {
            return Result.error(CodeMsg.ACCESS_LIMIT_REACHED);
        }
//        log.debug("path = " + path);

        model.addAttribute("user", user);
        // 内存标记，减少redis访问
        boolean over = localOverMap.get(goodsId);
        if (over) {
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        // 预减库存
        long stock = redisService.decr(GoodsKey.getGoodsStock, "" + goodsId);// 10
        if (stock < 0) {
//			afterPropertiesSet();
            long stock2 = redisService.decr(GoodsKey.getGoodsStock, "" + goodsId);// 10
            if (stock2 < 0) {
                localOverMap.put(goodsId, true);
                return Result.error(CodeMsg.SECKILL_OVER);
            }
        }
        // 判断重复秒杀
//		SeckillOrder order = orderService.getOrderByUserIdGoodsId(user.getId(), goodsId);
//		if (order != null) {
//			return Result.error(CodeMsg.REPEATE_SECKILL);
//		}
        // 入队
        SeckillMessage message = new SeckillMessage();
        message.setUser(user);
        message.setGoodsId(goodsId);

        log.debug(message.toString());

        sender.sendSeckillMessage(message);
        return Result.success(0);// 排队中
    }

    @Override
    public void afterPropertiesSet() {
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        if (goodsVoList == null) {
            return;
        }
        for (GoodsVo goods : goodsVoList) {
            redisService.set(GoodsKey.getGoodsStock, "" + goods.getId(), goods.getStockCount());
            // 初始化商品都是没有处理过的
            localOverMap.put(goods.getId(), false);
        }
    }
}
