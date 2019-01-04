package com.xu.seckill.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xu.seckill.bean.User;
import com.xu.seckill.redis.RedisService;
import com.xu.seckill.result.Result;
import com.xu.seckill.service.GoodsService;
import com.xu.seckill.service.UserService;
import com.xu.seckill.vo.GoodsDetailVo;
import com.xu.seckill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    private static Logger log = LoggerFactory.getLogger(GoodsController.class);
    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;


    @RequestMapping(value = "/list")
    public String list(Model model, User user) {

        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        log.debug(user.toString());
        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsList);

        return "goodsList";
    }


    @RequestMapping(value = "/detail/{goodsId}")
    public String detail(Model model, User user, @PathVariable("goodsId") long goodsId) {

        model.addAttribute("user", user);
        log.debug(user.toString());
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        log.debug(goods.toString());


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
