package com.wuliji.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuliji.common.utils.MallResult;
import com.wuliji.pojo.TbUser;
import com.wuliji.service.RegisterService;

/**
 * 注册功能Controller
 * @author Administrator
 *
 */
@Controller
public class RegisterController {
	
	@Autowired
	private RegisterService registerService;
	
	@RequestMapping("/page/register")
	public String showResgiter() {
		return "register";
	}
	
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public MallResult checkData(@PathVariable String param, @PathVariable Integer type) {
		return registerService.checkData(param, type);
	}
	
	@RequestMapping(value="/user/register", method=RequestMethod.POST)
	@ResponseBody
	public MallResult register(TbUser user) {
		MallResult result = registerService.register(user);
		return result;
	}
	
}
