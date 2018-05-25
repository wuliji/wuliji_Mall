package com.wuliji.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wuliji.common.jedis.JedisClient;
import com.wuliji.common.utils.MallResult;
import com.wuliji.mapper.TbOrderItemMapper;
import com.wuliji.mapper.TbOrderMapper;
import com.wuliji.mapper.TbOrderShippingMapper;
import com.wuliji.order.pojo.OrderInfo;
import com.wuliji.order.service.OrderService;
import com.wuliji.pojo.TbOrderItem;
import com.wuliji.pojo.TbOrderShipping;

/**
 * 订单服务
 * @author Administrator
 *
 */
@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private TbOrderShippingMapper orderShipMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${ORDER_ID_GEN_KEY}")
	private String ORDER_ID_GEN_KEY;
	@Value("${ORDER_ID_START}")
	private String ORDER_ID_START;
	@Value("${ORDER_DETAIL_ID_GEN_KEY}")
	private String ORDER_DETAIL_ID_GEN_KEY;
	
	@Override
	public MallResult createOrder(OrderInfo orderInfo) {
		//生成订单号。使用redis的increase生成
		if(!jedisClient.exists(ORDER_ID_GEN_KEY)) {//给第一个初始值
			jedisClient.set(ORDER_ID_GEN_KEY, ORDER_ID_START);
		}
		String orderId = jedisClient.incr(ORDER_ID_GEN_KEY).toString();
		//补全orderInfo属性
		orderInfo.setOrderId(orderId);
		orderInfo.setStatus(1);//设置1.未付款 2.已付款 3.未发货 4.已发货 5.交易成功 6.交易关闭
		orderInfo.setCreateTime(new Date());
		orderInfo.setUpdateTime(new Date());
		//插入订单表
		orderMapper.insert(orderInfo);
		//向订单明细表插入数据
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		for (TbOrderItem tbOrderItem : orderItems) {
			//生成明细id
			String odId = jedisClient.incr(ORDER_DETAIL_ID_GEN_KEY).toString();
			//补全pojo属性
			tbOrderItem.setId(odId);
			tbOrderItem.setOrderId(orderId);
			//插入明细
			orderItemMapper.insert(tbOrderItem);
		}
		//向订单物流表插入数据
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(new Date());
		orderShipping.setUpdated(new Date());
		orderShipMapper.insert(orderShipping);
		//返回订单号
		return MallResult.ok(orderId);
	}

}
