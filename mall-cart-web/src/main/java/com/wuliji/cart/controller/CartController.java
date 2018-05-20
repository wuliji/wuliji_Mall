package com.wuliji.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuliji.cart.service.CartService;
import com.wuliji.common.utils.CookieUtils;
import com.wuliji.common.utils.JsonUtils;
import com.wuliji.common.utils.MallResult;
import com.wuliji.pojo.TbItem;
import com.wuliji.pojo.TbUser;
import com.wuliji.service.ItemService;

/**
 * 购物车视图层
 * @author Administrator
 *
 */
@Controller
public class CartController {
	
	@Autowired
	private ItemService itemService;
	@Value("${COOKIE_CART_EXPIRE}")
	private Integer COOKIE_CART_EXPIRE;
	@Autowired
	private CartService cartService;
	
	/**
	 * 添加商品到购物车
	 * @param itemId
	 * @param num
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable Long itemId, @RequestParam(defaultValue="1") Integer num, 
			HttpServletRequest request, HttpServletResponse response) {
		//判断用户登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		if(user != null) {
			//保存到服务端
			cartService.addCart(user.getId(), itemId, num);
			//返回逻辑视图
			return "cartSuccess";
		}
		//从cookie中取购物车列表
		List<TbItem> cartList = gerCartListFromCookie(request);
		//判断商品在商品列表中是否存在
		boolean flag = false;
		for(TbItem tbItem : cartList) {
			if(tbItem.getId() == itemId.longValue()) {
				//对原有商品进行数量相加
				tbItem.setNum(tbItem.getNum() + num);
				flag = true;
				break;
			}
		}
		TbItem tbItem = null;
		//如果不存在，根据商品id查询商品信息，得到TbItem对象
		if(!flag) {
			tbItem = itemService.getItemById(itemId);
			//设置商品数量
			tbItem.setNum(num);
			//取一张图片
			String image = tbItem.getImage();
			if(StringUtils.isNotBlank(image)) {
				tbItem.setImage(image.split(",")[0]);
			}
		}
		//把商品添加到商品列表中
		cartList.add(tbItem);
		//将商品列表写入到coolie中
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
		//返回添加成功页面
		return "cartSuccess";
	}
	
	/**
	 * 从cookie中取购物车列表的处理
	 * @param req
	 * @return
	 */
	private static List<TbItem> gerCartListFromCookie(HttpServletRequest req){
		String json = CookieUtils.getCookieValue(req, "cart", true);
		//判断json是否为空
		if(StringUtils.isBlank(json)) {
			return new ArrayList<>();
		}
		//把json转换成商品列表
		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}
	
	/**
	 * 展示购物车列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request, HttpServletResponse response) {
		//从coolie中取购物车列表
		List<TbItem> cartList = gerCartListFromCookie(request);
		//判断用户是否为登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		//如果为登录状态，从cookie去购物车列表，不为空把cookie中的购物车商品和redis中的商品信息合并
		if(user != null) {
			cartService.mergeCart(user.getId(), cartList);
		}
		//将cookie中的购物车信息删除
		CookieUtils.deleteCookie(request, response, "cart");
		//从redis服务端取购物车列表
		cartList = cartService.getCartList(user.getId());
		//把列表传递给页面
		request.setAttribute("cartList", cartList);
		//返回逻辑视图
		return "cart";
	}
	
	/**
	 * 更新购物车商品
	 * @param itemId
	 * @param num
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public MallResult updateCartNum(@PathVariable Long itemId, @PathVariable Integer num
			, HttpServletRequest request, HttpServletResponse response) {
		//判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		if(user != null) {
			cartService.updateCartNum(user.getId(), itemId, num);
			return MallResult.ok();
		}
		//从cookie中取购物车列表
		List<TbItem> cartList = gerCartListFromCookie(request);
		//遍历商品列表找到对应的商品
		for (TbItem tbItem : cartList) {
			if(tbItem.getId().longValue() == itemId) {
				//更新数量
				tbItem.setNum(num);
				break;
			}
		}
		//将购物车列表写回cookie中去
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
		//返回成功
		return MallResult.ok();
	}
	
	@RequestMapping("/cart/delete/${itemId}")
	public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request
			, HttpServletResponse response) {
		//判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		if(user != null) {
			cartService.deleteCartItem(user.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		//从cookie中取购物车列表
		List<TbItem> cartList = gerCartListFromCookie(request);
		//遍历列表，找到要删除的商品
		for (TbItem tbItem : cartList) {
			if(tbItem.getId().longValue() == itemId) {
				//删除商品
				cartList.remove(tbItem);
				break;
			}
		}
		//把购物车列表写入cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
		//返回逻辑视图 redirect形式转跳
		return "redirect:/cart/cart.html";
	}
	
}
