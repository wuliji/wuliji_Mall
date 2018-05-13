package com.wuliji.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 注册功能Controller
 * @author Administrator
 *
 */
@Controller
public class RegisterController {
	
	@RequestMapping("/page/register")
	public String showResgiter() {
		return "register";
	}
}
