package com.wuliji.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.wuliji.common.utils.MallResult;
import com.wuliji.mapper.TbUserMapper;
import com.wuliji.pojo.TbUser;
import com.wuliji.pojo.TbUserExample;
import com.wuliji.pojo.TbUserExample.Criteria;
import com.wuliji.service.RegisterService;

/**
 * 用户注册处理Service
 * @author Administrator
 *
 */
@Service
public class RegisterServiceImpl implements RegisterService{

	@Autowired
	private TbUserMapper userMapper;
	
	@Override
	public MallResult checkData(String param, int type) {
		//根据不同的type生成不同的查询条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		//1：用户名 2：手机号 3：邮箱
		if(type == 1) {
			criteria.andUsernameEqualTo(param);
		}else if(type == 2) {
			criteria.andPhoneEqualTo(param);
		}else if(type == 3) {
			criteria.andEmailEqualTo(param);
		}else {
			return MallResult.build(400, "数据类型错误");
		}
		//执行查询
		List<TbUser> list = userMapper.selectByExample(example);
		//判断结果中是否包含数据
		if(list != null && list.size() > 0) {
			//如果有数据返回false
			return MallResult.ok(false);
		}
		//如果没有数据返回true
		return MallResult.ok(true);
	}

	@Override
	public MallResult register(TbUser user) {
		//数据有效性校验
		if(StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())
				|| StringUtils.isBlank(user.getPhone())) {
			return MallResult.build(400, "用户数据不完整，注册失败");
		}
		MallResult result = checkData(user.getUsername(), 1);
		if(!(boolean) result.getData()) {
			return MallResult.build(400, "此用户名已经被占用");
		}
		result = checkData(user.getUsername(), 2);
		if(!(boolean) result.getData()) {
			return MallResult.build(400, "此手机号已经被占用");
		}
		//补全pojo属性
		user.setCreated(new Date());
		user.setUpdated(new Date());
		//对password进行MD5加密
		String md5 = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5);
		//把用户数据插入到数据库中
		userMapper.insert(user);
		//返回成功
		return MallResult.ok();
	}
	
}
