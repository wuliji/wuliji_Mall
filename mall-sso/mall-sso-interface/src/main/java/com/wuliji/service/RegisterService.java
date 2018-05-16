package com.wuliji.service;

import com.wuliji.common.utils.MallResult;
import com.wuliji.pojo.TbUser;

public interface RegisterService {
	
	MallResult checkData(String param, int type);
	MallResult register(TbUser user);
}
