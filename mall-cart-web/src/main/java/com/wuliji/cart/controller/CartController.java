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

import com.wuliji.common.utils.CookieUtils;
import com.wuliji.common.utils.JsonUtils;
import com.wuliji.pojo.TbItem;
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
	
	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable Long itemId, @RequestParam(defaultValue="1") Integer num, 
			HttpServletRequest request, HttpServletResponse response) {
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
}
