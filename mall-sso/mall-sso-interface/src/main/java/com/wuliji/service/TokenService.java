package com.wuliji.service;

import com.wuliji.common.utils.MallResult;

public interface TokenService {
	
	MallResult getUserByToken(String token);
}
