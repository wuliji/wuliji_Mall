package com.wuliji.order.service;

import com.wuliji.common.utils.MallResult;
import com.wuliji.order.pojo.OrderInfo;

public interface OrderService {
	
	MallResult createOrder(OrderInfo orderInfo);
}
