package com.xu.seckill.controller;

import com.xu.seckill.bean.Goods;
import com.xu.seckill.bean.Order;
import com.xu.seckill.bean.User;
import com.xu.seckill.exception.GlobalException;
import com.xu.seckill.redis.RedisService;
import com.xu.seckill.result.CodeMsg;
import com.xu.seckill.result.Result;
import com.xu.seckill.service.GoodsService;
import com.xu.seckill.service.OrderService;
import com.xu.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    RedisService redisService;

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;


    @RequestMapping("/detail/{orderId}")
    public String detail(Model model, @PathVariable("orderId") String orderId) {

        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            throw new GlobalException(CodeMsg.ORDER_NOT_EXIST);
        }
        long goodsId = order.getGoodsId();
        Goods goods = goodsService.getGoodsById(goodsId);
        model.addAttribute("order", order);
        model.addAttribute("goods", goods);

        return "orderDetail";
    }

    @RequestMapping("/pay/{orderId}")
    public void payOrder(Model model, @PathVariable("orderId") String orderId) {
        Order order = orderService.getOrderById(orderId);
    }
}
