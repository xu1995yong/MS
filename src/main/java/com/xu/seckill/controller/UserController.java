package com.xu.seckill.controller;

import com.xu.seckill.bean.User;
import com.xu.seckill.redis.RedisService;
import com.xu.seckill.result.Result;
import com.xu.seckill.service.UserService;
import com.xu.seckill.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class UserController {
    private static Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/login/login")
    public String toLogin() {
        return "login";
    }

    @PostMapping("/login/do_login")
    @ResponseBody
    public Result<String> doLogin(HttpServletRequest request, HttpServletResponse response, LoginVo loginVo) {// 加入JSR303参数校验
        log.debug(loginVo.toString());
        String token = userService.login(request, response, loginVo);
        return Result.success(token);
    }

    @GetMapping("/login/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        boolean success = userService.logout(request, response);

        if (success) {
            return "redirect:/goods/list";
        }
        return "error";
    }

    @RequestMapping("/user/info")
    @ResponseBody
    public Result<User> info(Model model, User user) {
        return Result.success(user);
    }
}