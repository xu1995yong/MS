package com.xu.seckill.config;

import com.xu.seckill.service.UserService;
import com.xu.seckill.util.CookieUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    private static Logger log = LoggerFactory.getLogger(LoginInterceptor.class);

    /**
     * Handler执行之前调用这个方法
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求的URL
        String url = request.getRequestURI();
        log.debug("拦截到url:" + url);

        Cookie[] cookies = request.getCookies();

        Cookie cookie = CookieUtils.getCookieByName(cookies, UserService.COOKIE_NAME_TOKEN);
        if (cookie != null) {
            String cookieToken = cookie.getValue();
            log.debug("得到的token为：" + cookieToken);
            return true;
        } else {
//            request.getRequestDispatcher("/login/login").forward(request, response);
            response.sendRedirect("/login/login");
            log.debug("cookie is null");
            log.debug("token is null");
            return false;
        }
    }

    /**
     * Handler执行完成之后调用这个方法
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exc)
            throws Exception {

    }

    /**
     * Handler执行之后，ModelAndView返回之前调用这个方法
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

}