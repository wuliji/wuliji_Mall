package com.wuliji.service;

import com.wuliji.common.utils.MallResult;

public interface LoginService {
	
	MallResult userLogin(String username, String password);
}
