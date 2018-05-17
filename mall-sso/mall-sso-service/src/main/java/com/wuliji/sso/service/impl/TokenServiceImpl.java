package com.wuliji.sso.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.wuliji.common.jedis.JedisClient;
import com.wuliji.common.utils.JsonUtils;
import com.wuliji.common.utils.MallResult;
import com.wuliji.pojo.TbUser;
import com.wuliji.service.TokenService;

/**
 * 根据token取用户信息
 * @author Administrator
 *
 */
@Service
public class TokenServiceImpl implements TokenService{

	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	@Override
	public MallResult getUserByToken(String token) {
		//根据token到redis中取用户信息
		String json = jedisClient.get("SESSION:" + token);
		//取不到用户信息，登录过期，返回
		if(StringUtils.isBlank(json)) {
			return MallResult.build(201, "用户登录已过期");
		}
		//取到用户信息，更新token的过期时间
		jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);
		//返回结果，MallResult封装TbUser
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		return MallResult.ok(user);
	}
	
}
