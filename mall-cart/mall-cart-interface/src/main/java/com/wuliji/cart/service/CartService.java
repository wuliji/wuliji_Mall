package com.wuliji.cart.service;

import java.util.List;

import com.wuliji.common.utils.MallResult;
import com.wuliji.pojo.TbItem;

public interface CartService {
	
	MallResult addCart(Long userId, Long itemId, Integer num);
	MallResult mergeCart(Long userId, List<TbItem> tbItemList);
	List<TbItem> getCartList(Long userId);
	MallResult updateCartNum(Long userId, Long itemId, Integer num);
	MallResult deleteCartItem(Long userId, Long itemId);
	MallResult clearCartItem(Long userId);
}
