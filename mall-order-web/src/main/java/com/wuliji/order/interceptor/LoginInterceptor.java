package com.wuliji.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.wuliji.cart.service.CartService;
import com.wuliji.common.utils.CookieUtils;
import com.wuliji.common.utils.JsonUtils;
import com.wuliji.common.utils.MallResult;
import com.wuliji.pojo.TbItem;
import com.wuliji.pojo.TbUser;
import com.wuliji.service.TokenService;

/**
 * 用户登录拦截器
 * @author Administrator
 *
 */
public class LoginInterceptor implements HandlerInterceptor {

	@Value("${SSO_URL}")
	private String SSO_URL;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private CartService cartService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//从cookie中取token，判断token是否存在
		String token = CookieUtils.getCookieValue(request, "token");
		//token不存在则跳转到sso系统的登录页面。用户登录成功后，跳转到当前请求的url
		if(StringUtils.isBlank(token)) {
			response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
			//拦截
			return false;
		}
		//如果token存在，需要调用sso系统的服务，判断token是否过期
		MallResult mallResult = tokenService.getUserByToken(token);
		if(mallResult.getStatus() != 200) {//登录过期
			response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
			//拦截
			return false;
		}
		//没过期则将用户信息写入到request
		TbUser user = (TbUser) mallResult.getData();
		request.setAttribute("user", user);
		//判断cookie中是否有购物车数据，有则合并到服务端
		String cartList = CookieUtils.getCookieValue(request, "cart", true);
		if(StringUtils.isNotBlank(cartList)) {
			//合并购物车
			cartService.mergeCart(user.getId(), JsonUtils.jsonToList(cartList, TbItem.class));
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
