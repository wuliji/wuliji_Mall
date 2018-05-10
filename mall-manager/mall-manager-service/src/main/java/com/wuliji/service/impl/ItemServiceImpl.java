package com.wuliji.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wuliji.common.jedis.JedisClient;
import com.wuliji.common.pojo.EasyUIDataGridResult;
import com.wuliji.common.utils.IDUtils;
import com.wuliji.common.utils.JsonUtils;
import com.wuliji.common.utils.MallResult;
import com.wuliji.mapper.TbItemDescMapper;
import com.wuliji.mapper.TbItemMapper;
import com.wuliji.pojo.TbItem;
import com.wuliji.pojo.TbItemDesc;
import com.wuliji.pojo.TbItemExample;
import com.wuliji.service.ItemService;

/**
 * 商品管理
 * @author Administrator
 *
 */
@Service
public class ItemServiceImpl implements ItemService{

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource
	private Destination topicDestination;
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${REDIS_ITEM_PRE}")
	private String REDIS_ITEM_PRE;
	@Value("${ITEM_CACHE_EXPIRE}")
	private Integer ITEM_CACHE_EXPIRE;
	
	@Override
	public TbItem getItemById(long itemId) {
		//查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE);
			if(StringUtils.isNotBlank(json)) {
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				return tbItem;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//缓存没有，查询数据库
		TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		if(tbItem != null) {
			//把结果添加到缓存
			try {
				jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":BASE", JsonUtils.objectToJson(tbItem));
				//设置缓存过期时间
				jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":BASE", ITEM_CACHE_EXPIRE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return tbItem;
	}

	@Override
	/**
	 * 商品分页显示
	 */
	public EasyUIDataGridResult getItemList(int page, int rows) {
		//设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//创建一个返回对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		//取分页结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		result.setTotal(pageInfo.getTotal());
		return result;
	}

	@Override
	/**
	 * 商品添加
	 */
	public MallResult addItem(TbItem item, String desc) {
		//生成商品id
		final long itemId = IDUtils.genItemId();
		//补全item的属性
		item.setId(itemId);
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//向商品表插入数据
		itemMapper.insert(item);
		//创建一个商品描述表对应的pojo对象
		TbItemDesc itemDesc = new TbItemDesc();
		//补全属性
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		//向商品表插入数据
		itemDescMapper.insert(itemDesc);
		//发送商品添加的消息
		jmsTemplate.send(topicDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(itemId + "");
			}
		});
		//返回成功
		return MallResult.ok();
	}

	@Override
	/**
	 * 商品删除
	 */
	public MallResult deleteItem(String[] ids) {
		for (String id : ids) {
			itemMapper.deleteByPrimaryKey(Long.valueOf(id));
		}
		return MallResult.ok();
	}

	@Override
	/**
	 * 查询商品信息
	 */
	public MallResult editItem(Long id) {
		TbItem item = itemMapper.selectByPrimaryKey(id);
		return MallResult.ok(item);
	}

	@Override
	/**
	 * 查询商品描述
	 */
	public MallResult editItemDesc(Long id) {
		TbItemDesc data = itemDescMapper.selectByPrimaryKey(id);
		return MallResult.ok(data);
	}

	@Override
	public MallResult updateItem(TbItem item, String desc) {
		//修改更新时间
		Long id = item.getId();
		TbItem tbItem = itemMapper.selectByPrimaryKey(id);
		Date created = tbItem.getCreated();
		item.setUpdated(new Date());
		item.setStatus((byte) 1);
		item.setCreated(created);
		itemMapper.updateByPrimaryKey(item);
		//修改描述
		TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(id);
		tbItemDesc.setUpdated(new Date());
		tbItemDesc.setCreated(created);
		tbItemDesc.setItemDesc(desc);
		itemDescMapper.updateByPrimaryKey(tbItemDesc);
		return MallResult.ok();
	}

	@Override
	public MallResult instock(String[] ids) {
		for (String id : ids) {
			TbItem tbItem = itemMapper.selectByPrimaryKey(Long.valueOf(id));
			Date created = tbItem.getCreated();
			tbItem.setUpdated(new Date());
			tbItem.setCreated(created);
			tbItem.setStatus((byte) 2);
			itemMapper.updateByPrimaryKey(tbItem);
		}
		return MallResult.ok();
	}

	@Override
	public MallResult reshelf(String[] ids) {
		for (String id : ids) {
			TbItem tbItem = itemMapper.selectByPrimaryKey(Long.valueOf(id));
			Date created = tbItem.getCreated();
			tbItem.setUpdated(new Date());
			tbItem.setCreated(created);
			tbItem.setStatus((byte) 1);
			itemMapper.updateByPrimaryKey(tbItem);
		}
		return MallResult.ok();
	}

	@Override
	public TbItemDesc getItemDescById(long itemId) {
		//查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE);
			if(StringUtils.isNotBlank(json)) {
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return tbItemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		if(tbItemDesc != null) {
			//把结果添加到缓存
			try {
				jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":DESC", JsonUtils.objectToJson(tbItemDesc));
				//设置缓存过期时间
				jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":DESC", ITEM_CACHE_EXPIRE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return tbItemDesc;
	}
	
}
