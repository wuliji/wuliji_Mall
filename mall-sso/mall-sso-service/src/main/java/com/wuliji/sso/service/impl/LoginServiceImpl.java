package com.wuliji.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.wuliji.common.jedis.JedisClient;
import com.wuliji.common.utils.JsonUtils;
import com.wuliji.common.utils.MallResult;
import com.wuliji.mapper.TbUserMapper;
import com.wuliji.pojo.TbUser;
import com.wuliji.pojo.TbUserExample;
import com.wuliji.pojo.TbUserExample.Criteria;
import com.wuliji.service.LoginService;

/**
 * 用户登录处理
 * @author Administrator
 *
 */
@Service
public class LoginServiceImpl implements LoginService{

	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	@Override
	public MallResult userLogin(String username, String password) {
		 //* 1.用户名密码校验，不正确则返回失败
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = userMapper.selectByExample(example);
		if(list == null || list.size() == 0) {
			return MallResult.build(400, "用户或密码错误");
		}
		//取用户信息
		TbUser user = list.get(0);
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
			return MallResult.build(400, "用户或密码错误");
		}
		 //* 2.生成token
		String token = UUID.randomUUID().toString();
		 //* 3.用户信息写入Redis，key为redis，value为用户信息
		user.setPassword(null);//防止密码写入到信息中去
		jedisClient.set("SESSION:" + token, JsonUtils.objectToJson(user));
		 //* 4.设置session过期时间
		jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);
		 //* 5.把token返回
		return MallResult.ok(token);
	}
	
}
