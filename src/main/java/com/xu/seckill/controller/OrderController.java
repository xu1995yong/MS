package com.xu.seckill.controller;

import com.xu.seckill.bean.OrderInfo;
import com.xu.seckill.bean.User;
import com.xu.seckill.redis.RedisService;
import com.xu.seckill.result.CodeMsg;
import com.xu.seckill.result.Result;
import com.xu.seckill.service.GoodsService;
import com.xu.seckill.service.OrderService;
import com.xu.seckill.service.UserService;
import com.xu.seckill.vo.GoodsVo;
import com.xu.seckill.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

	@RequestMapping("/detail")
	@ResponseBody
	public Result<OrderDetailVo> info(Model model, User user, @RequestParam("orderId") long orderId) {
		if (user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		OrderInfo order = orderService.getOrderById(orderId);
		if (order == null) {
			return Result.error(CodeMsg.ORDER_NOT_EXIST);
		}
		long goodsId = order.getGoodsId();
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		OrderDetailVo vo = new OrderDetailVo();
		vo.setOrder(order);
		vo.setGoods(goods);
		return Result.success(vo);
	}

}
