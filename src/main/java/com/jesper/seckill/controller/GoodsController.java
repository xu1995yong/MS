package com.jesper.seckill.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import com.alibaba.druid.util.StringUtils;
import com.jesper.seckill.bean.User;
import com.jesper.seckill.redis.GoodsKey;
import com.jesper.seckill.redis.RedisService;
import com.jesper.seckill.result.Result;
import com.jesper.seckill.service.GoodsService;
import com.jesper.seckill.service.UserService;
import com.jesper.seckill.vo.GoodsDetailVo;
import com.jesper.seckill.vo.GoodsVo;

@Controller
@RequestMapping("/goods")
public class GoodsController {
	private static Logger log = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	UserService userService;

	@Autowired
	RedisService redisService;

	@Autowired
	GoodsService goodsService;

//	@Autowired
//	ApplicationContext applicationContext;

	@Autowired
	ThymeleafViewResolver thymeleafViewResolver;

	@RequestMapping(value = "/list")
	public String list(Model model, User user) {

		List<GoodsVo> goodsList = goodsService.listGoodsVo();
		model.addAttribute("user", user);
		model.addAttribute("goodsList", goodsList);

		return "goodsList";
	}
//	@RequestMapping(value = "/list", produces = "text/html")
//	@ResponseBody
//	public String list(HttpServletRequest request, HttpServletResponse response, Model model, User user) {
//
//		// 取缓存
////		String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
////		if (!StringUtils.isEmpty(html)) {
////			log.debug("从缓存中获取GoodsList");
////			return html;
////		}
//		List<GoodsVo> goodsList = goodsService.listGoodsVo();
//		model.addAttribute("user", user);
//		model.addAttribute("goodsList", goodsList);
//
////		// 手动渲染
////		WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(),
////				model.asMap());
////		html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
////
////		if (!StringUtils.isEmpty(html)) {
////			redisService.set(GoodsKey.getGoodsList, "", html);
////			log.debug("GoodsList已添加到缓存");
////		}
////		log.debug(user.toString());
////		return html;
//
//	}

	@RequestMapping(value = "/detail/{goodsId}")
//	@ResponseBody
	public String detail(Model model, User user, @PathVariable("goodsId") long goodsId) {

//		// 取缓存
//		String  html = redisService.get(GoodsKey.getGoodsDetail, "" + goodsId, String.class);
//		if (!StringUtils.isEmpty(html)) {
//			return html;
//		}

		model.addAttribute("user", user);
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		model.addAttribute("goods", goods);

		log.debug(goods.toString());

		// 手动渲染
//		WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(),
//				model.asMap());
//
//		html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
//		if (!StringUtils.isEmpty(html)) {
//			redisService.set(GoodsKey.getGoodsDetail, "" + goodsId, html);
//		}
//		return html;

		return "goodsDetail";
	}

	@GetMapping(value = "/detailToJson/{goodsId}")
	@ResponseBody
	public Result<GoodsDetailVo> detailToJson(HttpServletRequest request, HttpServletResponse response, Model model,
			User user, @PathVariable("goodsId") long goodsId) {

		// 根据id查询商品详情
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		model.addAttribute("goods", goods);

		long startTime = goods.getStartDate().getTime();
		long endTime = goods.getEndDate().getTime();
		long now = System.currentTimeMillis();

		int seckillStatus = 0;
		int remainSeconds = 0;

		if (now < startTime) {// 秒杀还没开始，倒计时
			seckillStatus = 0;
			remainSeconds = (int) ((startTime - now) / 1000);
		} else if (now > endTime) {// 秒杀已经结束
			seckillStatus = 2;
			remainSeconds = -1;
		} else {// 秒杀进行中
			seckillStatus = 1;
			remainSeconds = 0;
		}
		GoodsDetailVo vo = new GoodsDetailVo();
		vo.setGoods(goods);
		vo.setUser(user);
		vo.setRemainSeconds(remainSeconds);
		vo.setSeckillStatus(seckillStatus);

		return Result.success(vo);
	}

}
