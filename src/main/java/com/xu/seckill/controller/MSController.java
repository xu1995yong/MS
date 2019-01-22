package com.xu.seckill.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.xu.seckill.service.MSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;


@Controller
public class MSController {
    private static Logger log = LoggerFactory.getLogger(MSController.class);

    @Autowired
    MSService seckillService;

    RateLimiter rateLimiter = RateLimiter.create(10);


    @PostMapping(value = "/seckill")
    @ResponseBody
    public String doSeckill(@RequestParam("userId") long userId, @RequestParam("goodsId") long goodsId) {

        String retVal;
//        if (!rateLimiter.tryAcquire(1, TimeUnit.MILLISECONDS)) {
//            retVal = "LIMITED";
//        }

        String orderId = seckillService.seckill(userId, goodsId, 1);
        if (Objects.isNull(orderId)) {
            retVal = "FAILED";
        } else {
            retVal = "SUCCESS";
        }
        return retVal;
    }

}
