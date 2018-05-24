package com.wuliji.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuliji.cart.service.CartService;
import com.wuliji.pojo.TbItem;
import com.wuliji.pojo.TbUser;

/**
 * 订单管理Controller
 * @author Administrator
 *
 */
@Controller
public class OrderController {
	
	@Autowired
	private CartService cartService;
	
	@RequestMapping("/order/order-cart")
	public String showOrderCart(HttpServletRequest request) {
		//取用户id
		TbUser user = (TbUser) request.getAttribute("user");
		//根据用户id取收货地址列表
		//使用静态数据
		//取支付方式列表
		
		//根据用户id取购物车列表
		List<TbItem> cartList = cartService.getCartList(user.getId());
		//把购物车列表传递给jsp
		request.setAttribute("cartList", cartList);
		//返回逻辑视图
		return "order-cart";
	}
}
