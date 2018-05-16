package com.wuliji.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用户登录处理
 * @author Administrator
 *
 */
@Controller
public class LoginController {
	
	@RequestMapping("/page/login")
	public String showLogin() {
		return "login";
	}
}
