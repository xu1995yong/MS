package com.xu.seckill.controller;

import com.xu.seckill.redis.RedisService;
import com.xu.seckill.service.GoodsService;
import com.xu.seckill.service.OrderService;
import com.xu.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;


    //TODO 根据用户和商品id获取订单

//    @RequestMapping("/detail/{orderId}")
//    @ResponseBody
//    public Result<MSOrder> detail(Model model, User user, @PathVariable("orderId") long orderId) {
//
//        MSOrder order = orderService.getOrderById(orderId);
//        if (order == null) {
//            return Result.error(CodeMsg.ORDER_NOT_EXIST);
//        }
//        long goodsId = order.getGoodsId();
//        MSGoods msGoods = goodsService.getMSGoodsById(goodsId);
//        OrderDetailVo vo = new OrderDetailVo();
//        vo.setOrder(order);
//        vo.setGoods(msGoods);
//        return Result.success(vo);
//    }

//    @RequestMapping("/payOrder/{orderId}")
//    @ResponseBody
//    public Result<Integer> payOrder(Model model, User user, @PathVariable("orderId") long orderId) {
//        if (user == null) {
//            return Result.error(CodeMsg.SESSION_ERROR);
//        }
//        OrderInfo order = orderService.getOrderById(orderId);
//        if (order == null) {
//            return Result.error(CodeMsg.ORDER_NOT_EXIST);
//        }
//        long goodsId = order.getGoodsId();
//        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
//        OrderDetailVo vo = new OrderDetailVo();
//        vo.setOrder(order);
//        vo.setGoods(goods);
    //   return Result.success(vo);
//    }


}
