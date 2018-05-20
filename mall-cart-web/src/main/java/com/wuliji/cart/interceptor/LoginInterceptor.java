package com.wuliji.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.wuliji.common.utils.CookieUtils;
import com.wuliji.common.utils.MallResult;
import com.wuliji.pojo.TbUser;
import com.wuliji.service.TokenService;

public class LoginInterceptor implements HandlerInterceptor{
	
	@Autowired
	private TokenService tokenService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//前处理：执行Hander之前执行此方法，返回true则放行，返回false就拦截
		//1.从cookie中取token
		String token = CookieUtils.getCookieValue(request, "token");
		//2.没有token则直接未登录，放行
		if(StringUtils.isBlank(token)) {
			return true;
		}
		//3.取到token，需要调用SSO系统的服务，根据token取用户信息
		MallResult mallResult = tokenService.getUserByToken(token);
		//4.没有取到用户信息，则登录过期，直接放行
		if(mallResult.getStatus() != 200) {
			return true;
		}
		//5.取到用户信息，将token中的购物车消息同步到redis中。只需要判断Controller中request是否包含User信息
		TbUser user = (TbUser) mallResult.getData();
		request.setAttribute("user", user);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//handler执行后，返回ModelAndView之前进行处理
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//完成处理，返回ModelAndView之后，可以在此处理异常
		
	}
	
}
