package com.wuliji.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuliji.common.utils.CookieUtils;
import com.wuliji.common.utils.MallResult;
import com.wuliji.service.LoginService;

/**
 * 用户登录处理
 * @author Administrator
 *
 */
@Controller
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	
	@RequestMapping("/page/login")
	public String showLogin(String redirect, Model model) {
		model.addAttribute("redirect", redirect);
		return "login";
	}
	
	@RequestMapping(value="/user/login", method=RequestMethod.POST)
	@ResponseBody
	public MallResult login(String username, String password,
			HttpServletRequest request, HttpServletResponse response) {
		MallResult result = loginService.userLogin(username, password);
		//判断是否登录成功，是则写入cookie，否则返回结果
		if(result.getStatus() == 200) {
			String token = result.getData().toString();
			//将token写入到cookie中
			CookieUtils.setCookie(request, response, TOKEN_KEY, token);
		}
		//返回结果
		return result;
	}
}
