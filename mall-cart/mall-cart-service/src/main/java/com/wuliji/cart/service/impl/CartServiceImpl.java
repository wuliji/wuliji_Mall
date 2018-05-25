package com.wuliji.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wuliji.cart.service.CartService;
import com.wuliji.common.jedis.JedisClient;
import com.wuliji.common.utils.JsonUtils;
import com.wuliji.common.utils.MallResult;
import com.wuliji.mapper.TbItemMapper;
import com.wuliji.pojo.TbItem;

/**
 * 购物车处理服务
 * @author Administrator
 *
 */
@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;
	@Autowired
	private TbItemMapper itemMapper;
	
	@Override
	public MallResult addCart(Long userId, Long itemId, Integer num) {
		//向redis中添加购物车
		//数据类型是hash key是用户id field是商品id value为商品信息
		//判断商品是否存在
		Boolean hexists = jedisClient.hexists(REDIS_CART_PRE + ":" + userId, itemId + "");
		//如果存在数量相加
		if(hexists) {
			String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
			//把json转换成TbItem
			TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
			tbItem.setNum(tbItem.getNum() + num);
			//写回redis
			jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
			return MallResult.ok();
		}
		//不存在，根据商品id取商品信息，然后添加到购物车列表
		TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		tbItem.setNum(num);
		//取图片
		String image = tbItem.getImage();
		if(StringUtils.isNotBlank(image)) {
			tbItem.setImage(image.split(",")[0]);
		}
		jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
		return MallResult.ok();
	}

	/**
	 * 合并购物车
	 */
	@Override
	public MallResult mergeCart(Long userId, List<TbItem> tbItemList) {
		//遍历购物列表
		//把列表添加到购物车
		//判断购物车是否有此商品
		//如果有，数量相加
		//无则添加新商品
		for (TbItem tbItem : tbItemList) {
			addCart(userId, tbItem.getId(), tbItem.getNum());
		}
		return MallResult.ok();
	}

	/**
	 * 获得redis中购物车列表
	 */
	@Override
	public List<TbItem> getCartList(Long userId) {
		//根据用户id查询购物车列表
		List<String> jsonList = jedisClient.hvals(REDIS_CART_PRE + ":" + userId);
		List<TbItem> itemList = new ArrayList<>();
		for (String string : jsonList) {
			//创建TbItem对象
			TbItem tbItem = JsonUtils.jsonToPojo(string, TbItem.class);
			itemList.add(tbItem);
		}
		return itemList;
	}

	/**
	 * 更新购物车数量
	 */
	@Override
	public MallResult updateCartNum(Long userId, Long itemId, Integer num) {
		//从redis中取商品信息
		String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
		//更新商品数量
		TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
		tbItem.setNum(num);
		//写回redis
		jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
		return MallResult.ok();
	}

	@Override
	public MallResult deleteCartItem(Long userId, Long itemId) {
		//从redis中找到相应的商品
		jedisClient.hdel(REDIS_CART_PRE + ":" + userId, itemId + "");
		return MallResult.ok();
	}

	@Override
	public MallResult clearCartItem(Long userId) {
		//删除购物车信息
		jedisClient.del(REDIS_CART_PRE + ":" + userId);
		return MallResult.ok();
	}

}
