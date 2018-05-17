package com.wuliji.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuliji.common.utils.JsonUtils;
import com.wuliji.common.utils.MallResult;
import com.wuliji.service.TokenService;

/**
 * token信息处理
 * @author Administrator
 *
 */
@Controller
public class TokenController {

	@Autowired
	private TokenService tokenService;
	
	@RequestMapping(value="/user/token/{token}",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)//将返回的字符串用Application/json访问
	@ResponseBody
	public Object getUserByToken(@PathVariable String token, String callback) {
		MallResult result = tokenService.getUserByToken(token);
		//响应之前，判断是否为json请求
		if(StringUtils.isNotBlank(callback)) {
			//把结果封装成一个js语句响应
			//return callback + "(" + JsonUtils.objectToJson(result) + ")";
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		return JsonUtils.objectToJson(result);
	}
	
}
