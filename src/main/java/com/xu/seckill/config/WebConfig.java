package com.xu.seckill.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


//自定参数解析器, 作用：改变SpringMVC的Controller传入参数，实现可以User替换Token做为参数从登陆页面传到商品列表页面
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    UserArgumentResolver userArgumentResolver;
    @Autowired
    LoginInterceptor loginInterceptor;

    // SpringMVC框架回调addArgumentResolvers，然后给Controller的参数赋值
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(userArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册监控拦截器
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/goods/**", "/login/**", "/js/**", "/img/**", "/bootstrap/**", "/jquery-validation/**", "/layer/**", "/error/**");
    }

}