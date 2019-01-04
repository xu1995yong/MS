package com.xu.seckill.controller;

import com.xu.seckill.result.Result;
import com.xu.seckill.service.UserService;
import com.xu.seckill.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class LoginController {
	private static Logger log = LoggerFactory.getLogger(LoginController.class);

	@Autowired
    UserService userService;

	@RequestMapping("/")
	public String toLogin() {
		return "login";
	}

	@PostMapping("/login/do_login")
	@ResponseBody
	public Result<String> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {// 加入JSR303参数校验
		log.debug(loginVo.toString());
		String token = userService.login(response, loginVo);
		return Result.success(token);
	}

}