package com.xu.seckill.controller;

import com.xu.seckill.bean.User;
import com.xu.seckill.redis.RedisService;
import com.xu.seckill.result.Result;
import com.xu.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
    UserService userService;

	@Autowired
    RedisService redisService;

	@RequestMapping("/info")
	@ResponseBody
	public Result<User> info(Model model, User user) {
		return Result.success(user);
	}
}